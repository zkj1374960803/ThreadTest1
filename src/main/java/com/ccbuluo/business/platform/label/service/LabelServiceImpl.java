package com.ccbuluo.business.platform.label.service;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dao.BizServiceLabelDao;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.core.common.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签实现类
 * @author zhangkangjian
 * @date 2018-07-02 16:57:45
 */
@Service
public class LabelServiceImpl implements LabelService {
    @Resource(name = "bizServiceLabelDao")
    private BizServiceLabelDao bizServiceLabelDao;
    @Autowired
    UserHolder userHolder;

    /**
     * 创建标签
     * @param bizServiceLabel 标签实体
     * @author zhangkangjian
     * @date 2018-07-02 16:59:11
     */
    @Override
    public void createLabel(BizServiceLabel bizServiceLabel) {
        String loggedUserId = userHolder.getLoggedUserId();
        bizServiceLabel.setCreator(loggedUserId);
        bizServiceLabel.setOperator(loggedUserId);
        bizServiceLabelDao.saveEntity(bizServiceLabel);
    }

    /**
     * 删除标签
     * @param id 标签id
     * @author zhangkangjian
     * @date 2018-07-03 11:38:31
     */
    @Override
    public void deleteLabel(Long id) {
        bizServiceLabelDao.deleteById(id);
    }

    /**
     * 查询标签列表
     * @return List<ListLabelDTO> 标签的列表
     * @author zhangkangjian
     * @date 2018-07-03 11:46:25
     */
    @Override
    public List<ListLabelDTO> findListLabel() {
        return bizServiceLabelDao.findListLabel();
    }
}
