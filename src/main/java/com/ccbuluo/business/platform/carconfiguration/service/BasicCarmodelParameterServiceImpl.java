package com.ccbuluo.business.platform.carconfiguration.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelConfigurationDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelParameterDao;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelParameter;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 车型参数配置service实现类
 * @author wuyibo
 * @date 2018-05-08 12:04:47
 * @version v 1.0.0
 */
@Service
public class BasicCarmodelParameterServiceImpl implements BasicCarmodelParameterService{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasicCarmodelParameterDao basicCarmodelParameterDao;
    @Autowired
    private UserHolder userHolder;
    @Resource
    BasicCarmodelConfigurationDao basicCarmodelConfigurationDao;

    /**
     * 被车型引用过的参数不能删除
     */
    private static final String CARMODELPARAMETER_CANNOT_DELETE = "被车型引用过的参数不能删除！";
    
    /**
     * 分页查询所有配置参数
     * @param parameterName 配置参数名称
     * @param offset 偏移量
     * @param limit 步长
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:58:58
     */
    @Override
    public Page<CarmodelParameter> queryPageForParameter(String parameterName, String valueType, Integer carmodelLabelId, int offset, int limit) {
        Page<CarmodelParameter> carmodelParameterPage = this.basicCarmodelParameterDao.queryPageForParameter(parameterName, offset, limit);
        return carmodelParameterPage;
    }

    /**
     * 查询所用的配置参数
     * @param
     * @exception
     * @author chaoshuai
     * @Date 2018-05-09 10:12:31
     */
    @Override
    public List<CarmodelParameter> queryAllParameter() {
        List<CarmodelParameter> carmodelParameters = this.basicCarmodelParameterDao.queryAllParameter();
        return carmodelParameters;
    }

    /**
     * 根据id查询配置参数详情
     * @param id 配置参数id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    @Override
    public CarmodelParameter queryForParameterById(Long id) {
        CarmodelParameter carmodelParameter = this.basicCarmodelParameterDao.getById(id);
        return carmodelParameter;
    }

    /**
     * 根据配置参数名称查询配置参数详情
     * @param parameterName 配置参数名称
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:24:17
     */
    @Override
    public List<CarmodelParameter> getByParameterName(String parameterName) {
        CarmodelParameter carmodelParameter = new CarmodelParameter();
        carmodelParameter.setParameterName(parameterName);
        List<CarmodelParameter> parameter = this.basicCarmodelParameterDao.getParameter(carmodelParameter);
        return parameter;
    }

    /**
     * 新增车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 15:33:04
     */
    @Override
    public StatusDto createParameter(CarmodelParameter carmodelParameter) {
        List<CarmodelParameter> carmodelParameters = this.basicCarmodelParameterDao.getParameter(carmodelParameter);
        if( carmodelParameters.size() > 0){
            return StatusDto.buildFailureStatusDto("配置参数名称不能重复！！！");
        }
        if(StringUtils.isNotBlank(carmodelParameter.getOptionalList())){
            boolean flag = checkRepeat(carmodelParameter.getOptionalList());
            if (!flag){
                return StatusDto.buildFailureStatusDto("可选值不能重复！！！！");
            }
        }
        try {
            carmodelParameter.preInsert(userHolder.getLoggedUserId());
            this.basicCarmodelParameterDao.createParameter(carmodelParameter);
            return StatusDto.buildSuccessStatusDto("新增配置参数成功！！！");
        }catch (Exception e){
            logger.error("新增配置参数失败！！！" , e);
            throw e;
        }
    }



    /**
     * 编辑车型配置参数
     * @param carmodelParameter 车型配置参数实体
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 16:32:16
     */
    @Override
    public StatusDto editParameter(CarmodelParameter carmodelParameter) {
        List<CarmodelParameter> carmodelParameters = this.basicCarmodelParameterDao.getParameter(carmodelParameter);
        if ( carmodelParameters.size() > 0){
            return StatusDto.buildFailureStatusDto("配置参数名称不能重复！！！");
        }
        if(StringUtils.isNotBlank(carmodelParameter.getOptionalList())){
            boolean flag = checkRepeat(carmodelParameter.getOptionalList());
            if (!flag){
                return StatusDto.buildFailureStatusDto("可选值不能重复！！！！");
            }
        }
        try {
            carmodelParameter.preUpdate(userHolder.getLoggedUserId());
            this.basicCarmodelParameterDao.updateParameter(carmodelParameter);
            return StatusDto.buildSuccessStatusDto("编辑配置参数成功！！！");
        }catch (Exception e){
            logger.error("编辑配置参数失败！！！",e);
            throw e;
        }

    }

    /**
     * 根据id删除车型参数配置
     * @param id 车型参数配置id
     * @exception
     * @author chaoshuai
     * @Date 2018-05-08 14:11:22
     */
    @Override
    public StatusDto deleteParameter(Long id) {
        try {
            // 被车型参数引用过的参数不能删除CARMODELPARAMETER_CANNOT_DELETE
            StatusDto statusDto = findCarmodelParameterById(id);
            if (Constants.ERROR_CODE.equals(statusDto.getCode())) {
                return statusDto;
            }
            this.basicCarmodelParameterDao.deleteParameter(id);
            return StatusDto.buildSuccessStatusDto("删除车型参数配置成功！！！");
        }catch (Exception e){
            logger.error("删除车型参数配置失败！！！",e);
            throw e;
        }
    }
    /**
     * * 车型参数是否可以删除
     * * @param labelId 车型标签id
     * * @return com.ccbuluo.http.StatusDto
     * * @exception
     * @author wuyibo
     * @date 2018-07-30 14:02:30
     */
    public StatusDto findCarmodelParameterById(Long id) {
        int count = basicCarmodelConfigurationDao.findCarmodelParameterById(id);
        StringBuilder result = new StringBuilder();
        if (count > 0) {
            result.append(CARMODELPARAMETER_CANNOT_DELETE);
        }
        if (StringUtils.isNotBlank(result.toString())) {
            return StatusDto.buildFailureStatusDto(result.toString());
        }
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 可选值验重
     * @param optional 字符串
     * @return boolean
     * @exception
     * @author chaoshuai
     * @Date 2018-05-14 18:38:50
     */
    private boolean checkRepeat(String optional) {
        String[] optionals = optional.split(",");
        Set<String> set = new HashSet<String>();
        for(String str : optionals){
            set.add(str);
        }
        if( set.size() != optionals.length ){
            // 有重复
            return false;
        }else{
            // 不重复
            return true;
        }
    }
}
