package com.ccbuluo.business.platform.custmanager.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.custmanager.dao.BizServiceCustmanagerDao;
import com.ccbuluo.business.platform.custmanager.dto.CustManagerDetailDTO;
import com.ccbuluo.business.platform.custmanager.dto.QueryUserListDTO;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.json.JsonUtils;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.model.RelUserRole;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.BasicUserRoleService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户经理实现端
 * @author zhangkangjian
 * @date 2018-07-18 10:26:50
 */
@Service
public class CustmanagerServiceImpl implements CustmanagerService{
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;
    @Autowired
    UserHolder userHolder;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserRoleService basicUserRoleService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @Resource
    private BizServiceCustmanagerDao bizServiceCustmanagerDao;

    /**
     * 创建客户经理
     * @param userInfoDTO           用户的基本信息
     * @param bizServiceCustmanager 用户业务数据
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-07-18 10:30:22
     */
    @Override
    public StatusDto<String> createCustManager(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager) {
        // 参数校验
        checkedRoleCodeAndOrgCode(userInfoDTO);
        // 保存基础用户
        String useruuid = saveUser(userInfoDTO);
        // 保存客户经理信息
        saveCustManager(bizServiceCustmanager, useruuid);
        return StatusDto.buildSuccessStatusDto();
    }
    /**
     *  保存客户经理信息
     * @param bizServiceCustmanager 客户经理信息
     * @param useruuid 用户uuid
     * @exception CommonException 自定义异常
     * @author zhangkangjian
     * @date 2018-07-21 12:01:28
     */
    private void saveCustManager(BizServiceCustmanager bizServiceCustmanager, String useruuid) {
        // 保存客户经理信息
        String loggedUserId = userHolder.getLoggedUserId();
        try {
            bizServiceCustmanager.setCreator(loggedUserId);
            bizServiceCustmanager.setOperator(loggedUserId);
            bizServiceCustmanager.setUserUuid(useruuid);
            bizServiceCustmanagerDao.saveBizServiceCustmanager(bizServiceCustmanager);
        }catch (Exception e){
            // 删除用户信息
            e.printStackTrace();
            innerUserInfoService.deleteUserInfoByUseruuid(useruuid);
            throw new CommonException(Constants.ERROR_CODE, "创建客户经理失败！");
        }
    }
    /**
     *  保存基础用户信息,保存成功返回uuid
     * @param userInfoDTO 用户的信息
     * @exception CommonException 自定义异常
     * @return StatusDto<String> 状态
     * @author zhangkangjian
     * @date 2018-07-21 12:02:26
     */
    private String saveUser(UserInfoDTO userInfoDTO) {
        String loggedUserId = userHolder.getLoggedUserId();
        userInfoDTO.setUserType(Constants.USER_TYPE_INNER);
        userInfoDTO.setUserSource(Constants.USER_SOURCE_INNER);
        userInfoDTO.setUserStatus(Constants.USER_STATUS_INNER);
        userInfoDTO.setFreezeStatus(Constants.FREEZE_STATUS_YES);
        userInfoDTO.setOperator(loggedUserId);
        userInfoDTO.setOperateTime(System.currentTimeMillis() / 1000);
        userInfoDTO.setCreator(loggedUserId);
        userInfoDTO.setUserTypeInAndOut(Constants.USER_STATUS_INNER);
        // 保存基础用户信息
        StatusDto<String> userAndRole = innerUserInfoService.saveUserAndRole(userInfoDTO);
        if(!userAndRole.isSuccess()){
            throw new CommonException(userAndRole.getCode(), userAndRole.getMessage());
        }
        String useruuid = userAndRole.getData();
        userInfoDTO.setUseruuid(useruuid);
        StatusDto<String> updateUser = innerUserInfoService.updateUser(userInfoDTO);
        if(!updateUser.isSuccess()){
            throw new CommonException(userAndRole.getCode(), updateUser.getMessage());
        }
        return useruuid;
    }

