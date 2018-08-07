package com.ccbuluo.business.thrid;

import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BasicCarmodelManageDao extends BaseDao<BasicCarmodelManage> {
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
     * @date 2018-08-07 11:55:41
     */
    public int saveEntity(BasicCarmodelManage entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_manage ( carmodel_name,carbrand_id,")
            .append("carseries_id,model_title,car_type,model_master_image,model_image,")
            .append("carmodel_number,carmodel_status,create_time,creator,operate_time,")
            .append("operator,delete_flag ) VALUES (  :carmodelName, :carbrandId,")
            .append(" :carseriesId, :modelTitle, :carType, :modelMasterImage, :modelImage,")
            .append(" :carmodelNumber, :carmodelStatus, :createTime, :creator,")
            .append(" :operateTime, :operator, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int update(BasicCarmodelManage entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_manage SET carmodel_name = :carmodelName,")
            .append("carbrand_id = :carbrandId,carseries_id = :carseriesId,")
            .append("model_title = :modelTitle,car_type = :carType,")
            .append("model_master_image = :modelMasterImage,model_image = :modelImage,")
            .append("carmodel_number = :carmodelNumber,carmodel_status = :carmodelStatus,")
            .append("create_time = :createTime,creator = :creator,")
            .append("operate_time = :operateTime,operator = :operator,")
            .append("delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public BasicCarmodelManage getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,carmodel_name,carbrand_id,carseries_id,model_title,")
            .append("car_type,model_master_image,model_image,carmodel_number,")
            .append("carmodel_status,create_time,creator,operate_time,operator,delete_flag")
            .append(" FROM basic_carmodel_manage WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BasicCarmodelManage.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-07 11:55:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM basic_carmodel_manage WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }
}
