package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.http.StatusDto;

/**
 * 物料类型service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:22:23
 */
public interface EquiptypeService {
    /**
    * 保存物料类型
    * @param bizServiceEquiptype 物料类型实体
    * @return 保存是否成功
    * @author liuduo
    * @date 2018-07-17 14:31:15
    */
    int save(BizServiceEquiptype bizServiceEquiptype);
}
