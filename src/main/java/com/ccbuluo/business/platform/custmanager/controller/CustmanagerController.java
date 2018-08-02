package com.ccbuluo.business.platform.custmanager.controller;


import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.custmanager.dto.CustManagerDetailDTO;
import com.ccbuluo.business.platform.custmanager.dto.QueryUserListDTO;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.business.platform.custmanager.service.CustmanagerServiceImpl;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.controller.BaseController;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.BasicUserOrganizationDTO;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 客户经理
 * @author zhangkangjian
 * @date 2018-07-17 19:37:21
 */
@Api(tags = "客户经理")
@RestController
@RequestMapping("/custmanager/custmanager")
public class CustmanagerController extends BaseController {
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @Autowired
    private UserHolder userHolder;
    @Resource
    private CustmanagerServiceImpl custmanagerServiceImpl;


    /**
     * 添加用户
     * @param userInfoDTO 用户的详细的信息
     * @param bizServiceCustmanager 用户的业务数据
     * @author zhangkangjian
     * @date 2018-05-21 15:45:12
     */
    @ApiOperation(value = "创建用户", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "用户名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "gender", value = "性别", required = true, paramType = "query"),
        @ApiImplicitParam(name = "ethnic", value = "名族", required = true, paramType = "query"),
        @ApiImplicitParam(name = "ethnicCode", value = "名族code", required = true, paramType = "query"),
        @ApiImplicitParam(name = "birthday", value = "生日(秒值)", required = true, paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "certificateType", value = "证件类型1:身份证2:护照3:港澳4:台湾", required = true, paramType = "query"),
        @ApiImplicitParam(name = "certificateNo", value = "证件编号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "telephone", value = "联系电话", required = true, paramType = "query"),
        @ApiImplicitParam(name = "hiredate", value = "入职时间(秒值)", required = true, paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "officePhone", value = "办公手机号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "receivingAddress", value = "收货地址", required = true, paramType = "query"),
        @ApiImplicitParam(name = "orgCode", value = "组织架构code", required = true, paramType = "query"),
        @ApiImplicitParam(name = "servicecenterCode", value = "服务中心code", required = true, paramType = "query")
    })
    @PostMapping("/createuser")
    public StatusDto<String> createUser(@ApiIgnore UserInfoDTO userInfoDTO, @ApiIgnore BizServiceCustmanager bizServiceCustmanager) {
        return custmanagerServiceImpl.createCustManager(userInfoDTO, bizServiceCustmanager);
    }
    /**
     * 查询客户经理列表
     * @param userInfoDTO 用户查询条件
     * @return
     * @author zhangkangjian
     * @date 2018-07-18 16:31:38
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询客户经理列表", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userStatus", value = "用户状态 null:全部1:在职0:离职", required = false, paramType = "query"),
        @ApiImplicitParam(name = "name", value = "用户姓名", required = false, paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "起始值", required = true, paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "显示的数量", required = true, paramType = "query")
    })
    public StatusDto<Page<QueryUserListDTO>> queryUserList(@ApiIgnore UserInfoDTO userInfoDTO){
        return custmanagerServiceImpl.queryUserList(userInfoDTO);

    }
    /**
     * 编辑客户经理
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-07-19 14:00:34
     */
    @PostMapping("/edit")
    @ApiOperation(value = "编辑客户经理", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "useruuid", value = "用户uuid", required = true, paramType = "query"),
        @ApiImplicitParam(name = "name", value = "用户名称", required = true, paramType = "query"),
        @ApiImplicitParam(name = "gender", value = "性别", required = true, paramType = "query"),
        @ApiImplicitParam(name = "ethnic", value = "名族", required = true, paramType = "query"),
        @ApiImplicitParam(name = "ethnicCode", value = "名族code", required = true, paramType = "query"),
        @ApiImplicitParam(name = "birthday", value = "生日(秒值)", required = true, paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "certificateType", value = "证件类型1:身份证2:护照3:港澳4:台湾", required = true, paramType = "query"),
        @ApiImplicitParam(name = "certificateNo", value = "证件编号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "telephone", value = "联系电话", required = true, paramType = "query"),
        @ApiImplicitParam(name = "hiredate", value = "入职时间(秒值)", required = true, paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "officePhone", value = "办公手机号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "receivingAddress", value = "收货地址", required = true, paramType = "query"),
        @ApiImplicitParam(name = "orgCode", value = "组织架构code", required = true, paramType = "query"),
        @ApiImplicitParam(name = "servicecenterCode", value = "服务中心code", required = true, paramType = "query")
    })
    public StatusDto editUser(@ApiIgnore UserInfoDTO userInfoDTO, @ApiIgnore BizServiceCustmanager bizServiceCustmanager) {
        return custmanagerServiceImpl.editUser(userInfoDTO, bizServiceCustmanager);
    }

    /**
     * 查询客户经理详情
     * @param useruuid 用户uuid
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-19 14:44:11
     */
    @ApiOperation(value = "查询客户经理详情", notes = "【张康健】")
    @ApiImplicitParam(name = "useruuid", value = "用户uuid", required = true, paramType = "path")
    @PostMapping("/detail/{useruuid}")
    public StatusDto<CustManagerDetailDTO> detailUser(@PathVariable String useruuid) throws IOException {
        return custmanagerServiceImpl.custManagerDetail(useruuid);
    }

    /**
     * 更新客户经理信息
     * @param bizServiceCustmanager 客户经理信息
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-25 14:14:11
     */
    @ApiOperation(value = "更新客户经理信息", notes = "【张康健】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userUuid", value = "用户uuid", required = true, paramType = "query"),
        @ApiImplicitParam(name = "officePhone", value = "办公手机号", required = true, paramType = "query"),
        @ApiImplicitParam(name = "receivingAddress", value = "收货地址", required = true, paramType = "query"),
        @ApiImplicitParam(name = "servicecenterCode", value = "服务中心code", required = true, paramType = "query"),
        @ApiImplicitParam(name = "vinNumber", value = "vin", required = true, paramType = "query"),
        @ApiImplicitParam(name = "name", value = "客户经理姓名", required = true, paramType = "query")
    })
    @PostMapping("/updatecustmanager")
    public StatusDto<String> updateCustManager(@ApiIgnore BizServiceCustmanager bizServiceCustmanager) {
        return custmanagerServiceImpl.updateCustManager(bizServiceCustmanager);
    }

    /**
     * 获取组织架构子节点
     * @param parentId 组织架构父级id
     * @param isSearchClose 是否获取关闭的组织架构
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-31 14:58:20
     */
    @ApiImplicitParams({
        @ApiImplicitParam(name = "parentId", value = "组织架构父级id(注：第一次调用填0)", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "isSearchClose", value = "是否获取关闭的组织架构（注：不获取关闭的组织架构，false）", required = true, paramType = "query", dataType = "boolean")
    })
    @ApiOperation(value = "查询部门树", notes = "【张康健】")
    @GetMapping("/orglist")
    public StatusDto queryOrganizationByParentId(int parentId, boolean isSearchClose) {
       return custmanagerServiceImpl.queryOrgList(parentId, isSearchClose);
    }


}
