package com.ccbuluo.business.platform.order.service;


import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.BizAllocateTradeorder;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调拨申请交易订单
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
public class TradeOrderServiceImpl implements TradeOrderService {
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private UserHolder userHolder;
    @Resource
    private GenerateDocCodeService generateDocCodeService;
    @Resource
    BizAllocateTradeorderDao bizAllocateTradeorderDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *  采购申请处理
     * @param applyNo 申请单编号
     *  @return 0返回失败，1返回成功
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    public int purchaseApplyHandle(String applyNo){
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailDTO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                return 0;
            }
            // 构建生成订单
            BizAllocateTradeorder bizAllocateTradeorder = buildOrderEntity(details);
            // 保存生成订单
            return bizAllocateTradeorderDao.saveEntity(bizAllocateTradeorder);
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
    }

    /**
     *  调拨申请处理
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    public int allocateApplyHandle(String applyNo){
        return 0;
    }

    // 构建生成订单
    private BizAllocateTradeorder buildOrderEntity(List<AllocateapplyDetailDTO> details){
        BizAllocateTradeorder bt = new BizAllocateTradeorder();
        // 生成订单编号
        StatusDto<String> supplierCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.SW);
        if(!supplierCode.isSuccess()){
            throw new CommonException(supplierCode.getCode(), "生成订单编号失败！");
        }
        bt.setOrderNo(supplierCode.getData());//订单号
        bt.setCreator(userHolder.getLoggedUserId());//处理人
        bt.setCreateTime(new Date());
        bt.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        bt.setOrderStatus("PAYMENTWAITING");//默认待支付
        for(AllocateapplyDetailDTO bd : details){
            bt.setApplyNo(bd.getApplyNo());// 申请单编号
            bt.setPurchaserOrgno(bd.getInstockOrgno());//买方机构
            bt.setSellerOrgno(bd.getOutstockOrgno());//卖方机构
            break;
        }
        // 计算订单总价
        BigDecimal total = getTatal(details);
        bt.setTotalPrice(total);
        return bt;
    }
    // 计算订单总价
    private BigDecimal getTatal(List<AllocateapplyDetailDTO> details){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(AllocateapplyDetailDTO bd : details){
            bigDecimal.add(bd.getSellPrice());
        }
        return bigDecimal;
    }

    // 构建占用库存
    private List<BizStockDetail> buildStockEntity(List<AllocateapplyDetailDTO> details){
        return null;
    }

}
