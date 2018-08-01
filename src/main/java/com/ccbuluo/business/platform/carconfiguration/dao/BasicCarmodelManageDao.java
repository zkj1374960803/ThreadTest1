package com.ccbuluo.business.platform.carconfiguration.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelManage;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 车型管理dao
 * @author chaoshuai
 * @date 2018-05-08 10:48:58
 */
@Repository
public class BasicCarmodelManageDao extends BaseDao<CarmodelManage> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    private String SQL_BUILD = "id, carbrand_id, carseries_id,carmodel_number, carmodel_name, car_type, model_title, model_master_image, model_image, carmodel_status, create_time, creator, operate_time, operator, delete_flag";

    /**
     * 分页查询车型列表
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param status 状态
     * @param offset
     * @param limit
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 19:37:29
     */
    public Page<CarmodelManageDTO> queryPageForCarModelManage(Long carbrandId, Long carseriesId, Integer status, String carmodelName, int offset, int limit){
        Map<String ,Object> param = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bcmm.carbrand_id,bcmm.carseries_id,bcmm.id,bcmm.carmodel_name,bcmm.carmodel_number, bcmm.car_type ,bcmm.model_image, ")
            .append(" bcmm.model_master_image,bcmm.`carmodel_status`,bcmm.model_title,a.car_count,bcm.carbrand_name ")
            .append(" FROM basic_carmodel_manage bcmm  LEFT JOIN basic_carbrand_manage bcm on bcmm.carbrand_id=bcm.id ")
            .append(" LEFT JOIN (SELECT bcci.carmodel_id, count(*) AS car_count FROM basic_carcore_info bcci WHERE bcci.delete_flag = 0 GROUP BY bcci.carmodel_id) a  ")
            .append(" ON a.carmodel_id = bcmm.id WHERE bcmm.delete_flag = :deleteFlag ");

        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        if (null != carbrandId){
            sql.append(" AND bcmm.carbrand_id = :carbrandId ");
            param.put("carbrandId",carbrandId);
        }
        if (null != carseriesId){
            sql.append(" AND bcmm.carseries_id = :carseriesId ");
            param.put("carseriesId",carseriesId);
        }
        if (null != status){
            param.put("status",status);
            sql.append(" AND bcmm.carmodel_status = :status ");
        }
        if(null != carmodelName && !"".equals(carmodelName)){
            param.put("carmodelName",carmodelName);
            sql.append(" AND bcmm.carmodel_name LIKE CONCAT('%',:carmodelName,'%') ");
        }
        sql.append(" ORDER BY bcmm.operate_time DESC");
        Page<CarmodelManageDTO> carmodelManageDTOPage = super.queryPageForBean(CarmodelManageDTO.class, sql.toString(), param, offset, limit);
        return carmodelManageDTOPage;
    }

    /**
     * 查询所有的车型
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 11:20:13
     */
    public List<CarmodelManage> queryAllModel(){
        Map<String,Object> param = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ")
           .append(SQL_BUILD)
           .append(" FROM basic_carmodel_manage ORDER BY id ");
        List<CarmodelManage> carmodelManages = super.queryListBean(CarmodelManage.class, sql.toString(), param);
        return carmodelManages;
    }

    /**
     * 根据id查询车型基本信息
     * @param id 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 17:44:03
     */
    public CarmodelManage getById(Long id){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("id",id);
        StringBuilder sql = new StringBuilder();
        sql.append("select ")
            .append(SQL_BUILD)
            .append(" FROM basic_carmodel_manage")
            .append(" where id = :id");
        CarmodelManage carmodelManage = super.findForBean(CarmodelManage.class, sql.toString(), param);
        return carmodelManage;
    }

    /**
     * 根据车系id查询车型
     * @param id 车系id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-10 09:37:13
     */
    public List<CarmodelManage> getByCarSeriesId(Long id){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("id",id);
        StringBuilder sql = new StringBuilder();
        sql.append("select ")
            .append(SQL_BUILD)
            .append(" FROM basic_carmodel_manage")
            .append(" where carseries_id = :id");
        List<CarmodelManage> carmodelManages = super.queryListBean(CarmodelManage.class, sql.toString(), param);
        return carmodelManages;
    }


    /**
     * 根据id查询车型扩展信息
     * @param id 车型id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 17:44:03
     */
    public CarmodelManageDTO getCarmodelManageDtoById(Long id){
        Map<String ,Object> param = Maps.newHashMap();
        param.put("id",id);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bcmm.id, bcmm.carbrand_id,bcmm.carmodel_number, bcmm.carseries_id,bcmm.carmodel_name, bcmm.car_type, ")
           .append(" bcmm.model_title, bcmm.model_master_image, bcmm.model_image, bcmm.carmodel_status,bcbm.carbrand_logo ")
           .append(" FROM basic_carmodel_manage bcmm  ")
           .append(" LEFT JOIN basic_carbrand_manage bcbm ON bcmm.carbrand_id = bcbm.id WHERE bcmm.id = :id");
        CarmodelManageDTO carmodelManageDTO = super.findForBean(CarmodelManageDTO.class, sql.toString(), param);
        return carmodelManageDTO;
    }

    /**
     * 新增车型
     * @param carmodelManage 车型实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:43:22
     */
    public long create(CarmodelManage carmodelManage){
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO basic_carmodel_manage ")
            .append(" (carbrand_id, carseries_id, car_type, carmodel_name, model_title, model_master_image, model_image,carmodel_number, create_time, creator,operate_time,operator) ")
            .append(" VALUES ( :carbrandId, :carseriesId, :carType, :carmodelName, :modelTitle, :modelMasterImage, :modelImage, :carmodelNumber, :createTime, :creator, :operateTime, :operator) ");
        Long rid = super.saveRid(sql.toString(), carmodelManage);
        return rid;
    }

    /**
     * 编辑车型
     * @param carmodelManage 车型
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 11:08:24
     */
    public int edit(CarmodelManage carmodelManage){
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE basic_carmodel_manage SET carbrand_id= :carbrandId, carseries_id= :carseriesId,carmodel_name= :carmodelName,car_type= :carType, model_title= :modelTitle, ")
           .append(" model_master_image= :modelMasterImage, model_image= :modelImage, operate_time= :operateTime, operator= :operator WHERE id= :id ");
        int i = super.updateForBean(sql.toString(), carmodelManage);
        return i;
    }

    /**
     * 车型停用启用
     * @param carmodelManage 车型实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 17:19:31
     */
    public int stopOperationCarModel(CarmodelManage carmodelManage){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_manage SET carmodel_status = :carmodelStatus ,operate_time = :operateTime,operator = :operator where id = :id");
        int i = super.updateForBean(sql.toString(), carmodelManage);
        return i;
    }

    /**
     * 该车系下车型数量
     * @param id
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:44:43
     */
    public int countCarmodelManage(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carmodel_manage ")
            .append("   WHERE carseries_id = :carseriesId AND delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carseriesId", id);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 根据车型id获取 车型名称、品牌logo
     * @param id 车型id
     * @return
     * @exception
     * @author lizhao
     * @date 2018-05-08 16:44:43
     */
    public Map<String,Object> getCarmodelAndLogo(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bcmm.carmodel_name as carmodelName,bcbm.carbrand_logo as carbrandLogo FROM ")
            .append(" basic_carmodel_manage bcmm LEFT JOIN  basic_carbrand_manage bcbm ON bcmm.carbrand_id = bcbm.id ")
            .append(" WHERE bcmm.id = :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForMap(sql.toString(), params);
    }

    /**
     * 获取全部车型 下拉用
     * @return 结果集
     * @author Ryze
     * @date 2018-06-12 15:31:17
     */
    public List<Map<String, Object>> queryAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,carmodel_name as name FROM ")
            .append(" basic_carmodel_manage WHERE carmodel_status = :carmodelStatus AND delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carmodelStatus", Constants.YES);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryListMap(sql.toString(), params);
    }

    /**
     * 查询部分的车型
     * @param
     * @param carmodelId 车型id集合
     * @exception
     * @author wuyibo
     * @Date 2018-06-21 18:43:05
     */
    public List<CarmodelManage> queryPartModel(List<Long> carmodelId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ")
            .append(SQL_BUILD)
            .append(" FROM basic_carmodel_manage ");
        if (null != carmodelId && carmodelId.size() > 0) {
            sql.append(" WHERE id in (:carmodelId) ");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("carmodelId", carmodelId);
        return super.queryListBean(CarmodelManage.class, sql.toString(), params);
    }
    /**
     * 数据验证唯一性
     * @param carmodelManageDTO 车型基本信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author wuyibo
     * @date 2018-08-01 09:37:13
     */
    public int countEntity(CarmodelManageDTO carmodelManageDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carmodel_manage ")
                .append("   WHERE carmodel_name = :carmodelName AND delete_flag = :deleteFlag");
        if (null != carmodelManageDTO.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("carmodelName", carmodelManageDTO.getCarmodelName());
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("id", carmodelManageDTO.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }
    /**
     * 删除车型
     * @param id 车型id
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-01 09:37:13
     */
    public int deleteCarmodelManageById(Long id){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_manage SET delete_flag = :deleteFlag  WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }

}
