package com.ccbuluo.business.platform.projectcode.service;

import com.ccbuluo.business.constants.BizErrorCodeEnum;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.projectcode.dao.GenerateProjectCodeDao;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;
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
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;

    /**
     * 根据前缀生成相应的编码
     * @param prefix 实体前缀
     * @return
     * @author liupengfei
     * @date 2018-07-03 17:21:37
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized StatusDto<String> grantCode(CodePrefixEnum prefix)  throws TException{
        StatusDto<String> resultDto;
        switch (prefix){
            case FW:    // 服务中心
                resultDto = getCode(prefix.toString(), 5, 3,"A#B#C");
                break;
            case FC:    // 仓库
                resultDto = getCode(prefix.toString(), 6, 3,"A#B#C");
                break;
            case FG:    // 供应商
                resultDto = getCode(prefix.toString(), 6, 3,"A#B#C");
                break;
            case FK:    // 零配件分类
                resultDto = getCode(prefix.toString(), 6, 3,"A#B#C");
                break;
            case FM:    // 零配件模板
                resultDto = getCode(prefix.toString(), 5, 3,"A#B#C");
                break;
            case FP:    // 零配件
                resultDto = getCode(prefix.toString(), 6, 3,"A#B#C");
                break;
            default:
                resultDto = StatusDto.buildStatusDtoWithCode(BizErrorCodeEnum.CODE_UNKONEPREFIX.getErrorCode(),
                        BizErrorCodeEnum.CODE_UNKONEPREFIX.getMessage());
                    break;
        }
        return resultDto;
    }


    /**
     * 生成各种code
     * @param prefix 前缀
     * @param autoIncreasedcodeLength 自动增长的位数
     * @param randomlength 随机码的长度
     * @param order 编码生成的组合A:代表前缀，B:代表自增，C:代表随机码（比如：A#B#C值为：FM+00001+323）
     * @return 生成的编码
     * @author liuduo servicedev:projectcode:FW
     * @date 2018-06-29 10:51:58
     */
    @Transactional(rollbackFor = Exception.class)
     StatusDto<String> getCode(String prefix, int autoIncreasedcodeLength, int randomlength, String order) throws TException {
        try {
            StatusDto<String> newCodeDto = StatusDto.buildSuccessStatusDto();
            String newCode = "";
            // 根据前缀从redis中获取最大code
            String redisKey = buildRedisKey(prefix);
            String redisCode = jedisCluster.get(redisKey);
            if (StringUtils.isNotBlank(redisCode)) {
                newCode = produceCode(prefix, autoIncreasedcodeLength, redisCode, randomlength, order);
            }
            if (StringUtils.isNotBlank(newCode)) {
                newCodeDto.setData(newCode);
                return newCodeDto;
            }
            // redis里没有编码，或根据redis生成编码异常时，从数据库中查询
            String dbCode = generateProjectCodeDao.getMaxCode(prefix);
            if (StringUtils.isNotBlank(dbCode)) {
                newCode = produceCode(prefix, autoIncreasedcodeLength, dbCode, randomlength, order);
            }
            if (StringUtils.isNotBlank(newCode)){
                newCodeDto.setData(newCode);
                return newCodeDto;
            }

            // 该前缀的编码第一次生成，从自增部分从1开始
            produceCode(prefix, autoIncreasedcodeLength, null, randomlength, order);
            newCodeDto.setData(newCode);
            return newCodeDto;
        }
        catch (Exception exp){
            return StatusDto.buildStatusDtoWithCode(BizErrorCodeEnum.CODE_EXCEPTION.getErrorCode(),
                    BizErrorCodeEnum.CODE_EXCEPTION.getMessage());
        }
    }


    /**
     * 根据上次的自增段计数值，生成最新编码
     * @param prefix 前缀
     * @param autoIncreasedcodeSize
     * @param code 老的自增段计数值
     * @param randomlength 随机码位数
     * @return 最新的编码
     * @author liupengfei
     * @date 2018-07-03 17:08:07
     */
    @Transactional(rollbackFor = Exception.class)
    public String produceCode(String prefix, int autoIncreasedcodeSize, String code, int randomlength, String order) {
        try {
            String redisKey = buildRedisKey(prefix);
            // 需要自增的字符串
            int parkNum;
            if(StringUtils.isEmpty(code)){
                parkNum = Constants.FLAG_ONE;
            }
            else {
                parkNum = Integer.parseInt(code);
                parkNum++;
            }
            String format = String.format("%0"+autoIncreasedcodeSize+"d", parkNum);
            // 获取随机数
            String randomCode = getRandom(randomlength);
            String newCode = getNewCode(prefix,format,randomCode,order);

            // 更新数据库记录值
            generateProjectCodeDao.updateMaxCode(prefix,parkNum);
            // 数据库更新成功之后再更新重新放入redis，
            jedisCluster.set(redisKey, String.valueOf(parkNum));

            return newCode;
        }catch (Exception exp){
            logger.error(String.format("前缀%s生成编码时异常"), exp);
            throw exp;
        }
    }


    private String buildRedisKey(String prefix){
        return String.format("%s:%s", BusinessPropertyHolder.PROJECTCODE_REDIS_KEYPERFIX, prefix);
    }


    /**
     * 获取不同位数的随机码
     * @param randomlength 随机码位数
     * @author weijb
     * @date 2018-07-09 17:01:36
     */
    private String getRandom(int randomlength){
        StringBuilder randomCode = new StringBuilder();
        Random random = new Random();
        randomCode.append(random.nextInt(10)).append(random.nextInt(10)).append(random.nextInt(10));
        return randomCode.toString();
    }

    /**
     *
     * @param prefix 前缀
     * @param format 自增
     * @param randomCode 随机码
     * @param order 组合顺序
     * @return
     * @exception
     * @author weijb
     * @date 2018-07-09 17:44:17
     */
    private String getNewCode(String prefix , String format ,String randomCode,String order){
        StringBuilder newCode = new StringBuilder();
        Map<String,String> map = new HashMap<String,String>();
        map.put("A",prefix);
        map.put("B",format);
        map.put("C",randomCode);
        if(null != order){
            String[] ord = order.split("#");
            if(ord.length == 3){
                newCode.append(map.get(ord[0])).append(map.get(ord[1])).append(map.get(ord[2]));
            }
        }
        return newCode.toString();
    }

}
