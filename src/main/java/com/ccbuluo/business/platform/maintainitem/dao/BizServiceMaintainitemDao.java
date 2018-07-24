package com.ccbuluo.business.platform.maintainitem.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 维修服务项 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
 * @version V1.0.0
 */
@Repository
public class BizServiceMaintainitemDao extends BaseDao<BizServiceMaintainitem> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 维修服务项实体
     * @param entity 维修服务项实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int saveEntity(BizServiceMaintainitem entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_maintainitem ( maintainitem_code,")
            .append("maintainitem_name,unit_price,remark,creator,create_time,operator,")
            .append("operate_time,delete_flag ) VALUES (  :maintainitemCode,")
            .append(" :maintainitemName, :unitPrice, :remark, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 维修服务项实体
     * @param entity 维修服务项实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int update(BizServiceMaintainitem entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_maintainitem SET ")
            .append("maintainitem_name = :maintainitemName,unit_price = :unitPrice,")
            .append("operator = :operator,operate_time = :operateTime WHERE id= :id");

        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取维修服务项详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public DetailBizServiceMaintainitemDTO getById(long id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,maintainitem_code,maintainitem_name,unit_price")
            .append(" FROM biz_service_maintainitem WHERE id= :id");

        return super.findForBean(DetailBizServiceMaintainitemDTO.class, sql.toString(), params);
    }

    /**
     * 删除维修服务项
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_service_maintainitem WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 服务名字检验（新增用）
     * @param maintainitemName 服务名字
     * @return 是否重复
     * @author liuduo
     * @date 2018-07-18 10:32:12
     */
    public Boolean checkName(String maintainitemName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maintainitemName", maintainitemName);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_maintainitem WHERE maintainitem_name = :maintainitemName";

        return findForObject(sql, params, Boolean.class);
    }


    /**
     * 检查服务是否重复（编辑用）
     * @param saveBizServiceMaintainitemDTO 工时实体
     * @return 名字的是否重复
     * @author liuduo
     * @date 2018-07-18 10:29:20
     */
    public Boolean editCheckName(SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maintainitemName", saveBizServiceMaintainitemDTO.getMaintainitemName());
        params.put("id", saveBizServiceMaintainitemDTO.getId());

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_maintainitem WHERE id <> :id AND maintainitem_name = :maintainitemName";

        return findForObject(sql, params, Boolean.class);
    }

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 工时列表
     * @author liuduo
     * @date 2018-07-17 20:10:35
     */
    public Page<DetailBizServiceMaintainitemDTO> queryList(String keyword, Integer offset, Integer pagesize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bsmm.id,bsmm.maintainitem_code,bsmm.maintainitem_name,bsmm.unit_price, ")
            .append("  (SELECT COUNT(bsm.id) FROM biz_service_multipleprice AS bsm ")
            .append("  WHERE bsm.maintainitem_code = bsmm.maintainitem_code) AS multipleNum")
            .append("  FROM biz_service_maintainitem AS bsmm WHERE 1 = 1 ");
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
            sql.append(" AND bsmm.maintainitem_name LIKE CONCAT('%',:keyword,'%')");
        }
        sql.append("  AND bsmm.delete_flag = :deleteFlag ORDER BY bsmm.operate_time DESC");

        return queryPageForBean(DetailBizServiceMaintainitemDTO.class, sql.toString(), params, offset, pagesize);
    }
}
