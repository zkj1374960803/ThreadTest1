package com.ccbuluo.business.platform.label.service;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签接口
 * @author zhangkangjian
 * @date 2018-07-02 16:52:09
 */
@Service
public interface LabelService {
    /**
     *  创建标签
     * @param bizServiceLabel 标签实体
     * @author zhangkangjian
     * @date 2018-07-02 16:59:11
     */
    ListLabelDTO createLabel(BizServiceLabel bizServiceLabel);
    /**
     * 删除标签
     * @param id 标签id
     * @author zhangkangjian
     * @date 2018-07-03 11:38:31
     */
    void deleteLabel(Long id);
    /**
     * 查询标签列表
     * @return List<ListLabelDTO> 标签的列表
     * @author zhangkangjian
     * @date 2018-07-03 11:46:25
     */
    List<ListLabelDTO> findListLabel();
    /**
     * 编辑标签
     * @param id 标签id
     * @param labelName 标签名称
     * @author zhangkangjian
     * @date 2018-07-11 18:33:14
     */
    void editlabel(Long id, String labelName);
}
