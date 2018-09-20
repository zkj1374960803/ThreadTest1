package com.ccbuluo.business.platform.equipment.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizServiceEquipment;
import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.dto.SaveBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.servicelog.service.ServiceLogService;
import com.ccbuluo.business.platform.supplier.service.SupplierService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
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
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private AllocateApplyService allocateApplyService;
    @Autowired
    private ServiceLogService serviceLogService;

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
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.SAVE.name());
        bizServiceLog.setSubjectType("EquipmentServiceImpl");
        bizServiceLog.setSubjectKeyvalue(serviceCenterCode);
        bizServiceLog.setLogContent("保存物料");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
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
        // 查询最新生效价格
        DetailBizServiceEquipmentDTO equipmentDTO = bizServiceEquipmentDao.getById(id);
        try {
            BigDecimal suggestedPrice = bizServiceEquipmentDao.findSuggestedPrice(equipmentDTO.getEquipCode());
            equipmentDTO.setSuggestedPrice(suggestedPrice);
        }catch (Exception e){
            e.printStackTrace();
        }
        return equipmentDTO;
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
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
        bizServiceLog.setSubjectType("EquipmentServiceImpl");
        bizServiceLog.setSubjectKeyvalue(saveBizServiceEquipmentDTO.getEquipName());
        bizServiceLog.setLogContent("编辑物料");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return bizServiceEquipmentDao.update(bizServiceEquipment);
    }

    /**
     * 查询物料列表
     * @param equiptypeId 物料类型id
     * @param keyword 关键字
     * @param carpartsPriceType
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @Override
    public Page<DetailBizServiceEquipmentDTO> queryList(Long equiptypeId, String keyword, String carpartsPriceType, Integer offset, Integer pagesize) {
        // 查询有价格的物料code
        List<String> productNoList = bizServiceEquipmentDao.queryProductPrice(Constants.PRODUCT_TYPE_EQUIPMENT);
        return bizServiceEquipmentDao.queryList(carpartsPriceType, productNoList, equiptypeId, keyword, offset, pagesize);
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

    /**
     * 根据code删除物料
     * @param equipCode 物料code
     * @return 是否删除成功
     * @author liuduo
     * @date 2018-08-23 11:10:57
     */
    @Override
    public int delete(String equipCode) {
        // 根据code查询物料是否已经被供应商关联或者申请里有该物料
        Boolean aboolean = supplierService.getSupplier(equipCode);
        Boolean bboolean = allocateApplyService.getEquipMent(equipCode);
        if (aboolean || bboolean) {
            return Constants.FAILURE_ONE;
        }
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.DELETE.name());
        bizServiceLog.setSubjectType("EquipmentServiceImpl");
        bizServiceLog.setSubjectKeyvalue(equipCode);
        bizServiceLog.setLogContent("删除物料");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        // 删除物料
        return bizServiceEquipmentDao.delete(equipCode);
    }

}
