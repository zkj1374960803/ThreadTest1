package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.custmanager.allocateapply.dto.QueryPendingMaterialsDTO;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply.ApplyStatusEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleContext;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.PurchaseApplyHandleStrategy;
import com.ccbuluo.business.platform.claimorder.dao.ClaimOrderDao;
import com.ccbuluo.business.platform.claimorder.service.ClaimOrderService;
import com.ccbuluo.business.platform.custmanager.dao.BizServiceCustmanagerDao;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.business.platform.custmanager.service.CustmanagerService;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.instock.service.InstockOrderService;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weakref.jmx.internal.guava.collect.Lists;
import org.weakref.jmx.internal.guava.collect.Maps;


import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 申请管理
 * @author zhangkangjian
 * @date 2018-08-07 14:29:54
 */
@Service
public class AllocateApplyServiceImpl implements AllocateApplyService {
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    UserHolder userHolder;
    @Resource
    BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    GenerateDocCodeService generateDocCodeService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;
    @Resource(name = "claimOrderServiceImpl")
    private ClaimOrderService claimOrderServiceImpl;
    @Resource
    private ClaimOrderDao claimOrderDao;
    @Resource
    private ApplyHandleContext applyHandleContext;
    @Autowired
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    BizServiceCustmanagerDao bizServiceCustmanagerDao;
    @Resource(name = "custmanagerServiceImpl")
    CustmanagerService custmanagerServiceImpl;
    @Resource(name = "instockOrderServiceImpl")
    private InstockOrderService instockOrderService;
    @Resource
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Resource
    private PurchaseApplyHandleStrategy purchaseApplyHandleStrategy;
    @Autowired
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Resource
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;

