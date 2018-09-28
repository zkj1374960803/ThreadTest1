package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;

/**
 * 零配件价格service
 * @author zhangkangjian
 * @date 2018-09-06 16:17:38
 */
public interface CarpartsProductPriceService {
    /**
     * 查询零配件的信息和价格
     * @param queryCarpartsProductDTO 查询零配件条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>>
     * @author zhangkangjian
     * @date 2018-09-06 16:25:05
     */
    StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductPriceList(QueryCarpartsProductDTO queryCarpartsProductDTO);
    /**
     * 设置价格
     * @param relProductPrice
     * @author zhangkangjian
     * @date 2018-09-06 19:27:11
     */
    void saveProductPrice(RelProductPrice relProductPrice);
    /**
     * 查询维修单的零配件列表
     * @param queryCarpartsProductDTO 查询条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 查询分页信息
     * @author zhangkangjian
     * @date 2018-09-28 10:31:47
     */
    StatusDto<Page<BasicCarpartsProductDTO>> queryServiceProductList(QueryCarpartsProductDTO queryCarpartsProductDTO);
}
