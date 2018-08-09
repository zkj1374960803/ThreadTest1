package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.platform.instock.dto.SaveBizInstockOrderDTO;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 入库单service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:04:39
 */
public interface InstockOrderService {
    /**
     * 根据申请单号状态查询申请单号集合
     * @param applyStatus 申请单状态
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    List<String> queryApplyNo(String applyStatus);

    /**
     * 保存入库单
     * @param applyNo 申请单号
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    StatusDto<String> saveInstockOrder(String applyNo, List<BizInstockorderDetail> bizInstockorderDetailList);
}
