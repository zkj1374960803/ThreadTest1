package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceEquipment;
import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquiptypeDao;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquiptypeDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private BizServiceEquipmentDao bizServiceEquipmentDao;
    @Autowired
    private UserHolder userHolder;

    /**
     * 保存物料类型
     * @param saveBizServiceEquiptypeDTO 物料类型实体
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 14:31:15
     */
    @Override
    public int save(SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO) {
        // 名字校验
        Boolean aboolean =  bizServiceEquiptypeDao.checkName(saveBizServiceEquiptypeDTO.getTypeName());
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        BizServiceEquiptype bizServiceEquiptype = new BizServiceEquiptype();
        bizServiceEquiptype.setTypeName(saveBizServiceEquiptypeDTO.getTypeName());
        bizServiceEquiptype.setRemark(saveBizServiceEquiptypeDTO.getRemark());
        bizServiceEquiptype.preInsert(userHolder.getLoggedUserId());
        return bizServiceEquiptypeDao.saveEntity(bizServiceEquiptype);
    }

    /**
     * 查询物料列表
     * @return 物料类型列表
     * @author liuduo
     * @date 2018-07-17 14:48:07
     */
    @Override
    public List<BizServiceEquiptype> queryList() {
        return bizServiceEquiptypeDao.queryList();
    }

    /**
     * 修改物料类型
     * @param saveBizServiceEquiptypeDTO 物料类型实体
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-07-17 14:54:18
     */
    @Override
    public int edit(SaveBizServiceEquiptypeDTO saveBizServiceEquiptypeDTO) {
        // 名字校验
        Boolean aboolean =  bizServiceEquiptypeDao.editCheckName(saveBizServiceEquiptypeDTO);
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        BizServiceEquiptype bizServiceEquiptype = new BizServiceEquiptype();
        bizServiceEquiptype.setTypeName(saveBizServiceEquiptypeDTO.getTypeName());
        bizServiceEquiptype.setRemark(saveBizServiceEquiptypeDTO.getRemark());
        bizServiceEquiptype.preUpdate(userHolder.getLoggedUserId());
        bizServiceEquiptype.setId(saveBizServiceEquiptypeDTO.getId());
        return bizServiceEquiptypeDao.update(bizServiceEquiptype);
    }

    /**
     * 根据id删除物料类型
     * @param id 物料类型id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-17 15:03:48
     */
    @Override
    public int deleteById(Long id) {
        // 查看该物料类型下是否有物料
        Boolean aboolean = bizServiceEquipmentDao.checkEquipmentByTypeId(id);
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        return bizServiceEquiptypeDao.deleteById(id);
    }
}
