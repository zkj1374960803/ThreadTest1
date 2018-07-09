package com.ccbuluo.business.platform.category.rest;

import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;

import com.ccbuluo.merchandiseintf.carparts.category.dto.QueryCategoryListDTO;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.merchandiseintf.carparts.entity.BasicCarpartsCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ThriftRPCClient("CarpartsCategoryService")
    private CarpartsCategoryService carpartsCategoryService;

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
    @ApiOperation(value = "添加分类",notes = "【张康健】")
    @GetMapping("/create")
    public StatusDto<BasicCarpartsCategory> create(BasicCarpartsCategory carpartsCategory){
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
    @GetMapping("/edit")
    public StatusDto<String> edit(Long id, String categoryName, String parentCode){
        StatusDtoThriftBean<String> edit = carpartsCategoryService.edit(id, categoryName, parentCode);
        return StatusDtoThriftUtils.resolve(edit, String.class);
    }

    /**
     * 查询分类（下拉框）
     * @param code 查询一级分类（不必传值） 查询二级必传
     * @return StatusDtoThriftList<BasicCarpartsCategory> 分类列表
     * @author zhangkangjian
     * @date 2018-07-09 09:55:28
     */
    @ApiOperation(value = "查询分类（下拉框）",notes = "【张康健】")
    @GetMapping("/querycategoryselectlist")
    public StatusDto<List<QueryCategoryListDTO>> queryCategoryList(String code){
        StatusDtoThriftList<QueryCategoryListDTO> list = carpartsCategoryService.queryCategoryList(code);
        return StatusDtoThriftUtils.resolve(list, QueryCategoryListDTO.class);
    }

    /**
     * 删除分类
     * @param code 当前分类的code
     * @return StatusDtoThriftBean<Boolean> 删除成功 true
     * @author zhangkangjian
     * @date 2018-07-09 09:16:25
     */
    @ApiOperation(value = "删除分类",notes = "【张康健】")
    @GetMapping("/deletecategory")
    public StatusDto<Boolean> deleteCategory(String code){
        StatusDtoThriftBean<Boolean> booleanStatusDtoThriftBean = carpartsCategoryService.deleteCategory(code);
        return StatusDtoThriftUtils.resolve(booleanStatusDtoThriftBean, Boolean.class);
    }

}
