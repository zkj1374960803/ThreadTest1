package com.ccbuluo.business.platform.label.dao;

import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.platform.label.dto.LabelServiceCenterDTO;
import com.ccbuluo.business.platform.label.dto.ListLabelDTO;
import com.ccbuluo.dao.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *  标签与服务中心关联关系dao
 * @author liuduo
 * @date 2018-07-03 09:14:06
 * @version V1.0.0
 */
@Repository
public class LabelServiceCenterDao extends BaseDao<LabelServiceCenterDTO> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }

    /**
     * 保存标签与服务中心关联关系
     * @param lableList 标签与服务中心关联关系
     * @return int 影响条数
     * @author liuduo
     * @date 2018-07-03 09:14:06
     */
    public int[] saveEntity(List<LabelServiceCenterDTO> lableList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO rel_center_label ( service_center_code,label_id,creator,")
            .append("operator,delete_flag ) VALUES (  :serviceCenterCode, :labelId, :creator,")
            .append(" :operator, :deleteFlag )");
        return super.batchSaveForListBean(sql.toString(), lableList);
    }


    /**
     * 根据服务中心code查询关联的标签
     * @param serviceCenterCode 服务中心code
     * @return 服务中心关联的标签
     * @author liuduo
     * @date 2018-07-05 10:20:34
     */
    public List<BizServiceLabel> getLabelServiceCenterByCode(String serviceCenterCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceCenterCode", serviceCenterCode);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,label_name FROM biz_service_label WHERE id IN (")
            .append(" SELECT label_id FROM rel_center_label WHERE service_center_code = :serviceCenterCode)");

        return queryListBean(BizServiceLabel.class, sql.toString(), params);
    }

    /**
     * 根据服务中心code删除关联关系
     * @param serviceCenterCode 服务中心code
     * @return 影响条数
     * @author liuduo
     * @date 2018-07-05 11:42:33
     */
    public int delLabelServiceCenter(String serviceCenterCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("serviceCenterCode", serviceCenterCode);

        String sql = "DELETE FROM rel_center_label WHERE service_center_code = :serviceCenterCode";

        return updateForMap(sql, params);
    }
}
