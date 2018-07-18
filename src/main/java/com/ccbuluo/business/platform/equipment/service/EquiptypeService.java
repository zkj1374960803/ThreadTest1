package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquiptypeDTO;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 物料类型service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:22:23
 */
public interface EquiptypeService {
    /**
    * 保存物料类型
    * @param saveBizServiceEquiptypeDTO 物料类型实体dto
    * @return 保存是否成功
    * @author liuduo
    * @date 2018-07-17 14:31:15
    */
    int save(SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO);
    /**
    * 查询物料列表
    * @return 物料类型列表
    * @author liuduo
    * @date 2018-07-17 14:48:07
    */
    List<BizServiceEquiptype> queryList();

    /**
     * 修改物料类型
     * @param saveBizServiceEquiptypeDTO 物料类型实体
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-07-17 14:54:18
     */
    int edit(SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO);

    /**
     * 根据id删除物料类型
     * @param id 物料类型id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-17 15:03:48
     */
    int deleteById(Long id);
}
