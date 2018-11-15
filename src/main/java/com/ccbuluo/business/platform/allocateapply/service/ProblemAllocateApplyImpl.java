package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.ProblemAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.BarterApplyHandleStrategy;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.RefundApplyHandleStrategy;
import com.ccbuluo.business.platform.instock.dao.BizInstockOrderDao;
import com.ccbuluo.business.platform.instock.dto.BizInstockOrderDTO;
import com.ccbuluo.business.platform.order.service.fifohandle.BarterStockInOutCallBack;
import com.ccbuluo.business.platform.outstock.dao.BizOutstockOrderDao;
import com.ccbuluo.business.platform.outstock.dto.BizOutstockOrderDTO;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockDetailDTO;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.business.platform.supplier.dto.QuerySupplierInfoDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述（1）
 *
 * @author weijb
 * @version v1.0.0
 * @date 创建时间（2）
 */
@Service
public class ProblemAllocateApplyImpl implements ProblemAllocateApply {

    @Resource
    private ProblemAllocateApplyDao problemAllocateApplyDao;
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyServiceImpl;
    @Resource
    BizOutstockOrderDao bizOutstockOrderDao;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @Autowired
    UserHolder userHolder;
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @Resource
    ProblemStockDetailDao problemStockDetailDao;
    @Resource
    private RefundApplyHandleStrategy refundApplyHandleStrategy;
    @Resource
    private BarterApplyHandleStrategy barterApplyHandleStrategy;
    @Autowired
    private BarterStockInOutCallBack barterStockInOutCallBack;
    @Autowired
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Resource
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    private BizServiceSupplierDao bizServiceSupplierDao;

    @Resource
    private BizInstockOrderDao bizInstockOrderDao;
    /**
     * 问题件申请列表
     *   @param type 物料或是零配件
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<ProblemAllocateapplyDetailDTO> queryProblemApplyList(String type,String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        return problemAllocateApplyDao.queryProblemApplyList(type,null,applyType, applyStatus, applyNo, offset, pageSize);
    }
    /**
     * 问题件申请列表
     *  @param type 物料或是零配件
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<QueryAllocateApplyListDTO> querySelfProblemApplyList(String type,String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        Page<ProblemAllocateapplyDetailDTO> result = new Page<ProblemAllocateapplyDetailDTO>();
        // 获取用户的组织机构
        String userOrgCode = getUserOrgCode();
        // 查询分页的申请列表
        // 设置查询类型
        List<String> applyTypeList = List.of(BizAllocateApply.AllocateApplyTypeEnum.BARTER.name(),
            BizAllocateApply.AllocateApplyTypeEnum.REFUND.name(),
            BizAllocateApply.AllocateApplyTypeEnum.PLATFORMREFUND.name(),
            BizAllocateApply.AllocateApplyTypeEnum.PLATFORMBARTER.name());
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findProblemApplyList(type, applyType, applyStatus, applyNo, offset, pageSize, userOrgCode, applyTypeList);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<String> applyNos = null;
        List<QueryAllocateApplyListDTO> applyList = new ArrayList<QueryAllocateApplyListDTO>();
        if(rows != null){
            // 查出申请单号
            applyNos = rows.stream().map(QueryAllocateApplyListDTO::getApplyNo).collect(Collectors.toList());
            applyList = problemAllocateApplyDao.queryProblemHandleList(applyNos,BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM, applyTypeList);
        }
        for(QueryAllocateApplyListDTO apply : rows){
            Optional<QueryAllocateApplyListDTO> applyFilter = applyList.stream() .filter(applyDetail -> apply.getApplyNo().equals(applyDetail.getApplyNo())) .findFirst();
            if (applyFilter.isPresent()) {
                apply.setInstockTime(applyFilter.get().getInstockTime());
                apply.setOutstockTime(applyFilter.get().getOutstockTime());
            }
        }
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
     * 问题件处理列表
     *  @param type 物料或是零配件
     * @param processType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
     */
    @Override
    public Page<QueryAllocateApplyListDTO> queryProblemHandleList(String type,String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize){

        // 设置查询类型
        List<String> applyTypeList = List.of(BizAllocateApply.AllocateApplyTypeEnum.BARTER.name(),
            BizAllocateApply.AllocateApplyTypeEnum.REFUND.name(),
            BizAllocateApply.AllocateApplyTypeEnum.PLATFORMREFUND.name(),
            BizAllocateApply.AllocateApplyTypeEnum.PLATFORMBARTER.name());
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findProblemProcessHandleList(processType, type, applyStatus, applyNo, offset, pageSize, applyTypeList);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<QueryAllocateApplyListDTO> applyList = new ArrayList<QueryAllocateApplyListDTO>();
        List<String> applyNos = null;
        if(rows != null){
            // 查出申请单号
            applyNos = rows.stream().map(QueryAllocateApplyListDTO::getApplyNo).collect(Collectors.toList());
            // 查询平台的入库计划
            applyList = problemAllocateApplyDao.queryProblemHandleList(applyNos,BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM, applyTypeList);
            for(QueryAllocateApplyListDTO apply : rows){
                Optional<QueryAllocateApplyListDTO> applyFilter = applyList.stream() .filter(applyDetail -> apply.getApplyNo().equals(applyDetail.getApplyNo())) .findFirst();
                if (applyFilter.isPresent()) {
                    apply.setInstockTime(applyFilter.get().getInstockTime());
                    apply.setOutstockTime(applyFilter.get().getOutstockTime());
                }
            }
            allocateApplyServiceImpl.findOrgName(page);
        }
        return page;
    }

