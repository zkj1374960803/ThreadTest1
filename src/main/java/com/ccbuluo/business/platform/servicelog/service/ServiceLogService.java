package com.ccbuluo.business.platform.servicelog.service;

import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.platform.servicelog.dao.BizServiceLogDao;

/**
 * 日志service
 * @author zhangkangjian
 * @date 2018-09-04 16:30:08
 */
public interface ServiceLogService {

    /**
     * 新增日志记录
     * @param bizServiceLog 日志实体
     * @author zhangkangjian
     * @date 2018-09-04 16:31:34
     */
    void create(BizServiceLog bizServiceLog);
}
