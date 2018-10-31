package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquipmentDTO;
import com.ccbuluo.db.Page;

import java.util.List;
import java.util.Map;

/**
 * 物料service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:18:59
 */
public interface EquipmentService {
    /**
     * 保存物料
     * @param saveBizServiceEquipmentDTO
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 18:40:03
     */
    int save(SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO);

    /**
     * 根据id查询物料详情
     * @param id 物料id
     * @return 物料详情
     * @author liuduo
     * @date 2018-07-17 19:12:20
     */
    DetailBizServiceEquipmentDTO getById(Long id);

    /**
     * 编辑物料
     * @param saveBizServiceEquipmentDTO 物料实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 18:40:03
     */
    int editEquiptype(SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO);

    /**
     * 查询物料列表
     * @param equiptypeId 物料类型id
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    Page<DetailBizServiceEquipmentDTO> queryList(Long equiptypeId, String keyword, Integer offset, Integer pageSize);

    /**
     * 查询计量单位
     * @return 计量单位
     * @author liuduo
     * @date 2018-07-31 19:10:23
     */
    List<Map<String,String>> getUnit();

    /**
    * 根据物料类型id查询物料
    * @param equiptypeId 物料类型id
    * @return 物料
    * @author liuduo
    * @date 2018-08-02 10:41:20
    */
    List<DetailBizServiceEquipmentDTO> queryEqupmentByEquiptype(Long equiptypeId);

    /**
     * 根据code删除物料
     * @param equipCode 物料code
     * @return 是否删除成功
     * @author liuduo
     * @date 2018-08-23 11:10:57
     */
    int delete(String equipCode);
}
