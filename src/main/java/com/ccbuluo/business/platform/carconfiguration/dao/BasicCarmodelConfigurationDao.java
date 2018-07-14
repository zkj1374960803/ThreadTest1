package com.ccbuluo.business.platform.carconfiguration.dao;

import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelConfiguration;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 车型具体配置dao
 * @author chaoshuai
 * @date 2018-05-08 10:49:22
 */
@Repository
public class BasicCarmodelConfigurationDao extends BaseDao<CarmodelConfiguration> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 根据车型id查询车型配置
     * @param carmodelId 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 18:13:41
     */
    public List<CarmodelConfiguration> getByCarModelId(Long carmodelId){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("carmodelId",carmodelId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,carmodel_id,carmodel_parameter_id,parameter_name,parameter_value FROM basic_carmodel_configuration WHERE carmodel_id = :carmodelId ");
        List<CarmodelConfiguration> carmodelConfigurations = super.queryListBean(CarmodelConfiguration.class, sql.toString(), param);
        return carmodelConfigurations;
    }

    /**
     * 根据车型id和参数名称查询车型参数
     * @param carmodelId 车型id
     * @param parameterName 参数名称
     * @return com.ccbuluo.business.entity.CarmodelConfiguration
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 15:50:15
     */
    public CarmodelConfiguration getByCarModelIdAndName(Long carmodelId,String parameterName){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("carmodelId",carmodelId);
        param.put("parameterName",parameterName);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,carmodel_id,carmodel_parameter_id,parameter_name,parameter_value FROM basic_carmodel_configuration WHERE carmodel_id = :carmodelId AND parameter_name = :parameterName");
        CarmodelConfiguration carmodelConfiguration = super.findForBean(CarmodelConfiguration.class, sql.toString(), param);
        return carmodelConfiguration;
    }

    /**
     * 新增车型配置参数
     * @param carmodelConfiguration
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:37:32
     */
    public long create(CarmodelConfiguration carmodelConfiguration){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_configuration")
           .append(" (carmodel_id, carmodel_parameter_id, parameter_name, parameter_value) ")
           .append(" VALUES ( :carmodelId, :carmodelParameterId, :parameterName, :parameterValue) ");
        Long rid = super.saveRid(sql.toString(), carmodelConfiguration);
        return rid;
    }

    /**
     * 批量新增车型配置参数
     * @param carmodelConfigurations
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:37:32
     */
    public List<Long> batchCreate(List<CarmodelConfiguration> carmodelConfigurations){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_configuration")
            .append(" (carmodel_id, carmodel_parameter_id, parameter_name, parameter_value) ")
            .append(" VALUES ( :carmodelId, :carmodelParameterId, :parameterName, :parameterValue) ");
        List<Long> longs = super.batchInsertForListBean(sql.toString(), carmodelConfigurations);
        return longs;
    }

    /**
     * 批量修改
     * @param carmodelConfigurations
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 11:14:06
     */
    public List<Long> batchEdit(List<CarmodelConfiguration> carmodelConfigurations){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_configuration SET ")
           .append("  carmodel_id= :carmodelId, carmodel_parameter_id= :carmodelParameterId, parameter_name= :parameterName, parameter_value= :parameterValue WHERE id= :id");
        List<Long> longs = super.batchInsertForListBean(sql.toString(), carmodelConfigurations);
        return longs;
    }

    /**
     * 根据车型id删除车型配置
     * @param modelId 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-19 18:16:38
     */
    public int deleteByModelId(Long modelId){
        StringBuilder sql = new StringBuilder();
        Map<String , Object> param = Maps.newHashMap();
        param.put("modelId",modelId);
        sql.append(" DELETE FROM basic_carmodel_configuration WHERE carmodel_id = :modelId ");
        int i = super.updateForMap(sql.toString(), param);
        return i;
    }
}
