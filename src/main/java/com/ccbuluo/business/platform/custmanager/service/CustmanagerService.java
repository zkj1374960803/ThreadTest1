package com.ccbuluo.business.platform.custmanager.service;

import com.ccbuluo.business.platform.custmanager.dto.QueryUserListDTO;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

/**
 * 客户经理
 * @author zhangkangjian
 * @date 2018-07-18 10:24:12
 */
public interface CustmanagerService {
    /**
     * 创建客户经理
     * @param userInfoDTO 用户的基本信息
     * @param bizServiceCustmanager 用户业务数据
     * @return
     * @author zhangkangjian
     * @date 2018-07-18 10:30:22
     */
    StatusDto<String> createUser(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager);
    /**
     * 查询用户列表
     * @param userInfoDTO 查询条件
     * @return StatusDtoThriftList
     * @author zhangkangjian
     * @date 2018-07-18 16:55:03
     */
    StatusDto<Page<QueryUserListDTO>> queryUserList(UserInfoDTO userInfoDTO);
    /**
     * 编辑客户经理
     * @param userInfoDTO 用户的基本信息
     * @param bizServiceCustmanager 用户业务数据
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-18 10:30:22
     */
    StatusDto<String> editUser(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager);
    /**
     * 查询用户的详情
     * @param useruuid 用户的uuid
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-19 14:47:07
     */
    StatusDto detailUser(String useruuid) throws IOException;
}
