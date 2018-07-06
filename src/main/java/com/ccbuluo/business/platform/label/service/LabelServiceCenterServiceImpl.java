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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 保存标签与服务中心关联关系
     * @param lableList 标签与服务中心关联关系
     * @return 状态
     * @author liuduo
     * @date 2018-07-04 17:59:22
     */
    @Override
    public int[] save(List<LabelServiceCenterDTO> lableList) {
        return labelServiceCenterDao.saveEntity(lableList);
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
    public int editLabelServiceCenter(String serviceCenterCode, String labels) {
        // 删除之前关联的
        int affected = labelServiceCenterDao.delLabelServiceCenter(serviceCenterCode);
        if (affected == Constants.FAILURESTATUS) {
            return Constants.FAILURESTATUS;
        }
        SaveServiceCenterDTO saveServiceCenterDTO = new SaveServiceCenterDTO();
        saveServiceCenterDTO.setLabelIds(labels);
        int[] ints = serviceCenterService.saveLableServiceCenter(saveServiceCenterDTO, serviceCenterCode);
        if (ints.length == 0) {
            return Constants.FAILURESTATUS;
        }
        return Constants.SUCCESSSTATUS;
    }
}
