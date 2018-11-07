package com.ccbuluo.business.platform.storehouse.dao;

import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SearchStorehouseListDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

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
     * @param pageSize 每页数
     * @return 仓库列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    public Page<SearchStorehouseListDTO> queryList(String provinceName, String cityName, String areaName, Integer storeHouseStatus, String keyword, List<String> serviceCenterCode, Integer offset, Integer pageSize) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,storehouse_code,storehouse_name,storehouse_acreage,servicecenter_code,storehouse_address,storehouse_status FROM biz_service_storehouse")
            .append(" WHERE 1=1");
        if (storeHouseStatus != null) {
            params.put("storeHouseStatus", storeHouseStatus);
            sql.append(" AND storehouse_status = :storeHouseStatus");
        }
        if (StringUtils.isNotBlank(provinceName)) {
            params.put("provinceName", provinceName);
            sql.append(" AND province_name = :provinceName");
        }
        if (StringUtils.isNotBlank(cityName)) {
            params.put("cityName", cityName);
            sql.append(" AND city_name = :cityName");
        }
        if (StringUtils.isNotBlank(areaName)) {
            params.put("areaName", areaName);
            sql.append(" AND area_name = :areaName");
        }
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
            sql.append(" AND (storehouse_code LIKE CONCAT('%',:keyword,'%') OR storehouse_name LIKE CONCAT('%',:keyword,'%')");
            if(!serviceCenterCode.isEmpty()){
                params.put("serviceCenterCode", serviceCenterCode);
                sql.append("  OR servicecenter_code IN (:serviceCenterCode)");
            }
            sql.append(" )");
        }
        sql.append(" ORDER BY operate_time DESC");

        return queryPageForBean(SearchStorehouseListDTO.class, sql.toString(), params, offset, pageSize);
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
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        String sql = "SELECT COUNT(id) > 0 FROM biz_service_storehouse WHERE id <> :id AND storehouse_name = :storehouseName AND delete_flag = :deleteFlag";

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

    /**
     * 根据仓库code查询机构code
     * @param storeHouseCode 仓库code
     * @return 机构code
     * @author liuduo
     * @date 2018-08-07 16:08:52
     */
    public String getOrgCodeByStoreHouseCode(String storeHouseCode) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeHouseCode", storeHouseCode);

        String sql = "SELECT servicecenter_code FROM biz_service_storehouse WHERE storehouse_code = :storeHouseCode";

        return namedParameterJdbcTemplate.queryForObject(sql, param, String.class);
    }

    /**
     * 根据服务中心查询启用的仓库列表（下拉框）
     * @param serviceCenterCode 服务中心code
     * @return List<QueryStorehouseDTO> 仓库列表
     * @author zhangkangjian
     * @date 2018-08-07 15:15:11
     */
    public List<QueryStorehouseDTO> queryStorehouseByServiceCenterCode(String serviceCenterCode) {
        if(StringUtils.isBlank(serviceCenterCode)){
            return Collections.emptyList();
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("serviceCenterCode", serviceCenterCode);
        param.put("storehouseStatus", Constants.STATUS_FLAG_ONE);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT storehouse_code,storehouse_name ")
            .append(" FROM biz_service_storehouse ")
            .append("  WHERE servicecenter_code = :serviceCenterCode AND storehouse_status = :storehouseStatus ");
        return queryListBean(QueryStorehouseDTO.class, sql.toString(), param);
    }


    /**
     * 根据服务中心查询启用的仓库列表
     * @param serviceCenterCode 服务中心code
     * @return List<QueryStorehouseDTO> 仓库列表
     * @author zhangkangjian
     * @date 2018-08-07 15:15:11
     */
    public List<BizServiceStorehouse> queryStorehouseByServiceCenterCode(List<String> serviceCenterCode) {
        if(serviceCenterCode == null || serviceCenterCode.size() == 0){
            return Collections.emptyList();
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("serviceCenterCode", serviceCenterCode);
        param.put("storehouseStatus", Constants.STATUS_FLAG_ONE);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT storehouse_code,storehouse_name,servicecenter_code ")
            .append(" FROM biz_service_storehouse ")
            .append("  WHERE servicecenter_code in (:serviceCenterCode) AND storehouse_status = :storehouseStatus ");
        return queryListBean(BizServiceStorehouse.class, sql.toString(), param);
    }

    /**
     * 根据仓库code查询仓库信息
     * @param codes 仓库code
     * @return 仓库信息
     * @author liuduo
     * @date 2018-08-13 11:58:35
     */
    public List<QueryStorehouseDTO> queryByCode(List<String> codes) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("codes", codes);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT storehouse_code,storehouse_name FROM biz_service_storehouse ")
            .append(" WHERE storehouse_code IN(:codes)");

        return queryListBean(QueryStorehouseDTO.class, sql.toString(), param);
    }

    /**
     * 根据仓库code查询仓库信息
     * @param codes 仓库code
     * @return 仓库信息
     * @author liuduo
     * @date 2018-08-13 11:58:35
     */
    public QueryStorehouseDTO queryQueryStorehouseDTOByCode(String codes) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("codes", codes);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT storehouse_code,storehouse_name FROM biz_service_storehouse ")
            .append(" WHERE storehouse_code = :codes");
        return findForBean(QueryStorehouseDTO.class, sql.toString(), param);
    }
}
