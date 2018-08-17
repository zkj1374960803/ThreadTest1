package com.ccbuluo.business.platform.outstock.service;

import com.ccbuluo.business.entity.BizOutstockOrder;
import com.ccbuluo.business.entity.BizOutstockorderDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.outstock.dto.BizOutstockOrderDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;

/**
 * 出库单service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-09 11:34:08
 */
public interface OutstockOrderService {


    /**
     * 自动出库
     * @param applyNo                    申请单号
     * @param bizOutstockplanDetailList 出库计划
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    StatusDto<String> autoSaveOutstockOrder(String applyNo, List<BizOutstockplanDetail> bizOutstockplanDetailList);

    /**
     * 保存出库单
     * @param applyNo 申请单号
     * @param outRepositoryNo 出库仓库编号
     * @param transportorderNo 物流单号
     * @param bizOutstockorderDetailList 出库单详单
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    StatusDto<String> saveOutstockOrder(String applyNo, String outRepositoryNo, String transportorderNo, List<BizOutstockorderDetail> bizOutstockorderDetailList);

    /**
     * 查询等待发货的申请单
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-11 12:53:03
     */
    List<String> queryApplyNo();

    /**
     * 根据申请单号查询出库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 出库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    List<BizOutstockplanDetail> queryOutstockplan(String applyNo, String outRepositoryNo, String productType);

    /**
     * 分页查询出库单列表
     * @param productType 商品类型
     * @param outstockType 入库类型
     * @param outstockNo 入库单号
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    Page<BizOutstockOrder> queryOutstockList(String productType, String outstockType, String outstockNo, Integer offset, Integer pagesize);

    /**
     * 根据出库单号查询出库单详情
     * @param outstockNo 出库单号
     * @return 出库单详情
     * @author liuduo
     * @date 2018-08-13 10:23:03
     */
    BizOutstockOrderDTO getByOutstockNo(String outstockNo);

    /**
     * 根据申请单号查询出库仓库
     * @param applyNo 申请单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    List<String> getByApplyNo(String applyNo);
}
