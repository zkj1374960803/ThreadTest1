package com.ccbuluo.business.platform.order.service;

import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.business.constants.OrderStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleStrategy;
import com.ccbuluo.business.platform.claimorder.service.ClaimOrderService;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.dao.BizServiceorderDetailDao;
import com.ccbuluo.business.platform.servicelog.service.ServiceLogService;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.usercoreintf.dto.OrgWorkplaceDTO;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 支付功能
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-22 16:33:00
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Autowired
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Autowired
    private BizServiceOrderDao bizServiceOrderDao;
    @Autowired
    private BizServiceorderDetailDao bizServiceorderDetailDao;
    @Autowired
    private ClaimOrderService claimOrderService;
    @Resource
    private UserHolder userHolder;
    @Autowired
    private ServiceLogService serviceLogService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;

    /**
     *  支付完成调用接口
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-22 17:02:58
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto paymentCompletion(String applyNo){
        try {
            // 根据申请单获取申请单详情
            BizAllocateApply ba = bizAllocateApplyDao.getByNo(applyNo);
            if(null == ba){
                throw new CommonException("0", "无效的申请单！");
            }
            // 只有等待付款的状态才可以支付
            if ( !ba.getApplyStatus().equals(BizAllocateApply.ApplyStatusEnum.WAITINGPAYMENT.name())) {
                throw new CommonException("0", "只有待付款的申请才可以支付！");
            }
            String status = BizAllocateApply.ApplyStatusEnum.WAITDELIVERY.name();// 等待发货
            // 支付成功之后，如果是采购，则状态为平台待入库
            if(ba.getApplyType().equals(BizAllocateApply.AllocateApplyTypeEnum.PURCHASE.name())){
                status = BizAllocateApply.ApplyStatusEnum.INSTORE.name();// 等待平台入库
            }
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            String payerOrgno = ba.getInstockOrgno();// 买入方(支付方)
            String receiveOrgno = ba.getOutstockOrgno();//卖出方(接收方)
            BigDecimal sellTotal = getSellTotal(details);
            // 如果支付成功 TODO
            if(1 == 1){
                //更新申请单状态
                bizAllocateApplyDao.updateApplyOrderStatus(applyNo, status);
                // 更新订单状态
                bizAllocateTradeorderDao.updateTradeorderStatus(applyNo,OrderStatusEnum.PAYMENTCOMPLETION.name());
                // 记录日志
                StatusDtoThriftBean<OrgWorkplaceDTO> byCode = basicUserOrganizationService.getByCode(payerOrgno);

                addlog(applyNo,payerOrgno+"支付给"+receiveOrgno+sellTotal+"人民币",BizServiceLog.actionEnum.PAYMENT.name());
            }else{
                return StatusDto.buildFailureStatusDto("支付失败！");
            }
            return StatusDto.buildSuccessStatusDto("支付成功！");

        } catch (Exception e) {
            logger.error("支付失败！", e);
            throw e;
        }
    }
    /**
     *  根据申请单获取总价格
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-29 15:02:58
     */
    @Override
    public FindAllocateApplyDTO getTotalPrice(String applyNo){
        FindAllocateApplyDTO allocate = new FindAllocateApplyDTO();
        // 根据申请单获取申请单详情
        List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
        BigDecimal sellTotal = getSellTotal(details);
        BigDecimal costTotal = getCostTotal(details);
        allocate.setCreateTime(new Date());
        allocate.setSellTotalPrice(sellTotal);
        allocate.setCostTotalPrice(costTotal);
        return allocate;
    }
    /**
     *  计算订单总价（销售价）
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getSellTotal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal sellPrice = BigDecimal.ZERO;
        BigDecimal appNum = BigDecimal.ZERO;
        for(AllocateapplyDetailBO bd : details){
            if(null != bd.getSellPrice()){
                //单价
                sellPrice = bd.getSellPrice();
                // 数量
                appNum = BigDecimal.valueOf(bd.getApplyNum());
            }
            bigDecimal = bigDecimal.add(sellPrice.multiply(appNum));
        }
        return bigDecimal;
    }

    /**
     *  计算订单总价(成本价)
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private BigDecimal getCostTotal(List<AllocateapplyDetailBO> details){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal costPrice = BigDecimal.ZERO;
        BigDecimal appNum = BigDecimal.ZERO;
        for(AllocateapplyDetailBO bd : details){
            if(null != bd.getCostPrice()){
                //单价
                costPrice = bd.getCostPrice();
                // 数量
                appNum = BigDecimal.valueOf(bd.getApplyNum());
            }
            bigDecimal = bigDecimal.add(costPrice.multiply(appNum));
        }
        return bigDecimal;
    }

    /**
     *  服务单（维修单）支付功能
     * @param serviceOrderno 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-10 17:02:58
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto servicepaymentcompletion(String serviceOrderno){
        // 根据车辆查询收款人组织code
        BizServiceOrder serviceOrder = bizServiceOrderDao.getBizServiceOrderByServiceOrderno(serviceOrderno);
        String payerOrgno = "";// 这个待确认 TODO serviceOrder
        // 查询出过保的零配件（根据详单查新付款人组织编号和金额）
        List<Pair<String,BigDecimal>> list = getRreceiveInfo(serviceOrderno);
        for(Pair<String,BigDecimal> pair : list){
            String receiveOrgno = pair.getLeft();
            BigDecimal price = pair.getRight();
            // TODO 调用支付接口
            StatusDto statusDto = null;
            if(! statusDto.isSuccess()){
                return statusDto;
            }
            // 记录日志
            addlog(serviceOrderno,payerOrgno+"支付给"+receiveOrgno+price+"人民币",BizServiceLog.actionEnum.PAYMENT.name());
        }
        // 付款完成，状态改为待验收
        bizServiceOrderDao.editStatus(serviceOrderno, BizServiceOrder.OrderStatusEnum.WAITING_CHECKING.name());
        // 调用生成索赔单(支付成功)（有可能零配件都在质保范围）
        claimOrderService.generateClaimForm(serviceOrderno);
        return StatusDto.buildSuccessStatusDto("支付成功！");
    }
    private List<Pair<String,BigDecimal>> getRreceiveInfo(String serviceOrderno){
        List<BizServiceorderDetail> orderDetails = bizServiceorderDetailDao.getServiceorderDetailByOrderNo(serviceOrderno);
        // 根据维修服务的code分组
        Map<String, List<BizServiceorderDetail>> collect = orderDetails.stream().collect(Collectors.groupingBy(BizServiceorderDetail::getServiceOrgno));
        List<Pair<String,BigDecimal>> list = new ArrayList<Pair<String,BigDecimal>>();
        for (Map.Entry<String, List<BizServiceorderDetail>> entry : collect.entrySet()) {
            List<BizServiceorderDetail> value = entry.getValue();
            Pair<String,BigDecimal> pair = getPriceTatol(value);
            list.add(pair);
        }
        return list;
    }
    /**
     *  计算服务单价格
     * @param details 服务单详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private Pair<String,BigDecimal> getPriceTatol(List<BizServiceorderDetail> details){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        BigDecimal costPrice = BigDecimal.ZERO;
        String orgNo = "";
        for(BizServiceorderDetail detail : details){
            orgNo = detail.getServiceOrgno();
            // 使用的数量
            BigDecimal occupyNum = BigDecimal.valueOf(detail.getAmount());
            // 成本价格
            costPrice = BigDecimal.valueOf(detail.getUnitPrice());
            bigDecimal = bigDecimal.add(occupyNum.multiply(costPrice));
        }
        return Pair.of(orgNo, bigDecimal);
    }

    /**
     * 记录日志
     * @param applyNo 申请单号
     * @param content 日志内容
     * @param action 动作
     */
    private void addlog(String applyNo,String content,String action){
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.ERP.name());
        // BizServiceLog.actionEnum.UPDATE.name()
        bizServiceLog.setAction(action);
        bizServiceLog.setSubjectType("PaymentServiceImpl");
        bizServiceLog.setSubjectKeyvalue(applyNo);
        if (userHolder.getLoggedUser().getOrganization().getOrgType().equals(BizServiceOrder.ProcessorOrgtypeEnum.CUSTMANAGER.name())) {
            bizServiceLog.setLogContent("客户经理:"+content);
        } else {
            bizServiceLog.setLogContent("服务中心:"+content);
        }
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
    }

    /**
     *  根据维修单获取总价格
     * @param serviceOrderno 维修单号
     * @return StatusDto
     * @author weijb
     * @date 2018-09-11 14:02:58
     */
    @Override
    public BizServiceOrder getServiceOrdernoPrice(String serviceOrderno){
        BizServiceOrder serviceOrder = new BizServiceOrder();
        List<BizServiceorderDetail> orderDetails = bizServiceorderDetailDao.getServiceorderDetailByOrderNo(serviceOrderno);
        Pair<String,BigDecimal> pair = getPriceTatol(orderDetails);
        serviceOrder.setOrderCost(pair.getRight());
        return serviceOrder;
    }

    /**
     *  调用支付功能
     * @param payerOrgno 付款人
     * @param receiveOrgno 收款人
     * @param price 金额
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-11 15:13:28
     */
    private StatusDto payment(String payerOrgno,String receiveOrgno, BigDecimal price){
        double v = price.doubleValue();
        return null;
    }

    public static void main(String[] args) {
        double v =12;
        System.out.println(v);
        System.out.println(0 - v);
    }
}
