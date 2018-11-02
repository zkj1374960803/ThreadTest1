package com.ccbuluo.business.platform.stockdetail.service;


import com.ccbuluo.business.platform.stockdetail.dto.ProblemStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
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
     * @param category 是否根据类型查询
     *  @param type 物料或是零配件
     * @param productCategory 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(boolean category, String type, String productCategory, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize);
    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param category 是否根据类型查询
     * @param type 物料或是零配件
     * @param productCategory 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    Page<StockBizStockDetailDTO> querySelfStockBizStockDetailDTOList(boolean category, String type, String productCategory, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize);
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

    /**
     * 查询问题库存详情（平台用）
     * @param id 库存批次id
     * @return StatusDto
     * @author weijb
     * @date 2018-08-23 16:02:58
     */
    ProblemStockBizStockDetailDTO getProblemStockDetailById(Long id);

    /**
     * 根据商品类型和商品编号查询详情
     * @param procudtType 商品类型
     * @param productNo 商品编号
     * @return 问题件详情
     * @author liuduo
     * @date 2018-10-29 14:05:14
     */
    ProblemStockBizStockDetailDTO findByProductno(String procudtType, String productNo);

    /**
     * 根据申请单号修改退换类型
     * @param applyNo 申请单号
     * @param recedeType 退换类型
     * @author liuduo
     * @date 2018-10-29 16:59:30
     */
    StatusDto problemHandle(String applyNo, String recedeType);
}
