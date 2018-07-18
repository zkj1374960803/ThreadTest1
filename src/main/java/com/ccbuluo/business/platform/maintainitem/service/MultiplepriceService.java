package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMultiplepriceDTO;
import com.ccbuluo.db.Page;

/**
 * 倍数service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 10:21:52
 */
public interface MultiplepriceService {

    /**
     * 保存地区倍数
     * @param saveBizServiceMultiplepriceDTO 地区倍数dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 13:59:55
     */
    int save(SaveBizServiceMultiplepriceDTO saveBizServiceMultiplepriceDTO);

}
