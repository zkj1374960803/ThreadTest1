package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarbrandManageDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarseriesManageDao;
import com.ccbuluo.business.platform.carconfiguration.entity.CarbrandManage;
import com.ccbuluo.business.platform.carconfiguration.utils.RegularCodeProductor;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BasicCarbrandManageServiceImpl implements BasicCarbrandManageService{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasicCarbrandManageDao basicCarbrandManageDao;
    @Autowired
    private BasicCarseriesManageDao basicCarseriesManageDao;
    @Autowired
    private RegularCodeProductor regularCodeProductor;
    @Autowired
    private UserHolder userHolder;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;

    /**
     * 存储redis时当前模块的名字
     */
    private static final String CAR_BRAND_NUMBER = "car_brand_number";

    /**
     * 品牌新增
     * @param carbrandManage 品牌
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto saveCarbrandManage(CarbrandManage carbrandManage) {

        // 1.判断品牌是否存在
        if (findCarbrandRenameExist(carbrandManage)) {
            return StatusDto.buildFailureStatusDto("该品牌已经存在！");
        }
        // 2.品牌编码
        StatusDto<String> statusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FB);
        //获取code失败
        if(!statusDto.isSuccess()){
            return statusDto;
        }
        carbrandManage.setCarbrandNumber(statusDto.getData());
        // 3.公共字段
        carbrandManage.preInsert(userHolder.getLoggedUserId());

        try {
            // 4.保存品牌
            basicCarbrandManageDao.saveCarbrandManage(carbrandManage);
            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("品牌新增失败！", e);
            throw e;
        }
    }

    /**
     * 获取品牌编码
     * @param
     * @return java.lang.String
     * @exception
     * @author wuyibo
     * @date 2018-05-16 09:58:12
     */
    private String findCarBrandNumber() {
        // 获取认证中心的appId 构建redis存储的key
        String appId = SystemPropertyHolder.getBaseAppid();
        StringBuilder key = new StringBuilder();
        key.append(appId).append(Constants.CAR_COLON).append(Constants.CAR_CONFIGURATION).append(Constants.CAR_COLON).append(CAR_BRAND_NUMBER);
        return regularCodeProductor.getNextCode(key.toString(), Constants.CAR_BRAND_CODING, Constants.CAR_BRAND_LENGTH);
    }

    /**
     * 判断品牌是否存在
     * @param carbrandManage 品牌
     * @return boolean
     * @exception
     * @author wuyibo
     * @date 2018-05-16 09:54:13
     */
    private boolean findCarbrandRenameExist(CarbrandManage carbrandManage) {
        int count = basicCarbrandManageDao.countCarbrandRename(carbrandManage.getId(), carbrandManage.getCarbrandName(), Constants.DELETE_FLAG_NORMAL);
        return count > 0;
    }

    /**
     * 品牌编辑
     * @param carbrandManage 品牌
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto updateCarbrandManage(CarbrandManage carbrandManage) {

        // 1.判断品牌是否存在
        if (findCarbrandRenameExist(carbrandManage)) {
            return StatusDto.buildFailureStatusDto("该品牌已经存在！");
        }

        try {
            // 2.保存品牌
            carbrandManage.preUpdate(userHolder.getLoggedUserId());
            basicCarbrandManageDao.updateCarbrandManage(carbrandManage);
            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("品牌编辑失败！", e);
            throw e;
        }
    }

    /**
     * 品牌删除
     * @param id 品牌id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:36:30
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto deleteCarbrandManage(Long id) {

        // 1.判断品牌下是否有车系数据
        int count = basicCarseriesManageDao.countCarseriesManage(id);
        if (count > 0) {
            return StatusDto.buildFailureStatusDto("品牌下已有车系数据，无法删除！");
        }

        try {
            // 2.删除品牌
            basicCarbrandManageDao.deleteCarbrandManage(id);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("品牌删除失败！", e);
            throw e;
        }
    }

    /**
     * 品牌详情
     * @param id 品牌id
     * @return com.ccbuluo.business.entity.CarbrandManage
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:36:30
     */
    @Override
    public CarbrandManage findCarbrandManageDetail(Long id) {
        return basicCarbrandManageDao.findCarbrandManageDetail(id);
    }

    /**
     * 分页查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @param offset 偏移量
     * @param limit 步长
     * @return com.ccbuluo.db.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    @Override
    public Page<Map<String, Object>> queryCarbrandManagePage(String carbrandName, String initial, int offset, int limit) {
        return basicCarbrandManageDao.queryCarbrandManagePage(carbrandName, initial, offset, limit);
    }

    /**
     * 首字母索引列表
     * @param
     * @return java.util.List<java.lang.String>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 19:47:42
     */
    @Override
    public List<String> queryInitialList() {
        return basicCarbrandManageDao.queryInitialList();
    }

    /**
     * 查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @return java.util.List<com.ccbuluo.business.entity.CarbrandManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-09 10:50:30
     */
    @Override
    public List<CarbrandManage> queryCarbrandManageList(String carbrandName, String initial) {
        return basicCarbrandManageDao.queryCarbrandManageList(carbrandName, initial);
    }

}
