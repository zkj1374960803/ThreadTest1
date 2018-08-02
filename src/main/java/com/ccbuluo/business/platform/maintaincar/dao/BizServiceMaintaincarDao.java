package com.ccbuluo.business.platform.maintaincar.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintaincar;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.business.platform.maintaincar.dto.ListServiceMaintaincarDTO;
import com.ccbuluo.business.platform.maintaincar.dto.SearchBizServiceMaintaincarDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 客服经理 上门维修 使用的维修车 实体表 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
 * @version V1.0.0
 */
@Repository
public class BizServiceMaintaincarDao extends BaseDao<BizServiceMaintaincar> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 客服经理 上门维修 使用的维修车 实体表实体
     * @param entity 客服经理 上门维修 使用的维修车 实体表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int saveServiceMaintaincar(BizServiceMaintaincar entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_maintaincar ( mend_code,vin_number,")
            .append("car_status,carbrand_id,carseries_id,carmodel_id,cusmanager_uuid,")
            .append("cusmanager_name,beidou_number,remark,creator,create_time,operator,")
            .append("operate_time,delete_flag ) VALUES (  :mendCode, :vinNumber,")
            .append(" :carStatus, :carbrandId, :carseriesId, :carmodelId, :cusmanagerUuid,")
            .append(" :cusmanagerName, :beidouNumber, :remark, :creator, :createTime,")
            .append(" :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 客服经理 上门维修 使用的维修车 实体表实体
     * @param entity 客服经理 上门维修 使用的维修车 实体表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int updateServiceMaintaincar(BizServiceMaintaincar entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_maintaincar SET ")
            .append("vin_number = :vinNumber,car_status = :carStatus,")
            .append("carbrand_id = :carbrandId,carseries_id = :carseriesId,")
            .append("carmodel_id = :carmodelId,cusmanager_uuid = :cusmanagerUuid,")
            .append("cusmanager_name = :cusmanagerName,beidou_number = :beidouNumber,")
            .append("remark = :remark,operator = :operator,operate_time = :operateTime WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取客服经理 上门维修 使用的维修车 实体表详情
     * @param id  id
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public BizServiceMaintaincar queryServiceMaintaincarByCarId(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bci.id,bci.mend_code,bci.vin_number,bci.car_status,bci.carbrand_id,bci.carseries_id,")
            .append("bci.carmodel_id,bci.cusmanager_uuid,bci.cusmanager_name,bci.beidou_number,bci.remark,bcm.carbrand_name,bcmm.carseries_name,bcmmm.carmodel_name, ")
            .append("bci.creator,bci.create_time,bci.operator,bci.operate_time,bci.delete_flag")
            .append(" FROM biz_service_maintaincar bci LEFT JOIN basic_carbrand_manage bcm on bci.carbrand_id=bcm.id ")
            .append(" LEFT JOIN basic_carseries_manage bcmm on bci.carseries_id=bcmm.id ")
            .append(" LEFT JOIN basic_carmodel_manage bcmmm ON bci.carmodel_id=bcmmm.id ")
            .append("WHERE bci.id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceMaintaincar.class, sql.toString(), params);
    }

    /**
     * 删除客服经理 上门维修 使用的维修车 实体表
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int deleteCarcoreInfoByCarId(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_maintaincar SET delete_flag = :deleteFlag  WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        params.put("deleteFlag", com.ccbuluo.merchandiseintf.carparts.Constants.Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 车辆基本信息 车架号(VIN) 是否存在
     * @param bizServiceMaintaincar 车辆基本信息
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int countVinNumber(BizServiceMaintaincar bizServiceMaintaincar) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM biz_service_maintaincar ")
                .append("   WHERE vin_number = :vinNumber AND delete_flag = :deleteFlag");
        if (null != bizServiceMaintaincar.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("vinNumber", bizServiceMaintaincar.getVinNumber());
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("id", bizServiceMaintaincar.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车辆基本信息 北斗设备编号 是否存在
     * @param bizServiceMaintaincar 车辆基本信息
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int countBeidouNumber(BizServiceMaintaincar bizServiceMaintaincar) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM biz_service_maintaincar ")
                .append("   WHERE beidou_number = :beidouNumber AND delete_flag = :deleteFlag");
        if (null != bizServiceMaintaincar.getId()) {
            sql.append(" AND id != :id");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("beidouNumber", bizServiceMaintaincar.getBeidouNumber());
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        params.put("id", bizServiceMaintaincar.getId());
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车辆列表分页查询
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param
     * @param keyword (车辆编号或是车架号)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    public Page<SearchBizServiceMaintaincarDTO> queryCarcoreInfoList(Long carbrandId, Long carseriesId, Integer carStatus, String keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bci.id,bci.mend_code,bci.vin_number,bci.car_status,")
                .append("bcm.carbrand_name,bcmm.carseries_name,bcmmm.carmodel_name")
                .append(" FROM biz_service_maintaincar bci LEFT JOIN basic_carbrand_manage bcm on bci.carbrand_id=bcm.id ")
                .append(" LEFT JOIN basic_carseries_manage bcmm on bci.carseries_id=bcmm.id ")
                .append(" LEFT JOIN basic_carmodel_manage bcmmm ON bci.carmodel_id=bcmmm.id ")
                .append(" WHERE bci.delete_flag = :deleteFlag ");
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
        // 维修车状态
        if (null != carStatus) {
            param.put("carStatus", carStatus);
            sql.append(" AND bci.car_status = :carStatus ");
        }
        // 车架号
        if (StringUtils.isNotBlank(keyword)) {
            param.put("Keyword", keyword);
            //目前只根据车架号查询
            sql.append(" AND bci.vin_number LIKE CONCAT('%',:Keyword,'%') ");
        }
        sql.append("  ORDER BY bci.operate_time DESC");
        Page<SearchBizServiceMaintaincarDTO> DTOS = super.queryPageForBean(SearchBizServiceMaintaincarDTO.class, sql.toString(), param,offset,pageSize);
        return DTOS;
    }
    //查询未分配的维修车列表
    public List<ListServiceMaintaincarDTO> queryundistributedlist(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mend_code,vin_number ")
                .append(" FROM biz_service_maintaincar WHERE car_status=0 ");
        Map<String, Object> params = Maps.newHashMap();
        return super.queryListBean(ListServiceMaintaincarDTO.class, sql.toString(), params);
    }
    /**
     * 根据维修车code更新维修车状态
     * @param mendCode code
     * @param status
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author weijb
     * @date 2018-07-31 15:59:51
     */
    public int updatestatusbycode(String mendCode, Integer status){
        Map<String, Object> params = Maps.newHashMap();
        params.put("mendCode", mendCode);
        params.put("status", status);
        String sql = "UPDATE biz_service_maintaincar SET car_status = :status WHERE mend_code = :mendCode";
        return updateForMap(sql, params);
    }
    /**
     *  根据维修车code更新维修车信息
     * @param
     * @exception 
     * @return 
     * @author zhangkangjian
     * @date 2018-08-01 20:27:32
     */
    public int updateCustmanager(BizServiceCustmanager bizServiceCustmanager){
        String sql = "UPDATE biz_service_maintaincar SET cusmanager_uuid = :userUuid, cusmanager_name = :name, car_status = 1 WHERE mend_code = :mendCode";
        return updateForBean(sql, bizServiceCustmanager);
    }
    /**
     * 根据uuid更新维修车状态
     * @param useruuid 项目经理id
     * @param status
     * @return com.ccbuluo.http.StatusDto
     * @exception
     * @author zhangkangjian
     * @date 2018-07-31 15:59:51
     */
    public int updateStatusbyUuid(String useruuid, Integer status){
        Map<String, Object> params = Maps.newHashMap();
        params.put("useruuid", useruuid);
        params.put("status", status);
        String sql = "UPDATE biz_service_maintaincar SET car_status = :status,cusmanager_uuid = null,cusmanager_name = null WHERE cusmanager_uuid = :useruuid";
        return updateForMap(sql, params);
    }
    /**
     * 查询vin编码
     * @param useruudis 用户的uuids
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-01 20:52:09
     */
    public List<BizServiceCustmanager> queryVinNumberByuuid(List<String> useruudis) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("useruudis", useruudis);
        String sql = " SELECT a.cusmanager_uuid as 'userUuid', a.vin_number as 'vinNumber' FROM biz_service_maintaincar a WHERE a.`cusmanager_uuid` IN (:useruudis)";
        return queryListBean(BizServiceCustmanager.class, sql, params);
    }
}
