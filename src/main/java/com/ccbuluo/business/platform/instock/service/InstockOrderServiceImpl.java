package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.BizInstockOrder;
import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.inputstockplan.service.InputStockPlanService;
import com.ccbuluo.business.platform.instock.dao.BizInstockOrderDao;
import com.ccbuluo.business.platform.instock.dto.SaveBizInstockOrderDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.service.StockDetailService;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.http.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 入库单service实现
 *
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 14:06:14
 */
@Service
public class InstockOrderServiceImpl implements InstockOrderService {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private StoreHouseService storeHouseService;
    @Autowired
    private BizInstockOrderDao bizInstockOrderDao;
    @Autowired
    private InstockorderDetailService instockorderDetailService;
    @Autowired
    private GenerateDocCodeService generateDocCodeService;
    @Autowired
    private InputStockPlanService inputStockPlanService;
    @Autowired
    private StockDetailService stockDetailService;

    /**
     * 根据申请单号状态查询申请单号集合
     *
     * @param applyStatus 申请单状态
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    @Override
    public List<String> queryApplyNo(String applyStatus) {
        // todo 等待康健提交申请单service后实现    刘铎
        return null;
    }

    /**
     * 保存入库单
     *
     * @param applyNo 申请单号
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto<String> saveInstockOrder(String applyNo, List<BizInstockorderDetail> bizInstockorderDetailList) {
        try {
            // 查询入库计划
            List<BizInstockplanDetail> bizInstockplanDetails = inputStockPlanService.queryListByApplyNo(applyNo);
            // 校验是否有错误数据
            int status = checkInstockorderDetail(bizInstockorderDetailList, bizInstockplanDetails);
            if (status == Constants.FAILURESTATUS) {
                throw new CommonException("10001", "入库数量与计划不符！");
            }
            // 1、保存入库单
            // 根据申请单号查询基本信息 todo 等待康健提交后生成方法
            // 生成入库单编号
            String instockOrderno = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.R).getData();
            // 根据入库仓库编号查询入库机构编号
            String orgCodeByStoreHouseCode = storeHouseService.getOrgCodeByStoreHouseCode(bizAllocateApply.getInRepositoryNo());
            int i = saveInstockOrder(instockOrderno, "申请单的基本信息", orgCodeByStoreHouseCode);
            if (i == Constants.FAILURESTATUS) {
                throw new CommonException("10002", "生成入库单失败！");
            }
            // 2、保存入库单详单
            List<Long> ids = saveInstockorderDetail(instockOrderno, bizInstockorderDetailList);
            if (ids == null || ids.size() == 0) {
                throw new CommonException("10003", "生成入库单详单失败！");
            }
            // 3、修改库存明细
            saveStockDetail(applyNo, bizAllocateApply, bizInstockorderDetailList);
            // 4、更新入库计划明细中的实际入库数量
            updateInstockplan(bizInstockorderDetailList);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("生成入库单失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 修改入库计划中的实际入库数量
     * @param bizInstockorderDetailList 入库单详单
     * @author liuduo
     * @date 2018-08-08 20:29:20
     */
    private void updateInstockplan(List<BizInstockorderDetail> bizInstockorderDetailList) {
        List<BizInstockplanDetail> bizInstockplanDetailList = new ArrayList<>();
        bizInstockorderDetailList.forEach(item -> {
            BizInstockplanDetail bizStockDetail = new BizInstockplanDetail();
            bizStockDetail.setId(item.getInstockPlanid());
            bizStockDetail.setActualInstocknum(item.getInstockNum());
            Integer versionNo = inputStockPlanService.getVersionNoById(item.getInstockPlanid());
            bizStockDetail.setVersionNo(versionNo + Constants.FLAG_ONE);
            bizInstockplanDetailList.add(bizStockDetail);
        });
        if (!bizInstockplanDetailList.isEmpty()) {
            inputStockPlanService.updateActualInstockNum(bizInstockplanDetailList);
        }
    }

    /**
     * 校验是否有错误数据
     * @param bizInstockorderDetailList
     * @param bizInstockplanDetails
     * @return 是否有错误数据
     * @author liuduo
     * @date 2018-08-08 18:03:21
     */
    private int checkInstockorderDetail(List<BizInstockorderDetail> bizInstockorderDetailList, List<BizInstockplanDetail> bizInstockplanDetails) {
        List<BizInstockorderDetail> bizInstockorderDetailList1 = new ArrayList<>();
        // 校验是否有失败的入库详单
        Map<Long, BizInstockplanDetail> map = bizInstockplanDetails.stream().collect(Collectors.toMap(BizInstockplanDetail::getId, Function.identity()));
        bizInstockorderDetailList.forEach(item -> {
            // 判断问题件和实入数量相加是否大于入库计划的  计划入库数量=计划入库数量-实际入库数量,若大于，则抛异常
            BizInstockplanDetail bizInstockplanDetail = map.get(item.getInstockPlanid());
            if (null != item.getInstockNum() && null != item.getInstockProblemNum()) {
                // 实际入库数量
                Long actualNum = item.getInstockNum() + item.getInstockProblemNum();
                // 计划入库数量
                long planNum = bizInstockplanDetail.getPlanInstocknum() - bizInstockplanDetail.getActualInstocknum();
                if (actualNum > planNum) {
                    bizInstockorderDetailList1.add(item) ;
                }
            }
        });
        if (null != bizInstockorderDetailList1 && bizInstockorderDetailList1.size() > 0) {
            return Constants.FAILURESTATUS;
        }
        return Constants.SUCCESSSTATUS;
    }

