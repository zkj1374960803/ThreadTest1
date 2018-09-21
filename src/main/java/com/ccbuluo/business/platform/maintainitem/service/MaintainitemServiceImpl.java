package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMaintainitemDao;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.order.service.ServiceOrderService;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.servicelog.service.ServiceLogService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 工时service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 10:23:24
 */
@Service
public class MaintainitemServiceImpl implements MaintainitemService{

    @Autowired
    private BizServiceMaintainitemDao bizServiceMaintainitemDao;
    @Autowired
    private GenerateProjectCodeService generateProjectCodeService;
    @Autowired
    private MultiplepriceServiceImpl multiplepriceService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private ServiceLogService serviceLogService;
    @Autowired
    private ServiceOrderService serviceOrderService;

    /**
     * 保存工时
     * @param saveBizServiceMaintainitemDTO 工时实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 10:28:20
     */
    @Override
    public int save(SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO) {
        // 名字验重
        Boolean aboolean = bizServiceMaintainitemDao.checkName(saveBizServiceMaintainitemDTO.getMaintainitemName());
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        // 生成code
        String serviceCenterCode = null;
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FL);
        if (stringStatusDto.getCode().equals(Constants.SUCCESS_CODE)) {
            serviceCenterCode = stringStatusDto.getData();
        } else {
            return Constants.FAILURE_TWO;
        }
        BizServiceMaintainitem bizServiceMaintainitem = new BizServiceMaintainitem();
        bizServiceMaintainitem.setMaintainitemCode(serviceCenterCode);
        bizServiceMaintainitem.setMaintainitemName(saveBizServiceMaintainitemDTO.getMaintainitemName());
        bizServiceMaintainitem.setUnitPrice(saveBizServiceMaintainitemDTO.getUnitPrice());
        bizServiceMaintainitem.preInsert(userHolder.getLoggedUserId());
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.SAVE.name());
        bizServiceLog.setSubjectType("MaintainitemServiceImpl");
        bizServiceLog.setSubjectKeyvalue(serviceCenterCode);
        bizServiceLog.setLogContent("保存工时");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return bizServiceMaintainitemDao.saveEntity(bizServiceMaintainitem);
    }

    /**
     * 根据id查询详情
     * @param id 工时id
     * @return 工时详情
     * @author liuduo
     * @date 2018-07-18 10:57:07
     */
    @Override
    public DetailBizServiceMaintainitemDTO getById(Long id) {
        DetailBizServiceMaintainitemDTO byId = bizServiceMaintainitemDao.getById(id);
        Boolean aboolean = serviceOrderService.getByProductCode(byId.getMaintainitemCode());
        if (aboolean) {
            byId.setQuote(Constants.STATUS_FLAG_ZERO);
        } else {
            byId.setQuote(Constants.STATUS_FLAG_ONE);
        }
        return byId;
    }

    /**
     * 编辑工时
     * @param saveBizServiceMaintainitemDTO 工时实体dto
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-18 10:28:20
     */
    @Override
    public int edit(SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO) {
        // 名字验重
        Boolean aboolean =  bizServiceMaintainitemDao.editCheckName(saveBizServiceMaintainitemDTO);
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        BizServiceMaintainitem bizServiceMaintainitem = new BizServiceMaintainitem();
        bizServiceMaintainitem.setMaintainitemName(saveBizServiceMaintainitemDTO.getMaintainitemName());
        bizServiceMaintainitem.setUnitPrice(saveBizServiceMaintainitemDTO.getUnitPrice());
        bizServiceMaintainitem.preUpdate(userHolder.getLoggedUserId());
        bizServiceMaintainitem.setId(saveBizServiceMaintainitemDTO.getId());
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
        bizServiceLog.setSubjectType("MaintainitemServiceImpl");
        bizServiceLog.setSubjectKeyvalue(saveBizServiceMaintainitemDTO.getMaintainitemName());
        bizServiceLog.setLogContent("编辑工时");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return bizServiceMaintainitemDao.update(bizServiceMaintainitem);
    }

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @Override
    public Page<DetailBizServiceMaintainitemDTO> queryList(String keyword, Integer offset, Integer pageSize) {
        return bizServiceMaintainitemDao.queryList(keyword, offset, pageSize);
    }

    /**
     * 根据code删除工时
     * @param maintainitemCode 工时code
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-09-18 14:32:56
     */
    @Override
    public int delete(String maintainitemCode) {
        // 查询工时是否被引用
        Boolean aboolean = serviceOrderService.getByProductCode(maintainitemCode);
        if (aboolean) {
            return Constants.FAILURE_ONE;
        }
        // 删除工时
        return bizServiceMaintainitemDao.delete(maintainitemCode);
    }
}
