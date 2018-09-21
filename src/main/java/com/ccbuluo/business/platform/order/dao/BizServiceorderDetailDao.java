package com.ccbuluo.business.platform.order.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceorderDetail;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorderDTO;
import com.ccbuluo.business.platform.order.dto.SaveMaintaintemDTO;
import com.ccbuluo.business.platform.order.dto.SaveMerchandiseDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 记录维修任务中使用的工时和零配件的详情 dao
 * @author liuduo
 * @date 2018-09-06 16:51:41
 * @version V1.0.0
 */
@Repository
public class BizServiceorderDetailDao extends BaseDao<BizServiceorderDetail> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存 记录维修任务中使用的工时和零配件的详情实体
     * @param entity 记录维修任务中使用的工时和零配件的详情实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-09-06 16:51:41
     */
    public int saveEntity(BizServiceorderDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_serviceorder_detail ( order_no,product_no,")
            .append("product_type,unit_price,amount,warranty_type,service_orgno,")
            .append("service_orgname,service_userid,service_username,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :orderNo,")
            .append(" :productNo, :productType, :unitPrice, :amount, :warrantyType,")
            .append(" :serviceOrgno, :serviceOrgname, :serviceUserid, :serviceUsername,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark")
            .append(" )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 记录维修任务中使用的工时和零配件的详情实体
     * @param entity 记录维修任务中使用的工时和零配件的详情实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-09-06 16:51:41
     */
    public int update(BizServiceorderDetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_serviceorder_detail SET order_no = :orderNo,")
            .append("product_no = :productNo,product_type = :productType,")
            .append("unit_price = :unitPrice,amount = :amount,")
            .append("warranty_type = :warrantyType,service_orgno = :serviceOrgno,")
            .append("service_orgname = :serviceOrgname,service_userid = :serviceUserid,")
            .append("service_username = :serviceUsername,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取记录维修任务中使用的工时和零配件的详情详情
     * @param id  id
     * @author liuduo
     * @date 2018-09-06 16:51:41
     */
    public BizServiceorderDetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,order_no,product_no,product_type,unit_price,amount,")
            .append("warranty_type,service_orgno,service_orgname,service_userid,")
            .append("service_username,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_serviceorder_detail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizServiceorderDetail.class, sql.toString(), params);
    }

    /**
     * 查询维修单详单数据
     * @param bizServiceorderDetail 查询条件
     * @return List<BizServiceorderDetail>
     * @author zhangkangjian
     * @date 2018-09-08 11:42:12
     */
    public List<BizServiceorderDetail> queryListBizServiceorderDetail(BizServiceorderDetail bizServiceorderDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,order_no,product_no,product_type,unit_price,amount,")
            .append("warranty_type,service_orgno,service_orgname,service_userid,")
            .append("service_username,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark FROM biz_serviceorder_detail WHERE 1 = 1");
        if (StringUtils.isNotBlank(bizServiceorderDetail.getOrderNo())) {
            sql.append(" AND order_no = :orderNo ");
        }
        if(StringUtils.isNotBlank(bizServiceorderDetail.getWarrantyType())){
            sql.append(" AND warranty_type = :warrantyType ");
        }
        return queryListBean(BizServiceorderDetail.class, sql.toString(), bizServiceorderDetail);
    }

    /**
     * 删除记录维修任务中使用的工时和零配件的详情
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-09-06 16:51:41
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_serviceorder_detail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据维修单编号删除工时和零配件
     * @param serviceOrderno 维修单编号
     * @author liuduo
     * @date 2018-09-07 11:27:46
     */
    public void deleteByOrderNo(String serviceOrderno) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);

        String sql = "DELETE  FROM biz_serviceorder_detail WHERE order_no = :serviceOrderno";

        updateForMap(sql, params);
    }

    /**
     * 保存 记录维修任务中使用的工时详情实体
     * @param saveMaintaintemDTOS1 记录维修任务中使用的工时详情实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-09-06 16:51:41
     */
    public void saveMaintaintem(List<SaveMaintaintemDTO> saveMaintaintemDTOS1) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_serviceorder_detail ( order_no,product_no,")
            .append("product_type,unit_price,amount,warranty_type,service_orgno,")
            .append("service_orgname,service_userid,service_username,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :serviceOrderno,")
            .append(" :productNo, :productType, :unitPrice, :amount, :warrantyType,")
            .append(" :serviceOrgno, :serviceOrgname, :serviceUserid, :serviceUsername,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark)");

        batchInsertForListBean(sql.toString(), saveMaintaintemDTOS1);
    }

    /**
     * 查询某个商品的库存入库记录
     * @param orderNo 服务单号
     * @author weijb
     * @date 2018-08-23 16:59:51
     */
    public List<BizServiceorderDetail> queryServiceorderDetailList(String orderNo){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        param.put("orderNo", orderNo);
        param.put("productType", BizServiceorderDetail.ProductTypeEnum.FITTING.name());
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT id,order_no,product_no,product_type,unit_price,amount,")
                .append("warranty_type,service_orgno,service_orgname,service_userid,")
                .append("service_username,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark FROM biz_serviceorder_detail WHERE delete_flag = :deleteFlag and product_type = :productType and order_no = :orderNo");
        return super.queryListBean(BizServiceorderDetail.class, sql.toString(), param);
    }


    /**
     * 保存 记录维修任务中使用的零配件详情实体
     * @param saveMerchandiseDTOS1 记录维修任务中使用的零配件详情实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-09-06 16:51:41
     */
    public void saveMerchandise(List<SaveMerchandiseDTO> saveMerchandiseDTOS1) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_serviceorder_detail ( order_no,product_no,")
            .append("product_type,unit_price,amount,warranty_type,service_orgno,")
            .append("service_orgname,service_userid,service_username,creator,create_time,")
            .append("operator,operate_time,delete_flag,remark ) VALUES (  :serviceOrderno,")
            .append(" :productNo, :productType, :unitPrice, :amount, :warrantyType,")
            .append(" :serviceOrgno, :serviceOrgname, :serviceUserid, :serviceUsername,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark)");

        batchInsertForListBean(sql.toString(), saveMerchandiseDTOS1);
    }

    /**
     * 获取服务单详情
     * @param serviceOrderno  服务单编号
     * @author weijb
     * @date 2018-09-10 16:51:41
     */
    public List<BizServiceorderDetail> getServiceorderDetailByOrderNo(String serviceOrderno) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,order_no,product_no,product_type,unit_price,amount,")
                .append("warranty_type,service_orgno,service_orgname,service_userid,")
                .append("service_username,creator,create_time,operator,operate_time,")
                .append("delete_flag,remark FROM biz_serviceorder_detail WHERE warranty_type = :warrantyType and order_no= :serviceOrderno");
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceOrderno", serviceOrderno);
        // 过保的零配件
        params.put("warrantyType", BizServiceorderDetail.WarrantyTypeEnum.OVERSHELFLIFE.name());
        return super.queryListBean(BizServiceorderDetail.class, sql.toString(), params);
    }
}
