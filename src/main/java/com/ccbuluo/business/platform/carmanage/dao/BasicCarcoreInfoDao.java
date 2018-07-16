package com.ccbuluo.business.platform.carmanage.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carmanage.dto.CarDTO;
import com.ccbuluo.business.platform.carmanage.dto.SearchCarcoreInfoDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆基本信息dao
 * @author chaoshuai
 * @date 2018-05-08 10:45:01
 */
@Repository
public class BasicCarcoreInfoDao extends BaseDao<CarcoreInfo> {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }


    /**
     * 新增车辆基本信息
     * @param entity 车辆基本信息
     * @return long
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public long saveCarcoreInfo(CarcoreInfo entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO carcore_info ( carcore_code,vin_number,engine_number,")
                .append("beidou_number,carbrand_id,carseries_id,carmodel_id,produce_time,")
                .append("remark,creator,create_time,operator,operate_time,delete_flag")
                .append(" ) VALUES (  :carcoreCode, :vinNumber, :engineNumber, :beidouNumber,")
                .append(" :carbrandId, :carseriesId, :carmodelId, :produceTime, :remark,")
                .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author weijb
     * @date 2018-07-13 17:29:31
     */
    public int updateCarcoreInfo(CarcoreInfo entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE carcore_info SET carcore_code = :carcoreCode,")
                .append("vin_number = :vinNumber,engine_number = :engineNumber,")
                .append("beidou_number = :beidouNumber,carbrand_id = :carbrandId,")
                .append("carseries_id = :carseriesId,carmodel_id = :carmodelId,")
                .append("produce_time = :produceTime,remark = :remark,creator = :creator,")
                .append("create_time = :createTime,operator = :operator,")
                .append("operate_time = :operateTime,delete_flag = :deleteFlag WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 车辆基本信息 车牌号 是否存在
     * @param carcoreInfo 车辆基本信息
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int countPlateNumber(CarcoreInfo carcoreInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carcore_info ")
            .append("   WHERE plate_number = :plateNumber AND delete_flag = :deleteFlag");
        if (null != carcoreInfo.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", carcoreInfo.getDeleteFlag());
        params.put("id", carcoreInfo.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车辆基本信息 车架号(VIN) 是否存在
     * @param carcoreInfo 车辆基本信息
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int countVinNumber(CarcoreInfo carcoreInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carcore_info ")
            .append("   WHERE vin_number = :vinNumber AND delete_flag = :deleteFlag");
        if (null != carcoreInfo.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("vinNumber", carcoreInfo.getVinNumber());
        params.put("deleteFlag", carcoreInfo.getDeleteFlag());
        params.put("id", carcoreInfo.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车辆基本信息 发动机号 是否存在
     * @param carcoreInfo 车辆基本信息
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int countEngineNumber(CarcoreInfo carcoreInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carcore_info ")
            .append("   WHERE engine_number = :engineNumber AND delete_flag = :deleteFlag");
        if (null != carcoreInfo.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("engineNumber", carcoreInfo.getEngineNumber());
        params.put("deleteFlag", carcoreInfo.getDeleteFlag());
        params.put("id", carcoreInfo.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车辆基本信息 北斗设备编号 是否存在
     * @param carcoreInfo 车辆基本信息
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int countBeidouNumber(CarcoreInfo carcoreInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carcore_info ")
            .append("   WHERE beidou_number = :beidouNumber AND delete_flag = :deleteFlag");
        if (null != carcoreInfo.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("beidouNumber", carcoreInfo.getBeidouNumber());
        params.put("deleteFlag", carcoreInfo.getDeleteFlag());
        params.put("id", carcoreInfo.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 查询编号 最大值
     * @param fieldNumber 字段名
     * @param tableName 表名
     * @return
     * @exception
     * @author wuyibo
     * @date 2018-05-14 15:59:41
     */
    public String findNumberMax(String fieldNumber, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MAX(" + fieldNumber + ") FROM " + tableName);
        Map<String, Object> params = Maps.newHashMap();
        params.put("fieldNumber", fieldNumber);
        params.put("tableName", tableName);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, String.class);
    }

    /**
     * 获取详情
     * @param id  id
     * @author weijb
     * @date 2018-07-13 17:29:31
     */
    public CarcoreInfo queryCarDetailByCarId(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,carcore_code,vin_number,engine_number,beidou_number,")
                .append("carbrand_id,carseries_id,carmodel_id,produce_time,remark,creator,")
                .append("create_time,operator,operate_time,delete_flag FROM carcore_info")
                .append(" WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(CarcoreInfo.class, sql.toString(), params);
    }

    /**
     * 根据车辆id删除车辆
     * @param carId 车辆id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    public int deleteCarcoreInfoByCarId(long carId) {
        String sql = "UPDATE carcore_info SET delete_flag = :deleteFlag WHERE id= :carId ";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("carId", carId);
        map.put("deleteFlag", com.ccbuluo.merchandiseintf.carparts.Constants.Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql, map);
    }

    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param Keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    public Page<SearchCarcoreInfoDTO> queryCarcoreInfoList(Long carbrandId, Long carseriesId, String Keyword, Integer offset, Integer pageSize){
//        Map<String, Object> params = Maps.newHashMap();
//        params.put("carbrandId", carbrandId);
//        params.put("carseriesId", carseriesId);
//        params.put("deleteFlag", com.ccbuluo.merchandiseintf.carparts.Constants.Constants.DELETE_FLAG_NORMAL);
//
//
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT id,carparts_code,carparts_name,category_code_path,fit_carmodel")
//                .append(" FROM basic_carparts_product WHERE 1=1 ");
//        //分类code
//        if (null != categoryCode) {
//            sql.append(" AND FIND_IN_SET(:categoryCode,category_code_path) ");
//        }
//        //零配件名称
//        if (com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils.isNotBlank(carpartsName)) {
//            sql.append(" AND carparts_name like CONCAT('%', :carpartsName, '%') ");
//        }
//        sql.append(" AND delete_flag = :deleteFlag  ORDER BY operate_time DESC");
//
//        return super.queryPageForBean(BasicCarpartsProductDTO.class, sql.toString(), params, offset, pageSize);
        return null;
    }

}
