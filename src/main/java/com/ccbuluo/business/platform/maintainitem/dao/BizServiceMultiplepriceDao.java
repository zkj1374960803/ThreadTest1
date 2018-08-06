package com.ccbuluo.business.platform.maintainitem.dao;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMultipleprice;
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
 * 服务项目 各地区对基本定价的倍数 dao
 * @author liuduo
 * @date 2018-07-17 13:57:53
 * @version V1.0.0
 */
@Repository
public class BizServiceMultiplepriceDao extends BaseDao<BizServiceMultipleprice> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
    }


    /**
     * 保存地区倍数
     * @param bizServiceMultiplepriceList1 地区倍数
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 13:59:55
     */
    public int[] saveMultipleprice(List<BizServiceMultipleprice> bizServiceMultiplepriceList1) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO biz_service_multipleprice ( maintainitem_code,multiple,")
            .append("province_code,province_name,city_code,city_name,remark,creator,")
            .append("create_time,operator,operate_time,delete_flag ) VALUES ( ")
            .append(" :maintainitemCode, :multiple, :provinceCode, :provinceName,")
            .append(" :cityCode, :cityName, :remark, :creator, :createTime, :operator,")
            .append(" :operateTime, :deleteFlag )");

        return super.batchSaveForListBean(sql.toString(), bizServiceMultiplepriceList1);
    }

    /**
     * 查询地区倍数列表
     * @param maintainitemCode 服务项code
     * @param provinceCode 省code
     * @param cityCode 市code
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 地区倍数列表
     * @author liuduo
     * @date 2018-07-18 15:35:18
     */
    public Page<BizServiceMultipleprice> queryList(String maintainitemCode, String provinceCode, String cityCode, Integer offset, Integer pagesize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("deleteFlag", Constants.DELETE_FLAG_NORMAL);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,maintainitem_code,multiple,province_code,province_name,city_code,city_name FROM biz_service_multipleprice ")
            .append(" WHERE 1=1");
        if (StringUtils.isNotBlank(maintainitemCode)) {
            params.put("maintainitemCode", maintainitemCode);
            sql.append(" AND maintainitem_code = :maintainitemCode");
        }
        if (StringUtils.isNotBlank(provinceCode)) {
            params.put("provinceCode", provinceCode);
            sql.append(" AND province_code = :provinceCode");
        }
        if (StringUtils.isNotBlank(cityCode)) {
            params.put("cityCode", cityCode);
            sql.append(" AND city_code = :cityCode");
        }
        sql.append(" AND delete_flag = :deleteFlag ORDER BY operate_time DESC");

        return queryPageForBean(BizServiceMultipleprice.class, sql.toString(), params, offset, pagesize);
    }

    /**
     * 根据id删除地区倍数
     * @param id 地区倍数id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-18 16:14:46
     */
    public int deleteById(Long id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
        params.put("deleteFlag", Constants.DELETE_FLAG_DELETE);

        String sql = "UPDATE  biz_service_multipleprice SET delete_flag = :deleteFlag WHERE id= :id";

        return super.updateForMap(sql, params);
    }

    /**
     * 根据服务项code查询地区code
     * @param maintainitemCode 服务项code
     * @return 已有的地区
     * @author liuduo
     * @date 2018-07-18 16:42:23
     */
    public List<String> queryAreaByCode(String maintainitemCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maintainitemCode", maintainitemCode);

        String sql = "SELECT city_code FROM biz_service_multipleprice WHERE maintainitem_code = :maintainitemCode";

        return querySingColum(String.class, sql, params);
    }

    /**
     * 更新地区倍数
     * @param bizServiceMultipleprice 地区实体
     * @return 更新是否成功
     * @author liuduo
     * @date 2018-07-18 17:02:58
     */
    public int update(BizServiceMultipleprice bizServiceMultipleprice) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE biz_service_multipleprice SET multiple = :multiple WHERE")
            .append(" maintainitem_code = :maintainitemCode AND city_code = :cityCode");

        return updateForBean(sql.toString(), bizServiceMultipleprice);
    }

    /**
     * 根据服务项删除原有地区倍数
     * @param maintainitemCode 服务项code
     * @author liuduo
     * @date 2018-07-30 16:27:24
     */
    public void deleteOld(String maintainitemCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("maintainitemCode", maintainitemCode);

        String sql = "DELETE FROM biz_service_multipleprice WHERE maintainitem_code = :maintainitemCode";

        super.updateForMap(sql, params);
    }
}
