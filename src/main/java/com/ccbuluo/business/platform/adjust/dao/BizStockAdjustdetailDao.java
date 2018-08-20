package com.ccbuluo.business.platform.adjust.dao;

import com.ccbuluo.business.entity.BizStockAdjustdetail;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustDetailDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustListDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  dao
 * @author liuduo
 * @date 2018-08-14 16:10:21
 * @version V1.0.0
 */
@Repository
public class BizStockAdjustdetailDao extends BaseDao<BizStockAdjustdetail> {
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
     * @date 2018-08-14 16:10:21
     */
    public int saveEntity(BizStockAdjustdetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_stock_adjustdetail ( adjust_docno,product_no,")
            .append("product_type,product_name,product_categoryname,perfect_num,")
            .append("actual_num,affect_stockid,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark ) VALUES (  :adjustDocno, :productNo,")
            .append(" :productType, :productName, :productCategoryname, :perfectNum,")
            .append(" :actualNum, :affectStockid, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag, :remark )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 实体
     * @param entity 实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public int update(BizStockAdjustdetail entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_stock_adjustdetail SET adjust_docno = :adjustDocno,")
            .append("product_no = :productNo,product_type = :productType,")
            .append("product_name = :productName,")
            .append("product_categoryname = :productCategoryname,")
            .append("perfect_num = :perfectNum,actual_num = :actualNum,")
            .append("affect_stockid = :affectStockid,creator = :creator,")
            .append("create_time = :createTime,operator = :operator,")
            .append("operate_time = :operateTime,delete_flag = :deleteFlag,")
            .append("remark = :remark WHERE id= :id");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取详情
     * @param id  id
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public BizStockAdjustdetail getById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,adjust_docno,product_no,product_type,product_name,")
            .append("product_categoryname,perfect_num,actual_num,affect_stockid,creator,")
            .append("create_time,operator,operate_time,delete_flag,remark")
            .append(" FROM biz_stock_adjustdetail WHERE id= :id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.findForBean(BizStockAdjustdetail.class, sql.toString(), params);
    }

    /**
     * 删除
     * @param id  id
     * @return 影响条数
     * @author liuduo
     * @date 2018-08-14 16:10:21
     */
    public int deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE  FROM biz_stock_adjustdetail WHERE id= :id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        return super.updateForMap(sql.toString(), params);
    }

    /**
     * 根据盘库单号查询盘库详情
     * @param adjustNo 盘库单号
     * @return 盘库详情
     * @author liuduo
     * @date 2018-08-15 14:37:37
     */
    public List<StockAdjustListDTO> getByAdjustNo(String adjustNo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("adjustNo", adjustNo);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,adjust_docno,product_no,product_type,product_name,product_categoryname,")
            .append(" IFNULL(perfect_num,0) AS dueNum,IFNULL(actual_num, 0) AS actualNum WHERE adjust_docno = :adjustNo");

        return queryListBean(StockAdjustListDTO.class, sql.toString(), params);
    }

    /**
     * 保存盘库详单
     * @param bizStockAdjustdetailList 盘库详单
     * @return 主键id
     * @author liuduo
     * @date 2018-08-15 09:01:44
     */
    public List<Long> saveAdjustDetail(List<BizStockAdjustdetail> bizStockAdjustdetailList) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_stock_adjustdetail ( adjust_docno,product_no,")
            .append("product_type,product_name,product_categoryname,perfect_num,")
            .append("actual_num,affect_stockid,creator,create_time,operator,operate_time,")
            .append("delete_flag,remark ) VALUES (  :adjustDocno, :productNo,")
            .append(" :productType, :productName, :productCategoryname, :perfectNum,")
            .append(" :actualNum, :affectStockid, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag, :remark )");

        return batchInsertForListBean(sql.toString(), bizStockAdjustdetailList);
    }
}
