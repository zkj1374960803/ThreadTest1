package com.ccbuluo.business.platform.label.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dao.BizServiceLabelDao;
import com.ccbuluo.business.platform.label.dao.LabelServiceCenterDao;
import com.ccbuluo.business.platform.label.dto.LabelServiceCenterDTO;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SaveServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.service.ServiceCenterService;
import com.ccbuluo.business.platform.servicecenter.service.ServiceCenterServiceImpl;
import com.ccbuluo.core.common.UserHolder;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签与服务中心关联关系接口
 * @author liuduo
 * @date 2018-07-02 16:57:45
 */
@Service
public class LabelServiceCenterServiceImpl implements LabelServiceCenterService {

    @Autowired
    private LabelServiceCenterDao labelServiceCenterDao;
    @Autowired
    private ServiceCenterServiceImpl serviceCenterService;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 保存标签与服务中心关联关系
     * @param lableList 标签与服务中心关联关系
     * @return 状态
     * @author liuduo
     * @date 2018-07-04 17:59:22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int[] save(List<LabelServiceCenterDTO> lableList) {
        try {
            return labelServiceCenterDao.saveEntity(lableList);
        } catch (Exception e) {
            logger.error("关联失败！", e);
            throw e;
        }
    }

    /**
     * 根据服务中心code查询关联的标签
     * @param serviceCenterCode 服务中心code
     * @return 服务中心关联的标签
     * @author liuduo
     * @date 2018-07-05 10:20:34
     */
    @Override
    public List<BizServiceLabel> getLabelServiceCenterByCode(String serviceCenterCode) {
        return labelServiceCenterDao.getLabelServiceCenterByCode(serviceCenterCode);
    }

    /**
     * 编辑标签与服务中心关联关系
     * @param serviceCenterCode 服务中心code
     * @param labels 标签ids
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-05 11:38:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editLabelServiceCenter(String serviceCenterCode, String labels) {
        try {
            // 删除之前关联的
            labelServiceCenterDao.delLabelServiceCenter(serviceCenterCode);
            SaveServiceCenterDTO saveServiceCenterDTO = new SaveServiceCenterDTO();
            saveServiceCenterDTO.setLabelIds(labels);
            serviceCenterService.saveLableServiceCenter(saveServiceCenterDTO, serviceCenterCode);
            return Constants.SUCCESSSTATUS;
        } catch (Exception e) {
            logger.error("关联失败！", e);
            throw e;
        }
    }
}
