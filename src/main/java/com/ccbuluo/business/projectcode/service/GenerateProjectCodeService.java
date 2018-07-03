package com.ccbuluo.business.projectcode.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.projectcode.dao.GenerateProjectCodeDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.Random;

/**
 * 生成项目所用的编码
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 09:10:40
 */
@Service
public class GenerateProjectCodeService {

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private GenerateProjectCodeDao generateProjectCodeDao;

    /**
     * 生成各种code
     * @param prefix 前缀
     * @param autoIncreasedcodeSize 自动增长的位数
     * @param fieldName  表中对应的编码字段名
     * @param tableName  表名
     * @param IsrandomCode 是否有随机码
     * @return 生成的编码
     * @author liuduo
     * @date 2018-06-29 10:51:58
     */
    public String getCode(String prefix, int autoIncreasedcodeSize, String fieldName, String tableName, Boolean IsrandomCode) {
        // 根据前缀从redis中获取最大code
        String redisCode = jedisCluster.get(prefix);
        if (StringUtils.isNotBlank(redisCode)) {
            jedisCluster.del(prefix);
            return produceCode(prefix, autoIncreasedcodeSize, redisCode, IsrandomCode);
        }
        // redis里没有编码，从数据库中查询
        String dbCode = generateProjectCodeDao.getMaxCode(fieldName, tableName);
        if (StringUtils.isNotBlank(dbCode)) {
            jedisCluster.del(prefix);
            return produceCode(prefix, autoIncreasedcodeSize, dbCode, IsrandomCode);
        }
        int i = autoIncreasedcodeSize - 1;
        return prefix + String.format("%0"+i+"d", Constants.FLAG_ONE);
    }


    private String produceCode(String prefix, int autoIncreasedcodeSize, String code, Boolean IsrandomCode) {
        String substring = code.substring(0, 2);
        // 需要自增的字符串
        String substring2 = code.substring(2, code.length());
        int parkNum = Integer.parseInt(substring2);
        parkNum++;
        String format = String.format("%0"+autoIncreasedcodeSize+"d", parkNum);
        String newCode = "";
        if (IsrandomCode) {
            Random random = new Random();
            int randomCode = random.nextInt(10);
            newCode = substring + format + randomCode;
        } else {
            newCode = substring + format;
        }
        //重新放入redis
        jedisCluster.set(prefix, newCode);
        return newCode;
    }
}
