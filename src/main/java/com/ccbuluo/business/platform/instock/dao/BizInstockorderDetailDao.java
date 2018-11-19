package com.ccbuluo.business.platform.instock.dao;

import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.platform.instock.dto.InstockorderDetailDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  入库单详情dao
 * @author liuduo
 * @date 2018-08-07 11:55:41
 * @version V1.0.0
 */
@Repository
public class BizInstockorderDetailDao extends BaseDao<BizInstockorderDetail> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }



    public List<Long> saveInstockorderDetail(List<BizInstockorderDetail> bizInstockorderDetailList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_instockorder_detail ( instock_orderno,instock_planid,")
            .append("product_no,product_name,product_type,product_categoryname,")
            .append("supplier_no,instock_num,stock_type,unit,cost_price,creator,create_time,operator,")
            .append("operate_time,delete_flag,remark ) VALUES (  :instockOrderno,")
            .append(" :instockPlanid, :productNo, :productName, :productType,")
            .append(" :productCategoryname, :supplierNo, :instockNum, :stockType, :unit, :costPrice,")
            .append(" :creator, :createTime, :operator, :operateTime, :deleteFlag, :remark")
            .append(" )");
        return super.batchInsertForListBean(sql.toString(), bizInstockorderDetailList);
    }

    /**
     * 根据入库单号查询入库单详单集合
     * @param instockNo 入库单号
     * @return 入库单详单集合
     * @author liuduo
     * @date 2018-08-10 10:56:57
     */
    public List<BizInstockorderDetail> queryListByinstockNo(String instockNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,instock_orderno,instock_planid,product_no,product_name,stock_type,purchase_info,")
            .append("product_type,product_categoryname,supplier_no,instock_num,unit,cost_price")
            .append(" FROM biz_instockorder_detail WHERE instock_orderno= :instockNo");
        Map<String, Object> params = Maps.newHashMap();
        params.put("instockNo", instockNo);

        return queryListBean(BizInstockorderDetail.class, sql.toString(), params);
    }

    /**
     * 修改入库单详单的库存id
     * @param bizInstockorderDetailList1 入库单详单
     * @author liuduo
     * @date 2018-08-10 11:04:51
     */
    public void updateInstockorderStockId(List<BizInstockorderDetail> bizInstockorderDetailList1) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_instockorder_detail SET stock_id = :stockId,operator = :operator,")
            .append(" operate_time = :operateTime WHERE id = :id");

        batchUpdateForListBean(sql.toString(), bizInstockorderDetailList1);
    }

    /**
     * 根据入库单号查询入库单详单
     * @param instockNo 入库单号
     * @return 入库单详单
     * @author liuduo
     * @date 2018-08-13 15:44:47
     */
    public List<InstockorderDetailDTO> getByInstockNo(String instockNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("instockNo", instockNo);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bid.instock_orderno,bid.product_no,bid.product_name,bid.product_categoryname,bid.supplier_no,")
            .append(" bss.supplier_name,bid.instock_num,bid.cost_price,bid.unit,bid.stock_type FROM biz_instockorder_detail AS bid")
            .append(" LEFT JOIN biz_service_supplier AS bss ON bss.supplier_code = bid.supplier_no")
            .append("  WHERE bid.instock_orderno = :instockNo");

        return queryListBean(InstockorderDetailDTO.class, sql.toString(), params);
    }
}
