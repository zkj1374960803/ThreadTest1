package com.ccbuluo.business.platform.projectcode.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.projectcode.dao.GenerateProjectCodeDao;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.thrift.proxy.ThriftProxyServiceFactory;
import com.ccbuluo.usercoreintf.BasicUserOrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(GenerateProjectCodeService.class);

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private GenerateProjectCodeDao generateProjectCodeDao;

    /**
     * 根据前缀生成相应的编码
     * @param prefix 实体前缀
     * @return
     * @author liupengfei
     * @date 2018-07-03 17:21:37
     */
    public String grantCode(CodePrefixEnum prefix)  throws TException{
        String newCode = "";
        switch (prefix){
            case FW:    // 服务中心
                newCode = getCode(prefix.toString(), 5, "org_code",
                        "basic_user_organization", true);
                break;
            case FC:    // 仓库
                newCode = getCode(prefix.toString(), 6, "storehouse_code",
                        "biz_service_storehouse", false);
                break;
            case FG:    //供应商
                newCode = getCode(prefix.toString(), 6, "supplier_code",
                        "biz_service_supplier", false);
                break;
            case FK:    //零配件分类
                break;
            case FM:    //零配件模板
                break;
            case FP:    //零配件
                break;
            case FS:    //员工
                break;
            default:
                    break;
        }
        return newCode;
    }


    /**
     * 生成各种code
     * @param prefix 前缀
     * @param autoIncreasedcodeSize 自动增长的位数
     * @param fieldName  表中对应的编码字段名
     * @param tableName  表名
     * @param isRandomCode 是否有随机码
     * @return 生成的编码
     * @author liuduo servicedev:projectcode:FW
     * @date 2018-06-29 10:51:58
     */
    private String getCode(String prefix, int autoIncreasedcodeSize, String fieldName, String tableName, Boolean isRandomCode) throws TException {
        // 根据前缀从redis中获取最大code
        String redisKey = buildRedisKey(prefix);
        String redisCode = jedisCluster.get(redisKey);
        String newCode = "";
        if (StringUtils.isNotBlank(redisCode)) {
            newCode =  produceCode(prefix, autoIncreasedcodeSize, redisCode, isRandomCode);
        }
        if(StringUtils.isNotBlank(redisCode)){
            return newCode;
        }
        // redis里没有编码，或根据redis生成编码异常时，从数据库中查询
        String dbCode = null;
        // 如果是服务中心类型的编码，需要调用内部户中心服务
        if (prefix.equals(CodePrefixEnum.FW.toString())) {
            dbCode = getServer().getMaxCode();
        } else {
            dbCode = generateProjectCodeDao.getMaxCode(fieldName, tableName);
        }
        if (StringUtils.isNotBlank(dbCode)) {
            return produceCode(prefix, autoIncreasedcodeSize, dbCode, isRandomCode);
        }
        // todo 如果第一次时，redis和数据库里都没数据则从1开始,需要判断有没有随机码
        return prefix + String.format("%0"+autoIncreasedcodeSize+"d", Constants.FLAG_ONE);
    }


    /**
     * 生成最新编码
     * @param prefix 前缀
     * @param autoIncreasedcodeSize
     * @param code
     * @param isRandomCode
     * @return 最新的编码
     * @author liupengfei
     * @date 2018-07-03 17:08:07
     */
    private String produceCode(String prefix, int autoIncreasedcodeSize, String code, Boolean isRandomCode) {
        try {
            String redisKey = buildRedisKey(prefix);
            String substring2 = null;
            // 需要自增的字符串
            if (isRandomCode) {
                substring2 = code.substring(2, code.length()-1);
            } else {
                substring2 = code.substring(2, code.length());
            }

            int parkNum = Integer.parseInt(substring2);
            parkNum++;
            String format = String.format("%0"+autoIncreasedcodeSize+"d", parkNum);
            String newCode = "";
            if (isRandomCode) {
                Random random = new Random();
                int randomCode = random.nextInt(10);
                newCode = prefix + format + randomCode;
            } else {
                newCode = prefix + format;
            }
            // 重新放入redis
            jedisCluster.set(redisKey, newCode);
            return newCode;
        }catch (Exception exp){
            logger.error(String.format("前缀%s生成编码时异常"), exp);
            return "";
        }
    }


    private String buildRedisKey(String prefix){
        return String.format("%s:%s", BusinessPropertyHolder.PROJECTCODE_REDIS_KEYPERFIX, prefix);
    }


    private BasicUserOrganizationService.Iface getServer() {
        return (BasicUserOrganizationService.Iface) ThriftProxyServiceFactory.newInstance(BasicUserOrganizationService.class, SystemPropertyHolder.getUserCoreRpcSerName());
    }


}
