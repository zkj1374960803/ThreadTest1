package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.db.Page;

import java.util.List;

/**
 * 工时service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 10:21:52
 */
public interface MaintainitemService {
    /**
     * 保存工时
     * @param saveBizServiceMaintainitemDTO 工时实体dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 10:28:20
     */
    int save(SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO);

    /**
     * 根据id查询详情
     * @param id 工时id
     * @return 工时详情
     * @author liuduo
     * @date 2018-07-18 10:57:07
     */
    DetailBizServiceMaintainitemDTO getById(Long id);

    /**
     * 编辑工时
     * @param saveBizServiceMaintainitemDTO 工时实体dto
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-18 10:28:20
     */
    int edit(SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO);

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 物料列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    Page<DetailBizServiceMaintainitemDTO> queryList(String keyword, Integer offset, Integer pagesize);
}
