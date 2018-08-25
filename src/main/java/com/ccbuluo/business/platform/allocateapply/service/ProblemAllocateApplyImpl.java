package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dao.ProblemAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProblemAllocateapplyDetailDTO;
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
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

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
    public Page<ProblemAllocateapplyDetailDTO> querySelfProblemApplyList(String type,String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        // 获取用户的组织机构
        String userOrgCode = getUserOrgCode();
        return problemAllocateApplyDao.queryProblemApplyList(type,userOrgCode,applyType, applyStatus, applyNo, offset, pageSize);
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
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
     */
    @Override
    public Page<ProblemAllocateapplyDetailDTO> queryProblemHandleList(String type,String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        return problemAllocateApplyDao.queryProblemHandleList(type,null,applyType, applyStatus, applyNo, offset, pageSize);
    }
    /**
     * 问题件处理列表
     *  @param type 物料或是零配件
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
     */
    @Override
    public Page<ProblemAllocateapplyDetailDTO> querySelfProblemHandleList(String type,String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        // 获取用户的组织机构
        String userOrgCode = getUserOrgCode();
        return problemAllocateApplyDao.queryProblemHandleList(type,userOrgCode, applyType, applyStatus, applyNo, offset, pageSize);
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
