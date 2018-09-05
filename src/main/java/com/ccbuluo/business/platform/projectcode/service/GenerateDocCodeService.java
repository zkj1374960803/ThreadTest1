package com.ccbuluo.business.platform.projectcode.service;

import com.ccbuluo.business.constants.BizErrorCodeEnum;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.http.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.time.LocalDateTime;

/**
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-07 20:11:59
 */
@Service
public class GenerateDocCodeService {

    Logger logger = LoggerFactory.getLogger(GenerateProjectCodeService.class);
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 根据前缀生成各种单号的编码
     * @param prefix 实体前缀
     * @return
     * @author weijb
     * @date 2018-08-06 14:41:37
     */
    public synchronized StatusDto<String> grantCodeByPrefix(DocCodePrefixEnum prefix) {
        StatusDto<String> resultDto;
        switch (prefix){
            case SW:    // 申请单号
                resultDto = getCode(prefix.toString());
                break;
            case R:    // 入库单号
                resultDto = getCode(prefix.toString());
                break;
            case C:    // 出库单号
                resultDto = getCode(prefix.toString());
                break;
            case PK:    // 盘库单号
                resultDto = getCode(prefix.toString());
                break;
            case TH:    // 退换单号
                resultDto = getCode(prefix.toString());
                break;
            case JY:    // 交易单号
                resultDto = getCode(prefix.toString());
                break;
            case DD:    // 维修订单号
                resultDto = getCode(prefix.toString());
                break;
            default:
                resultDto = StatusDto.buildStatusDtoWithCode(BizErrorCodeEnum.CODE_UNKONEPREFIX.getErrorCode(),
                        BizErrorCodeEnum.CODE_UNKONEPREFIX.getMessage());
                break;
        }
        return resultDto;
    }

    /**
     * 根据前缀生成各种单号的编码
     * @param prefix 实体前缀
     * @return
     * @author weijb
     * @date 2018-08-06 14:41:37
     */
    private StatusDto<String> getCode(String prefix){
        StatusDto<String> newCodeDto = StatusDto.buildSuccessStatusDto();
        try {
            StringBuilder newCode = new StringBuilder();//根据时间获取字段
            newCode.append(prefix).append(getTime()).append(getIncrease(prefix));
            if (StringUtils.isNotBlank(newCode)) {
                newCodeDto.setData(newCode.toString());
                return newCodeDto;
            }
        }catch (Exception exp){
            logger.error(String.format("前缀%s生成编码时异常"), exp);
            throw exp;
        }
        return newCodeDto;
    }

    // 获取年月日十分字符串
    private String getTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(localDateTime.getYear()).append(String.format("%0"+2+"d", localDateTime.getMonthValue())).append(String.format("%0"+2+"d", localDateTime.getDayOfMonth()))
                .append(String.format("%0"+2+"d", localDateTime.getHour())).append(String.format("%0"+2+"d", localDateTime.getMinute()));
        return sb.toString();
    }

    // 获取两位自增数据
    private String getIncrease(String prefix){
        String newCode = "";
        try {
            // 根据前缀从redis中获取最大code
            String redisKey = buildRedisKey(prefix);
            String redisCodeStr = jedisCluster.get(redisKey);
            if (StringUtils.isNotBlank(redisCodeStr)) {
                Long redisCode = Long.valueOf(redisCodeStr);
                redisCode++;
                newCode = produceCode(redisCode);
                // 重新放入redis，
                jedisCluster.set(redisKey, String.valueOf(redisCode));
            }else{
                // 第一次为空赋值为1
                jedisCluster.set(redisKey, "1");
            }
        }catch (Exception exp){
            logger.error(String.format("前缀%s生成编码时异常"), exp);
            throw exp;
        }
        return newCode;
    }
    // 获取后两位自增字符
    private String produceCode(Long redisCode){
        if(redisCode >= 100){
            redisCode = redisCode % 100;
        }
        return String.format("%0"+2+"d", redisCode);
    }
    private String buildRedisKey(String prefix){
        return String.format("%s:%s", BusinessPropertyHolder.PROJECTCODE_REDIS_KEYPERFIX, prefix);
    }
}