    /**
     * 创建物料或者零配件申请
     * @param allocateApplyDTO 申请单实体
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAllocateApply(AllocateApplyDTO allocateApplyDTO) {
        String loggedUserId = userHolder.getLoggedUserId();
        allocateApplyDTO.setOperator(loggedUserId);
        allocateApplyDTO.setCreator(loggedUserId);
        allocateApplyDTO.setApplyer(loggedUserId);
        allocateApplyDTO.setApplyerName(userHolder.getLoggedUser().getName());

        // 生成申请单编号
        StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.SW);
        allocateApplyDTO.setApplyNo(stringStatusDto.getData());
        String userOrgCode = getUserOrgCode();
        if(userOrgCode != null) {
            allocateApplyDTO.setApplyorgNo(userOrgCode);
        }
        // 默认申请的状态为待处理
        allocateApplyDTO.setApplyStatus(ApplyStatusEnum.PENDING.name());

        // 默认处理机构赋值
        if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(allocateApplyDTO.getApplyorgNo())) {
            allocateApplyDTO.setProcessOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setProcessOrgtype(OrganizationTypeEnum.PLATFORM.name());
        }else{
            // 遗漏的处理类型补充
            allocateApplyDTO.setProcessOrgno(allocateApplyDTO.getOutstockOrgno());
            String outstockOrgtype = allocateApplyDTO.getOutstockOrgtype();
            if(StringUtils.isBlank(outstockOrgtype)){
                allocateApplyDTO.setProcessOrgtype(OrganizationTypeEnum.PLATFORM.name());
            }else {
                allocateApplyDTO.setProcessOrgtype(outstockOrgtype);
            }
        }

        // 创建申请的机构是不是平台并且出库机构平台的话，处理机构为出库机构，出库机构设置为空
        if(!BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(userOrgCode) &&
            BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(allocateApplyDTO.getOutstockOrgno())){
            allocateApplyDTO.setProcessOrgno(allocateApplyDTO.getOutstockOrgno());
            allocateApplyDTO.setOutstockOrgno(null);
        }


        // 根据处理类型，设置申请的类型、各种相关机构、状态等属性
        String processType = allocateApplyDTO.getProcessType();
        if(AllocateApplyTypeEnum.BARTER.name().equals(processType) || AllocateApplyTypeEnum.REFUND.name().equals(processType)){
            allocateApplyDTO.setApplyType(processType);
            StatusDto<String> thcode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.TH);
            allocateApplyDTO.setApplyNo(thcode.getData());
            allocateApplyDTO.setProcessOrgtype(OrganizationTypeEnum.PLATFORM.name());
            allocateApplyDTO.setOutstockOrgtype(OrganizationTypeEnum.PLATFORM.name());
            allocateApplyDTO.setOutstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setProcessOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setApplyStatus(BizAllocateApply.ReturnApplyStatusEnum.PRODRETURNED.name());
        }else{
            // 根据处理机构类型判断
            allocateApplyDTO.setApplyType(AllocateApplyTypeEnum.SAMELEVEL.name());
        }


        // 默认出库机构类型orgType
        String outOrgType = getOrgTypeByCode(allocateApplyDTO.getOutstockOrgno());
        if(StringUtils.isBlank(outOrgType)){
            allocateApplyDTO.setOutstockOrgtype(OrganizationTypeEnum.PLATFORM.name());
        }else {
            allocateApplyDTO.setOutstockOrgtype(outOrgType);
            allocateApplyDTO.setProcessOrgtype(outOrgType);
        }
        // 如果是退款类型，没有入库机构，不需要查询机构类型
        if(!AllocateApplyTypeEnum.REFUND.name().equals(processType)){
            // 入库仓库查询入库组织机构
            String orgCode = bizServiceStorehouseDao.getOrgCodeByStoreHouseCode(allocateApplyDTO.getInRepositoryNo());
            allocateApplyDTO.setInstockOrgno(orgCode);
        }
        // 保存申请单基础数据
        bizAllocateApplyDao.saveEntity(allocateApplyDTO);
        // 保存申请单详情数据
        batchInsertForapplyDetailList(allocateApplyDTO, loggedUserId, processType);
        // 已经自动处理了，调用 申请构建计划的方法
        if(AllocateApplyTypeEnum.BARTER.name().equals(processType) || AllocateApplyTypeEnum.REFUND.name().equals(processType)){
            applyHandleContext.applyHandle(allocateApplyDTO.getApplyNo());
        }

    }
    /**
     * 批量保存申请详单数据
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-23 16:49:20
     */
    private void batchInsertForapplyDetailList(AllocateApplyDTO allocateApplyDTO, String loggedUserId, String processType) {
        List<AllocateapplyDetailDTO> allocateapplyDetailList = allocateApplyDTO.getAllocateapplyDetailList();
        // 过滤掉申请数量小于零的
        List<AllocateapplyDetailDTO> filterAllocateapply = allocateapplyDetailList.stream().filter(dto -> dto.getApplyNum() > 0).collect(Collectors.toList());
        filterAllocateapply.stream().forEach(a -> {
            a.setApplyNo(allocateApplyDTO.getApplyNo());
            a.setOperator(loggedUserId);
            a.setCreator(loggedUserId);
            if(AllocateApplyTypeEnum.BARTER.name().equals(processType) || AllocateApplyTypeEnum.REFUND.name().equals(processType)){
                a.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());
            }else {
                a.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
            }
        });
        bizAllocateApplyDao.batchInsertForapplyDetailList(filterAllocateapply);
    }

    /**
     * 查询组织架构类型
     * @param orgCode 组织架构code
     * @return String 机构类型
     * @author zhangkangjian
     * @date 2018-08-10 12:04:37
     */
    private String getOrgTypeByCode(String orgCode) {
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(orgCode);
        if(orgByCode.isSuccess()){
            StatusDto<BasicUserOrganization> resolve = StatusDtoThriftUtils.resolve(orgByCode, BasicUserOrganization.class);
            BasicUserOrganization data = resolve.getData();
            if(data != null){
                return data.getOrgType();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询申请单详情
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    @Override
    public FindAllocateApplyDTO findDetail(String applyNo) {
        FindAllocateApplyDTO allocateApplyDTO = bizAllocateApplyDao.findDetail(applyNo);
        if(allocateApplyDTO == null){
            throw new CommonException(Constants.ERROR_CODE, "根据申请编号查询详情数据异常！");
        }
        // 查询组织架构的名字
        StatusDtoThriftBean<BasicUserOrganization> outstockOrgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getOutstockOrgno());
        StatusDtoThriftBean<BasicUserOrganization> instockOrgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getInstockOrgno());
        StatusDtoThriftBean<BasicUserOrganization> applyorgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getApplyorgNo());
        StatusDto<BasicUserOrganization> outstockOrgNameresolve = StatusDtoThriftUtils.resolve(outstockOrgName, BasicUserOrganization.class);
        StatusDto<BasicUserOrganization> instockOrgNameresolve = StatusDtoThriftUtils.resolve(instockOrgName, BasicUserOrganization.class);
        StatusDto<BasicUserOrganization> applyorgNameResolve = StatusDtoThriftUtils.resolve(applyorgName, BasicUserOrganization.class);
        Optional.ofNullable(outstockOrgNameresolve.getData()).ifPresent(a -> allocateApplyDTO.setOutstockOrgName(a.getOrgName()));
        Optional.ofNullable(instockOrgNameresolve.getData()).ifPresent(a -> allocateApplyDTO.setInstockOrgName(a.getOrgName()));
        Optional.ofNullable(applyorgNameResolve.getData()).ifPresent(a -> {
                String orgName = a.getOrgName();
                String orgType = a.getOrgType();
                if(StringUtils.isNotBlank(orgType)){
                    allocateApplyDTO.setOrgType(orgType);
                }else {
                    allocateApplyDTO.setOrgType(OrganizationTypeEnum.PLATFORM.name());
                }
                allocateApplyDTO.setApplyorgName(orgName);
        });
        // 查询申请单的详单
        List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTOS = bizAllocateApplyDao.queryAllocateapplyDetail(applyNo);
        if (queryAllocateapplyDetailDTOS != null) {
            String productType = queryAllocateapplyDetailDTOS.get(0).getProductType();
            if (!Constants.PRODUCT_TYPE_EQUIPMENT.equals(productType)) {
                // 查询零配件的名称
                List<String> productNo = queryAllocateapplyDetailDTOS.stream().map(QueryAllocateapplyDetailDTO::getProductNo).collect(Collectors.toList());
                StatusDtoThriftList<BasicCarpartsProductDTO> basicCarpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCarpartsCodes(productNo);
                List<BasicCarpartsProductDTO> basicCarpartsProductList = StatusDtoThriftUtils.resolve(basicCarpartsProductDTOList, BasicCarpartsProductDTO.class).getData();
                Optional.ofNullable(basicCarpartsProductList).ifPresent(a ->{
                    Map<String, BasicCarpartsProductDTO> basicCarpartsProductDTOMap = a.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, b -> b, (k1, k2) -> k1));
                    queryAllocateapplyDetailDTOS.stream().forEach(c -> {
                        BasicCarpartsProductDTO basicCarpartsProductDTO = basicCarpartsProductDTOMap.get(c.getProductNo());
                        if (basicCarpartsProductDTO != null) {
                            c.setProductName(basicCarpartsProductDTO.getCarpartsName());
                        }
                    });
                });
            }
        }
        allocateApplyDTO.setQueryAllocateapplyDetailDTO(queryAllocateapplyDetailDTOS);
        return allocateApplyDTO;
    }

    /**
     * 查询申请列表
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @Override
    public Page<QueryAllocateApplyListDTO> findApplyList(String productType, String orgType, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize) {
        // 获取用户的组织机构
        String userOrgCode = getUserOrgCode();
        List<String> orgCodes = getOrgCodesByOrgType(orgType);
        // 查询分页的申请列表
        // 如果类型是空的话，全部类型，查询所有的申请数据
        Page<QueryAllocateApplyListDTO> page = new Page<>();
        if(StringUtils.isBlank(orgType)){
            // 查询分页的处理申请列表
            page = bizAllocateApplyDao.findApplyList(productType,orgCodes, processType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        }else {
            // 类型不是空，查不到机构的编码
            if(orgCodes == null || orgCodes.size() == 0){
                return new Page<>(offset, pageSize);
            }else {
                // 查询分页的处理申请列表
                page = bizAllocateApplyDao.findApplyList(productType,orgCodes, processType, applyStatus, applyNo, offset, pageSize, userOrgCode);
            }
        }
        // 查询机构的名称
        Optional.ofNullable(page.getRows()).ifPresent(a ->{
            List<String> outstockOrgno = a.stream().filter(b -> b.getOutstockOrgno() != null).map(QueryAllocateApplyListDTO::getOutstockOrgno).collect(Collectors.toList());
            List<String> instockOrgno = a.stream().filter(b -> b.getInstockOrgno() != null).map(QueryAllocateApplyListDTO::getInstockOrgno).collect(Collectors.toList());
            Map<String, BasicUserOrganization> outOrganizationMap = basicUserOrganizationService.queryOrganizationByOrgCodes(outstockOrgno);
            Map<String, BasicUserOrganization> inOrganizationMap = basicUserOrganizationService.queryOrganizationByOrgCodes(instockOrgno);
            a.stream().forEach(b ->{
                BasicUserOrganization outOrganization = outOrganizationMap.get(b.getOutstockOrgno());
                BasicUserOrganization inOrganization = inOrganizationMap.get(b.getInstockOrgno());
                if(outOrganization != null){
                    b.setOutstockOrgname(outOrganization.getOrgName());
                }
                if(inOrganization != null){
                    b.setInstockOrgName(inOrganization.getOrgName());
                }
            });
        });
        return page;
    }

    /**
     * 查询处理申请列表
     * @param orgType
     * @param applyStatus
     * @param applyNo
     * @param offset
     * @param pageSize
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @Override
    public Page<QueryAllocateApplyListDTO> findProcessApplyList(String productType,String orgType, String applyStatus, String applyNo, Integer offset, Integer pageSize) {
        // 查询机构的名称
        StatusDtoThriftList<String> orgCodeStatusDtoThriftList = basicUserOrganizationService.queryOrgNameByOrgCode(applyNo);
        StatusDto<List<String>> orgCodeStatusDto = StatusDtoThriftUtils.resolve(orgCodeStatusDtoThriftList, String.class);
        List<String> orgCodeStatus = orgCodeStatusDto.getData();
        String userOrgCode = getUserOrgCode();
        List<String> orgCodesByOrgType = getOrgCodesByOrgType(orgType);
        // 如果类型是空的话，全部类型，查询所有的申请数据
        Page<QueryAllocateApplyListDTO> page;
        if(StringUtils.isBlank(orgType)){
            // 查询分页的处理申请列表
            page = bizAllocateApplyDao.findProcessApplyList(orgCodeStatus, productType,orgCodesByOrgType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        }else {
            // 类型不是空，查不到机构的编码
            if(orgCodesByOrgType == null || orgCodesByOrgType.size() == 0){
                return new Page<>(offset, pageSize);
            }else {
                // 查询分页的处理申请列表
                page = bizAllocateApplyDao.findProcessApplyList(orgCodeStatus, productType,orgCodesByOrgType, applyStatus, applyNo, offset, pageSize, userOrgCode);
            }
        }
        // 查询机构的名称和类型
        Optional.ofNullable(page.getRows()).ifPresent(a ->{
            List<String> orgCode = a.stream().map(QueryAllocateApplyListDTO::getApplyorgNo).collect(Collectors.toList());
            Map<String, BasicUserOrganization> basicUserOrganizationMap = basicUserOrganizationService.queryOrganizationByOrgCodes(orgCode);
            a.stream().forEach(b ->{
                BasicUserOrganization basicUserOrganization = basicUserOrganizationMap.get(b.getApplyorgNo());
                if(basicUserOrganization != null){
                    b.setOrgName(basicUserOrganization.getOrgName());
                    b.setOrgType(basicUserOrganization.getOrgType());
                }
            });
        });
        return page;
    }


    /**
     * 获取用户的组织机构
     * @return String 用户的orgCode
     * @author zhangkangjian
     * @date 2018-08-09 15:38:41
     */
    private String getUserOrgCode() {
        BusinessUser loggedUser = userHolder.getLoggedUser();
        if(loggedUser != null){
            Organization organization = loggedUser.getOrganization();
            if(organization == null){
                throw new CommonException(Constants.ERROR_CODE, loggedUser.getName() + ":组织架构数据异常");
            }
            String orgCode = organization.getOrgCode();
            if(StringUtils.isBlank(orgCode)){
                throw new CommonException(Constants.ERROR_CODE, loggedUser.getName() + ":组织架构数据异常");
            }
            return orgCode;
        }
        return StringUtils.EMPTY;
    }

    /**
     * 人为触发的正常调拨类型的申请处理
     * @param processApplyDTO
     * @author zhangkangjian
     * @date 2018-08-10 11:24:53
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processApply(ProcessApplyDTO processApplyDTO) {
        // 更新基础数据
        processApplyDTO.setApplyProcessor(userHolder.getLoggedUserId());
        processApplyDTO.setProcessTime(new Date());
        processApplyDTO.setApplyStatus(ApplyStatusEnum.WAITINGPAYMENT.name());
        bizAllocateApplyDao.updateAllocateApply(processApplyDTO);
        saveProcessApply(processApplyDTO.getProcessApplyDetailDTO());
        // 生成出入库计划
        applyHandleContext.applyHandle(processApplyDTO.getApplyNo());
    }

    /**
     * 保存申请详单数据
     * @param processApplyDetailDTO 申请单实体
     * @author zhangkangjian
     * @date 2018-09-12 16:07:20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessApply(List<ProcessApplyDetailDTO> processApplyDetailDTO) {
        // 更新申请单的详单数据
        bizAllocateApplyDao.batchUpdateForApplyDetail(processApplyDetailDTO);
    }

    /**
     * 驳回申请
     *
     * @param applyNo     申请单号
     * @param processMemo 驳回理由
     * @author zhangkangjian
     * @date 2018-09-12 16:16:17
     */
    @Override
    public void rejectApply(String applyNo, String processMemo) {
        String userOrgCode = getUserOrgCode();
        FindAllocateApplyDTO detail = findDetail(applyNo);
        String applyStatus = detail.getApplyStatus();
        ProcessApplyDTO processApplyDTO = new ProcessApplyDTO();
        processApplyDTO.setApplyNo(applyNo);
        processApplyDTO.setProcessMemo(processMemo);
        processApplyDTO.setApplyStatus(ApplyStatusEnum.REJECT.name());
        Long versionNo = bizAllocateApplyDao.findVersionNo(processApplyDTO.getApplyNo());
        processApplyDTO.setVersionNo(versionNo);
        if(ApplyStatusEnum.PENDING.name().equals(applyStatus)){
            String processOrgno = detail.getProcessOrgno();
            if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(userOrgCode) ||
                BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(processOrgno)){
                // 可以驳回
                bizAllocateApplyDao.updateAllocateApply(processApplyDTO);
            }else {
                throw new CommonException(Constants.ERROR_CODE, "不可以驳回申请");
            }

        }else {
            throw new CommonException(Constants.ERROR_CODE, "申请单状态不正确，不可以驳回申请");
        }


    }

    /**
     * 处理申请
     *
     * @param applyNo       申请单号
     * @param outstockOrgno 出库机构
     * @author zhangkangjian
     * @date 2018-09-12 17:47:14
     */
    @Override
    public void processOutStockOrg(String applyNo, String outstockOrgno) {
        ProcessApplyDTO processApplyDTO = new ProcessApplyDTO();
        StatusDtoThriftBean<BasicUserOrganization> outstockOrg = basicUserOrganizationService.findOrgByCode(outstockOrgno);
        StatusDto<BasicUserOrganization> basicUserOrganizationResolve = StatusDtoThriftUtils.resolve(outstockOrg, BasicUserOrganization.class);
        BasicUserOrganization basicUserOrganization = basicUserOrganizationResolve.getData();
        processApplyDTO.setOutstockOrgType(basicUserOrganization.getOrgType());
        processApplyDTO.setOutstockOrgno(outstockOrgno);
        processApplyDTO.setApplyNo(applyNo);
        Long versionNo = bizAllocateApplyDao.findVersionNo(processApplyDTO.getApplyNo());
        processApplyDTO.setVersionNo(versionNo);
        bizAllocateApplyDao.updateAllocateApply(processApplyDTO);
    }

    /**
     * 查询采购列表
     * @param queryPurchaseListDTO 查询条件
     * @return Page<QueryPurchaseListDTO>
     * @author zhangkangjian
     * @date 2018-09-13 09:58:35
     */
    @Override
    public Page<QueryPurchaseListDTO> queryPurchaseLise(QueryPurchaseListDTO queryPurchaseListDTO) {
        queryPurchaseListDTO.setApplyType(InstockTypeEnum.PURCHASE.name());
        Page<QueryPurchaseListDTO> allocateApplyByCode = bizAllocateApplyDao.queryAllocateApplyByCode(queryPurchaseListDTO);
        return allocateApplyByCode;
    }

    /**
     * 创建采购单
     * @param createPurchaseBillDTO
     * @author zhangkangjian
     * @date 2018-09-13 13:58:37
     */
    @Override
    public void createPurchaseBill(CreatePurchaseBillDTO createPurchaseBillDTO) {
        // 生成采购编号
        StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.CG);
        String data = stringStatusDto.getData();
        AllocateApplyDTO allocateApplyDTO = new AllocateApplyDTO();
        allocateApplyDTO.setApplyNo(data);
        String loggedUserId = userHolder.getLoggedUserId();
        allocateApplyDTO.setOperator(loggedUserId);
        allocateApplyDTO.setCreator(loggedUserId);
        allocateApplyDTO.setApplyer(loggedUserId);
        allocateApplyDTO.setApplyerName(userHolder.getLoggedUser().getName());
        String userOrgCode = getUserOrgCode();
        if(userOrgCode != null) {
            allocateApplyDTO.setApplyorgNo(userOrgCode);
        }
        // 默认申请的状态为待处理
        allocateApplyDTO.setApplyStatus(ApplyStatusEnum.PENDING.name());
        allocateApplyDTO.setProcessOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        allocateApplyDTO.setProcessOrgtype(OrganizationTypeEnum.PLATFORM.name());
        allocateApplyDTO.setOutstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        allocateApplyDTO.setOutstockOrgtype(OrganizationTypeEnum.PLATFORM.name());
        String inRepositoryNo = createPurchaseBillDTO.getInRepositoryNo();
        allocateApplyDTO.setInRepositoryNo(inRepositoryNo);
        String orgCode = bizServiceStorehouseDao.getOrgCodeByStoreHouseCode(allocateApplyDTO.getInRepositoryNo());
        allocateApplyDTO.setInstockOrgno(orgCode);
        allocateApplyDTO.setApplyStatus(ApplyStatusEnum.PENDING.name());
        allocateApplyDTO.setProcessType(InstockTypeEnum.PURCHASE.name());
        allocateApplyDTO.setApplyType(InstockTypeEnum.PURCHASE.name());
        // 保存申请单基础数据
        bizAllocateApplyDao.saveEntity(allocateApplyDTO);
        List<AllocateapplyDetailDTO> allocateapplyDetailList = createPurchaseBillDTO.getAllocateapplyDetailList();
        allocateApplyDTO.setAllocateapplyDetailList(allocateapplyDetailList);
        // 保存申请单详情数据
        batchInsertForapplyDetailList(allocateApplyDTO, loggedUserId, allocateApplyDTO.getProcessType());
    }

    /**
     * 采购单填报价格（确认报价）
     *
     * @param confirmationQuoteDTO 报价DTO
     * @author zhangkangjian
     * @date 2018-09-13 15:45:47
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmationQuote(ConfirmationQuoteDTO confirmationQuoteDTO) {
        List<BizAllocateTradeorder> list = Lists.newArrayList();
        // 批量更新采购申请详单的价格
         List<PurchaseProductInfo> purchaseProductInfo = confirmationQuoteDTO.getPurchaseProductInfo();
        bizAllocateApplyDao.batchupdatePurchaseProductInfo(purchaseProductInfo);
        List<PerpayAmountDTO> perpayAmountDTO = confirmationQuoteDTO.getPerpayAmountDTO();
        String applyNo = confirmationQuoteDTO.getApplyNo();

        // 根据申请单获取申请单详情
        List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
        if(null == details || details.size() == 0){
            throw new CommonException("0", "申请单为空！");
        }
        Map<String, List<AllocateapplyDetailBO>> allocateapplyDetailBOMap = details.stream().collect(Collectors.groupingBy(AllocateapplyDetailBO::getSupplierNo));
        Map<String, PerpayAmountDTO> perpayAmountDTOMap = perpayAmountDTO.stream().collect(Collectors.toMap(PerpayAmountDTO::getSupplierCode, a -> a,(k1,k2)->k1));
        allocateapplyDetailBOMap.forEach((key, value) -> {
            PerpayAmountDTO perpayAmountDTO1 = perpayAmountDTOMap.get(key);
            List<BizAllocateTradeorder> bizAllocateTradeorderList = purchaseApplyHandleStrategy.buildOrderEntityList(value);
            BizAllocateTradeorder bizAllocateTradeorder = bizAllocateTradeorderList.get(0);
            if(perpayAmountDTO1 != null){
                bizAllocateTradeorder.setSellerOrgno(key);
                bizAllocateTradeorder.setPerpayAmount(perpayAmountDTO1.getPerpayAmount());
                bizAllocateTradeorder.setTradeType(InstockTypeEnum.PURCHASE.name());
            }
            list.addAll(bizAllocateTradeorderList);
        });
        // 保存生成订单
        bizAllocateTradeorderDao.batchInsertAllocateTradeorder(list);
        // 生成出入库计划
        applyHandleContext.applyHandle(confirmationQuoteDTO.getApplyNo());
    }

    /**
     * 查询采购单的付款信息
     *
     * @param applyNo 采购单号
     * @return List<PerpayAmountDTO>
     * @author zhangkangjian
     * @date 2018-09-13 11:17:58
     */
    @Override
    public List<PerpayAmountDTO> queryPaymentInfo(String applyNo) {
        return bizAllocateTradeorderDao.queryPaymentInfo(applyNo);
    }

    /**
     * 根据申请单号和入库仓库查询入库计划
     * @param applyNo 申请单号
     * @param inRepositoryNo 入库仓库
     * @return 入库计划
     * @author zhangkangjian
     * @date 2018-08-11 13:17:42
     */
    @Override
    public List<BizInstockplanDetail> queryListByApplyNoAndInReNo(String applyNo, String inRepositoryNo) {
        return bizInstockplanDetailDao.queryListByApplyNoAndInReNo(applyNo, inRepositoryNo);
    }

    /**
     * 根据申请单号查询出库计划
     * @param applyNo         申请单号
     * @param outRepositoryNo 出库仓库编号
     * @return 出库计划
     * @author zhangkangjian
     * @date 2018-09-13 11:17:58
     */
    @Override
    public List<BizOutstockplanDetail> queryOutstockplan(String applyNo, String outRepositoryNo) {
        return bizOutstockplanDetailDao.queryOutstockplan(applyNo, outRepositoryNo);
    }

    /**
     *  更新申请单的详单数据
     * @param processApplyDTO
     * @param applyNo
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-30 11:15:58
     */
    private void batchUpdateForApplyDetail(ProcessApplyDTO processApplyDTO, String applyNo) {
        List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTOS = bizAllocateApplyDao.queryAllocateapplyDetail(applyNo);
        Optional.ofNullable(queryAllocateapplyDetailDTOS).ifPresent(a ->{
            Map<Long, QueryAllocateapplyDetailDTO> map = a.stream().collect(Collectors.toMap(QueryAllocateapplyDetailDTO::getId, b -> b,(k1, k2)->k1));
            List<ProcessApplyDetailDTO> processApplyDetailDTO = processApplyDTO.getProcessApplyDetailDTO();
            processApplyDetailDTO.stream().forEach(c ->{
                QueryAllocateapplyDetailDTO queryAllocateapplyDetailDTO = map.get(c.getId());
                if(queryAllocateapplyDetailDTO != null){
                    c.setSupplierNo(queryAllocateapplyDetailDTO.getSupplierNo());
                }
            });
        });
        bizAllocateApplyDao.batchUpdateForApplyDetail(processApplyDTO.getProcessApplyDetailDTO());
    }


    /**
     * 查询可调拨库存列表
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @Override
    public Page<FindStockListDTO> findStockList(FindStockListDTO findStockListDTO) {
        // 根据分类查询供应商的code
        List<BasicCarpartsProductDTO> carpartsProductDTOList = Lists.newArrayList();
        if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(findStockListDTO.getProductType())){
            // 查询类型下所有的code
            carpartsProductDTOList  = bizAllocateApplyDao.findEquipmentCode(findStockListDTO.getEquiptypeId());
        }else {
            // 查询分类下所有商品的code
            carpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCategoryCode(findStockListDTO.getCategoryCode());
        }
        List<String> productCode = carpartsProductDTOList.stream().map(BasicCarpartsProductDTO::getCarpartsCode).collect(Collectors.toList());
        if(productCode == null || productCode.size() == 0){
            return new Page<FindStockListDTO>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
        }
        // 根据类型查询服务中心的code
        List<String> orgCode = null;
        String orgNo = findStockListDTO.getOrgNo();
        if(StringUtils.isBlank(orgNo)){
            String type = findStockListDTO.getType();
            if(StringUtils.isBlank(type)){
                Page<FindStockListDTO> page = bizAllocateApplyDao.findStockList(findStockListDTO, productCode, null);
                return page;
            }else {
                orgCode = getOrgCodesByOrgType(type);
                if(orgCode == null || orgCode.size() == 0){
                    return new Page<>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
                }else {
                    Page<FindStockListDTO> page = bizAllocateApplyDao.findStockList(findStockListDTO, productCode, orgCode);
                    return page;
                }
            }
        }else {
            Page<FindStockListDTO> page = bizAllocateApplyDao.findStockList(findStockListDTO, productCode, orgCode);
            return page;
        }

    }

    /**
     *  根据类型查询服务中心的code
     * @param type 机构的类型
     * @return List<String> 机构的code
     * @author zhangkangjian
     * @date 2018-08-20 16:17:55
     */
    @Override
    public List<String> getOrgCodesByOrgType(String type) {
        if(StringUtils.isBlank(type)){
            return Collections.emptyList();
        }
        if(OrganizationTypeEnum.PLATFORM.name().equals(type)){
            return List.of(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        }
        QueryOrgDTO orgDTO = new QueryOrgDTO();
        orgDTO.setOrgType(type);
        orgDTO.setStatus(Constants.FREEZE_STATUS_YES);
        StatusDtoThriftList<BasicUserOrganization> basicUserOrganization = basicUserOrganizationService.queryOrgListByOrgType(type, true);
        StatusDto<List<BasicUserOrganization>> resolve = StatusDtoThriftUtils.resolve(basicUserOrganization, BasicUserOrganization.class);
        List<BasicUserOrganization> data = resolve.getData();
        List<String> orgCode = null;
        if(data != null && data.size() > 0){
            orgCode = data.stream().map(BasicUserOrganization::getOrgCode).collect(Collectors.toList());
        }
        return orgCode;
    }

    /**
     * 问题件申请查询(创建问题件，查询问题件列表)
     * @param orgCode 机构的code
     * @return StatusDto<List < StockBizStockDetailDTO>>
     * @author zhangkangjian
     * @date 2018-08-22 14:37:40
     */
    @Override
    public List<StockBizStockDetailDTO> queryProblemStockList(String orgCode, String productType) {
        return bizAllocateApplyDao.queryProblemStockList(orgCode, productType);
    }

    /**
     * 查询售后平台的信息
     *
     * @return StatusDto<BasicUserOrganization>
     * @author zhangkangjian
     * @date 2018-08-23 11:12:47
     */
    @Override
    public List<BasicUserOrganization> queryTopPlatform() {
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        StatusDto<BasicUserOrganization> resolve = StatusDtoThriftUtils.resolve(orgByCode, BasicUserOrganization.class);
        return List.of(resolve.getData());
    }

    /**
     * 根据物料code查询物料是否被申请
     * @param equipCode 物料code
     * @return 物料是否被申请
     * @author liuduo
     * @date 2018-08-23 16:01:38
     */
    @Override
    public Boolean getEquipMent(String equipCode) {
        return bizAllocateapplyDetailDao.getEquipMent(equipCode);
    }


    /**
     * 查询客户经理关联的服务中心
     *
     * @param useruuid
     * @return StatusDto<Map < String , String>>
     * @author zhangkangjian
     * @date 2018-08-24 17:37:13
     */
    @Override
    public Map<String, String> findCustManagerServiceCenter(String useruuid) throws IOException {
        if(StringUtils.isBlank(useruuid)){
            String loggedUserId = userHolder.getLoggedUserId();
            useruuid = loggedUserId;
        }
        HashMap<String, String> map = Maps.newHashMap();
        // 查询客户经理的详情
        BizServiceCustmanager bizServiceCustmanager = bizServiceCustmanagerDao.queryCustManagerByUuid(useruuid);
        if(bizServiceCustmanager == null){
            throw new CommonException(Constants.ERROR_CODE, "此用户不是客户经理");
        }
        String servicecenterCode = bizServiceCustmanager.getServicecenterCode();
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(servicecenterCode);
        StatusDto<BasicUserOrganization> resolve = StatusDtoThriftUtils.resolve(orgByCode, BasicUserOrganization.class);
        BasicUserOrganization data = resolve.getData();
        if(data == null){
            throw new CommonException(Constants.ERROR_CODE, "根据code查询服务中心，数据异常！");
        }
        String orgName = data.getOrgName();
        String orgCode = data.getOrgCode();
        map.put("orgName", orgName);
        map.put("orgCode", orgCode);

        String userOrgCode = getUserOrgCode();
        List<QueryStorehouseDTO> queryStorehouseDTOList = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(userOrgCode);
        if(queryStorehouseDTOList != null && queryStorehouseDTOList.size() > 0){
            QueryStorehouseDTO queryStorehouseDTO = queryStorehouseDTOList.get(0);
            String storehouseCode = queryStorehouseDTO.getStorehouseCode();
            String storehouseName = queryStorehouseDTO.getStorehouseName();
            map.put("storehouseCode", storehouseCode);
            map.put("storehouseName", storehouseName);
        }
        return map;
    }

    /**
     * 查询客户经理待领取的物料
     * @param completeStatus 状态（DOING待领取，COMPLETE已领取）
     * @param keyword     物料编号/物料名称
     * @param offset      偏移量
     * @param pageSize    每页显示的数量
     * @return Page<QueryPendingMaterialsDTO>
     * @author zhangkangjian
     * @date 2018-08-25 20:40:35
     */
    @Override
    public Page<QueryPendingMaterialsDTO> queryPendingMaterials(String completeStatus, String keyword, Integer offset, Integer pageSize) {
        return bizAllocateapplyDetailDao.queryPendingMaterials(completeStatus, keyword, userHolder.getLoggedUserId(), offset, pageSize);
    }

    /**
     * 客户经理领取物料
     * @param id 入库计划的id
     * @param productNo 商品的编号
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-27 14:53:10
     */
    @Override
    public void receivingmaterials(Long id, String productNo) {
        Assert.isTrue(id != null, "必填参数异常！");
        Assert.isTrue(StringUtils.isNotBlank(productNo), "必填参数异常！");
        BizInstockplanDetail bizInstockplanDetail = bizInstockplanDetailDao.queryListById(id);
        bizInstockplanDetail.setActualInstocknum(bizInstockplanDetail.getPlanInstocknum());
        instockOrderService.autoSaveInstockOrder(bizInstockplanDetail.getTradeNo(), bizInstockplanDetail.getInstockRepositoryNo(), List.of(bizInstockplanDetail));
    }

    /**
     * 查询当前登陆人的物料库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page < FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-31 14:47:54
     */
    @Override
    public Page<FindStockListDTO> findStockListByOrgCode(FindStockListDTO findStockListDTO) {
        // 根据分类查询供应商的code
        List<BasicCarpartsProductDTO> carpartsProductDTOList = Lists.newArrayList();
        if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(findStockListDTO.getProductType())){
            // 查询类型下所有的code
            carpartsProductDTOList  = bizAllocateApplyDao.findEquipmentCode(findStockListDTO.getEquiptypeId());
        }else {
            // 查询分类下所有商品的code
            carpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCategoryCode(findStockListDTO.getCategoryCode());
        }
        List<String> productCode = carpartsProductDTOList.stream().map(BasicCarpartsProductDTO::getCarpartsCode).collect(Collectors.toList());
        if(productCode == null || productCode.size() == 0){
            return new Page<FindStockListDTO>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
        }
        String userOrgCode = getUserOrgCode();
        Page<FindStockListDTO> page = bizAllocateApplyDao.findStockList(findStockListDTO, productCode, List.of(userOrgCode));
        return page;
    }

    /**
     * 查看所有零配件调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page < FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @Override
    public Page<FindStockListDTO> findAllStockList(FindStockListDTO findStockListDTO) {
        List<FindStockListDTO> findStockListDTOList = Lists.newArrayList();
        StatusDtoThriftPage<BasicCarpartsProductDTO> basicCarpartsProductDTO = carpartsProductService.queryCarpartsProductList(findStockListDTO.getCategoryCode(), findStockListDTO.getProductNo(), findStockListDTO.getOffset(), findStockListDTO.getPageSize());
        StatusDto<Page<BasicCarpartsProductDTO>> basicCarpartsProductDTOResolve = StatusDtoThriftUtils.resolve(basicCarpartsProductDTO, BasicCarpartsProductDTO.class);
        Page<BasicCarpartsProductDTO> basicCarpartsProductDTOPage = basicCarpartsProductDTOResolve.getData();
        // 统计库存列表里所有的库存
        Optional.ofNullable(basicCarpartsProductDTOPage.getRows()).ifPresent(a ->{
            List<String> productCodeList = a.stream().map(BasicCarpartsProductDTO::getCarpartsCode).collect(Collectors.toList());
            String userOrgCode = getUserOrgCode();
            List<FindStockListDTO> resFindStockListDTO = bizAllocateApplyDao.findAllStockList(productCodeList, userOrgCode);
            // 转map
            Map<String, FindStockListDTO> findStockListDTOMap = resFindStockListDTO.stream().collect(Collectors.toMap(FindStockListDTO::getProductNo, b -> b,(k1,k2)->k1));
            a.forEach(c ->{
                String carpartsCode = c.getCarpartsCode();
                FindStockListDTO getFindStockListDTO = findStockListDTOMap.get(carpartsCode);
                // 分类code截取
                String categoryCodePath = c.getCategoryCodePath();
                int i = categoryCodePath.indexOf("-") + 1;
                String substring = categoryCodePath.substring(i, categoryCodePath.length() - 1);
                if(getFindStockListDTO != null){
                    getFindStockListDTO.setProductCategoryname(substring);
                    findStockListDTOList.add(getFindStockListDTO);
                }else {
                    FindStockListDTO findStockListDTO1 = new FindStockListDTO(c.getCarpartsCode(), c.getCarpartsName(), substring, c.getUnitName(), 0);
                    findStockListDTOList.add(findStockListDTO1);
                }
            });
        });
        Page<FindStockListDTO> page = new Page<>(basicCarpartsProductDTOPage.getOffset(), basicCarpartsProductDTOPage.getLimit());
        page.setRows(findStockListDTOList);
        page.setTotal(basicCarpartsProductDTOPage.getTotal());
        page.setTotalPage(basicCarpartsProductDTOPage.getTotalPage());
        return page;
    }

    /**
     * 查看所有物料调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page < FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-31 14:47:54
     */
    @Override
    public Page<FindStockListDTO> findAllEquipmentStockList(FindStockListDTO findStockListDTO) {
        String userOrgCode = getUserOrgCode();
        Page<FindStockListDTO> page = bizAllocateApplyDao.findAllEquipmentStockList(findStockListDTO, userOrgCode);
        Optional.ofNullable(page.getRows()).ifPresent(a ->{
            a.stream().forEach(b -> {
                String unit = b.getUnit();
                if(StringUtils.isNotBlank(unit)){
                    b.setUnit(ProductUnitEnum.valueOf(unit).getLabel());
                }
            });
        });
        return page;
    }


    /**
     * 修改申请单状态
     * @param applyNo 申请单号
     * @param status 申请单状态
     * @author liuduo
     * @date 2018-08-10 13:44:37
     */
    @Override
    public void updateApplyOrderStatus(String applyNo, String status) {
        bizAllocateApplyDao.updateApplyOrderStatus(applyNo, status);
    }

    /**
     * 根据申请单状态查询申请单
     * @param status 申请单状态
     * @return 状态为等待收货的申请单
     * @author liuduo
     * @date 2018-08-11 12:56:39
     */
    @Override
    public List<String> queryApplyNo(List<String> status, String orgCode, String productType, Integer stockType) {
        return bizAllocateApplyDao.queryApplyNo(status, orgCode, productType, stockType);
    }

    /**
     * 查询可调拨库存列表
     * @param orgDTO 查询条件
     * @param productNo
     * @return StatusDtoThriftPage<QueryOrgDTO>
     * @author zhangkangjian
     * @date 2018-08-13 17:19:54
     */
    @Override
    public Page<QueryOrgDTO> queryTransferStock(QueryOrgDTO orgDTO, String productNo, Integer offset, Integer pageSize) {
        List<String> name = List.of(OrganizationTypeEnum.PLATFORM.name(), OrganizationTypeEnum.SERVICECENTER.name(), OrganizationTypeEnum.CUSTMANAGER.name());
        orgDTO.setStatus(Constants.FREEZE_STATUS_YES);
        orgDTO.setOrgTypeList(name);
        StatusDtoThriftList<QueryOrgDTO> queryOrgDTOList = basicUserOrganizationService.queryOrgAndWorkInfo(orgDTO);
        StatusDto<List<QueryOrgDTO>> resolve = StatusDtoThriftUtils.resolve(queryOrgDTOList, QueryOrgDTO.class);
        List<QueryOrgDTO> queryOrgList = resolve.getData();
        Map<String, QueryOrgDTO> queryOrgDTOMap = queryOrgList.stream().collect(Collectors.toMap(QueryOrgDTO::getOrgCode, a -> a,(k1,k2)->k1));
        List<String> orgCodes = queryOrgList.stream().map(QueryOrgDTO::getOrgCode).collect(Collectors.toList());
        orgCodes.add(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        // 查询库存数量
        Page<QueryOrgDTO> findStockListDTO = bizAllocateApplyDao.findStockNum(productNo, orgCodes , offset, pageSize);
        List<QueryOrgDTO> rows = findStockListDTO.getRows();
        rows.stream().forEach(a -> {
            QueryOrgDTO org = queryOrgDTOMap.get(a.getOrgCode());
            if(org != null){
                a.setAddress(org.getAddress());
                a.setProvince(org.getProvince());
                a.setOrgCode(org.getOrgCode());
                a.setOrgName(org.getOrgName());
                a.setCity(org.getCity());
            }

        });
        return findStockListDTO;
    }

    /**
     * 库存校验
     * @param checkStockQuantityDTO
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-15 14:10:42
     */
    @Override
    public StatusDto<List<ProductStockInfoDTO>> checkStockQuantity(CheckStockQuantityDTO checkStockQuantityDTO) {
        Map<String, Object> map = bizAllocateApplyDao.queryStockQuantity(checkStockQuantityDTO.getOutstockOrgno(), checkStockQuantityDTO.getSellerOrgno());
        StatusDto<List<ProductStockInfoDTO>> listStatusDto = getListStatusDto(map, checkStockQuantityDTO.getProductInfoList());
        return listStatusDto;
    }

    private StatusDto<List<ProductStockInfoDTO>> getListStatusDto(Map<String, Object> map, List<ProductStockInfoDTO> allocateapplyDetailList) {
        String flag = Constants.SUCCESS_CODE;
        String message = "成功";
        for (int i = 0; i < allocateapplyDetailList.size(); i++) {
            ProductStockInfoDTO allocateapplyDetailDTO = allocateapplyDetailList.get(i);
            String productno = allocateapplyDetailDTO.getProductNo();
            Object obj = map.get(productno);
            if(obj != null){
                Long applyNum = allocateapplyDetailDTO.getApplyProductNum();
                BigDecimal bd = (BigDecimal)obj;
                Long res = bd.longValue();
                if(applyNum > res){
                    allocateapplyDetailDTO.setRealProductNum(res);
                    flag = Constants.ERROR_CODE;
                    message = "失败";
                }
            }else {
                allocateapplyDetailDTO.setRealProductNum(0L);
                flag = Constants.ERROR_CODE;
                message = "失败";
            }
        }
        StatusDto<List<ProductStockInfoDTO>> listStatusDto = StatusDto.buildDataSuccessStatusDto(allocateapplyDetailList);
        listStatusDto.setCode(flag);
        listStatusDto.setMessage(message);
        return listStatusDto;
    }

    /**
     * 维修单检查库存
     * @param checkStockQuantityDTO
     * @return StatusDto<List < ProductStockInfoDTO>>
     * @author zhangkangjian
     * @date 2018-09-11 16:21:56
     */
    @Override
    public StatusDto<List<ProductStockInfoDTO>> checkRepairOrderStock(CheckRepairOrderStockDTO checkStockQuantityDTO) {

        // 查询出库机构下所有库存
        Map<String, Object> map = bizAllocateApplyDao.queryStockQuantity(checkStockQuantityDTO.getOutstockOrgno(), null);
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setServiceOrderno(checkStockQuantityDTO.getServiceOrderno());
        // 查询零配件信息
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.FITTING.name());
        // 查询零配件列表信息
        List<ProductDetailDTO> fittingDetail = claimOrderDao.queryMaintainitemDetail(productDetailDTO);
        fittingDetail.forEach(a ->{
            Object stockNum = map.get(a.getProductNo());
            if(stockNum != null){
                Long amount = a.getAmount();
                map.put(a.getProductNo(), (Long)stockNum + amount);
            }
        });
        List<ProductStockInfoDTO> allocateapplyDetailList = checkStockQuantityDTO.getProductInfoList();

        StatusDto<List<ProductStockInfoDTO>> listStatusDto = getListStatusDto(map, allocateapplyDetailList);
        return listStatusDto;
    }


    /**
     * 撤销申请单
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 12:02:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto cancelApply(String applyNo){
        return applyHandleContext.cancelApply(applyNo);
    }
}
