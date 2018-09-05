package com.ccbuluo.business.platform.carmanage.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.entity.CarcoreInfo;
import com.ccbuluo.business.platform.carmanage.dto.*;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.ArrayList;
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
        sql.append("INSERT INTO basic_carcore_info ( car_number,vin_number,engine_number,")
                .append("beidou_number,carbrand_id,carseries_id,carmodel_id,produce_time,")
                .append("remark,car_status, creator,create_time,operator,operate_time,delete_flag,store_assigned")
                .append(" ) VALUES (  :carNumber, :vinNumber, :engineNumber, :beidouNumber,")
                .append(" :carbrandId, :carseriesId, :carmodelId, :produceTime, :remark, :carStatus,")
                .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :storeAssigned )");
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
        sql.append("UPDATE basic_carcore_info ")
                .append("SET vin_number = :vinNumber,engine_number = :engineNumber,")
                .append("beidou_number = :beidouNumber,carbrand_id = :carbrandId,")
                .append("carseries_id = :carseriesId,carmodel_id = :carmodelId,")
                .append("produce_time = :produceTime,remark = :remark,operator = :operator,")
                .append("operate_time = :operateTime WHERE id= :id");
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
        sql.append("SELECT id,car_number,vin_number,engine_number,beidou_number,")
                .append("carbrand_id,carseries_id,carmodel_id,produce_time,remark,car_status, creator,")
                .append("create_time,operator,operate_time,delete_flag,store_name,cusmanager_name,store_assigned FROM basic_carcore_info")
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
        String sql = "UPDATE basic_carcore_info SET delete_flag = :deleteFlag WHERE id= :carId ";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("carId", carId);
        map.put("deleteFlag", com.ccbuluo.merchandiseintf.carparts.Constants.Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql, map);
    }

    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param
     * @param Keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    public Page<SearchCarcoreInfoDTO> queryCarcoreInfoList(Long carbrandId, Long carseriesId, Integer storeAssigned,  String custmanagerUuid, String Keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bci.id,bci.car_number,bci.store_assigned,bci.vin_number,bci.car_status,bci.store_name,")
                .append("bcm.carbrand_name,bci.carseries_id,bci.cusmanager_name,bcmmm.carmodel_name")
                .append(" FROM basic_carcore_info bci LEFT JOIN basic_carbrand_manage bcm on bci.carbrand_id=bcm.id ")
                .append(" LEFT JOIN basic_carmodel_manage bcmmm ON bci.carmodel_id=bcmmm.id ")
                .append(" WHERE bci.delete_flag = :deleteFlag ");
        // 客户经理uuid
        if (StringUtils.isNotBlank(custmanagerUuid)) {
            param.put("custmanagerUuid", custmanagerUuid);
            param.put("carStatus", Constants.YES);
            sql.append(" AND bci.cusmanager_uuid = :custmanagerUuid AND bci.car_status = :carStatus");
        }
        // 品牌
        if (null != carbrandId) {
            param.put("carbrandId", carbrandId);
            sql.append(" AND bci.carbrand_id = :carbrandId ");
        }
        // 车系
        if (null != carseriesId) {
            param.put("carseriesId", carseriesId);
            sql.append(" AND bci.carseries_id = :carseriesId ");
        }
        // 车型
        if (null != storeAssigned) {
            param.put("storeAssigned", storeAssigned);
            sql.append(" AND bci.store_assigned = :storeAssigned ");
        }
        // 车架号
        if (StringUtils.isNotBlank(Keyword)) {
            param.put("Keyword", Keyword);
            sql.append(" AND (bci.vin_number LIKE CONCAT('%',:Keyword,'%') OR bci.car_number LIKE CONCAT('%',:Keyword,'%'))");
        }
        sql.append("  ORDER BY bci.operate_time DESC");
        Page<SearchCarcoreInfoDTO> DTOS = super.queryPageForBean(SearchCarcoreInfoDTO.class, sql.toString(), param,offset,pageSize);
        return DTOS;
    }

    /**
     * 查询未分配的车辆列表
     * @param vinNumber 车辆vin码
     * @author weijb
     * @date 2018-08-01 15:59:51
     */
    public List<ListCarcoreInfoDTO> queryuUndistributedList(String vinNumber){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bci.car_number,bci.vin_number,bcmmm.carbrand_name,bcm.carseries_name,bci.carmodel_id,")
            .append(" bci.store_name FROM basic_carcore_info AS bci")
            .append(" LEFT JOIN basic_carbrand_manage AS bcmmm ON bcmmm.id = bci.carbrand_id")
            .append(" LEFT JOIN basic_carseries_manage AS bcm ON bcm.id = bci.carseries_id")
            .append(" WHERE bci.delete_flag = :deleteFlag AND bci.car_status = :carStatus ")
            .append(" AND bci.store_assigned = :storeAssigned ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carStatus", Constants.NO);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("storeAssigned", Constants.YES);
        if (StringUtils.isNotBlank(vinNumber)) {
            params.put("vinNumber", vinNumber);
            sql.append(" AND bci.vin_number LIKE CONCAT('%',:vinNumber,'%')");
        }
        return super.queryListBean(ListCarcoreInfoDTO.class, sql.toString(), params);
    }
    /**
     * 根据车辆code更新车辆状态
     * @param list
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    public int updateStatusByCode(List<UpdateCarcoreInfoDTO> list){
        String sql = "update basic_carcore_info set cusmanager_uuid=:cusmanagerUuid, cusmanager_name=:cusmanagerName, car_status=:carStatus  where car_number=:carNumber";
        return batchUpdateForListBean(sql, list);
    }

    /**
     * 根据车架号查询车辆信息
     * @param vinNumber 车辆vin
     * @exception
     * @author weijb
     * @date 2018-06-08 13:55:14
     */
    public VinCarcoreInfoDTO getCarInfoByVin(String vinNumber){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT car_number,vin_number,engine_number,beidou_number ")
                .append(" FROM basic_carcore_info WHERE vin_number= :vinNumber");
        Map<String, Object> params = Maps.newHashMap();
        params.put("vinNumber", vinNumber);
        return super.findForBean(VinCarcoreInfoDTO.class, sql.toString(), params);
    }

    /**
     * 查询未分配的车辆列表（车型）
     * @param carModelIds 车型id
     * @author weijb
     * @date 2018-08-01 15:59:51
     */
    public List<ListCarcoreInfoDTO> queryCarMobelNameByIds(List<Long> carModelIds) {
        if(carModelIds.size() == 0){
            return new ArrayList<ListCarcoreInfoDTO>();
        }
        String sql = "SELECT id as carmodelId,carmodel_name as carmodelName FROM basic_carmodel_manage WHERE id IN (:carModelIds)";
        Map<String, Object> params = Maps.newHashMap();
        params.put("carModelIds", carModelIds);
        return queryListBean(ListCarcoreInfoDTO.class, sql, params);
    }

    /**
     * 解除车辆与客户经理的关联关系
     * @param carNumber 车辆编号
     * @return 操作是否成功
     * @author liuduo
     * @date 2018-08-01 14:23:04
     */
    public int release(String carNumber) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("carNumber", carNumber);
        params.put("cusmanagerUuid", null);
        params.put("carStatus", Constants.NO);
        String sql = "UPDATE basic_carcore_info SET cusmanager_uuid = :cusmanagerUuid,car_status = :carStatus WHERE car_number = :carNumber";

        return updateForMap(sql, params);
    }
    /**
     * 根据车辆vin更新车辆的门店信息
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-08-01 15:55:14
     */
    public int batchUpdateCarcoreInfoByVin(List<CarcoreInfoByVinDTO> list){
        String sql = "update basic_carcore_info set store_code=:storeCode, store_name=:storeName, plate_number=:plateNumber, store_assigned=1  where vin_number=:vinNumber";
        return batchUpdateForListBean(sql, list);
    }

    /**
     * 根据客户经理uuids查询名下的车辆数
     * @param cusmanagerUuids 客户经理uuids
     * @return 客户经理名下的车辆数
     * @author liuduo
     * @date 2018-08-02 10:09:30
     */
    public List<CusmanagerCarCountDTO> queryCarNumByCusmanagerUuid(List<String> cusmanagerUuids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("cusmanagerUuids", cusmanagerUuids);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT cusmanager_uuid AS cusmanagerUuid,COUNT(id) AS carNum FROM basic_carcore_info WHERE")
            .append(" cusmanager_uuid IN (:cusmanagerUuids) GROUP BY cusmanager_uuid");

        return queryListBean(CusmanagerCarCountDTO.class, sql.toString(), params);
    }
    /**
     * 根据车型id查询此车型是否被车辆引用过（次数）
     * @param carmodelId
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-08-03 12:41:36
     */
    public int findCarmodelParameterById(Long carmodelId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carcore_info ")
                .append("   WHERE carmodel_id = :carmodelId and delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("carmodelId", carmodelId);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 根据车辆编号把客户经理和门店信息置空
     * @param carNumber
     * @return
     */
    public int updateCarcoreInfoManager(String carNumber) {
        String sql = "UPDATE basic_carcore_info SET cusmanager_name = ''  WHERE car_number= :carNumber";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("carNumber", carNumber);
        return super.updateForMap(sql, map);
    }

    /**
     * 根据车牌号查询客户经理
     * @param carNo 车牌号
     * @return 客户经理uuid
     * @author liuduo
     * @date 2018-09-04 11:25:42
     */
    public String getUuidByPlateNum(String carNo) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("carNo", carNo);

        String sql = "SELECT cusmanager_uuid FROM basic_carcore_info WHERE plate_number = :carNo";

        return findForObject(sql, map, String.class);
    }

    /**
     * 根据车牌号查询车辆信息
     * @param carNo 车牌号
     * @return 车辆信息
     * @author liuduo
     * @date 2018-09-04 16:18:43
     */
    public CarcoreInfoDTO getCarByCarNo(String carNo) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("carNo", carNo);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bci.vin_number,bci.engine_number,bci.beidou_number,bci.produce_time,bci.store_name,")
            .append(" bci.carmodel_id,bcm.carbrand_name,bcmm.carseries_name FROM basic_carcore_info AS bci ")
            .append("　LEFT JOIN basic_carbrand_manage AS bcm ON bcm.id = bci.carbrand_id")
            .append(" LEFT JOIN basic_carseries_manage AS bcmm ON bcmm.id = bci.carseries_id")
            .append("  WHERE plate_number = :carNo");

        return findForBean(CarcoreInfoDTO.class, sql.toString(), map);
    }

    /**
     * 查询车牌号
     * @param orgCode 机构编号
     * @param statusFlagZero 状态（0为门店，1为客户经理）
     * @return 车牌号
     * @author liuduo
     * @date 2018-09-04 18:45:42
     */
    public List<String> queryCarNoList(String orgCode, int statusFlagZero) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("orgCode", orgCode);
        map.put("statusFlagZero", statusFlagZero);
        map.put("storeAssigned", Constants.YES);
        map.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT plate_number FROM basic_carcore_info WHERE 1 = 1");
        // 是门店
        if (statusFlagZero == Constants.STATUS_FLAG_ZERO) {
            sql.append(" AND store_code = orgCode AND store_assigned = :storeAssigned");
        } else {
            sql.append(" AND cusmanager_uuid = :orgCode");
        }
        sql.append(" AND delete_flag = :deleteFlag");

        return querySingColum(String.class, sql.toString(), map);
    }
}
