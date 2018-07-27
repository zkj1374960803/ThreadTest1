package com.ccbuluo.business.platform.storehouse.dao;

import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.storehouse.dto.SearchStorehouseListDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-07-03 09:14:06
 * @version V1.0.0
 */
@Repository
public class BizServiceStorehouseDao extends BaseDao<BizServiceStorehouse> {
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
     * @date 2018-07-03 09:14:06
     */
    public int saveEntity(BizServiceStorehouse entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_storehouse ( storehouse_code,storehouse_name,")
            .append("storehouse_acreage,servicecenter_code,storehouse_status,longitude,latitude,storehouse_address,")
            .append("province_code,province_name,city_code,city_name,area_code,area_name,")
            .append("creator,create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :storehouseCode, :storehouseName, :storehouseAcreage,")
            .append(" :servicecenterCode, :storehouseStatus,:longitude,:latitude,:storehouseAddress, :provinceCode, :provinceName,")
            .append(" :cityCode, :cityName, :areaCode, :areaName, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int update(BizServiceStorehouse entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_storehouse SET ")
            .append("storehouse_name = :storehouseName,")
            .append("storehouse_acreage = :storehouseAcreage,")
            .append("servicecenter_code = :servicecenterCode,storehouse_address = :storehouseAddress,")
            .append("storehouse_status = :storehouseStatus,province_code = :provinceCode,")
            .append("province_name = :provinceName,city_code = :cityCode,longitude = :longitude,latitude = :latitude,")
            .append("city_name = :cityName,area_code = :areaCode,area_name = :areaName,")
            .append("operator = :operator,operate_time = :operateTime WHERE id= :id");

        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public BizServiceStorehouse getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,storehouse_code,storehouse_name,storehouse_acreage,longitude,latitude,storehouse_address,")
            .append(" servicecenter_code,storehouse_status,province_code,province_name,")
            .append(" city_code,city_name,area_code,area_name")
            .append(" FROM biz_service_storehouse WHERE id= :id AND delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.findForBean(BizServiceStorehouse.class, sql.toString(), params);
    }


    /**
     * 启停仓库
     * @param id 仓库id
     * @param storeHouseStatus 仓库状态
     * @return  操作是否成功
     * @author liuduo
     * @date 2018-07-03 10:37:55
     */
    public int editStoreHouseStatus(Long id, Integer storeHouseStatus, String operator) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        params.put("storeHouseStatus", storeHouseStatus);
        params.put("operator", operator);
        params.put("operateTime", System.currentTimeMillis());

        String sql = "UPDATE biz_service_storehouse SET storehouse_status = :storeHouseStatus,operator = :operator,operate_time = FROM_UNIXTIME( :operateTime ) WHERE id = :id";

        return updateForMap(sql, params);
    }

    /**
     * 查询仓库列表
     * @param provinceName 省
     * @param cityName 市
     * @param areaName 区
     * @param storeHouseStatus 状态
     * @param keyword 关键字
     * @param serviceCenterCode 服务中心的code
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 仓库列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    public Page<SearchStorehouseListDTO> queryList(String provinceName, String cityName, String areaName, Integer storeHouseStatus, String keyword, List<String> serviceCenterCode, Integer offset, Integer pagesize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("provinceName", provinceName);
        params.put("cityName", cityName);
        params.put("areaName", areaName);
        params.put("storeHouseStatus", storeHouseStatus);
        params.put("keyword", keyword);
        params.put("serviceCenterCode", serviceCenterCode);


        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,storehouse_code,storehouse_name,storehouse_acreage,servicecenter_code,storehouse_status FROM biz_service_storehouse")
            .append(" WHERE 1=1");
        if (storeHouseStatus != null) {
            sql.append(" AND storehouse_status = :storeHouseStatus");
        }
        if (StringUtils.isNotBlank(provinceName)) {
            sql.append(" AND province_name = :provinceName");
        }
        if (StringUtils.isNotBlank(cityName)) {
            sql.append(" AND city_name = :cityName");
        }
        if (StringUtils.isNotBlank(areaName)) {
            sql.append(" AND area_name = :areaName");
        }
        if (StringUtils.isNotBlank(keyword)) {
            sql.append(" AND (storehouse_code LIKE CONCAT('%',:keyword,'%') OR storehouse_name LIKE CONCAT('%',:keyword,'%')");
            if(!serviceCenterCode.isEmpty()){
                sql.append("  OR servicecenter_code IN (:serviceCenterCode)");
            }
            sql.append(" )");
        }
        sql.append(" ORDER BY operate_time DESC");

        return queryPageForBean(SearchStorehouseListDTO.class, sql.toString(), params, offset, pagesize);
    }

    /**
     * 仓库名称验重（新增用）
     * @param storehouseName 仓库名称
     * @return 名字是否存在
     * @author liuduo
     * @date 2018-07-04 20:10:47
     */
    public Boolean storeHouseNameCheck(String storehouseName) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storehouseName", storehouseName);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_storehouse WHERE storehouse_name = :storehouseName";

        return findForObject(sql, param, Boolean.class);
    }

    /**
     * 仓库名称验重（编辑用）
     * @param id 仓库id
     * @param storehouseName 仓库名字
     * @return 名字是否存在
     * @author liuduo
     * @date 2018-07-04 20:20:15
     */
    public Boolean checkName(Long id, String storehouseName) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storehouseName", storehouseName);
        param.put("id", id);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_storehouse WHERE id <> :id AND storehouse_name = :storehouseName";

        return findForObject(sql, param, Boolean.class);
    }

    /**
     * 根据服务中心code查询仓库
     * @param serviceCenterCode 服务中心code
     * @return 服务中心关联的仓库
     * @author liuduo
     * @date 2018-07-05 10:27:40
     */
    public List<BizServiceStorehouse> getStorehousrByCode(String serviceCenterCode) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("serviceCenterCode", serviceCenterCode);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,storehouse_code,storehouse_status,storehouse_name,storehouse_acreage,longitude,")
            .append(" latitude,storehouse_address,province_name,city_name,area_name FROM biz_service_storehouse WHERE servicecenter_code = :serviceCenterCode");

        return queryListBean(BizServiceStorehouse.class, sql.toString(), param);
    }
}
