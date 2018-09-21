package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelParameter;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 车型参数配置service
 * @author chaoshuai
 * @date 2018-05-08 10:51:23
 */
public interface BasicCarmodelParameterService {
    /**
     * 根据id删除车型参数配置
     * @param id 车型参数配置id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:11:22
     */
    StatusDto deleteParameter(Long id);

    /**
     * 根据id查询配置参数详情
     * @param id 配置参数id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    CarmodelParameter queryForParameterById(Long id);

    /**
     * 新增车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 15:33:04
     */
    StatusDto createParameter(CarmodelParameter carmodelParameter);

    /**
     * 编辑车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 16:32:16
     */
    StatusDto editParameter(CarmodelParameter carmodelParameter);

    /**
     * 分页查询所有配置参数
     * @param parameterName 配置参数名称
     * @param offset 偏移量
     * @param limit 步长
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:58:58
     */
    Page<CarmodelParameter> queryPageForParameter(String parameterName, String valueType, Integer carmodelLabelId, int offset, int limit);

    /**
     * 查询所用的配置参数
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 10:12:31
     */
    List<CarmodelParameter> queryAllParameter();

    /**
     * 根据配置参数名称查询配置参数详情
     * @param parameterName 配置参数名称
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    List<CarmodelParameter> getByParameterName(String parameterName);
}
