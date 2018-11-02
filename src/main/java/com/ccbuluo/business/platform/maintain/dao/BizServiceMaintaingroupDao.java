package com.ccbuluo.business.platform.maintain.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.dto.SaveBizServiceMaintaingroup;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Console;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-10-30 10:24:19
 * @version V1.0.0
 */
@Repository
public class BizServiceMaintaingroupDao extends BaseDao<BizServiceMaintaingroup> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 实体
     * @param entity 实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-10-30 10:24:19
     */
    public int saveEntity(SaveBizServiceMaintaingroup entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_maintaingroup ( group_code,group_name,")
            .append("group_price,group_status,group_image,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :groupCode, :groupName,")
            .append(" :groupPrice, :groupStatus, :groupImage, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-10-30 10:24:19
     */
    public int update(SaveBizServiceMaintaingroup entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_maintaingroup SET ")
            .append("group_name = :groupName,group_price = :groupPrice,")
            .append("group_image = :groupImage,")
            .append("operator = :operator,operate_time = :operateTime,")
            .append("remark = :remark WHERE group_code= :groupCode");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param groupNo  保养套餐编号
     * @author liuduo
     * @date 2018-10-30 10:24:19
     */
    public SaveBizServiceMaintaingroup getByGroupNo(String groupNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,group_code,group_name,group_price,group_status,group_image")
            .append(" FROM biz_service_maintaingroup WHERE group_code= :groupNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("groupNo", groupNo);
        return super.findForBean(SaveBizServiceMaintaingroup.class, sql.toString(), params);
    }


    /**
     * 修改保修套餐状态
     * @param groupNo 保修套餐编号
     * @param groupStatus 保修套餐状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-10-30 14:20:22
     */
    public void editStatus(String groupNo, String groupStatus) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("groupNo", groupNo);
        params.put("groupStatus", groupStatus);

        String sql = "UPDATE biz_service_maintaingroup SET group_status = :groupStatus WHERE group_code = :groupNo";

        updateForMap(sql, params);
    }

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
    public Page<BizServiceMaintaingroup> list(String groupStatus, String keyword, Integer offset, Integer pageSize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,group_code,group_name,group_price,group_status,group_image ")
            .append(" FROM biz_service_maintaingroup WHERE 1=1");
        if (StringUtils.isNotBlank(groupStatus)) {
            params.put("groupStatus", groupStatus);
            sql.append(" AND group_status = :groupStatus");
        }
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
            sql.append(" AND (group_code LIKE CONCAT('%',:keyword,'%') OR group_name LIKE CONCAT('%',:keyword,'%')");
        }
        sql.append(" AND delete_flag = :deleteFlag ORDER BY operate_time DESC");

        return queryPageForBean(BizServiceMaintaingroup.class, sql.toString(), params, offset, pageSize);
    }

    /**
     * 名字校验（新增用）
     * @param groupName 保养套餐名字
     * @return 名字是否重复
     * @author liuduo
     * @date 2018-11-02 16:15:05
     */
    public Boolean checkName(String groupName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("groupName", groupName);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_maintaingroup WHERE group_name = :groupName";

        return findForObject(sql, params, Boolean.class);
    }

    /**
     * 名字校验（编辑用）
     * @param id 保养套餐id
     * @param groupName 保养套餐名字
     * @return 名字是否重复
     * @author liuduo
     * @date 2018-11-02 16:15:05
     */
    public Boolean editCheckName(Long id, String groupName) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("groupName", groupName);
        param.put("id", id);
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_maintaingroup WHERE id <> :id AND group_name = :groupName AND delete_flag = :deleteFlag";

        return findForObject(sql, param, Boolean.class);
    }
}
