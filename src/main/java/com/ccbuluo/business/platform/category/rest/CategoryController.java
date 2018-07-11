package com.ccbuluo.business.platform.category.rest;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.http.*;

import com.ccbuluo.merchandiseintf.carparts.category.dto.QueryCategoryListDTO;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.merchandiseintf.carparts.entity.BasicCarpartsCategory;
import io.swagger.annotations.*;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类
 * @author zhangkangjian
 * @date 2018-07-09 10:14:20
 */
@Api(tags = "分类(平台端)")
@RestController
@RequestMapping("/category/category")
public class CategoryController extends BaseController {
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;
    @Autowired
    UserHolder userHolder;
    @Resource
    GenerateProjectCodeService generateProjectCodeService;

    /**
     * 查询树形列表
     * @return StatusDto<List<BasicCarpartsCategory>>
     * @author zhangkangjian
     * @date 2018-07-09 10:22:28
     */
    @ApiOperation(value = "查询树形列表",notes = "【张康健】")
    @GetMapping("/querycategorylist")
    public StatusDto<List<QueryCategoryListDTO>> queryCategoryList(){
        StatusDtoThriftList<QueryCategoryListDTO> list =
            carpartsCategoryService.queryCategoryList();
        return StatusDtoThriftUtils.resolve(list, QueryCategoryListDTO.class);
    }
    /**
     * 添加分类
     * @param carpartsCategory 信息
     * @return StatusDtoThriftBean<BasicCarpartsCategory>
     * @author zhangkangjian
     * @date 2018-07-05 17:50:48
     */
    @ApiOperation(value = "添加分类（添加成功返回分类信息）",notes = "【张康健】")
    @PostMapping("/create")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryName", value = "分类名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "parentCode", value = "父级分类code", required = true, paramType = "query"),
        @ApiImplicitParam(name = "sortNo", value = "排序号", required = true, paramType = "query")
    })
    public StatusDto<BasicCarpartsCategory> create(@ApiIgnore BasicCarpartsCategory carpartsCategory) throws TException {
        String loggedUserId = userHolder.getLoggedUserId();
        carpartsCategory.setCreator(loggedUserId);
        carpartsCategory.setOperator(loggedUserId);
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FK);
        carpartsCategory.setCategoryCode(stringStatusDto.getData());
        StatusDtoThriftBean<BasicCarpartsCategory> bean = carpartsCategoryService.create(carpartsCategory);
            return StatusDtoThriftUtils.resolve(bean, BasicCarpartsCategory.class);
    }

    /**
     * 编辑分类
     * @param id 当前分类id
     * @param categoryName 添加的分类名称
     * @param parentCode 父级分类code
     * @return  StatusDtoThriftBean<BasicCarpartsCategory>
     * @author zhangkangjian
     * @date 2018-07-06 15:18:09
     */
    @ApiOperation(value = "编辑分类",notes = "【张康健】")
    @PostMapping("/edit")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "当前分类id", required = true, paramType = "query"),
        @ApiImplicitParam(name = "categoryName", value = "当前分类name", required = true, paramType = "query"),
        @ApiImplicitParam(name = "parentCode", value = "父级分类code", required = true, paramType = "query")
    })
    public StatusDto<String> edit(Long id, String categoryName, String parentCode){
        StatusDtoThriftBean<String> edit = carpartsCategoryService.edit(id, categoryName, parentCode);
        return StatusDtoThriftUtils.resolve(edit, String.class);
    }

    /**
     * 查询分类（下拉框）
     * @param parentCode 查询一级分类（不必传值） 查询二级或三级必传
     * @return StatusDtoThriftList<BasicCarpartsCategory> 分类列表
     * @author zhangkangjian
     * @date 2018-07-09 09:55:28
     */
    @ApiOperation(value = "查询分类（下拉框）",notes = "【张康健】")
    @GetMapping("/querycategoryselectlist")
    @ApiImplicitParam(name = "parentCode", value = "父级分类code（查询一级分类（不传值） 查询二级或三级必传）", paramType = "query")
    public StatusDto<List<QueryCategoryListDTO>> queryCategoryList(String parentCode){
        StatusDtoThriftList<QueryCategoryListDTO> list = carpartsCategoryService.queryCategorySelectList(parentCode);
        return StatusDtoThriftUtils.resolve(list, QueryCategoryListDTO.class);
    }

    /**
     * 删除分类
     * @param categoryCode 当前分类的code
     * @return StatusDtoThriftBean<Boolean> 删除成功 true
     * @author zhangkangjian
     * @date 2018-07-09 09:16:25
     */
    @ApiOperation(value = "删除分类",notes = "【张康健】")
    @PostMapping("/deletecategory")
    @ApiImplicitParam(name = "categoryCode", value = "当前分类code", required = true, paramType = "query")
    public StatusDto<Boolean> deleteCategory(String categoryCode){
        StatusDtoThriftBoolean<Boolean> booleanStatusDtoThriftBoolean = carpartsCategoryService.deleteCategory(categoryCode);
        return StatusDtoThriftUtils.resolve(booleanStatusDtoThriftBoolean, Boolean.class);
    }

}