    /**
     * 用户信息校验
     * @param userInfoDTO 用户信息
     * @return StatusDto<String> 状态dto
     * @author zhangkangjian
     * @date 2018-07-19 11:53:11
     */
    private void checkedRoleCodeAndOrgCode(UserInfoDTO userInfoDTO) {
        // 角色校验
        StatusDtoThriftList<Long> statusDtoRole = basicUserRoleService.queryRoleByRoleCode(Constants.CUSTMANAGER_ROLE_CODE);
        if(!statusDtoRole.isSuccess()){
            throw new CommonException(statusDtoRole.getCode(), "客户经理角色不存在！");
        }
        // 组织架构校验
        userInfoDTO.setOrgCode(BusinessPropertyHolder.TOP_SERVICECENTER);
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(userInfoDTO.getOrgCode());
        if(!orgByCode.isSuccess()){
            throw new CommonException(statusDtoRole.getCode(), "组织架构不存在！");
        }
        StatusDto<List<Long>> resolve = StatusDtoThriftUtils.resolve(statusDtoRole, Long.class);
        userInfoDTO.setRoles(resolve.getData());
    }

    /**
     * 查询用户列表
     * @param userInfoDTO 查询条件
     * @return StatusDtoThriftList
     * @author zhangkangjian
     * @date 2018-07-18 16:55:03
     */
    @Override
    public StatusDto<Page<QueryUserListDTO>> queryUserList(UserInfoDTO userInfoDTO) {
        userInfoDTO.setAppId(SystemPropertyHolder.getBaseAppid());
        userInfoDTO.setSecretId(SystemPropertyHolder.getBaseSecret());
        userInfoDTO.setOrgCode(BusinessPropertyHolder.TOP_SERVICECENTER);
        // 排序字段
        userInfoDTO.setSortField(Constants.SORT_FIELD_ID);
        //查询用户信息
        StatusDtoThriftPage<UserInfoDTO> userInfoDTOStatusDtoThriftPage = innerUserInfoService.queryUserList(userInfoDTO);
        StatusDto<Page<UserInfoDTO>> userInfoStatusDto = StatusDtoThriftUtils.resolve(userInfoDTOStatusDtoThriftPage, UserInfoDTO.class);
        // List<UserInfoDTO> 转换成 List<QueryUserListDTO>
        StatusDto<List<QueryUserListDTO>> custManagerList = userInfoDTOConversionQueryUserListDTO(userInfoStatusDto.getData().getRows());
        if(!custManagerList.isSuccess()){
            return StatusDto.buildFailure(custManagerList.getMessage());
        }
        // 组装分页信息
        Page<QueryUserListDTO> page = buildCustManagerData(userInfoStatusDto, custManagerList);
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 编辑客户经理
     * @param userInfoDTO           用户的基本信息
     * @param bizServiceCustmanager 用户业务数据
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-18 10:30:22
     */
    @Override
    public StatusDto<String> editUser(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager) {
        checkedRoleCodeAndOrgCode(userInfoDTO);
        String loggedUserId = userHolder.getLoggedUserId();
        long timeMillis = System.currentTimeMillis();
        userInfoDTO.setOperateTime(timeMillis);
        userInfoDTO.setOperator(loggedUserId);
        StatusDto<String> updateUser = innerUserInfoService.updateUser(userInfoDTO);
        if(!updateUser.isSuccess()){
            return StatusDto.buildFailure("更新失败！");
        }
        bizServiceCustmanager.setOperator(loggedUserId);
        bizServiceCustmanager.setOperateTime(new Date());
        bizServiceCustmanager.setUserUuid(userInfoDTO.getUseruuid());
        bizServiceCustmanagerDao.updateBizServiceCustmanager(bizServiceCustmanager);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询用户的详情
     * @param useruuid 用户的uuid
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-19 14:47:07
     */
    @Override
    public StatusDto<CustManagerDetailDTO> custManagerDetail(String useruuid) throws IOException {
        // 查询用户的基础信息和角色信息
        StatusDto<UserInfoDTO> resolve = findUserAndRoleDetail(useruuid);
        // 类型转换 UserInfoDTO ——> CustManagerDetailDTO
        CustManagerDetailDTO custManagerDetailDTO = userInfoDTOConversionCustManagerDetailDTO(resolve);
        // 查询客户经理的信息
        findCustManagerDetail(useruuid, custManagerDetailDTO);
        return StatusDto.buildDataSuccessStatusDto(custManagerDetailDTO);
    }

    /**
     *  查询客户经理并填充信息
     * @param useruuid
     * @param custManagerDetailDTO
     * @author zhangkangjian
     * @date 2018-07-21 17:28:53
     */
    private void findCustManagerDetail(String useruuid, CustManagerDetailDTO custManagerDetailDTO) {
        BizServiceCustmanager bizServiceCustmanager = bizServiceCustmanagerDao.queryCustManagerByUuid(useruuid);
        if(bizServiceCustmanager != null){
            custManagerDetailDTO.setOfficePhone(bizServiceCustmanager.getOfficePhone());
            custManagerDetailDTO.setReceivingAddress(bizServiceCustmanager.getReceivingAddress());
        }
        int age = getAgeByBirth(new Date(custManagerDetailDTO.getBirthday() * 1000));
        custManagerDetailDTO.setAge(age);
    }

    /**
     *  类型转换 UserInfoDTO ——> CustManagerDetailDTO
     * @param resolve 用户信息
     * @exception IOException
     * @return CustManagerDetailDTO 客户经理dto
     * @author zhangkangjian
     * @date 2018-07-21 17:26:59
     */
    private CustManagerDetailDTO userInfoDTOConversionCustManagerDetailDTO(StatusDto<UserInfoDTO> resolve) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = JsonUtils.writeValue(resolve.getData());
        return mapper.readValue(json, CustManagerDetailDTO.class);
    }

    /**
     * 查询用户信息和角色信息
     * @param useruuid 用户的uuid
     * @return StatusDto<UserInfoDTO> 用户的信息
     * @author zhangkangjian
     * @date 2018-07-21 17:17:20
     */
    private StatusDto<UserInfoDTO> findUserAndRoleDetail(String useruuid) {
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(useruuid);
        StatusDto<UserInfoDTO> resolve = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class);
        List<RelUserRole> userRoles = resolve.getData().getUserRoles();
        if(userRoles != null && userRoles.size() > 0){
            List<RelUserRole> temp = userRoles.stream().filter(a -> Constants.CUSTMANAGER_ROLE_CODE.equals(a.getRoleCode())).collect(Collectors.toList());
            if( temp != null && temp.size() > 0){
                RelUserRole relUserRole = temp.get(0);
                resolve.getData().setRoleName(relUserRole.getRoleName());
            }
        }
        return resolve;
    }

