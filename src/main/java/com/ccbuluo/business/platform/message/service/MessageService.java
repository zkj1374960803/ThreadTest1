package com.ccbuluo.business.platform.message.service;

import com.ccbuluo.business.platform.message.dto.CreateMessageDTO;
import com.ccbuluo.business.platform.message.dto.FindMessagerListDTO;
import com.ccbuluo.db.Page;

/**
 * 消息接口
 * @author zhangkangjian
 * @date 2018-07-28 14:13:50
 */
public interface MessageService {

    /**
     *  新增消息
     * @param createMessageDTO 消息dto
     * @author zhangkangjian
     * @date 2018-07-28 14:14:28
     */
    void creatMessage(CreateMessageDTO createMessageDTO);

    /**
     *  查询消息列表
     * @param createMessageDTO 消息dto
     * @author zhangkangjian
     * @date 2018-07-28 14:14:28
     */
    Page<FindMessagerListDTO> findMessagerList(CreateMessageDTO createMessageDTO);

    /**
     * 未读消息的数量
     * @param useruuid 用户的uuid
     * @return Long 未读消息的数量
     * @author zhangkangjian
     * @date 2018-07-28 14:46:44
     */
    Long findUnreadNum(String useruuid);

    /**
     * 查询这个用户的所有消息
     * @param useruuid 用户的uuid
     * @exception
     * @return FindMessagerListDTO 消息
     * @author zhangkangjian
     * @date 2018-07-30 09:51:14
     */
    Page<FindMessagerListDTO> queryUserMessage(String useruuid);
}
