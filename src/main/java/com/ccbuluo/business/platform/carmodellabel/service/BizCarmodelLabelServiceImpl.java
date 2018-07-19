package com.ccbuluo.business.platform.carmodellabel.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizCarmodelLabel;
import com.ccbuluo.business.platform.carmodellabel.dao.BizCarmodelLabelDao;
import com.ccbuluo.business.platform.carmodellabel.dto.SearchBizCarmodelLabelDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 车型标签基本信息service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BizCarmodelLabelServiceImpl implements BizCarmodelLabelService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BizCarmodelLabelDao bizCarmodelLabelDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired

    /**
     * 该标签已经存在！
     */
    private static final String CARMODELLABEL_VERIFY = "该标签已经存在！";


    /**
     * 新增车型标签
     * @param bizCarmodelLabel 车型标签to
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto saveCarmodelLabel(BizCarmodelLabel bizCarmodelLabel) {
        // 1.1 车型标签验证唯一性
        StatusDto statusDto = findServiceMaintaincarVerification(bizCarmodelLabel);
        if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
            return statusDto;
        }
        try {
            // 1.保存车型标签基本信息
            buildServiceMaintaincar(bizCarmodelLabel);
            long carcoreInfoId = bizCarmodelLabelDao.saveCarmodelLabel(bizCarmodelLabel);

            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("车型标签新增失败！", e);
            throw e;
        }
    }
    /**
     * 编辑车型标签
     * @param bizCarmodelLabel 车辆信息dto
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto editCarmodelLabel(BizCarmodelLabel bizCarmodelLabel){
        // 1.1 车型标签基本信息验证唯一性
        StatusDto statusDto = findServiceMaintaincarVerification(bizCarmodelLabel);
        if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
            return statusDto;
        }

        try {

            // 1.保存车型标签基本信息
            buildServiceMaintaincar(bizCarmodelLabel);
            long carcoreInfoId = bizCarmodelLabelDao.updateCarmodelLabel(bizCarmodelLabel);

            return StatusDto.buildSuccessStatusDto();

        } catch (Exception e) {
            logger.error("车型标签更新失败！", e);
            throw e;
        }
    }

    /**
     * 构建车型标签信息实体
     * @param bizCarmodelLabel
     * @return void
     * @exception
     * @author wuyibo
     * @date 2018-05-10 18:38:06
     */
    private void buildServiceMaintaincar(BizCarmodelLabel bizCarmodelLabel) {
        // 3.通用字段
        bizCarmodelLabel.preInsert(userHolder.getLoggedUserId());
    }

    /**
     *      * 车型标签基本信息验证唯一性
     *      * @param bizCarmodelLabel 车型标签基本信息
     *      * @return com.ccbuluo.http.StatusDto
     *      * @exception
     * @author wuyibo
     * @date 2018-05-09 14:02:30
     */
    @Override
    public StatusDto findServiceMaintaincarVerification(BizCarmodelLabel bizCarmodelLabel) {
        bizCarmodelLabel.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
        int vinNumberCount = bizCarmodelLabelDao.findCarmodelLabelExist(bizCarmodelLabel);
        StringBuilder result = new StringBuilder();
        if (vinNumberCount > 0) {
            result.append(CARMODELLABEL_VERIFY);
        }
        if (StringUtils.isNotBlank(result.toString())) {
            return StatusDto.buildFailureStatusDto(result.toString());
        }
        return StatusDto.buildSuccessStatusDto();
    }
    /**
     * 根据车型标签id查询车型标签详情
     * @param labelCode 车型标签labelCode
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @Override
    public BizCarmodelLabel queryCarmodelLabelBylabelCode(String labelCode){
        return bizCarmodelLabelDao.queryCarmodelLabelBylabelCode(labelCode);
    }

    /**
     * 根据车型标签id删除车型标签
     * @param labelCode 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    @Override
    public int deleteCarcoreInfoBylabelCode(String labelCode){
        return bizCarmodelLabelDao.deleteCarcoreInfoBylabelCode(labelCode);
    }
    /**
     * 车型标签列表分页查询
     * @param Keyword (车型标签名称)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    @Override
    public Page<SearchBizCarmodelLabelDTO> queryCarmodelLabelList(String Keyword, Integer offset, Integer pageSize){
        return bizCarmodelLabelDao.queryCarmodelLabelList(Keyword, offset, pageSize);
    }


}
