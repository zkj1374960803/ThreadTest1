package com.ccbuluo.business.platform.category.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.merchandiseintf.carparts.entity.BasicCarpartsCategory;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangkangjian
 * @date 2018-07-11 11:30:23
 */
@Service
public class CategoryServiceImpl {

    @Resource
    private UserHolder userHolder;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;
    @Resource
    GenerateProjectCodeService generateProjectCodeService;

    /**
     * 创建分类
     * @param carpartsCategory 分类信息
     * @return StatusDto<BasicCarpartsCategory> 状态dto
     * @author zhangkangjian
     * @date 2018-07-11 11:38:01
     */
    public StatusDto<BasicCarpartsCategory> create(BasicCarpartsCategory carpartsCategory) throws TException {
        String loggedUserId = userHolder.getLoggedUserId();
        carpartsCategory.setCreator(loggedUserId);
        carpartsCategory.setOperator(loggedUserId);
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FK);
        if(!stringStatusDto.isSuccess()){
            return StatusDto.buildFailure("生成分类编号失败！");
        }
        carpartsCategory.setCategoryCode(stringStatusDto.getData());
        StatusDtoThriftBean<BasicCarpartsCategory> bean = carpartsCategoryService.create(carpartsCategory);
        return StatusDtoThriftUtils.resolve(bean, BasicCarpartsCategory.class);
    }
}
