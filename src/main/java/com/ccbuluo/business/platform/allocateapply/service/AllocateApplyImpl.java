package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:29:54
 */
@Service
public class AllocateApplyImpl implements AllocateApply{
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    /**
     * 创建物料或者零配件申请
     * @param bizAllocateApply 申请单实体
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    @Override
    public void createAllocateApply(BizAllocateApply bizAllocateApply) {
        // 保存申请单基础数据
        bizAllocateApplyDao.saveEntity(bizAllocateApply);
        // 保存申请单详情数据
        List<BizAllocateapplyDetail> allocateapplyDetailList = bizAllocateApply.getAllocateapplyDetailList();
        bizAllocateApplyDao.batchInsertForapplyDetailList(allocateapplyDetailList);

    }
}
