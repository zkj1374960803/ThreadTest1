package com.ccbuluo.business.platform.stockdetail.service;


import com.ccbuluo.business.platform.stockdetail.dto.ProblemStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;

import java.util.List;

/**
 * 功能描述（1）
 *
 * @author weijb
 * @version v1.0.0
 * @date 创建时间（2）
 */
public interface ProblemStockDetailService {

    /**
     * 带条件分页查询所有零配件的问题库存
     *  @param type 物料或是零配件
     * @param productCategory 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String type, String productCategory, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize);
    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param type 物料或是零配件
     * @param productCategory 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    Page<StockBizStockDetailDTO> querySelfStockBizStockDetailDTOList(String type, String productCategory, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize);
    /**
     * 根据物料code查询某个物料的问题件库存
     * @param type 物料或是零配件
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    Page<StockBizStockDetailDTO> getProdectStockBizStockDetailByCode(String type, String productNo, Integer offset, Integer pageSize);
    /**
     * 根据物料code查询某个物料在当前登录机构的问题件库存
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    Page<StockBizStockDetailDTO> getSelfProdectStockBizStockDetailByCode(String productNo, Integer offset, Integer pageSize);

    /**
     * 查询问题库存详情
     * @param id 库存批次id
     * @return StatusDto
     * @author weijb
     * @date 2018-08-23 16:02:58
     */
    ProblemStockBizStockDetailDTO getProblemStockDetail(Long id);
}
