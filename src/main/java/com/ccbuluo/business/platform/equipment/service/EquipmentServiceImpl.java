package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.entity.BizServiceEquipment;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * 物料service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-17 14:19:41
 */
@Service
public class EquipmentServiceImpl implements EquipmentService{

    @Autowired
    private BizServiceEquipmentDao bizServiceEquipmentDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private GenerateProjectCodeService generateProjectCodeService;

    /**
     * 保存物料
     * @param saveBizServiceEquipmentDTO
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 18:40:03
     */
    @Override
    public int save(SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO) {
        // 名字校验
        Boolean aboolean =  bizServiceEquipmentDao.checkName(saveBizServiceEquipmentDTO.getEquipName());
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        // 生成code
        String serviceCenterCode = null;
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FA);
        if (stringStatusDto.getCode().equals(Constants.SUCCESS_CODE)) {
            serviceCenterCode = stringStatusDto.getData();
        } else {
            return Constants.FAILURE_TWO;
        }
        BizServiceEquipment bizServiceEquipment = new BizServiceEquipment();
        bizServiceEquipment.setEquipCode(serviceCenterCode);
        bizServiceEquipment.setEquipName(saveBizServiceEquipmentDTO.getEquipName());
        bizServiceEquipment.setEquiptypeId(saveBizServiceEquipmentDTO.getEquiptypeId());
        bizServiceEquipment.setEquipUnit(saveBizServiceEquipmentDTO.getEquipUnit());
        bizServiceEquipment.preInsert(userHolder.getLoggedUserId());
        bizServiceEquipment.setRemark(saveBizServiceEquipmentDTO.getRemark());
        return bizServiceEquipmentDao.saveEntity(bizServiceEquipment);
    }

    /**
     * 根据id查询物料详情
     * @param id 物料id
     * @return 物料详情
     * @author liuduo
     * @date 2018-07-17 19:12:20
     */
    @Override
    public DetailBizServiceEquipmentDTO getById(Long id) {
        return bizServiceEquipmentDao.getById(id);
    }

    /**
     * 编辑物料
     * @param saveBizServiceEquipmentDTO 物料实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-17 18:40:03
     */
    @Override
    public int editEquiptype(SaveBizServiceEquipmentDTO saveBizServiceEquipmentDTO) {
        // 名字校验
        Boolean aboolean =  bizServiceEquipmentDao.editCheckName(saveBizServiceEquipmentDTO);
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        BizServiceEquipment bizServiceEquipment = new BizServiceEquipment();
        bizServiceEquipment.setEquipName(saveBizServiceEquipmentDTO.getEquipName());
        bizServiceEquipment.setEquiptypeId(saveBizServiceEquipmentDTO.getEquiptypeId());
        bizServiceEquipment.setEquipUnit(saveBizServiceEquipmentDTO.getEquipUnit());
        bizServiceEquipment.preUpdate(userHolder.getLoggedUserId());
        bizServiceEquipment.setId(saveBizServiceEquipmentDTO.getId());
        bizServiceEquipment.setRemark(saveBizServiceEquipmentDTO.getRemark());
        return bizServiceEquipmentDao.update(bizServiceEquipment);
    }

    /**
     * 查询物料列表
     * @param equiptypeId 物料类型id
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @Override
    public Page<DetailBizServiceEquipmentDTO> queryList(Long equiptypeId, String keyword, Integer offset, Integer pagesize) {
        return bizServiceEquipmentDao.queryList(equiptypeId, keyword, offset, pagesize);
    }

    /**
     * 返回计量单位
     * @return 枚举
     * @author liuduo
     * @date 2018-08-01 17:56:07
     */
    @Override
    public List<Map<String, String>> getUnit() {
        List<Map<String,String>> list = new ArrayList<>();
        List<ProductUnitEnum> equipUnitEnums = Arrays.asList(ProductUnitEnum.values());
        equipUnitEnums.forEach(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("unitKey", item.name());
            map.put("unitValue", item.getLabel());
            list.add(map);
        });
        return list;
    }

    /**
     * 根据物料类型id查询物料
     * @param equiptypeId 物料类型id
     * @return 物料
     * @author liuduo
     * @date 2018-08-02 10:41:20
     */
    @Override
    public List<DetailBizServiceEquipmentDTO> queryEqupmentByEquiptype(Long equiptypeId) {
        return bizServiceEquipmentDao.queryEqupmentByEquiptype(equiptypeId);
    }

}
