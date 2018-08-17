package com.ccbuluo.business.platform.carconfiguration.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarbrandManage;
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
 * 品牌管理dao
 * @author chaoshuai
 * @date 2018-05-08 10:43:32
 */
@Repository
public class BasicCarbrandManageDao extends BaseDao<CarbrandManage> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private UserHolder userHolder;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 品牌名称是否存在
     * @param id 品牌id
     * @param carbrandName 品牌名称
     * @param deleteFlag 是否删除
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    public int countCarbrandRename(Long id, String carbrandName, int deleteFlag) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carbrand_manage ")
            .append("   WHERE carbrand_name = :carbrandName AND delete_flag = :deleteFlag");
        if (null != id) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("carbrandName", carbrandName);
        params.put("deleteFlag", deleteFlag);
        params.put("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 品牌新增
     * @param carbrandManage 品牌
     * @return long
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    public long saveCarbrandManage(CarbrandManage carbrandManage) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carbrand_manage ( ")
            .append("   carbrand_name, initial, carbrand_logo, sort_number, carbrand_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" ) ")
            .append(" VALUES ( ")
            .append("   :carbrandName, getFirstHanZiCode(:carbrandName), :carbrandLogo, :sortNumber, :carbrandNumber, ")
            .append("   :createTime, :creator, :operateTime, :operator, :deleteFlag ")
            .append(" ) ");
        return super.saveRid(sql.toString(), carbrandManage);
    }

    /**
     * 品牌编辑
     * @param carbrandManage 品牌
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 14:12:58
     */
    public int updateCarbrandManage(CarbrandManage carbrandManage) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carbrand_manage SET ")
            .append("   carbrand_name = :carbrandName, initial = getFirstHanZiCode(:carbrandName), carbrand_logo = :carbrandLogo, sort_number = :sortNumber, ")
            .append("   operate_time = :operateTime, operator = :operator ")
            .append(" WHERE id = :id");
        return super.updateForBean(sql.toString(), carbrandManage);
    }

    /**
     * 品牌删除
     * @param id 品牌id
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:41:59
     */
    public int deleteCarbrandManage(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carbrand_manage SET ")
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
     * 品牌详情
     * @param id 品牌id
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-08 15:41:59
     */
    public CarbrandManage findCarbrandManageDetail(Long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   id, carbrand_name, initial, carbrand_logo, sort_number, carbrand_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" FROM basic_carbrand_manage ")
            .append(" WHERE id = :id ");
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", id);
        return super.findForBean(CarbrandManage.class, sql.toString(), param);
    }

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
    public Page<Map<String, Object>> queryCarbrandManagePage(String carbrandName, String initial, int offset, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   cbm.id AS id, cbm.carbrand_number AS carbrandNumber, cbm.carbrand_name AS carbrandName, cbm.initial AS initial, ")
            .append("   cbm.carbrand_logo AS carbrandLogo, cbm.sort_number AS sortNumber, IFNULL(pp.carseriesManageCount, 0) AS carseriesManageCount ")
            .append(" FROM basic_carbrand_manage cbm ")
            .append(" LEFT JOIN (SELECT csm.carbrand_id, COUNT(*) AS carseriesManageCount FROM  basic_carseries_manage csm WHERE csm.delete_flag = :deleteFlag GROUP BY csm.carbrand_id ) pp ON pp.carbrand_id = cbm.id ")
            .append(" WHERE cbm.delete_flag = :deleteFlag ");
        if (StringUtils.isNotBlank(initial)) {
            sql.append(" AND cbm.initial = :initial ");
        }
        if (StringUtils.isNotBlank(carbrandName)) {
            sql.append(" AND cbm.carbrand_name LIKE CONCAT('%',:carbrandName, '%') ");
        }
        sql.append(" ORDER BY cbm.operate_time DESC ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carbrandName", carbrandName);
        params.put("initial", initial);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryPageForMap(sql.toString(), params, offset, limit);
    }

    /**
     * 首字母索引列表
     * @param
     * @return java.util.List<java.lang.String>
     * @exception
     * @author wuyibo
     * @date 2018-05-08 19:47:42
     */
    public List<String> queryInitialList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.initial FROM basic_carbrand_manage a ")
            .append(" WHERE a.delete_flag = :deleteFlag GROUP BY a.initial ORDER BY a.initial ASC");
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.querySingColum(String.class, sql.toString(), params);
    }

    /**
     * 查询品牌列表
     * @param carbrandName 品牌首字母
     * @param initial 品牌名称
     * @return java.util.List<com.ccbuluo.business.entity.CarbrandManage>
     * @exception
     * @author wuyibo
     * @date 2018-05-09 10:50:30
     */
    public List<CarbrandManage> queryCarbrandManageList(String carbrandName, String initial) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("   id, carbrand_name, initial, carbrand_logo, sort_number, carbrand_number, ")
            .append("   create_time, creator, operate_time, operator, delete_flag ")
            .append(" FROM basic_carbrand_manage ")
            .append(" WHERE delete_flag = :deleteFlag ");

        if (StringUtils.isNotBlank(initial)) {
            sql.append(" AND initial = :initial ");
        }
        if (StringUtils.isNotBlank(carbrandName)) {
            sql.append(" AND carbrand_name LIKE CONCAT(:carbrandName, '%') ");
        }
        sql.append(" ORDER BY initial ASC, sort_number ASC ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carbrandName", carbrandName);
        params.put("initial", initial);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryListBean(CarbrandManage.class, sql.toString(), params);
    }

}
