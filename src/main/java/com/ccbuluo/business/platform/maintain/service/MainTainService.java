package com.ccbuluo.business.platform.maintain.service;

import com.ccbuluo.business.entity.BizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.dto.SaveBizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.dto.SearchBizServiceMaintaingroup;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

/**
 * 保养service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-10-30 10:13:24
 */
public interface MainTainService {
    /**
     * 保存保养单
     * @param bizServiceMaintaingroup 保养单信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-10-30 10:34:10
     */
    StatusDto saveMainTain(SaveBizServiceMaintaingroup bizServiceMaintaingroup);

    /**
     * 编辑保养单
     * @param bizServiceMaintaingroup 保养单信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-10-30 10:34:10
     */
    StatusDto editMainTain(SaveBizServiceMaintaingroup bizServiceMaintaingroup);

    /**
     * 修改保修套餐状态
     * @param groupNo 保修套餐编号
     * @param groupStatus 保修套餐状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-10-30 14:20:22
     */
    StatusDto editStatus(String groupNo, String groupStatus);

    /**
     * 查询保养套餐列表
     * @param groupStatus 保养套餐状态
     * @param keyword 保养套餐编号或名字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 保养列表
     * @author liuduo
     * @date 2018-10-30 14:32:21
     */
    StatusDto<Page<BizServiceMaintaingroup>> list(String groupStatus, String keyword, Integer offset, Integer pageSize);

    /**
     * 根据保养套餐编号查询保养套餐详情
     * @param groupNo 保养套餐编号
     * @return 保养详情
     * @author liuduo
     * @date 2018-10-30 15:16:26
     */
    StatusDto<SearchBizServiceMaintaingroup> getDetailByGroupNo(String groupNo);
}
