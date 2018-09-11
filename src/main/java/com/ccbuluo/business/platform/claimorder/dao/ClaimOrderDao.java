package com.ccbuluo.business.platform.claimorder.dao;

import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.claimorder.dto.QueryClaimorderListDTO;
import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-09-08 13:00:03
 */
@Repository
public class ClaimOrderDao extends BaseDao<ClaimOrderDao> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 批量保存索赔单
     * @param bizServiceClaimorders 索赔单数据
     * @author zhangkangjian
     * @date 2018-09-08 13:02:21
     */
    public void batchSaveServiceClaimorders(List<BizServiceClaimorder> bizServiceClaimorders) {
        if(bizServiceClaimorders == null || bizServiceClaimorders.size() == 0){
            return;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO biz_service_claimorder ( ")
            .append(" claim_ordno,service_ordno,tracking_no,refund_adress,doc_status, ")
            .append(" claim_orgno,claim_orgname,process_orgno,process_orgname,claim_amount, ")
            .append(" actual_amount,repay_time,creator,operator,remark,delete_flag)  ")
            .append(" VALUES(:claimOrdno,:serviceOrdno,:trackingNo, ")
            .append(" :refundAdress,:docStatus,:claimOrgno,:claimOrgname, ")
            .append(" :processOrgno,:processOrgname,:claimAmount, ")
            .append(" :actualAmount,:repayTime,:creator,:operator,:remark,:deleteFlag) ");
        batchInsertForListBean(sql.toString(), bizServiceClaimorders);
    }

    /**
     * 更新索赔单的状态
     * @param bizServiceClaimorder 更新数据
     * @author zhangkangjian
     * @date 2018-09-08 14:29:30
     */
    public void updateClaimOrder(BizServiceClaimorder bizServiceClaimorder) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE biz_service_claimorder SET ")
            .append(" tracking_no = :trackingNo, refund_adress = :refundAdress, doc_status = :docStatus ")
            .append(" WHERE claim_ordno = :claimOrdno ");
        updateForBean(sql.toString(), bizServiceClaimorder);
    }



    /**
     * 根据索赔单号查询详情
     * @param bizServiceClaimorder 查询条件
     * @return BizServiceClaimorder 索赔单详情
     * @author zhangkangjian
     * @date 2018-09-08 14:41:53
     */
    public BizServiceClaimorder findClaimOrderDetailByClaimOrdno(BizServiceClaimorder bizServiceClaimorder) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,claim_ordno,service_ordno,tracking_no, ")
            .append(" refund_adress,doc_status,claim_orgno,claim_orgname, ")
            .append(" process_orgno,process_orgname,claim_amount,actual_amount, ")
            .append(" UNIX_TIMESTAMP(repay_time) as 'repayTime',creator,UNIX_TIMESTAMP(create_time) as 'createTime',operator, ")
            .append(" operate_time,delete_flag,remark,UNIX_TIMESTAMP(process_time) as 'processTime'  ")
            .append(" FROM biz_service_claimorder  ")
            .append(" WHERE claim_ordno = :claimOrdno ");
        return findForBean(BizServiceClaimorder.class, sql.toString(), bizServiceClaimorder);
    }

    /**
     * 查询工时的信息
     * @param productDetailDTO 查询条件
     * @return List<SaveMaintaintemDTO> 工时的列表
     * @author zhangkangjian
     * @date 2018-09-08 14:54:41
     */
    public List<ProductDetailDTO> queryMaintainitemDetail(ProductDetailDTO productDetailDTO) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT a.product_no,b.maintainitem_name as 'productName',a.unit_price, a.amount,a.warranty_type,a.service_username,a.service_userid  ")
            .append(" FROM biz_serviceorder_detail a ")
            .append("  LEFT JOIN biz_service_maintainitem b ON a.product_no = b.maintainitem_code ")
            .append(" WHERE 1 = 1  ");
        if(StringUtils.isNotBlank(productDetailDTO.getServiceOrderno())){
            sql.append(" AND a.order_no = :serviceOrderno ");
        }
        if(StringUtils.isNotBlank(productDetailDTO.getProductType())){
            sql.append(" AND a.product_type = :productType ");
        }
        if(StringUtils.isNotBlank(productDetailDTO.getWarrantyType())){
            sql.append(" AND a.warranty_type = :warrantyType ");
        }
        SqlParameterSource param = new BeanPropertySqlParameterSource(productDetailDTO);
        return queryListBean(ProductDetailDTO.class, sql.toString(), param);
    }

    /**
     *  查询索赔单列表
     * @param claimOrdno 索赔单号
     * @param userOrgCode 用户orgcode
     * @param offset 偏移量
     * @param pageSize 每页显示的数量
     * @return Page<QueryClaimorderListDTO> 分页的索赔单列表
     * @author zhangkangjian
     * @date 2018-09-08 16:16:03
     */
    public Page<QueryClaimorderListDTO> queryClaimorderList(String claimOrdno, String docStatus, String userOrgCode, Integer offset, Integer pageSize){
        HashMap<String, Object> map = Maps.newHashMap();
        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT a.claim_ordno,a.doc_status, a.service_ordno,b.car_no,UNIX_TIMESTAMP(a.create_time) as 'createTime'  ")
            .append(" FROM biz_service_claimorder a  ")
            .append(" LEFT JOIN biz_service_order b ON a.service_ordno = b.service_orderno ")
            .append("  WHERE 1 = 1  ");
        if(StringUtils.isNotBlank(userOrgCode)){
            map.put("userOrgCode", userOrgCode);
            sql.append(" AND a.claim_orgno =:userOrgCode ");
        }
        if(StringUtils.isNotBlank(claimOrdno)){
            map.put("claimOrdno", claimOrdno);
            sql.append(" AND a.claim_ordno =:claimOrdno ");
        }
        if(StringUtils.isNotBlank(docStatus)){
            map.put("docStatus", docStatus);
            sql.append(" AND a.doc_status =:docStatus");
        }
        return queryPageForBean(QueryClaimorderListDTO.class, sql.toString(), map, offset, pageSize);
    }

    /**
     * 更新索赔单状态
     * @param claimOrdno 索赔单编号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 11:00:18
     */
    public void updateDocStatus(String claimOrdno, String docStatus) {
        if(StringUtils.isAnyBlank(claimOrdno, docStatus)){
            return;
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("claimOrdno", claimOrdno);
        map.put("docStatus", docStatus);
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE biz_service_claimorder SET ")
            .append(" doc_status = :docStatus ")
            .append(" WHERE claim_ordno = :claimOrdno ");
        updateForBean(sql.toString(), map);
    }

    /**
     * 更新索赔单状态和验收时间
     * @param claimOrdno 索赔单编号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 11:00:18
     */
    public void updateDocStatusAndProcessTime(String claimOrdno, String docStatus) {
        if(StringUtils.isAnyBlank(claimOrdno, docStatus)){
            return;
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("claimOrdno", claimOrdno);
        map.put("docStatus", docStatus);
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE biz_service_claimorder SET ")
            .append(" doc_status = :docStatus, process_time = now()")
            .append(" WHERE claim_ordno = :claimOrdno ");
        updateForBean(sql.toString(), map);
    }

    /**
     * 更新索赔单状态和支付时间
     * @param claimOrdno 索赔单编号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 11:00:18
     */
    public void updateDocStatusAndRepayTime(String claimOrdno, String docStatus) {
        if(StringUtils.isAnyBlank(claimOrdno, docStatus)){
            return;
        }
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("claimOrdno", claimOrdno);
        map.put("docStatus", docStatus);
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE biz_service_claimorder SET ")
            .append(" doc_status = :docStatus, repay_time = now()")
            .append(" WHERE claim_ordno = :claimOrdno ");
        updateForBean(sql.toString(), map);
    }
}
