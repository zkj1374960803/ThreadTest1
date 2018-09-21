package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.ProblemAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.BarterApplyHandleStrategy;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.RefundApplyHandleStrategy;
import com.ccbuluo.business.platform.outstock.dao.BizOutstockOrderDao;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockDetailDTO;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findProblemApplyList(type, applyType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<String> applyNos = null;
        List<QueryAllocateApplyListDTO> applyList = new ArrayList<QueryAllocateApplyListDTO>();
        if(rows != null){
            // 查出申请单号
            applyNos = rows.stream().map(QueryAllocateApplyListDTO::getApplyNo).collect(Collectors.toList());
            applyList = problemAllocateApplyDao.queryProblemHandleList(applyNos,BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
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
        // 如果类型是空的话，全部类型，查询所有的申请数据
        Page<QueryAllocateApplyListDTO> page;
        page = bizAllocateApplyDao.findProblemProcessHandleList(processType, type, applyStatus, applyNo, offset, pageSize);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<QueryAllocateApplyListDTO> applyList = new ArrayList<QueryAllocateApplyListDTO>();
        List<String> applyNos = null;
        if(rows != null){
            // 查出申请单号
            applyNos = rows.stream().map(QueryAllocateApplyListDTO::getApplyNo).collect(Collectors.toList());
            // 查询平台的入库计划
            applyList = problemAllocateApplyDao.queryProblemHandleList(applyNos,BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
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
     *  根据类型查询服务中心的code
     * @param type 机构的类型
     * @return List<String> 机构的code
     * @author zhangkangjian
     * @date 2018-08-20 16:17:55
     */
    private List<String> getOrgCodesByOrgType(String type) {
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
     * 查询退换货申请单详情(申请)
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 20:02:58
     */
    @Override
    public FindAllocateApplyDTO getProblemdetailApplyDetail(String applyNo){
        FindAllocateApplyDTO allocateApplyDTO = allocateApplyServiceImpl.findDetail(applyNo);
        // 获取出库人和出库时间
        ProblemAllocateapplyDetailDTO info = problemAllocateApplyDao.getProblemdetailApplyDetail(applyNo, allocateApplyDTO.getApplyorgNo());
        if(null != info){
            String inOperatorName = getUserNameByUuid(info.getInstockOperator());
            allocateApplyDTO.setOutstockOperatorName(allocateApplyDTO.getApplyerName());// 出库人（自动出库人）
            allocateApplyDTO.setOutstockTime(allocateApplyDTO.getCreateTime());// 出库时间 （自动出库时间）
            allocateApplyDTO.setInstockOperatorName(inOperatorName); // 入库人
            allocateApplyDTO.setInstockTime(info.getInstockTime());// 入库时间
            allocateApplyDTO.setTransportorderNo(info.getTransportorderNo());// 物流单号
            allocateApplyDTO.setTotalPrice(info.getTotalPrice());
        }
        // 计算成本价格
        convertCostPrice(allocateApplyDTO);
        return allocateApplyDTO;
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
            allocateApplyDTO.setTransportorderNo(info.getTransportorderNo());// 物流单号
            allocateApplyDTO.setTotalPrice(info.getTotalPrice());
        }
        // 计算成本价格
        convertCostPrice(allocateApplyDTO);
        return allocateApplyDTO;
    }
    /**
     *  获取成本价格
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-03 10:18:33
     */
    private void convertCostPrice(FindAllocateApplyDTO allocateApplyDTO){
         List<StockDetailDTO> list = problemStockDetailDao.queryStockDetailListByAppNo(allocateApplyDTO.getApplyorgNo());
        for(QueryAllocateapplyDetailDTO applyDetail : allocateApplyDTO.getQueryAllocateapplyDetailDTO()){
            Optional<StockDetailDTO> applyFilter = list.stream() .filter(stockDetail -> applyDetail.getProductNo().equals(stockDetail.getProductNo())) .findFirst();
            if (applyFilter.isPresent()) {
                applyDetail.setCostPrice(applyFilter.get().getCostPrice());
            }
        }
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

}
