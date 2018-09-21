package com.ccbuluo.business.platform.label.service;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dto.LabelServiceCenterDTO;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签与服务中心关联关系接口
 * @author liuduo
 * @date 2018-07-02 16:52:09
 */
@Service
public interface LabelServiceCenterService {
    /**
     * 保存标签与服务中心关联关系
     * @param lableList 标签与服务中心关联关系
     * @return 状态
     * @author liuduo
     * @date 2018-07-04 17:59:22
     */
    int[] save(List<LabelServiceCenterDTO> lableList);

    /**
    * 根据服务中心code查询关联的标签
    * @param serviceCenterCode 服务中心code
    * @return 服务中心关联的标签
    * @author liuduo
    * @date 2018-07-05 10:20:34
    */
    List<BizServiceLabel> getLabelServiceCenterByCode(String serviceCenterCode);
    /**
    * 编辑标签与服务中心关联关系
    * @param serviceCenterCode 服务中心code
    * @param labels 标签ids
    * @return 编辑是否成功
    * @author liuduo
    * @date 2018-07-05 11:38:58
    */
    int editLabelServiceCenter(String serviceCenterCode, String labels);
}
