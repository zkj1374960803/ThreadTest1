package com.ccbuluo.business.platform.order.service;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.auth0.jwt.internal.org.apache.commons.lang3.tuple.Pair;
import com.ccbuluo.account.AccountTransactionDTO;
import com.ccbuluo.account.AccountTypeEnumThrift;
import com.ccbuluo.account.BizFinanceAccountService;
import com.ccbuluo.account.TransactionTypeEnumThrift;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrderStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleStrategy;
import com.ccbuluo.business.platform.claimorder.service.ClaimOrderService;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
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
    @Resource
    private UserHolder userHolder;
    @Autowired
    private ServiceLogService serviceLogService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
//    @ThriftRPCClient("UserCoreSerService")
    private BizFinanceAccountService bizFinanceAccountService;
    @Resource
    private BizInstockplanDetailDao bizInstockplanDetailDao;

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
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(ba.getApplyNo());
            if(null == details || details.size() == 0){
                throw new CommonException("0", "无效的申请单！");
            }
            BigDecimal sellTotal = getSellTotal(details);
            // 商品类型
            String productType = details.get(0).getProductType();
            // 构建申请单
            List<AccountTransactionDTO> payments = buildApplyPayment(ba,sellTotal,productType);
            // 支付
            StatusDto statusDto = bizFinanceAccountService.makeTrading(payments);
            // 如果支付成功
            if(statusDto.isSuccess()){
                //更新申请单状态
                bizAllocateApplyDao.updateApplyOrderStatus(applyNo, status);
                // 更新订单状态
                bizAllocateTradeorderDao.updateTradeorderStatus(applyNo,OrderStatusEnum.PAYMENTCOMPLETION.name());
                // 如果是调拨，要更改申请方入库计划状态
                if(BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.toString().equals(ba.getApplyType())){
                    bizInstockplanDetailDao.updateCompleteStatus(applyNo);
                }
                addlog(applyNo,ba.getInstockOrgno()+"支付给"+ba.getOutstockOrgno()+sellTotal+"人民币",BizServiceLog.actionEnum.PAYMENT.name());
            }else{
                return statusDto;
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
        // 构建申请单
        List<AccountTransactionDTO> payments = buildOrderPayment(list,payerOrgno,serviceOrderno);
        StatusDto statusDto = bizFinanceAccountService.makeTrading(payments);
        // 如果支付失败
        if(! statusDto.isSuccess()){
            return statusDto;
        }
        for(Pair<String,BigDecimal> pair : list){
            String receiveOrgno = pair.getLeft();
            BigDecimal price = pair.getRight();
            // 记录日志
            addlog(serviceOrderno,payerOrgno+"支付给"+receiveOrgno+price+"人民币",BizServiceLog.actionEnum.PAYMENT.name());
        }
        // 付款完成，状态改为待验收
        bizServiceOrderDao.editStatus(serviceOrderno, BizServiceOrder.OrderStatusEnum.WAITING_CHECKING.name());
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
     *  构建维修单
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-12 10:07:36
     */
    private List<AccountTransactionDTO> buildOrderPayment(List<Pair<String,BigDecimal>> pairs,String payerOrgno,String serviceOrderno){
        List<AccountTransactionDTO> list = new ArrayList<AccountTransactionDTO>();
        AccountTransactionDTO accountPayer = buildAccountTransactionDTO(payerOrgno,serviceOrderno);
        // 交易类型
        accountPayer.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.THREE_GUARANTEES_REFUND_PAYMENT);
        // 付款金额
        BigDecimal sellTotal = BigDecimal.ZERO;
        for(Pair<String,BigDecimal> pair : pairs){
            // 收款方
            String receiveOrgno = pair.getLeft();
            BigDecimal price = pair.getRight();
            sellTotal = sellTotal.add(price);
            AccountTransactionDTO accountReceive = buildAccountTransactionDTO(receiveOrgno,serviceOrderno);
            // 交易类型
            accountReceive.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.THREE_GUARANTEES_REFUND_COLLECTION);
            // 收款
            accountReceive.setAmount(price.doubleValue());
            list.add(accountReceive);
        }
        accountPayer.setAmount(0 - sellTotal.doubleValue());
        list.add(accountPayer);
        return list;
    }

    /**
     *  构建申请单
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-12 10:07:36
     */
    private List<AccountTransactionDTO> buildApplyPayment(BizAllocateApply ba,BigDecimal sellTotal,String productType){
        String payerOrgno = ba.getInstockOrgno();// 买入方(支付方)
        String receiveOrgno = ba.getOutstockOrgno();//卖出方(接收方)
        TransactionTypeEnumThrift transactionTypeEnum = null;
        List<AccountTransactionDTO> list = new ArrayList<AccountTransactionDTO>();
        AccountTransactionDTO accountPayer = buildAccountTransactionDTO(payerOrgno,ba.getApplyNo());
        AccountTransactionDTO accountReceive = buildAccountTransactionDTO(receiveOrgno,ba.getApplyNo());
        // 采购
        if(BizAllocateApply.AllocateApplyTypeEnum.PURCHASE.toString().equals(ba.getApplyType())){
            // 零配件
            if(Constants.PRODUCT_TYPE_FITTINGS.equals(productType)){
                // 付款
                accountPayer.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.SPAREPARTS_EXTERNAL_PURCHASE_PAYMENT);
            }
            // 物料
            if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(productType)){
                // 付款
                accountPayer.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.MATERIAL_EXTERNAL_PURCHASE_PAYMENT);
            }
            // 采购的支付方是平台
            accountPayer.setOrganizationCode(Constants.AFTER_SALE_PLATFORM);
        }
        // 调拨
        if(BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.toString().equals(ba.getApplyType())){
            // 零配件
            if(Constants.PRODUCT_TYPE_FITTINGS.equals(productType)){
                // 付款
                accountPayer.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.SPARE_PARTS_TRANSFER_PAYMENT);
                // 收款
                accountReceive.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.SPARE_PARTS_TRANSFER_RECEIPT);
            }
            // 物料
            if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(productType)){
                // 付款
                accountPayer.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.MATERIAL_TRANSFER_PAYMENT);
                // 收款
                accountReceive.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.MATERIAL_TRANSFER_RECEIPT);
            }
        }
        // 退货
        if(BizAllocateApply.AllocateApplyTypeEnum.REFUND.toString().equals(ba.getApplyType())){
            // 付款
            accountPayer.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.PROBLEM_PIECE_REFUND_PAYMENT);
            // 收款
            accountReceive.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.PROBLEM_PIECE_REFUND_RECEIPT);
            // 退货的支付方是平台
            accountPayer.setOrganizationCode(Constants.AFTER_SALE_PLATFORM);
        }
        // 付款
        accountPayer.setAmount(0 - sellTotal.doubleValue());
        // 收款
        accountPayer.setAmount(sellTotal.doubleValue());
        // 过滤
        if(StringUtils.isNotBlank(payerOrgno)){
            list.add(accountPayer);
        }
        if(StringUtils.isNotBlank(receiveOrgno)){
            list.add(accountReceive);
        }
        return list;
    }

    /**
     *  构建支付对象
     * @param orgNo 组织机构
     * @param applyNo 单号
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-12 10:50:36
     */
    private AccountTransactionDTO buildAccountTransactionDTO(String orgNo,String applyNo){
        AccountTransactionDTO transaction = new AccountTransactionDTO();
        // 账户
        transaction.setOrganizationCode(orgNo);
        // 账户类型
        transaction.setAccountTypeEnumThrift(AccountTypeEnumThrift.SMALL_CHANGE);
        // 业务单号
        transaction.setBusinessSourceDocumentNumber(applyNo);
        return transaction;
    }
}
