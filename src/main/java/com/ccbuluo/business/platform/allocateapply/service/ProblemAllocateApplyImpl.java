package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.ProblemAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProblemAllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.outstock.dao.BizOutstockOrderDao;
import com.ccbuluo.business.platform.outstock.dto.BizOutstockOrderDTO;
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
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
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
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findApplyList(type, applyType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<String> applyNos = null;
        if(rows != null){
            // 查出申请单号
            applyNos = rows.stream().map(QueryAllocateApplyListDTO::getApplyNo).collect(Collectors.toList());
            rows = problemAllocateApplyDao.queryProblemHandleList(applyNos);
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
        String userOrgCode = getUserOrgCode();
        List<String> orgCodesByOrgType = getOrgCodesByOrgType(OrganizationTypeEnum.PLATFORM.name());
        // 如果类型是空的话，全部类型，查询所有的申请数据
        Page<QueryAllocateApplyListDTO> page;
        page = bizAllocateApplyDao.findProcessHandleList(processType, type,orgCodesByOrgType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        List<QueryAllocateApplyListDTO> rows = page.getRows();
        List<String> applyNos = null;
        if(rows != null){
            // 查出申请单号
            applyNos = rows.stream().map(QueryAllocateApplyListDTO::getApplyNo).collect(Collectors.toList());
            rows = problemAllocateApplyDao.queryProblemHandleList(applyNos);
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
            String outOperatorName = getUserNameByUuid(info.getOutstockOperator());
            String inOperatorName = getUserNameByUuid(info.getInstockOperator());
            allocateApplyDTO.setOutstockOperatorName(outOperatorName);// 出库人
            allocateApplyDTO.setOutstockTime(info.getOutstockTime());// 出库时间
            allocateApplyDTO.setInstockOperatorName(inOperatorName); // 入库人
            allocateApplyDTO.setInstockTime(info.getInstockTime());// 入库时间
            allocateApplyDTO.setTransportorderNo(info.getTransportorderNo());// 物流单号
        }
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
            String outOperatorName = getUserNameByUuid(info.getOutstockOperator());
            String inOperatorName = getUserNameByUuid(info.getInstockOperator());
            allocateApplyDTO.setOutstockOperatorName(outOperatorName);// 出库人
            allocateApplyDTO.setOutstockTime(info.getOutstockTime());// 出库时间
            allocateApplyDTO.setInstockOperatorName(inOperatorName); // 入库人
            allocateApplyDTO.setInstockTime(info.getInstockTime());// 入库时间
            allocateApplyDTO.setTransportorderNo(info.getTransportorderNo());// 物流单号
        }
        return allocateApplyDTO;
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
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(uuid);
        StatusDto<UserInfoDTO> resolve = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class);
        String operatorName = "";
        if(null != resolve){
            operatorName = resolve.getData().getName();
        }
        return operatorName;
    }

}
