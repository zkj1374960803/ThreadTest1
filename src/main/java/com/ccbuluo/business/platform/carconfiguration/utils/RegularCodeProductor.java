package com.ccbuluo.business.platform.carconfiguration.utils;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.carmanage.dao.BasicCarcoreInfoDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * 统一根据规则生成编码
 *
 * @author liwancai
 * @version V1.0.0
 * @date 2018/5/9 17:58
 */
@Component
public class RegularCodeProductor {

    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private BasicCarcoreInfoDao basicCarcoreInfoDao;


    /**
     * 共享编码 字段名 表名
     */
    public static final String SHARED_AREA_NUMBER = "shared_area_number";
    public static final String BIZ_AHARE_AREA = "biz_share_area";

    /**
     * 品牌编码 字段名 表名
     */
    public static final String FIELD_CARBRAND_NUMBER = "carbrand_number";
    public static final String TABLE_CARBRAND_NUMBER = "basic_carbrand_manage";

    /**
     * 车系编码 字段名 表名
     */
    public static final String FIELD_CARSERIES_NUMBER = "carseries_number";
    public static final String TABLE_CARSERIES_NUMBER = "basic_carseries_manage";

    /**
     * 车型编码 字段名 表名
     */
    public static final String FIELD_CARMODEL_NUMBER = "carmodel_number";
    public static final String TABLE_CARMODEL_NUMBER = "basic_carmodel_manage";

    /**
     * 车辆编码 字段名 表名
     */
    public static final String FIELD_CAR_NUMBER = "car_number";
    public static final String TABLE_CAR_NUMBER = "basic_carcore_info";

    /**
     * 调拨单编码 字段名 表名
     */
    public static final String FIELD_RESERVATION_NUMBER = "allocation_number";
    public static final String TABLE_RESERVATION_NUMBER = "biz_requisition";

    /**
     * 保养单编码 字段名 表名
     */
    public static final String FIELD_MAINTAIN_NUMBER = "maintain_no";
    public static final String TABLE_MAINTAIN_NUMBER = "biz_carmaintain_info";
    /**
     * 保险记录编码 字段名 表名
     */
    public static final String FIELD_INSURANCE_NUMBER = "insurance_no";
    public static final String TABLE_INSURANCE_NUMBER = "biz_carinsurance_info";

    /**
     * 根据要求生成下一个编码，eg: getNextCode("P101","P",3) ----> "P102"，getNextCode(""/null,"P",3) ----> "P001"
     *
     * @param key    redis中存储的key名
     * @param prefix 编码前缀字符
     * @param length 补位长度
     * @return java.lang.String
     * @throws
     * @author liwancai
     * @date 2018-05-09 17:45:23
     */
    public synchronized String getNextCode(String key, String prefix, int length) {

        //根据key从redis获取当前最大的编码值
        String code = jedisCluster.get(key);
        int startNumber = 1;
        if (StringUtils.isNotBlank(code)) {
            startNumber = Integer.valueOf(code.replace(prefix, "")) + 1;
            jedisCluster.del(key);
        } else {
            String number = "";
            switch (prefix) {
                case Constants.CAR_BRAND_CODING:
                    number = basicCarcoreInfoDao.findNumberMax(FIELD_CARBRAND_NUMBER, TABLE_CARBRAND_NUMBER);
                    break;
                case Constants.CAR_SERIES_CODING:
                    number = basicCarcoreInfoDao.findNumberMax(FIELD_CARSERIES_NUMBER, TABLE_CARSERIES_NUMBER);
                    break;
                case Constants.CAR_CONFIGURATION_CODING:
                    number = basicCarcoreInfoDao.findNumberMax(FIELD_CARMODEL_NUMBER, TABLE_CARMODEL_NUMBER);
                    break;
                default:
            }
            if (StringUtils.isNotBlank(number)) {
                startNumber = Integer.valueOf(number.replace(prefix, "")) + 1;
                jedisCluster.del(key);
            }
        }

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(length);
        formatter.setGroupingUsed(false);
        String nextCode = prefix + formatter.format(startNumber);

        //重新放入redis
        jedisCluster.set(key, nextCode);

        return nextCode;
    }

