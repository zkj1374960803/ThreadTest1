package com.ccbuluo.business.platform.servicelog.service;

import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.platform.servicelog.dao.BizServiceLogDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 日志实现类
 * @author zhangkangjian
 * @date 2018-09-04 16:30:43
 */
@Service
public class ServiceLogServiceImpl implements ServiceLogService {
    @Resource
    private BizServiceLogDao bizServiceLogDao;

    /**
     * 新增日志记录
     * @param bizServiceLog 日志实体
     * @author zhangkangjian
     * @date 2018-09-04 16:31:34
     */
    @Override
    public void create(BizServiceLog bizServiceLog) {
        bizServiceLogDao.saveBizServiceLog(bizServiceLog);
    }
}
