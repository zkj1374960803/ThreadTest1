package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMaintainitemDao;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.ConstantCallSite;
import java.util.List;

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
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FL);
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
        return bizServiceMaintainitemDao.getById(id);
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
        return bizServiceMaintainitemDao.update(bizServiceMaintainitem);
    }

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    @Override
    public Page<DetailBizServiceMaintainitemDTO> queryList(String keyword, Integer offset, Integer pagesize) {
        return bizServiceMaintainitemDao.queryList(keyword, offset, pagesize);
    }
}