    /**
     * 根据要求生成下一个车辆编码，如: CL+年月日+3位自增编号+1位随机码，例如：CL1805030018
     *
     * @param key    redis中存储的key名
     * @param prefix 编码前缀字符
     * @param length 自增编号长度
     * @return java.lang.String
     * @throws
     * @author wuyibo
     * @date 2018-05-10 14:33:57
     */
    public synchronized String getCarNumberNextCode(String key, String prefix, int length) {

        //根据key从redis获取当前最大的编码值
        String code = jedisCluster.get(key);
        int startNumber = 1;

        if (StringUtils.isNotBlank(code)) {
            startNumber = Integer.valueOf(code.substring(8, 12)) + 1;
            jedisCluster.del(key);
        } else {
            String number = basicCarcoreInfoDao.findNumberMax(FIELD_CAR_NUMBER, TABLE_CAR_NUMBER);
            if (StringUtils.isNotBlank(number)) {
                startNumber = Integer.valueOf(number.substring(8, 12)) + 1;
                jedisCluster.del(key);
            }
        }
        // 年月日
        SimpleDateFormat sf = new SimpleDateFormat("yyMMdd");
        String temp = sf.format(new Date());
        // 3位自增编号
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(length);
        formatter.setGroupingUsed(false);
        // 1位随机码
        Random random = new Random();
        int result = random.nextInt(10);
        // CL+年月日+3位自增编号+1位随机码，例如：CL1805030018
        String nextCode = prefix + temp + formatter.format(startNumber) + result;

        //重新放入redis
        jedisCluster.set(key, nextCode);

        return nextCode;
    }

    /**
     * 根据要求生成下一个车辆编码，如: D+年月日+6位自增编号，例如：D180503001801
     *
     * @param key    redis中存储的key名
     * @param prefix 编码前缀字符
     * @param length 自增编号长度
     * @return java.lang.String
     * @throws
     * @author chaoshuai
     * @date 2018-05-28 14:33:57
     */
    public synchronized String getRequisitionNumberNextCode(String key, String prefix, int length) {

        //根据key从redis获取当前最大的编码值
        String code = jedisCluster.get(key);
        int startNumber = 1;

        if (StringUtils.isNotBlank(code)) {
            startNumber = Integer.valueOf(code.substring(7, 11)) + 1;
            jedisCluster.del(key);
        } else {
            String number = basicCarcoreInfoDao.findNumberMax(FIELD_RESERVATION_NUMBER, TABLE_RESERVATION_NUMBER);
            if (StringUtils.isNotBlank(number)) {
                startNumber = Integer.valueOf(number.substring(7, 11)) + 1;
                jedisCluster.del(key);
            }
        }
        // 年月日
        SimpleDateFormat sf = new SimpleDateFormat("yyMMdd");
        String temp = sf.format(new Date());
        // 6位自增编号
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(length);
        formatter.setGroupingUsed(false);

        // D+年月日+6位自增编号，例如：D180503001801
        String nextCode = prefix + temp + formatter.format(startNumber);

        //重新放入redis
        jedisCluster.set(key, nextCode);

        return nextCode;
    }


    /**
     * 生成最大code
     * @param key redis的key
     * @param code redis的value
     * @return 最大code
     * @author liuduo
     * @date 2018-06-22 10:22:54
     */
    private String getMaxCode(String key, String code) {
        String substring = code.substring(0, 10);
        // 需要自增的字符串
        String substring2 = code.substring(10, code.length());
        int parkNum = Integer.parseInt(substring2);
        parkNum++;
        String format = String.format(Constants.FORMAT_NUMBER, parkNum);
        String newCode = substring + format;
        //重新放入redis
        jedisCluster.set(key, newCode);
        return newCode;
    }

}
