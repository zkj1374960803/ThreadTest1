package com.ccbuluo.business.platform.message.service;

import com.ccbuluo.business.platform.message.dao.MessageDao;
import com.ccbuluo.business.platform.message.dto.CreateMessageDTO;
import com.ccbuluo.business.platform.message.dto.FindMessagerListDTO;
import com.ccbuluo.business.platform.message.dto.MessageUserDTO;
import com.ccbuluo.db.Page;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息实现
 * @author zhangkangjian
 * @date 2018-07-28 14:45:20
 */
@Service
public class MessageServiceImpl implements MessageService{
    @Resource
    private MessageDao messageDao;

    /**
     * 新增消息
     *
     * @param createMessageDTO 消息dto
     * @author zhangkangjian
     * @date 2018-07-28 14:14:28
     */
    @Override
    public void creatMessage(CreateMessageDTO createMessageDTO) {
        // 插入消息基础表
        Long id = messageDao.saveServiceMessage(createMessageDTO);
        // 插入消息关联表
        List<String> receiveName = createMessageDTO.getReceiveName();
        List<String> receiveUuid = createMessageDTO.getReceiveUuid();
        ArrayList<MessageUserDTO> messageUserDTOArrayList = Lists.newArrayList();
        if(receiveName != null && receiveUuid != null){
            if(receiveName.size() == receiveUuid.size()){
                for (int i = 0; i < receiveName.size(); i++) {
                    MessageUserDTO messageUserDTO = new MessageUserDTO();
                    messageUserDTO.setMessageId(id);
                    messageUserDTO.setReceiveName(receiveName.get(i));
                    messageUserDTO.setReceiveUuid(receiveUuid.get(i));
                    messageUserDTOArrayList.add(messageUserDTO);
                }
            }
        }
        messageDao.saveRelUserMessage(messageUserDTOArrayList);

    }

    /**
     * 查询消息列表
     *
     * @param createMessageDTO 消息dto
     * @author zhangkangjian
     * @date 2018-07-28 14:14:28
     */
    @Override
    public Page<FindMessagerListDTO> findMessagerList(CreateMessageDTO createMessageDTO) {
        return messageDao.findMessagerList(createMessageDTO);
    }

    /**
     * 未读消息的数量
     *
     * @param useruuid 用户的uuid
     * @return Long 未读消息的数量
     * @author zhangkangjian
     * @date 2018-07-28 14:46:44
     */
    @Override
    public Long findUnreadNum(String useruuid) {
        return messageDao.countUnreadNum(useruuid);
    }

    /**
     * 查询这个用户的所有消息
     *
     * @param useruuid 用户的uuid
     * @return FindMessagerListDTO 消息
     * @throws
     * @author zhangkangjian
     * @date 2018-07-30 09:51:14
     */
    @Override
    public Page<FindMessagerListDTO> queryUserMessage(String useruuid) {
        return messageDao.queryUserMessage(useruuid);
    }
}
