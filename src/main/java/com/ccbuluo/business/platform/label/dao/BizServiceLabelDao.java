package com.ccbuluo.business.platform.label.dao;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  标签dao
 * @author zhangkangjian
 * @date 2018-07-03 09:14:06
 * @version V1.0.0
 */
@Repository
public class BizServiceLabelDao extends BaseDao<BizServiceLabel> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 实体
     * @param entity 实体
     * @return Long id 新增返回i
     * @author zhangkangjian
     * @date 2018-07-03 09:14:06
     */
    public Long saveEntity(BizServiceLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO biz_service_label ( label_name,creator,")
            .append("operator,delete_flag ) VALUES (  :labelName, :creator,")
            .append(" :operator, :deleteFlag )");
        return super.saveRid(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int update(BizServiceLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_label SET label_name = :labelName,")
            .append("creator = :creator,create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public BizServiceLabel getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,label_name,creator,create_time,operator,operate_time,")
            .append("delete_flag FROM biz_service_label WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceLabel.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author zhangkangjian
     * @date 2018-07-03 09:14:06
     */
    public int deleteById(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_label WHERE id = :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 查询标签的列表
     * @return List<ListLabelDTO> 列表标签
     * @author zhangkangjian
     * @date 2018-07-03 11:47:54
     */
    public List<ListLabelDTO> findListLabel() {
        String sql = " SELECT a.id,a.label_name FROM biz_service_label a order by a.id desc";
        return queryListBean(ListLabelDTO.class, sql, Maps.newHashMap());
    }

    /**
     * 编辑标签
     * @param label 标签实体
     * @author zhangkangjian
     * @date 2018-07-11 18:33:14
     */
    public void editlabel(BizServiceLabel label) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_label SET label_name = :labelName,")
            .append("operator = :operator,operate_time = :operateTime WHERE id = :id ");
        super.updateForBean(sql.toString(), label);
    }
}
