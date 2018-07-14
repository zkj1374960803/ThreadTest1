package com.ccbuluo.business.platform.carconfiguration.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarseriesManage;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 车系管理dao
 * @author chaoshuai
 * @date 2018-05-08 10:44:30
 */
@Repository
public class BasicCarseriesManageDao extends BaseDao<CarseriesManage> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private UserHolder userHolder;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 车系名称是否存在
     * @param id 车系id
     * @param carseriesName 车系名称
     * @param deleteFlag 是否删除
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:08:02
     */
    public int countCarseriesRename(Long id, String carseriesName, int deleteFlag) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carseries_manage ")
            .append("   WHERE carseries_name = :carseriesName AND delete_flag = :deleteFlag");
        if (null != id) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("carseriesName", carseriesName);
        params.put("deleteFlag", deleteFlag);
        params.put("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车系新增
     * @param carseriesManage 车系
     * @return long
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:08:02
     */
    public long saveCarseriesManage(CarseriesManage carseriesManage) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carseries_manage ( ")
            .append("   carseries_name, carbrand_id, sort_number, carseries_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" ) ")
            .append(" VALUES ( ")
            .append("   :carseriesName, :carbrandId, :sortNumber, :carseriesNumber, ")
            .append("   :createTime, :creator, :operateTime, :operator, :deleteFlag ")
            .append(" ) ");
        return super.saveRid(sql.toString(), carseriesManage);
    }

    /**
     * 车系编辑
     * @param carseriesManage 车系
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:08:02
     */
    public int updateCarseriesManage(CarseriesManage carseriesManage) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carseries_manage SET ")
            .append("   carseries_name = :carseriesName, sort_number = :sortNumber, ")
            .append("   operate_time = :operateTime, operator = :operator ")
            .append(" WHERE id = :id");
        return super.updateForBean(sql.toString(), carseriesManage);
    }

    /**
     * 车系删除
     * @param id 车系id
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:08:02
     */
    public int deleteCarseriesManage(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carseries_manage SET ")
            .append("   delete_flag = :deleteFlag, operate_time = :operateTime, operator = :operator ")
            .append(" WHERE id = :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);
        params.put("operator", userHolder.getLoggedUserId());
        params.put("operateTime", new Date());
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 该品牌下车系数量
     * @param id 品牌id
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:08:02
     */
    public int countCarseriesManage(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carseries_manage ")
            .append("   WHERE carbrand_id = :carbrandId AND delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carbrandId", id);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车系详情
     * @param id 车系id
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 16:08:02
     */
    public CarseriesManage findCarseriesManageDetail(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   id, carseries_name, carbrand_id, sort_number, carseries_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" FROM basic_carseries_manage ")
            .append(" WHERE id = :id ");
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", id);
        return super.findForBean(CarseriesManage.class, sql.toString(), param);
    }

    /**
     * 分页查询品牌下车系列表
     * @param carbrandName 品牌名称
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @param offset 偏移量
     * @param limit 步长
     * @return com.ccbuluo.db.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    public Page<Map<String, Object>> queryCarseriesManagePage(String carbrandName, Long carbrandId, String carseriesName, int offset, int limit) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   id, carseries_name AS carseriesName, :carbrandName AS carbrandName, carseries_number AS carseriesNumber, sort_number AS sortNumber ")
            .append(" FROM basic_carseries_manage ")
            .append(" WHERE carbrand_id = :carbrandId AND delete_flag = :deleteFlag ");
        if (StringUtils.isNotBlank(carseriesName)) {
            sql.append(" AND carseries_name LIKE CONCAT(:carseriesName, '%') ");
        }
        sql.append(" ORDER BY id DESC");

        Map<String, Object> params = Maps.newHashMap();
        params.put("carbrandId", carbrandId);
        params.put("carseriesName", carseriesName);
        params.put("carbrandName", carbrandName);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryPageForMap(sql.toString(), params, offset, limit);
    }

    /**
     * 查询品牌下车系列表
     * @param carbrandId 品牌id
     * @param carseriesName 车系名称
     * @return java.util.List<com.ccbuluo.business.entity.CarseriesManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    public List<CarseriesManage> queryCarseriesManageList(Long carbrandId, String carseriesName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   id, carseries_name, carbrand_id, sort_number, carseries_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" FROM basic_carseries_manage ")
            .append(" WHERE carbrand_id = :carbrandId AND delete_flag = :deleteFlag ");
        if (StringUtils.isNotBlank(carseriesName)) {
            sql.append(" AND carseries_name LIKE CONCAT(:carseriesName, '%') ");
        }
        sql.append(" ORDER BY id DESC");

        Map<String, Object> params = Maps.newHashMap();
        params.put("carbrandId", carbrandId);
        params.put("carseriesName", carseriesName);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryListBean(CarseriesManage.class, sql.toString(), params);
    }

    /**
     * 查询所有车系 下拉框
     * @param
     * @return java.util.List<com.ccbuluo.business.entity.CarseriesManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 18:02:45
     */
    public List<CarseriesManage> queryAllCarseriesManageList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   id, carseries_name, carbrand_id, sort_number, carseries_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" FROM basic_carseries_manage ")
            .append(" WHERE delete_flag = :deleteFlag ")
            .append(" ORDER BY carbrand_id ASC, id DESC ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryListBean(CarseriesManage.class, sql.toString(), params);
    }
}