    /**
     * 查询退换货申请单详情(申请)
     *
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 20:02:58
     */
    @Override
    public FindAllocateApplyDTO getProblemdetailApplyDetail(String applyNo) {
        return null;
    }


    /**
     * 查询退换货申请单详情（处理）
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 20:02:58
     */
    @Override
    public FindAllocateApplyDTO getProblemdetailDetail(String applyNo){
        FindAllocateApplyDTO allocateApplyDTO = allocateApplyServiceImpl.findDetail(applyNo);
        // 获取出库人和出库时间
        ProblemAllocateapplyDetailDTO info = problemAllocateApplyDao.queryProblemApplyInfo(applyNo, allocateApplyDTO.getProcessOrgno());
        if(null != info){
            String inOperatorName = getUserNameByUuid(info.getInstockOperator());
            allocateApplyDTO.setOutstockOperatorName(allocateApplyDTO.getApplyerName());// 出库人（自动出库人）
            allocateApplyDTO.setOutstockTime(allocateApplyDTO.getCreateTime());// 出库时间 （自动出库时间）
            allocateApplyDTO.setInstockOperatorName(inOperatorName); // 入库人
            allocateApplyDTO.setInstockTime(info.getInstockTime());// 入库时间
            BizOutstockOrderDTO sigleOutStockByTradeDocno = bizOutstockOrderDao.getSigleOutStockByTradeDocno(applyNo);
            allocateApplyDTO.setTransportorderNo(sigleOutStockByTradeDocno.getTransportorderNo());// 物流单号
            allocateApplyDTO.setTotalPrice(info.getTotalPrice());
        }
        // 设置入库仓库名称
        QueryStorehouseDTO queryStorehouseDTO = bizServiceStorehouseDao.queryQueryStorehouseDTOByCode(allocateApplyDTO.getInRepositoryNo());
        if(queryStorehouseDTO != null){
            allocateApplyDTO.setInRepositoryName(queryStorehouseDTO.getStorehouseName());
        }
        // 设置申请完成时间
        if(!BizAllocateApply.ReturnApplyStatusEnum.REPLACECOMPLETED.name().equals(allocateApplyDTO.getApplyStatus())
            && !BizAllocateApply.ReturnApplyStatusEnum.REFUNDCOMPLETED.name().equals(allocateApplyDTO.getApplyStatus())) {
            // 状态没有完成，时间为空
            allocateApplyDTO.setOperateTime(null);
        }
        // 根据申请单号查询入库单号
        BizInstockOrderDTO instockOrderDTO = bizInstockOrderDao.getByTradeDocno(applyNo);
        if(instockOrderDTO != null){
            QueryStorehouseDTO queryStorehouse = bizServiceStorehouseDao.queryQueryStorehouseDTOByCode(instockOrderDTO.getInRepositoryNo());
            if(queryStorehouse != null){
                // 设置平台入库仓库
                allocateApplyDTO.setPlatformInRepositoryName(queryStorehouse.getStorehouseName());
            }
        }
        // 设置问题件详单
        ArrayList<QueryAllocateapplyDetailDTO> allocateapplyDetailList = setingProblemDetailList(applyNo, allocateApplyDTO);
        allocateApplyDTO.setQueryAllocateapplyDetailDTO(allocateapplyDetailList);
        return allocateApplyDTO;
    }

