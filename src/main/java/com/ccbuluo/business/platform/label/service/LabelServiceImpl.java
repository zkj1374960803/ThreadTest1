package com.ccbuluo.business.platform.label.service;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dao.BizServiceLabelDao;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.business.platform.supplier.service.SupplierService;
import com.ccbuluo.business.platform.supplier.service.SupplierServiceImpl;
import com.ccbuluo.core.common.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource(name = "supplierServiceImpl")
    SupplierServiceImpl supplierServiceImpl;

    /**
     * 创建标签
     * @param bizServiceLabel 标签实体
     * @author zhangkangjian
     * @date 2018-07-02 16:59:11
     */
    @Override
    public ListLabelDTO createLabel(BizServiceLabel bizServiceLabel) {
        // 标签验重
        supplierServiceImpl.compareRepeat(bizServiceLabel.getId(), bizServiceLabel.getLabelName(),"label_name", "biz_service_label", "标签名称重复！");
        // 保存标签
        String loggedUserId = userHolder.getLoggedUserId();
        bizServiceLabel.setCreator(loggedUserId);
        bizServiceLabel.setOperator(loggedUserId);
        Long aLong = bizServiceLabelDao.saveEntity(bizServiceLabel);
        ListLabelDTO lld = new ListLabelDTO();
        lld.setId(aLong);
        lld.setLabelName(bizServiceLabel.getLabelName());
        return lld;
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

    /**
     * 编辑标签
     * @param id 标签id
     * @param labelName 标签名称
     * @author zhangkangjian
     * @date 2018-07-11 18:33:14
     */
    @Override
    public void editlabel(Long id, String labelName) {
        supplierServiceImpl.compareRepeat(id, labelName,"label_name", "biz_service_label", "标签名称重复！");
        BizServiceLabel label = new BizServiceLabel();
        label.setId(id);
        label.setLabelName(labelName);
        label.setOperator(userHolder.getLoggedUserId());
        label.setOperateTime(new Date());
        bizServiceLabelDao.editlabel(label);
    }
}
