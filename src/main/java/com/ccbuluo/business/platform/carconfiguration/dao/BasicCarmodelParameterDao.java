package com.ccbuluo.business.platform.carconfiguration.dao;

import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelParameter;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 车型参数配置dao
 * @author chaoshuai
 * @date 2018-05-08 10:48:32
 */
@Repository
public class BasicCarmodelParameterDao extends BaseDao<CarmodelParameter> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    private String SQL_BUILD = " id, parameter_name, value_type, optional_list, manual_add_flag, required_flag, sort_number, carmodel_label_id, create_time, creator, operate_time, operator, delete_flag ";
    /**
     * 分页查询所有配置参数
     * @param parameterName 配置参数名称
     * @param offset 偏移量
     * @param limit 步长
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:58:58
     */
    public Page<CarmodelParameter> queryPageForParameter(String parameterName, String valueType, Integer carmodelLabelId, int offset, int limit){
        Map<String ,Object> param = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bcp.id,bcp.parameter_name,bcp.value_type,bcp.optional_list,bcp.sort_number,bcp.required_flag,bci.label_name FROM basic_carmodel_parameter bcp ");
        sql.append(" LEFT JOIN basic_carmodel_label bci on bcp.carmodel_label_id=bci.id where 1=1 ");
        if(null != parameterName){
            sql.append(" and bcp.parameter_name LIKE CONCAT('%',:parameterName,'%') ");
            param.put("parameterName",parameterName);
        }
        // 参数类型
        if(null != valueType){
            sql.append(" and bcp.value_type=valueType ");
            param.put("valueType",valueType);
        }
        // 参数标签
        if(null != carmodelLabelId){
            sql.append(" and bcp.carmodel_label_id=carmodelLabelId ");
            param.put("carmodelLabelId",carmodelLabelId);
        }
        sql.append(" ORDER BY bcp.operate_time DESC");
        Page<CarmodelParameter> parameterPage = super.queryPageForBean(CarmodelParameter.class, sql.toString(), param, offset, limit);
        return parameterPage;
    }

    /**
     * 查询所有的配置参数
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 10:12:31
     */
    public List<CarmodelParameter> queryAllParameter(){
        Map<String ,Object> param = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ")
            .append(SQL_BUILD)
            .append(" FROM basic_carmodel_parameter ").append(" order by sort_number ");
        List<CarmodelParameter> carmodelParameters = super.queryListBean(CarmodelParameter.class, sql.toString(), param);
        return carmodelParameters;
    }

    /**
     * 根据id查询配置参数详情
     * @param id 配置参数id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    public CarmodelParameter getById(Long id){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("id",id);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append(SQL_BUILD)
           .append(" FROM basic_carmodel_parameter WHERE id = :id");
        CarmodelParameter carmodelParameter = super.findForBean(CarmodelParameter.class, sql.toString(), param);
        return carmodelParameter;
    }

    /**
     * 根据配置参数查询配置参数详情
     * @param carmodelParameter 配置参数
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    public List<CarmodelParameter> getParameter(CarmodelParameter carmodelParameter){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append(SQL_BUILD)
           .append(" FROM basic_carmodel_parameter WHERE parameter_name = :parameterName");
        if(null != carmodelParameter.getId()){
            sql.append(" and id != :id ");
        }
        List<CarmodelParameter> carmodelParameters = super.queryListBean(CarmodelParameter.class, sql.toString(),carmodelParameter);
        return carmodelParameters;
    }

    /**
     * 新增车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 15:33:04
     */
    public long createParameter(CarmodelParameter carmodelParameter){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_parameter ")
            .append("( parameter_name, value_type, optional_list, manual_add_flag, required_flag, sort_number, carmodel_label_id, create_time, creator, operate_time, operator, delete_flag) ")
            .append(" VALUES ( :parameterName, :valueType, :optionalList, :manualAddFlag, :requiredFlag, :sortNumber, :carmodelLabelId, :createTime, :creator, :operateTime, :operator, :deleteFlag)");
        long i = super.saveRid(sql.toString(), carmodelParameter);
        return i;
    }

    /**
     * 编辑车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 16:32:16
     */
    public int updateParameter(CarmodelParameter carmodelParameter){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_parameter SET parameter_name= :parameterName, ")
            .append(" value_type= :valueType, optional_list= :optionalList, manual_add_flag= :manualAddFlag, required_flag= :requiredFlag,")
            .append(" sort_number= :sortNumber, carmodel_label_id= :carmodelLabelId, operate_time= :operateTime, operator= :operator WHERE id = :id");
        int i = super.updateForBean(sql.toString(), carmodelParameter);
        return i;
    }
    /**
     * 根据id删除车型参数配置
     * @param id 车型参数配置id
     * @return int
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 13:57:59
     */
    public int deleteParameter(Long id){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("id",id);
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM basic_carmodel_parameter WHERE id = :id");
        int i = super.updateForMap(sql.toString(), param);
        return i;
    }

}