    /**
     * 修改库存明细
     *
     * @param applyNo               申请单号
     * @param bizInstockorderDetailList 入库单详单
     * @param bizAllocateApply 申请单
     * @author liuduo
     * @date 2018-08-08 15:41:54
     */
    private void saveStockDetail(String applyNo, List<BizInstockorderDetail> bizInstockorderDetailList, BizAllocateApply bizAllocateApply) {
        // 根据入库详单的  供应商、商品、仓库、批次号  查询在库存中有无记录，有则更新，无则新增
        bizInstockorderDetailList.forEach(item -> {
            Long id = stockDetailService.getByinstockorderDeatil(item.getSupplierNo(), item.getProductNo(), bizAllocateApply.getInRepositoryNo(), applyNo);
            if (null != id) {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setId(id);
                bizStockDetail.setValidStock(item.getInstockNum());
                Integer versionNo = stockDetailService.getVersionNoById(id);
                stockDetailService.updateValidStock(bizStockDetail, versionNo);
            } else {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setRepositoryNo(bizAllocateApply.getInRepositoryNo());
                bizStockDetail.setOrgNo(bizAllocateApply.getInstockOrgno());
                bizStockDetail.setProductNo(item.getProductNo());
                bizStockDetail.setProductType(item.getProductType());
                bizStockDetail.setTradeNo(applyNo);
                bizStockDetail.setSupplierNo(item.getSupplierNo());
                bizStockDetail.setValidStock(item.getInstockNum());
                bizStockDetail.setSellerOrgno(bizAllocateApply.getOutstockOrgno());
                bizStockDetail.setCostPrice(item.getCostPrice());
                bizStockDetail.setInstockPlanid(id);
                bizStockDetail.preInsert(userHolder.getLoggedUserId());
                stockDetailService.saveStockDetail(bizStockDetail);
            }
        });
    }

    /**
     * 保存入库单详单
     *
     * @param bizInstockorderDetailList 入库单详单
     * @param instockOrderno            入库单编号
     * @return 主键id
     * @author liuduo
     * @date 2018-08-08 11:48:49
     */
    private List<Long> saveInstockorderDetail(String instockOrderno, List<BizInstockorderDetail> bizInstockorderDetailList) {
        bizInstockorderDetailList.forEach(item -> {
            BizInstockorderDetail bizInstockorderDetail = new BizInstockorderDetail();
            bizInstockorderDetail.setInstockOrderno(instockOrderno);
            bizInstockorderDetail.preInsert(userHolder.getLoggedUserId());
            bizInstockorderDetailList.add(bizInstockorderDetail);
        });
        return instockorderDetailService.save(bizInstockorderDetailList);
    }

    /**
     * 保存入库单
     *
     * @param instockOrderno          入库单号
     * @param bizAllocateApply        申请单详情
     * @param orgCodeByStoreHouseCode 入库机构code
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-08-08 10:56:05
     */
    private int saveInstockOrder(String instockOrderno, BizAllocateApply bizAllocateApply, String orgCodeByStoreHouseCode) {
        BizInstockOrder bizInstockOrder = new BizInstockOrder();
        bizInstockOrder.setInstockOrderno(instockOrderno);
        bizInstockOrder.setTradeDocno(bizAllocateApply.getApplyNo());
        bizInstockOrder.setInRepositoryNo(bizAllocateApply.getInRepositoryNo());
        bizInstockOrder.setInstockOrgno(orgCodeByStoreHouseCode);
        bizInstockOrder.setInstockOperator(userHolder.getLoggedUserId());
        bizInstockOrder.setInstockOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        if (userHolder.getLoggedUser().getOrganization().getOrgCode().equals("SC000001")) {
            bizInstockOrder.setInstockType(bizAllocateApply.getProcessType());
        } else {
            bizInstockOrder.setInstockType("调拨入库");
        }
        bizInstockOrder.setInstockTime(new Date());
        bizInstockOrder.setChecked("Complete");
        bizInstockOrder.setCheckedTime(new Date());
        bizInstockOrder.preInsert(userHolder.getLoggedUserId());
        return bizInstockOrderDao.saveEntity(bizInstockOrder);
    }

}
