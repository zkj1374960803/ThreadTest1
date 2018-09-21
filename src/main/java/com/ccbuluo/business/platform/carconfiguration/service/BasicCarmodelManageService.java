package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.platform.carconfiguration.dao.CarmodelManageDTO;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelManage;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;

import java.util.List;
import java.util.Map;


/**
 * 车型管理service
 * @author chaoshuai
 * @date 2018-05-08 10:51:52
 */
public interface BasicCarmodelManageService {
    /**
     * 车型停用启用
     * @param id 车型id
     * @param status 车型状态
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 17:19:31
     */
    StatusDto stopOperationCarModel(Long id, int status);

    /**
     * 分页查询车型列表
     * @param carbrandId 品牌id
     * @param carseriesId 车系id
     * @param status 状态
     * @param offset
     * @param limit
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 19:37:29
     */
    Page<CarmodelManageDTO> queryPageForCarModelManage(Long carbrandId, Long carseriesId, Integer status, String carmodelName, int offset, int limit);

    /**
     * 新增车型
     * @param carmodelManageDTO 车型实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:43:22
     */
    StatusDto createCarModel(CarmodelManageDTO carmodelManageDTO);

    /**
     * 编辑车型
     * @param carmodelManageDTO 车型实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 09:43:22
     */
    StatusDto editCarModel(CarmodelManageDTO carmodelManageDTO);

    /**
     * 根据车系id查询车型
     * @param id 车系id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-10 09:37:13
     */
    List<CarmodelManage> getByCarSeriesId(Long id);

    /**
     * 查询所有的车型
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-11 11:20:13
     */
    List<CarmodelManage> queryAllModel();

    /**
     * 根据车型id 获取车型名称 和 logo
     * @param id 车型id
     * @return Map<String,Object> eg：{"carmodelName":"","carbrandLogo":""}
     * @exception
     * @author lizhao
     * @date 2018-05-29 17:36:27
     */
    Map<String,Object> getCarmodelAndLogo(Long id);
    /**
     * 获取全部车型 下拉用
     * @return 结果集
     * @author Ryze
     * @date 2018-06-12 15:31:17
     */
    List<Map<String,Object>> queryAll();
    /**
     * 删除车型
     * @param id 车型id
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-01 09:37:13
     */
    StatusDto deleteCarmodelManageById(Long id);
    /**
     * 把车型id转换成车型名字
     */
    void buildCarModeName(List<BasicCarpartsProductDTO> list);

    /**
     * 根据车型id查询车型名字
     * @param carmodelId 车型id
     * @return 车型名字
     * @author liuduo
     * @date 2018-09-04 16:48:11
     */
    String getNameById(Long carmodelId);

}
