package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizAllocateApply.AllocateApplyTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply.ApplyStatusEnum;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleContext;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
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


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
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
    @Resource
    private ApplyHandleContext applyHandleContext;
    @Autowired
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;

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
        // 入库仓库查询入库组织机构
        String orgCode = bizServiceStorehouseDao.getOrgCodeByStoreHouseCode(allocateApplyDTO.getInRepositoryNo());
        allocateApplyDTO.setInstockOrgno(orgCode);
        // 生成申请单编号
        StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.SW);
        allocateApplyDTO.setApplyNo(stringStatusDto.getData());
        String userOrgCode = getUserOrgCode();
        if(userOrgCode != null) {
            allocateApplyDTO.setApplyorgNo(userOrgCode);
        }
        // 默认申请的状态为待处理
        allocateApplyDTO.setApplyStatus(ApplyStatusEnum.PENDING.name());

        // 默认出库机构类型
        String outOrgType = getOrgTypeByCode(allocateApplyDTO.getOutstockOrgno());
        if(StringUtils.isBlank(outOrgType)){
            allocateApplyDTO.setOutstockOrgtype(OrganizationTypeEnum.PLATFORM.name());
        }else {
            allocateApplyDTO.setOutstockOrgtype(outOrgType);
        }
        // 默认处理机构赋值
        if(allocateApplyDTO.getApplyorgNo().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
            allocateApplyDTO.setProcessOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setProcessOrgtype(OrganizationTypeEnum.PLATFORM.name());
        }else{
            allocateApplyDTO.setProcessOrgno(allocateApplyDTO.getOutstockOrgno());
            allocateApplyDTO.setProcessOrgtype(allocateApplyDTO.getOutstockOrgtype());
        }


        // 根据处理类型，设置申请的类型、各种相关机构、状态等属性
        String processType = allocateApplyDTO.getProcessType();
        if(AllocateApplyTypeEnum.PURCHASE.name().equals(processType)){
            allocateApplyDTO.setApplyType(AllocateApplyTypeEnum.PURCHASE.name());
            allocateApplyDTO.setOutstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setProcessOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setApplyStatus(ApplyStatusEnum.WAITINGPAYMENT.name());
        }else if(AllocateApplyTypeEnum.BARTER.name().equals(processType) || AllocateApplyTypeEnum.REFUND.equals(processType)){
            allocateApplyDTO.setApplyType(processType);
            StatusDto<String> thcode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.TH);
            allocateApplyDTO.setApplyNo(thcode.getData());

            allocateApplyDTO.setOutstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setProcessOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            allocateApplyDTO.setApplyStatus(BizAllocateApply.ReturnApplyStatusEnum.PRODRETURNED.name());
        }else{
            // 根据处理机构类型判断
            if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(allocateApplyDTO.getProcessOrgno())){
                allocateApplyDTO.setApplyType(AllocateApplyTypeEnum.PLATFORMALLOCATE.name());
                allocateApplyDTO.setApplyStatus(ApplyStatusEnum.WAITINGPAYMENT.name());
            }else {
                allocateApplyDTO.setApplyType(AllocateApplyTypeEnum.SAMELEVEL.name());
            }
        }
        // 如果申请机构和处理机构是同一人，说明创建和处理是一起做的，则处理已经完成了
        if (allocateApplyDTO.getApplyorgNo().equals(allocateApplyDTO.getProcessOrgno())) {
            allocateApplyDTO.setApplyProcessor(loggedUserId);
            allocateApplyDTO.setProcessTime(new Date());
        }
        // 保存申请单基础数据
        bizAllocateApplyDao.saveEntity(allocateApplyDTO);
        // 保存申请单详情数据
        batchInsertForapplyDetailList(allocateApplyDTO, loggedUserId, processType);

        // 已经自动处理了，调用 申请构建计划的方法
        if(allocateApplyDTO.getApplyorgNo().equals(allocateApplyDTO.getProcessOrgno()) || AllocateApplyTypeEnum.BARTER.name().equals(processType)
            || AllocateApplyTypeEnum.REFUND.equals(processType)){
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
        allocateapplyDetailList.stream().forEach(a -> {
            a.setApplyNo(allocateApplyDTO.getApplyNo());
            a.setOperator(loggedUserId);
            a.setCreator(loggedUserId);
            if(AllocateApplyTypeEnum.BARTER.name().equals(processType) || AllocateApplyTypeEnum.REFUND.equals(processType)){
                a.setStockType(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.name());
            }else {
                a.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
            }
        });
        bizAllocateApplyDao.batchInsertForapplyDetailList(allocateapplyDetailList);
    }

    /**
     *  查询组织架构类型
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
     *
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    @Override
    public FindAllocateApplyDTO findDetail(String applyNo) {
        FindAllocateApplyDTO allocateApplyDTO = bizAllocateApplyDao.findDetail(applyNo);
        // 查询组织架构的名字
        StatusDtoThriftBean<BasicUserOrganization> outstockOrgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getOutstockOrgno());
        StatusDtoThriftBean<BasicUserOrganization> instockOrgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getInstockOrgno());
        StatusDtoThriftBean<BasicUserOrganization> applyorgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getApplyorgNo());
        StatusDto<BasicUserOrganization> outstockOrgNameresolve = StatusDtoThriftUtils.resolve(outstockOrgName, BasicUserOrganization.class);
        StatusDto<BasicUserOrganization> instockOrgNameresolve = StatusDtoThriftUtils.resolve(instockOrgName, BasicUserOrganization.class);
        StatusDto<BasicUserOrganization> applyorgNameResolve = StatusDtoThriftUtils.resolve(applyorgName, BasicUserOrganization.class);
        BasicUserOrganization outstockOrgdata = outstockOrgNameresolve.getData();
        BasicUserOrganization instockOrgdata = instockOrgNameresolve.getData();
        BasicUserOrganization applyorgNamedata = applyorgNameResolve.getData();
        // 设置来源
        allocateApplyDTO.setOrgType(applyorgNamedata.getOrgType());
        if (outstockOrgdata != null) {
            String orgName = outstockOrgdata.getOrgName();
            allocateApplyDTO.setOutstockOrgName(orgName);
        }
        if (instockOrgdata != null) {
            String orgName = instockOrgdata.getOrgName();
            allocateApplyDTO.setInstockOrgName(orgName);
        }
        if(applyorgNamedata != null){
            String orgName = applyorgNamedata.getOrgName();
            allocateApplyDTO.setApplyorgName(orgName);
        }
        // 查询申请单的详单
        List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTOS = bizAllocateApplyDao.queryAllocateapplyDetail(applyNo);
        if (queryAllocateapplyDetailDTOS != null) {
            String productType = queryAllocateapplyDetailDTOS.get(0).getProductType();
            if (!Constants.PRODUCT_TYPE_EQUIPMENT.equals(productType)) {
                // 查询零配件的名称
                List<String> productNo = queryAllocateapplyDetailDTOS.stream().map(QueryAllocateapplyDetailDTO::getProductNo).collect(Collectors.toList());
                StatusDtoThriftList<BasicCarpartsProductDTO> basicCarpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCarpartsCodes(productNo);
                if (basicCarpartsProductDTOList.isSuccess()) {
                    StatusDto<List<BasicCarpartsProductDTO>> resolve = StatusDtoThriftUtils.resolve(basicCarpartsProductDTOList, BasicCarpartsProductDTO.class);
                    List<BasicCarpartsProductDTO> basicCarpartsProductList = resolve.getData();
                    if (basicCarpartsProductList != null) {
                        Map<String, BasicCarpartsProductDTO> basicCarpartsProductDTOMap = basicCarpartsProductList.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, a -> a, (k1, k2) -> k1));
                        queryAllocateapplyDetailDTOS.stream().forEach(a -> {
                            BasicCarpartsProductDTO basicCarpartsProductDTO = basicCarpartsProductDTOMap.get(a.getProductNo());
                            if (basicCarpartsProductDTO != null) {
                                a.setProductName(basicCarpartsProductDTO.getCarpartsName());
                            }
                        });
                    }
                }
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
    public Page<QueryAllocateApplyListDTO> findApplyList(String productType, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize) {
        // 获取用户的组织机构
        String userOrgCode = getUserOrgCode();
        // 查询分页的申请列表
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findApplyList(productType, processType, applyStatus, applyNo, offset, pageSize, userOrgCode);
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
     * 查询处理申请列表
     *
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
        String userOrgCode = getUserOrgCode();
        List<String> orgCodesByOrgType = getOrgCodesByOrgType(orgType);

        // 查询分页的申请列表
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findProcessApplyList(productType,orgCodesByOrgType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<String> orgCode = rows.stream().map(QueryAllocateApplyListDTO::getApplyorgNo).collect(Collectors.toList());
        Map<String, BasicUserOrganization> basicUserOrganizationMap = basicUserOrganizationService.queryOrganizationByOrgCodes(orgCode);
        rows.stream().forEach(a ->{
            BasicUserOrganization basicUserOrganization = basicUserOrganizationMap.get(a.getApplyorgNo());
            if(basicUserOrganization != null){
                a.setOrgName(basicUserOrganization.getOrgName());
                a.setOrgType(basicUserOrganization.getOrgType());
            }
        });
        return page;
    }

    /**
     * 人为触发的正常调拨类型的申请处理
     * @param processApplyDTO
     * @exception
     * @author zhangkangjian
     * @date 2018-08-10 11:24:53
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processApply(ProcessApplyDTO processApplyDTO) {
        String processType = processApplyDTO.getProcessType();
        // 如果是调拨类型的
        if(BizAllocateApply.ProcessTypeEnum.TRANSFER.name().equals(processType)){
            String applyNo = processApplyDTO.getApplyNo();
            FindAllocateApplyDTO detail = findDetail(applyNo);
            String processOrgno = detail.getProcessOrgno();
            // 根据处理类型判断
            if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(processOrgno)){
                processApplyDTO.setApplyType(AllocateApplyTypeEnum.PLATFORMALLOCATE.name());
            }else {
                processApplyDTO.setApplyType(AllocateApplyTypeEnum.SAMELEVEL.name());
            }
            String outstockOrgno = processApplyDTO.getOutstockOrgno();
            if(StringUtils.isBlank(outstockOrgno)){
                 throw new CommonException(Constants.ERROR_CODE, "参数异常！");
            }
            String orgTypeByCode = getOrgTypeByCode(outstockOrgno);
            processApplyDTO.setOutstockOrgType(orgTypeByCode);
        }else {
            processApplyDTO.setApplyType(AllocateApplyTypeEnum.PURCHASE.name());
        }

        // 查询版本号（数据库乐观锁）
        Long versionNo = bizAllocateApplyDao.findVersionNo(processApplyDTO.getApplyNo());
        processApplyDTO.setVersionNo(versionNo);
        // 处理通过，更新申请单状态
        processApplyDTO.setApplyStatus(ApplyStatusEnum.WAITINGPAYMENT.name());
        // 更新申请单的基础数据
        processApplyDTO.setApplyProcessor(userHolder.getLoggedUserId());
        processApplyDTO.setProcessTime(new Date());
        bizAllocateApplyDao.updateAllocateApply(processApplyDTO);
        // 更新申请单的详单数据
        bizAllocateApplyDao.batchUpdateForApplyDetail(processApplyDTO.getProcessApplyDetailDTO());
        // 生成出入库计划
        applyHandleContext.applyHandle(processApplyDTO.getApplyNo());
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
        List<String> productCode = null;
            List<BasicCarpartsProductDTO> carpartsProductDTOList;
            if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(findStockListDTO.getProductType())){
                // 查询类型下所有的code
                carpartsProductDTOList  = bizAllocateApplyDao.findEquipmentCode(findStockListDTO.getEquiptypeId());
            }else {
                // 查询分类下所有商品的code
                carpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCategoryCode(findStockListDTO.getCategoryCode());
            }

            productCode = carpartsProductDTOList.stream().map(BasicCarpartsProductDTO::getCarpartsCode).collect(Collectors.toList());
            if(productCode == null || productCode.size() == 0){
                return new Page<FindStockListDTO>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
            }
        // 根据类型查询服务中心的code
        List<String> orgCode = null;
        if(StringUtils.isBlank(findStockListDTO.getOrgNo())){
            orgCode = getOrgCodesByOrgType(findStockListDTO.getType());
        }
        // 如果类型是null的，查询全部库存
        if(StringUtils.isBlank(findStockListDTO.getType())){
            Page<FindStockListDTO> page = bizAllocateApplyDao.findStockList(findStockListDTO, productCode, orgCode);
            return page;
        }else {
            if(orgCode == null || orgCode.size() == 0){
                return new Page<>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
            }else {
                Page<FindStockListDTO> page = bizAllocateApplyDao.findStockList(findStockListDTO, productCode, orgCode);
                return page;
            }
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
        StatusDtoThriftList<QueryOrgDTO> queryOrgDTO = basicUserOrganizationService.queryOrgAndWorkInfo(orgDTO);
        StatusDto<List<QueryOrgDTO>> queryOrgDTOResolve = StatusDtoThriftUtils.resolve(queryOrgDTO, QueryOrgDTO.class);
        List<QueryOrgDTO> data = queryOrgDTOResolve.getData();
        List<String> orgCode = null;
        if(data != null && data.size() > 0){
            orgCode = data.stream().map(QueryOrgDTO::getOrgCode).collect(Collectors.toList());
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
     * @param applyNoStatus 申请单状态
     * @return 状态为等待收货的申请单
     * @author liuduo
     * @date 2018-08-11 12:56:39
     */
    @Override
    public List<String> queryApplyNo(String applyNoStatus, String orgCode, String productType, Integer stockType) {
        return bizAllocateApplyDao.queryApplyNo(applyNoStatus, orgCode, productType, stockType);
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
        // todo 张康健将组织机构作为入参
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
        String flag = Constants.SUCCESS_CODE;
        String message = "成功";
       /* FindAllocateApplyDTO detail = findDetail(checkStockQuantityDTO.getOutstockOrgno());
        // 如果是平台调拨，需要校验库存来源
        if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(detail.getProcessOrgno())){
            checkStockQuantityDTO.setSellerOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
        }*/
        Map<String, Object> map = bizAllocateApplyDao.queryStockQuantity(checkStockQuantityDTO.getOutstockOrgno(), checkStockQuantityDTO.getSellerOrgno());
        List<ProductStockInfoDTO> allocateapplyDetailList = checkStockQuantityDTO.getProductInfoList();
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
