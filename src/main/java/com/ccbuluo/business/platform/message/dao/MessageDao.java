package com.ccbuluo.business.platform.message.dao;

import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.platform.message.dto.CreateMessageDTO;
import com.ccbuluo.business.platform.message.dto.FindMessagerListDTO;
import com.ccbuluo.business.platform.message.dto.MessageUserDTO;
import com.ccbuluo.dao.BaseDao;
import com.ccbuluo.db.Page;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  消息dao
 * @author zhangkangjian
 * @date 2018-07-28 14:53:16
 */
@Repository
public class MessageDao extends BaseDao<MessageDao> {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 保存消息基础信息
     * @param createMessageDTO 消息实体
     * @return Long 消息的id
     * @author zhangkangjian
     * @date 2018-07-28 15:18:50
     */
    public Long saveServiceMessage(CreateMessageDTO createMessageDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO biz_service_message ( ")
            .append(" message_type,message_content,status, ")
            .append(" send_uuid,send_name,creator,create_time, ")
            .append(" operator,operate_time,delete_flag)  ")
            .append(" VALUES (  ")
            .append(" :messageType,:messageContent,:status, ")
            .append(" :sendUuid,:sendName,:creator,:createTime, ")
            .append(" :operator,:operateTime,:deleteFlag) ");
        return saveRid(sql.toString(), createMessageDTO);
    }

    /**
     * 批量保存用户消息
     * @param messageUserList 用户消息关联实体
     * @author zhangkangjian
     * @date 2018-07-28 15:28:15
     */
    public void saveRelUserMessage(ArrayList<MessageUserDTO> messageUserList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO rel_message_user ( ")
            .append(" manager_id,receive_uuid,receive_name,STATUS ")
            .append(" ) VALUES( ?,?,?,?) ");
        batchInsertForListBean(sql.toString(), messageUserList);
    }
    /**
     * 查询消息的列表
     * @param createMessageDTO 查询条件
     * @return List<FindMessagerListDTO> 消息列表
     * @author zhangkangjian
     * @date 2018-07-28 15:37:29
     */
    public Page<FindMessagerListDTO> findMessagerList(CreateMessageDTO createMessageDTO) {
        String sql = " select * from biz_service_message ";
        HashMap<String, Object> map = Maps.newHashMap();
        return queryPageForBean(FindMessagerListDTO.class, sql, map, createMessageDTO.getOffset(), createMessageDTO.getPageSize());
    }

    /**
     * 未读消息的数量
     * @param useruuid 用户的uuid
     * @return Long 未读的数量
     * @author zhangkangjian
     * @date 2018-07-28 15:45:33
     */
    public Long countUnreadNum(String useruuid) {
        String sql = " select count(*) from rel_message_user where useruuid = :useruuid ";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("useruuid", useruuid);
        return namedParameterJdbcTemplate.queryForObject(sql, map, Long.class);
    }

    /**
     *  查询这个用户的所有消息
     * @param useruuid 用户的uuid
     * @exception
     * @return Page<FindMessagerListDTO> 分页用户信息
     * @author zhangkangjian
     * @date 2018-07-30 09:55:08
     */
    public Page<FindMessagerListDTO> queryUserMessage(String useruuid) {
        String sql = " SELECT * FROM rel_message_user a LEFT JOIN biz_service_message b ON a.message_id = b.id WHERE a.user_uuid = :useruuid ";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("useruuid", useruuid);
        return queryPageForBean(FindMessagerListDTO.class, sql, map, 0, 10);
    }
}