    /**
     * 组装客户经理信息
     * @param resolve 用户的分页信息
     * @exception
     * @return Page<QueryUserListDTO>
     * @author zhangkangjian
     * @date 2018-07-19 11:22:26
     */
    private Page<QueryUserListDTO> buildCustManagerData(StatusDto<Page<UserInfoDTO>> resolve, StatusDto<List<QueryUserListDTO>> custManagerList) {
        // 数据组装
        List<UserInfoDTO> rows = resolve.getData().getRows();
        List<QueryUserListDTO> data = custManagerList.getData();
        List<String> useruudis = rows.stream().map(UserInfoDTO::getUseruuid).collect(Collectors.toList());
        // 查询客户经理的信息
        List<QueryUserListDTO> userList = bizServiceCustmanagerDao.queryCustManager(useruudis);
        if(data != null && data.size() > 0){
            data.stream().forEach(a ->{
                    String str = a.getUseruuid();
                    userList.stream().forEach(b ->{
                        String uuid = b.getUseruuid();
                        if(str.equals(uuid)){
                            a.setOfficePhone(b.getOfficePhone());
                        }
                    });
                }
            );
        }
        Page<QueryUserListDTO> page = new Page<QueryUserListDTO>();
        Page<UserInfoDTO> pageUser = resolve.getData();
        page.setRows(data);
        page.setLimit(pageUser.getLimit());
        page.setOffset(pageUser.getOffset());
        page.setTotal(pageUser.getTotal());
        page.setTotalPage(pageUser.getTotalPage());
        return page;
    }

    /**
     * UserInfoDTO 类型转换 QueryUserListDTO
     * @param rows 用户信息
     * @return StatusDto<List<QueryUserListDTO>>
     * @author zhangkangjian
     * @date 2018-07-18 19:45:07
     */
    private StatusDto<List<QueryUserListDTO>> userInfoDTOConversionQueryUserListDTO(List<UserInfoDTO> rows) {
        List<QueryUserListDTO> userList = null;
        if(rows != null && rows.size() > 0){
            String s = JsonUtils.writeValue(rows);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(
                List.class, QueryUserListDTO.class);
            try {
                userList = mapper.readValue(s, javaType);
            }catch (Exception e){
                e.printStackTrace();
                return StatusDto.buildFailure("查询错误");
            }
        }
        return StatusDto.buildDataSuccessStatusDto(userList);
    }
    /**
     *  根据生日计算年龄
     * @param birthday 生日
     * @return int 岁数
     * @author zhangkangjian
     * @date 2018-07-19 17:08:52
     */
    private int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);
            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            //异常后返回数据
            return 0;
        }
    }

}
