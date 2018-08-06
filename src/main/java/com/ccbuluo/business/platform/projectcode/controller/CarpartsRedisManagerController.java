package com.ccbuluo.business.platform.projectcode.controller;

import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;
import java.util.ArrayList;
import java.util.List;

/**
 * redis缓存管理
 *
 * @author wuyibo
 * @date 2018-07-11 15:52:33
 */
@Api(tags = "redis缓存管理")
@RestController
@RequestMapping("/afterSales/redisManager")
public class CarpartsRedisManagerController {
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 缓存列表
     * @author weijb
     * @date 2018-07-11 15:52:33
     */
    @ApiOperation(value = "缓存列表",notes = "【魏俊标】")
    @GetMapping("/redisList")
    public StatusDto<List<String>> redisList() throws TException {
        List<String> list = new ArrayList<String>();
        list.add("服务中心key=servicedev:projectcode:FW,value="+jedisCluster.get("servicedev:projectcode:FW"));
        list.add("仓库key=servicedev:projectcode:FC,value="+jedisCluster.get("servicedev:projectcode:FC"));
        list.add("供应商key=servicedev:projectcode:FG,value="+jedisCluster.get("servicedev:projectcode:FG"));
        list.add("零配件分类key=servicedev:projectcode:FK,value="+jedisCluster.get("servicedev:projectcode:FK"));
        list.add("零配件模板key=servicedev:projectcode:FM,value="+jedisCluster.get("servicedev:projectcode:FM"));
        list.add("零配件key=servicedev:projectcode:FP,value="+jedisCluster.get("servicedev:projectcode:FP"));
        return StatusDtoThriftUtils.resolve(StatusDtoThriftUtils.buildSuccess(list), String.class);
    }

    /**
     * 删除缓存
     * @param key key
     * @return  操作是否成功
     * @author weijb
     * @date 2018-07-11 15:52:33
     */
    @ApiOperation(value = "删除缓存", notes = "【魏俊标】")
    @ApiImplicitParam(name = "key", value = "key", required = true, dataType = "String", paramType = "path")
    @GetMapping("/deleteRedis/{key}")
    public StatusDto deleteCarpartsParameter(@PathVariable String key) {
        jedisCluster.del(key);
        return StatusDto.buildSuccessStatusDto();
    }

}
