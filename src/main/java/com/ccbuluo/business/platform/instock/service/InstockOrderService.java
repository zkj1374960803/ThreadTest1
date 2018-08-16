package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.instock.dto.BizInstockOrderDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 入库单service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:04:39
 */
public interface InstockOrderService {


    /**
     * 自动入库
     * @param applyNo 申请单编号
     * @param bizInstockplanDetails 入库计划集合
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-15 16:14:04
     */
    StatusDto<String> autoSaveInstockOrder(String applyNo, List<BizInstockplanDetail> bizInstockplanDetails);
    /**
     * 根据申请单号状态查询申请单号集合
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    List<String> queryApplyNo();

    /**
     * 保存入库单
     * @param applyNo 申请单号
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    StatusDto<String> saveInstockOrder(String applyNo, List<BizInstockorderDetail> bizInstockorderDetailList);

    /**
     * 根据申请单号查询入库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    List<BizInstockplanDetail> queryInstockplan(String applyNo, String productType);

    /**
     * 分页查询入库单列表
     * @param productType 商品类型
     * @param instockType 入库类型
     * @param instockNo 入库单号
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    Page<BizInstockOrder> queryInstockList(String productType, String instockType, String instockNo, Integer offset, Integer pagesize);

    /**
     * 根据入库单号查询入库单详情
     * @param instockNo 入库单号
     * @return 入库单详情
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    BizInstockOrderDTO getByInstockNo(String instockNo);
}
