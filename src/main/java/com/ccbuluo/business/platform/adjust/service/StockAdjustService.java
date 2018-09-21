package com.ccbuluo.business.platform.adjust.service;

import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.adjust.dto.SaveBizStockAdjustDTO;
import com.ccbuluo.business.platform.adjust.dto.SearchStockAdjustListDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustDetailDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustListDTO;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 盘库service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-14 16:15:30
 */
public interface StockAdjustService {
    /**
     * 查询物料类型集合
     * @return 物料类型集合
     * @author liuduo
     * @date 2018-08-14 16:36:23
     */
    List<BizServiceEquiptype> queryEquipmentType();

    /**
     * 根据物料类型id查询物料
     * @param equipTypeId 物料类型id
     * @return 物料
     * @author liuduo
     * @date 2018-08-14 16:41:20
     */
    List<DetailBizServiceEquipmentDTO> queryEquipmentByType(Long equipTypeId);

    /**
     * 查询新增物料盘库时用的列表
     * @param equipTypeId 物料类型id
     * @param equipmentcode 物料code
     * @return 新增物料盘库时用的列表
     * @author liuduo
     * @date 2018-08-14 17:43:53
     */
    List<StockAdjustListDTO> queryAdjustList(Long equipTypeId, String equipmentcode, String productType);

    /**
     * 保存盘库
     * @param saveBizStockAdjustDTO 盘库实体
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-08-14 19:17:37
     */
    StatusDto save(SaveBizStockAdjustDTO saveBizStockAdjustDTO);

    /**
     * 查询新增零配件盘库时用的列表
     * @param categoryCode 零配件分类code
     * @param productCode 商品code
     * @return 新增零配件盘库时用的列表
     * @author liuduo
     * @date 2018-08-15 09:23:53
     */
    List<StockAdjustListDTO> queryAdjustListByCategoryCode(String categoryCode, String productCode, String productType);

    /**
     * 查询盘库单列表
     * @param adjustResult 盘库结果
     * @param adjustSource 盘库单来源
     * @param keyWord 关键字（盘库单号/服务中心/客户经理）
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 盘库单列表
     * @author liuduo
     * @date 2018-08-15 11:03:46
     */
    Page<SearchStockAdjustListDTO> queryAdjustStockList(Integer adjustResult, String adjustSource, String keyWord, String productType, Integer offset, Integer pageSize);

    /**
     * 根据盘库单号查询盘库详情
     * @param adjustNo 盘库单号
     * @return 盘库详情
     * @author liuduo
     * @date 2018-08-15 14:37:37
     */
    StockAdjustDetailDTO getByAdjustNo(String adjustNo);
}
