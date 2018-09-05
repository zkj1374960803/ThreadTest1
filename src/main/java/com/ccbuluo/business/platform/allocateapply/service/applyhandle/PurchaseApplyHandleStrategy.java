package com.ccbuluo.business.platform.allocateapply.service.applyhandle;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购申请处理
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-13 18:13:50
 */
@Service
public class PurchaseApplyHandleStrategy extends DefaultApplyHandleStrategy {

    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private BizInstockplanDetailDao bizInstockplanDetailDao;
    @Resource
    private UserHolder userHolder;
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private GenerateDocCodeService generateDocCodeService;

    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     *  采购申请处理
     * @param ba 申请单
     * @author weijb
     * @date 2018-08-08 10:55:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto applyHandle(BizAllocateApply ba){
        String applyNo = ba.getApplyNo();
        try {
            // 根据申请单获取申请单详情
            List<AllocateapplyDetailBO> details = bizAllocateapplyDetailDao.getAllocateapplyDetailByapplyNo(applyNo);
            if(null == details || details.size() == 0){
                throw new CommonException("0", "申请单为空！");
            }
            // TODO 去掉订单
            // 构建平台入库计划并保存
            List<BizInstockplanDetail> instockplans = buildOutAndInstockplanDetail(details);
            bizInstockplanDetailDao.batchInsertInstockplanDetail(instockplans);
        } catch (Exception e) {
            logger.error("提交失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请处理成功！");
    }

    /**
     *  申请撤销
     * @param applyNo 申请单编号
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusDto cancelApply(String applyNo){
        try {
            // 删除入库计划
            bizInstockplanDetailDao.batchInsertInstockplanDetail(applyNo);
            //更新申请单状态(已撤销)
            bizAllocateApplyDao.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.CANCEL.name());
        } catch (Exception e) {
            logger.error("撤销失败！", e);
            throw e;
        }
        return StatusDto.buildSuccessStatusDto("申请撤销成功！");
    }

    /**
     *  入库之后回调事件
     * @param ba 申请单
     * @return
     */
    @Override
    public StatusDto platformInstockCallback(BizAllocateApply ba){
        return super.platformInstockCallback(ba);
    }

    /**
     *  构建出库和入库计划并保存
     * @param details 申请单详情
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private  List<BizInstockplanDetail> buildOutAndInstockplanDetail(List<AllocateapplyDetailBO> details){
        List<BizInstockplanDetail> inList = new ArrayList<BizInstockplanDetail>();
        // 平台入库计划
        instockplanPlatform(inList,details);
        return inList;
    }
    /**
     * 平台入库
     * @param details 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private void instockplanPlatform(List<BizInstockplanDetail> inList, List<AllocateapplyDetailBO> details){
        // 平台入库
        for(AllocateapplyDetailBO ad : details){
            BizInstockplanDetail instockplanPlatform = new BizInstockplanDetail();
            // 平台入库计划
            instockplanPlatform = buildBizInstockplanDetail(ad);
            // 成本价（从详单上获取）
            instockplanPlatform.setCostPrice(ad.getCostPrice());
            // 平台仓库编号
            instockplanPlatform.setInstockRepositoryNo(ad.getInRepositoryNo());
            // 平台机构编号
            instockplanPlatform.setInstockOrgno(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM);
            instockplanPlatform.setInstockType(InstockTypeEnum.PURCHASE.toString());// 交易类型
            inList.add(instockplanPlatform);
        }
    }

    /**
     * 构建生成订单
     * @param detail 申请详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */

    public BizAllocateTradeorder buildOrderEntity(AllocateapplyDetailBO detail){
        BizAllocateTradeorder bt = new BizAllocateTradeorder();
        // 生成订单编号
        StatusDto<String> supplierCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.JY);
        if(!supplierCode.isSuccess()){
            throw new CommonException(supplierCode.getCode(), "生成订单编号失败！");
        }
        bt.setOrderNo(supplierCode.getData());//订单号
        bt.setCreator(userHolder.getLoggedUserId());//处理人
        bt.setCreateTime(new Date());
        bt.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        bt.setOrderStatus(OrderStatusEnum.PAYMENTWAITING.name());//默认待支付
        bt.setApplyNo(detail.getApplyNo());// 申请单编号
        bt.setPurchaserOrgno(detail.getInstockOrgno());//买方机构
        bt.setSellerOrgno(detail.getOutstockOrgno());//卖方机构
        bt.setTradeType(detail.getApplyType());//交易类型
        return bt;
    }

    }
