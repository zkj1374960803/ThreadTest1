package com.ccbuluo.business.platform.order.service.fifohandle;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.inputstockplan.service.InputStockPlanService;
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

    /**
     * 更改申请单状态
     * @param applyNo 申请单号
     * @author liuduo
     * @date 2018-08-21 18:52:48
     */
    public void updateApplyStatus(String applyNo) {
        BizAllocateApply apply = bizAllocateApplyDao.getByNo(applyNo);
        FindAllocateApplyDTO detail = allocateApplyService.findDetail(applyNo);
        List<BizInstockplanDetail> bizInstockplanDetails3 = inputStockPlanService.queryListByApplyNoAndInReNo(applyNo, apply.getInRepositoryNo());
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
}
