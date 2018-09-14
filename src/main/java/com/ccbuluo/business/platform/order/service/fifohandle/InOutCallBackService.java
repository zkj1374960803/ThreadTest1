package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.entity.RelOrdstockOccupy;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.inputstockplan.service.InputStockPlanService;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.outstockplan.service.OutStockPlanService;
import com.ccbuluo.core.common.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 出入库回调共用方法
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-09-13 18:22:40
 */
@Service
public class InOutCallBackService {

    @Autowired
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    private InputStockPlanService inputStockPlanService;
    @Autowired
    private AllocateApplyService allocateApplyService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private OutStockPlanService outStockPlanService;
    @Autowired
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;

    /**
     * 更改申请单状态(入库回调用)
     * @param applyNo 申请单号
     * @param  inRepositoryNo 仓库编号
     * @author liuduo
     * @date 2018-08-21 18:52:48
     */
    public void updateApplyStatus(String applyNo,String inRepositoryNo) {
        FindAllocateApplyDTO detail = allocateApplyService.findDetail(applyNo);
        List<BizInstockplanDetail> bizInstockplanDetails3 = inputStockPlanService.queryListByApplyNoAndInReNo(applyNo, inRepositoryNo);
        List<BizInstockplanDetail> collect = bizInstockplanDetails3.stream().filter(item -> item.getCompleteStatus().equals(StockPlanStatusEnum.COMPLETE.name())).collect(Collectors.toList());
        if (collect.size() == bizInstockplanDetails3.size()) {
            String applyType = detail.getApplyType();
            String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
            if (StringUtils.isNotBlank(applyType)) {
                // 更改申请单状态
                switch (Enum.valueOf(BizAllocateApply.AllocateApplyTypeEnum.class, applyType)) {
                    case PURCHASE:
                    case SAMELEVEL:
                        allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.CONFIRMRECEIPT.toString());
                        break;
                    case BARTER:
                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PLATFORMOUTBOUND.toString());
                        } else {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REPLACECOMPLETED.toString());
                        }
                        break;
                    case REFUND:
                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.WAITINGREFUND.toString());
                        }
                        break;
                }
            }

        }
    }


    /**
     * 更改申请单状态(出库回调用)
     *
     * @param applyNo                   申请单号
     * @author liuduo
     * @date 2018-08-11 13:05:07
     */
    public void updateApplyOrderStatus(String applyNo) {
        BizAllocateApply apply = bizAllocateApplyDao.getByNo(applyNo);
        FindAllocateApplyDTO detail = allocateApplyService.findDetail(applyNo);
        // 根据申请单编号查询订单占用库存关系表
        List<RelOrdstockOccupy> relOrdstock = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(applyNo);
        String outRepositoryNo = "";
        if(null != relOrdstock && relOrdstock.size() > 0){
            outRepositoryNo = relOrdstock.get(0).getOutstockOrgno();
        }
        List<BizOutstockplanDetail> bizOutstockplanDetailList = outStockPlanService.queryOutstockplan(applyNo, StockPlanStatusEnum.DOING.toString(),outRepositoryNo);
        List<BizOutstockplanDetail> collect = bizOutstockplanDetailList.stream().filter(item -> item.getPlanStatus().equals(StockPlanStatusEnum.COMPLETE.name())).collect(Collectors.toList());
        String applyType = detail.getApplyType();
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        if (StringUtils.isNotBlank(applyType)) {
            // 判断本次交易的出库计划是否全部完成
            if (bizOutstockplanDetailList.size() == collect.size()) {
                switch (Enum.valueOf(BizAllocateApply.AllocateApplyTypeEnum.class, applyType)) {
                    case PLATFORMALLOCATE:
                    case PURCHASE:
                        allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.WAITINGRECEIPT.toString());
                        break;
                    case DIRECTALLOCATE:
                    case SAMELEVEL:
                        allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ApplyStatusEnum.WAITINGRECEIPT.toString());
                        break;
                    case BARTER:
                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REPLACEWAITIN.toString());
                        } else {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PRODRETURNED.toString());
                        }
                        break;
                    case REFUND:
                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REFUNDCOMPLETED.toString());
                        } else {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PRODRETURNED.toString());
                        }
                        break;
                }
            }

        }

    }
}
