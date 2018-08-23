package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.platform.allocateapply.dao.ProblemAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProblemAllocateapplyDetailDTO;
import com.ccbuluo.business.platform.outstock.dao.BizOutstockOrderDao;
import com.ccbuluo.business.platform.outstock.dto.BizOutstockOrderDTO;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
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

    /**
     * 问题件申请列表
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<ProblemAllocateapplyDetailDTO> queryProblemApplyList(String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        return problemAllocateApplyDao.queryProblemApplyList(applyType, applyStatus, applyNo, offset, pageSize);
    }
    /**
     * 问题件处理列表
     * @param applyType 申请类型
     * @param applyStatus 申请状态
     * @param applyNo 申请单号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-15 18:51:51
     */
    @Override
    public Page<ProblemAllocateapplyDetailDTO> queryProblemHandleList(String applyType, String applyStatus, String applyNo, Integer offset, Integer pageSize){
        return problemAllocateApplyDao.queryProblemHandleList(applyType, applyStatus, applyNo, offset, pageSize);
    }

    /**
     * 查询退换货申请单详情
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 20:02:58
     */
    @Override
    public FindAllocateApplyDTO getProblemdetailDetail(String applyNo){
        FindAllocateApplyDTO allocateApplyDTO = allocateApplyServiceImpl.findDetail(applyNo);
        // 获取出库人和出库时间
        BizOutstockOrderDTO oOutstockOrder = bizOutstockOrderDao.getByTradeDocno(applyNo);
        if(null != oOutstockOrder){
            String operatorName = getUserNameByUuid(oOutstockOrder.getOutstockOperator());
            allocateApplyDTO.setOutstockOperatorName(operatorName);// 出库人
            allocateApplyDTO.setOutstockTime(oOutstockOrder.getOutstockTime());// 出库时间
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
