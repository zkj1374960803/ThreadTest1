package com.ccbuluo.business.platform.carmodellabel.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizCarmodelLabel;
import com.ccbuluo.business.entity.BizServiceMaintaincar;
import com.ccbuluo.business.platform.carmodellabel.dto.BizCarmodelLabelDTO;
import com.ccbuluo.business.platform.carmodellabel.dto.SearchBizCarmodelLabelDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 车型标签 实体表 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
 * @version V1.0.0
 */
@Repository
public class BizCarmodelLabelDao extends BaseDao<BizServiceMaintaincar> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     *  保存车型标签
     * @param entity 保存车型标签 实体表实体
     * @return int 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int saveCarmodelLabel(BizCarmodelLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO basic_carmodel_label ( label_code,label_name,sort,")
            .append("creator,create_time,operator,operate_time,delete_flag ")
            .append(") VALUES (  :labelCode, :labelName, :sort, ")
            .append(":creator, :createTime, :operator, :operateTime, :deleteFlag )");
        return super.save(sql.toString(), entity);
    }

    /**
     * 编辑 车型标签 实体表实体
     * @param entity 车型标签 实体表实体
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int updateCarmodelLabel(BizCarmodelLabel entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_label SET ")
            .append(" label_name = :labelName,sort = :sort,")
            .append("operator = :operator,operate_time = :operateTime")
            .append(" WHERE label_code= :labelCode");
        return super.updateForBean(sql.toString(), entity);
    }

    /**
     * 获取 车型标签 实体表详情
     * @param labelCode  labelCode
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public BizCarmodelLabel queryCarmodelLabelBylabelCode(String labelCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,label_code,label_name,sort,")
            .append("creator,create_time,operator,operate_time,delete_flag")
            .append(" FROM basic_carmodel_label WHERE label_code= :labelCode");
        Map<String, Object> params = Maps.newHashMap();
        params.put("labelCode", labelCode);
        return super.findForBean(BizCarmodelLabel.class, sql.toString(), params);
    }

    /**
     * 删除 车型标签 实体表
     * @param labelId  labelId
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-17 13:57:53
     */
    public int deleteCarcoreInfoBylabelCode(Long labelId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE basic_carmodel_label SET delete_flag = :deleteFlag  WHERE id= :labelId ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("labelId", labelId);
        params.put("deleteFlag", com.ccbuluo.merchandiseintf.carparts.Constants.Constants.DELETE_FLAG_DELETE);
        return super.updateForMap(sql.toString(), params);
    }
    /**
     * 车型标签是否存在
     * @param bizCarmodelLabel
     * @return int
     * @exception
     * @author wuyibo
     * @date 2018-05-09 18:41:36
     */
    public int findCarmodelLabelExist(BizCarmodelLabel bizCarmodelLabel) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carmodel_label ")
                .append("   WHERE label_name = :labelName AND delete_flag = :deleteFlag");
        if (null != bizCarmodelLabel.getLabelCode()) {
            sql.append(" AND label_code != :labelCode");
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("labelCode", bizCarmodelLabel.getLabelCode());
        params.put("labelName", bizCarmodelLabel.getLabelName());
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车型标签列表分页查询
     * @param keyword (车型标签名称)
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-07-13 19:52:44
     */
    public Page<SearchBizCarmodelLabelDTO> queryCarmodelLabelList(String keyword, Integer offset, Integer pageSize){
        Map<String, Object> param = Maps.newHashMap();
        param.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bci.id,bci.label_code,bci.label_name,bci.sort,(SELECT count(*) from basic_carmodel_parameter where carmodel_label_id=bci.id)as parameterTotal")
                .append(" FROM basic_carmodel_label bci ")
                .append(" WHERE bci.delete_flag = :deleteFlag ");
        // 标签名称
        if (StringUtils.isNotBlank(keyword)) {
            param.put("keyword", keyword);
            sql.append(" AND bci.label_name LIKE CONCAT('%',:keyword,'%') ");
        }
        sql.append("  ORDER BY bci.operate_time DESC");
        Page<SearchBizCarmodelLabelDTO> DTOS = super.queryPageForBean(SearchBizCarmodelLabelDTO.class, sql.toString(), param,offset,pageSize);
        return DTOS;
    }
    /**
     * * 标签是否可以删除
     * * @param labelid 车型标签id
     * * @return com.ccbuluo.http.StatusDto
     * * @exception
     * @author wuyibo
     * @date 2018-07-30 14:02:30
     */
    public int findCarmodelParameterByLabelCode(long labelid) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM basic_carmodel_parameter ")
                .append(" WHERE carmodel_label_id != :labelid AND delete_flag = :deleteFlag");
        Map<String, Object> params = Maps.newHashMap();
        params.put("labelid", labelid);
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    /**
     * 车型标签列表查询
     * @author weijb
     * @date 2018-07-18 14:59:51
     */
    public List<BizCarmodelLabelDTO> getAllCarmodelLabelList(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,label_code,label_name,sort,")
                .append("creator,create_time,operator,operate_time,delete_flag")
                .append(" FROM basic_carmodel_label WHERE delete_flag = :deleteFlag ORDER BY sort,id");
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);
        return super.queryListBean(BizCarmodelLabelDTO.class, sql.toString(), params);
    }
}