    /**
     * 查询问题件详单
     * @param applyNo 申请单编号
     * @param allocateApplyDTO 问题件信息
     * @return ArrayList<QueryAllocateapplyDetailDTO>
     * @author zhangkangjian
     * @date 2018-11-09 15:02:32
     */
    private ArrayList<QueryAllocateapplyDetailDTO> setingProblemDetailList(String applyNo, FindAllocateApplyDTO allocateApplyDTO) {
        String applyorgNo = allocateApplyDTO.getApplyorgNo();
        List<BizOutstockplanDetail> bizOutstockplanDetails = bizOutstockplanDetailDao.queryOutstockplan(applyNo, null, null, null);
        allocateApplyServiceImpl.buildStockPlanDetail(bizOutstockplanDetails);
        ArrayList<QueryAllocateapplyDetailDTO> allocateapplyDetailList = Lists.newArrayList();
        List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTO = allocateApplyDTO.getQueryAllocateapplyDetailDTO();
        if(queryAllocateapplyDetailDTO != null && queryAllocateapplyDetailDTO.size() > 0){
            Map<String, QueryAllocateapplyDetailDTO> allocateapplyDetailMap = queryAllocateapplyDetailDTO.stream().collect(Collectors.toMap(QueryAllocateapplyDetailDTO::getProductNo, a -> a,(k1, k2)->k1));
            if(bizOutstockplanDetails != null && bizOutstockplanDetails.size() > 0){
                bizOutstockplanDetails.forEach(a ->{
                    try {
                        QueryAllocateapplyDetailDTO queryAllocateapply = allocateapplyDetailMap.get(a.getProductNo());
                        QueryAllocateapplyDetailDTO cloneAllocateapply = queryAllocateapply.clone();
                        cloneAllocateapply.setApplyNum(a.getActualOutstocknum());
                        cloneAllocateapply.setCostPrice(a.getCostPrice());
                        QuerySupplierInfoDTO querySupplierInfoDTO =
                            bizServiceSupplierDao.querySupplierInfoByCode(a.getSupplierNo());
                        cloneAllocateapply.setSupplierName(querySupplierInfoDTO.getSupplierName());
                        cloneAllocateapply.setCarpartsMarkno(a.getCarpartsMarkno());
                        cloneAllocateapply.setProductName(a.getProductName());
                        cloneAllocateapply.setUnit(a.getProductUnit());
                        cloneAllocateapply.setCarpartsImage(a.getCarpartsImage());
                        cloneAllocateapply.setCostPrice(a.getCostPrice());
                        allocateapplyDetailList.add(cloneAllocateapply);
                    } catch (Exception e) {
                        throw new CommonException(Constants.ERROR_CODE, "拷贝失败！");
                    }
                });
            }
        }
        return allocateapplyDetailList;
    }

    /**
     *  根据用户uuid获取用户名字
     * @param uuid
     * @exception 
     * @return 
     * @author weijb
     * @date 2018-08-21 16:46:25
     */
    private String getUserNameByUuid(String uuid){
        if(StringUtils.isBlank(uuid)){
            return "";
        }
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(uuid);
        StatusDto<UserInfoDTO> resolve = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class);
        String operatorName = "";
        if(null != resolve){
            operatorName = resolve.getData().getName();
        }
        return operatorName;
    }

    /**
     * 更改退换货类型
     * @param applyNo 申请单号
     * @param applyType 申请类型
     * @return StatusDto
     * @author weijb
     * @date 2018-09-21 19:02:58
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto changeApplyType(String applyNo, String applyType){
        // 根据申请单获取申请单详情 TODO 本期先不做
        BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
        if(null == ba || StringUtils.isNotBlank(applyType)){
            throw new CommonException(Constants.ERROR_CODE, "不存在的申请单！");
        }
        // 退款
        if(BizAllocateApply.AllocateApplyTypeEnum.REFUND.name().equals(applyType)){
            // 先清理换货的数据
            barterApplyHandleStrategy.cancelApply(applyNo);
            refundApplyHandleStrategy.applyHandle(ba);

        }
        // 换货
        if(BizAllocateApply.AllocateApplyTypeEnum.BARTER.name().equals(applyType)){
            // 先清理退款的数据
            refundApplyHandleStrategy.cancelApply(applyNo);
            barterApplyHandleStrategy.applyHandle(ba);
        }
        return StatusDto.buildSuccessStatusDto("更改成功！");
    }

    /**
     * 平台处理机构发起的退换货申请生成平台出库计划及机构的入库计划
     * @param applyNo 申请单号
     * @return 是否成功
     * @author liuduo
     * @date 2018-10-31 10:55:41
     */
    @Override
    public StatusDto generateOutStockPlan(String applyNo) {
        // 判断有没有生成出库计划
        List<BizOutstockplanDetail> outstockplansByApplyNo = bizOutstockplanDetailDao.getOutstockplansByApplyNo(applyNo, userHolder.getLoggedUser().getOrganization().getOrgCode());
        if (null != outstockplansByApplyNo && !outstockplansByApplyNo.isEmpty()) {
            return StatusDto.buildSuccessStatusDto();
        }
        // 生成出库计划
        BizAllocateApply apply = bizAllocateApplyDao.getByNo(applyNo);
        String curretOrgNo = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 只有卖方机构入库的时候才生成出库计划
        if(curretOrgNo.equals(apply.getOutstockOrgno())){
            barterStockInOutCallBack.platformInstockCallback(applyNo);
        }
        return StatusDto.buildSuccessStatusDto();
    }

}
