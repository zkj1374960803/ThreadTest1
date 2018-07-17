package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquiptypeDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 物料类型service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:23:04
 */
@Service
public class EquiptypeServiceImpl implements EquiptypeService{

    @Autowired
    private BizServiceEquiptypeDao bizServiceEquiptypeDao;
    @Autowired
    private UserHolder userHolder;

    /**
     * 保存物料类型
     * @param bizServiceEquiptype 物料类型实体
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 14:31:15
     */
    @Override
    public int save(BizServiceEquiptype bizServiceEquiptype) {
        // 名字校验
        Boolean aboolean =  bizServiceEquiptypeDao.checkName(bizServiceEquiptype.getTypeNeme());
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        bizServiceEquiptype.preInsert(userHolder.getLoggedUserId());
        return bizServiceEquiptypeDao.saveEntity(bizServiceEquiptype);
    }
}
