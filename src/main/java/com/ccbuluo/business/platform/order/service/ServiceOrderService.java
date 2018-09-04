package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.platform.order.dto.SaveServiceOrderDTO;
import com.ccbuluo.http.StatusDto;

/**
 * 描述 服务订单service
 * @author baoweiding
 * @date 2018-09-03 17:35:19
 * @version V1.0.0
 */
public interface ServiceOrderService {
    /**
     * 描述 新增服务订单
     * @param serviceOrderDTO
     * @return com.ccbuluo.http.StatusDto<java.lang.String>
     * @author baoweiding
     * @date 2018-09-03 18:54:01
     */
    StatusDto<String> saveOrder(SaveServiceOrderDTO serviceOrderDTO);
}
