package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.entity.BizServiceMultipleprice;
import com.ccbuluo.business.platform.maintainitem.dto.CorrespondAreaDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMultiplepriceDTO;
import com.ccbuluo.db.Page;

import java.util.List;

/**
 * 倍数service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 10:21:52
 */
public interface MultiplepriceService {

    /**
     * 保存地区倍数
     * @param saveBizServiceMaintainitemDTO 工时dto
     * @param maintainitemCode 工时code
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 13:59:55
     */
    int save(String maintainitemCode,  SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO);

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
    Page<BizServiceMultipleprice> queryList(String maintainitemCode, String provinceCode, String cityCode, Integer offset, Integer pagesize);

    /**
     * 根据id删除地区倍数
     * @param id 地区倍数id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-18 16:14:46
     */
    int deleteById(Long id);
}
