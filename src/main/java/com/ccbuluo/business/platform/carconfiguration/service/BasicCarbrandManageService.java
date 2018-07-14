package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.platform.carconfiguration.entity.CarbrandManage;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;
import java.util.Map;

/**
 * 品牌管理service
 * @author chaoshuai
 * @date 2018-05-08 10:54:56
 */
public interface BasicCarbrandManageService {

    /**
     * 品牌新增
     * @param carbrandManage 品牌
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    StatusDto saveCarbrandManage(CarbrandManage carbrandManage);

    /**
     * 品牌编辑
     * @param carbrandManage 品牌
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    StatusDto updateCarbrandManage(CarbrandManage carbrandManage);

    /**
     * 品牌删除
     * @param id 品牌id
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:25:51
     */
    StatusDto deleteCarbrandManage(Long id);

    /**
     * 品牌详情
     * @param id 品牌id
     * @return com.ccbuluo.business.entity.CarbrandManage
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:25:51
     */
    CarbrandManage findCarbrandManageDetail(Long id);

    /**
     * 分页查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @param offset 偏移量
     * @param limit 步长
     * @return com.ccbuluo.db.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    Page<Map<String, Object>> queryCarbrandManagePage(String carbrandName, String initial, int offset, int limit);

    /**
     * 首字母索引列表
     * @param
     * @return java.util.List<java.lang.String>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 19:47:23
     */
    List<String> queryInitialList();

    /**
     * 查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @return java.util.List<com.ccbuluo.business.entity.CarbrandManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-09 10:50:30
     */
    List<CarbrandManage> queryCarbrandManageList(String carbrandName, String initial);

}
