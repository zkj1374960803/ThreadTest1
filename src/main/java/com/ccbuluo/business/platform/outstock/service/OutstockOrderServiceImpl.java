package com.ccbuluo.business.platform.outstock.service;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.BizAllocateApply.ApplyStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleContext;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.service.ServiceOrderService;
import com.ccbuluo.business.platform.outstock.dao.BizOutstockOrderDao;
import com.ccbuluo.business.platform.outstock.dto.BizOutstockOrderDTO;
import com.ccbuluo.business.platform.outstock.dto.OutstockorderDetailDTO;
import com.ccbuluo.business.platform.outstock.dto.UpdatePlanStatusDTO;
import com.ccbuluo.business.platform.outstockplan.service.OutStockPlanService;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.dto.UpdateStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.service.StockDetailService;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.QueryNameByUseruuidsDTO;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 出库单service实现
 *
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-09 11:34:41
 */
@Service
public class OutstockOrderServiceImpl implements OutstockOrderService {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private GenerateDocCodeService generateDocCodeService;
    @Autowired
    private AllocateApplyService allocateApplyService;
    @Autowired
    private OutStockPlanService outStockPlanService;
    @Autowired
    private BizOutstockOrderDao bizOutstockOrderDao;
    @Autowired
    private OutstockorderDetailService outstockorderDetailService;
    @Autowired
    private StockDetailService stockDetailService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @Autowired
    private StoreHouseService storeHouseService;
    @Autowired
    private ApplyHandleContext applyHandleContext;
    @Autowired
    private BizServiceOrderDao bizServiceOrderDao;

    /**
     * 自动保存出库单
     *
     * @param applyNo                   申请单号
     * @param bizOutstockplanDetailList 出库计划
     * @param applyNoType 申请单类型
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto<String> autoSaveOutstockOrder(String applyNo, List<BizOutstockplanDetail> bizOutstockplanDetailList, String applyNoType) {
        try {
            Map<String, List<BizOutstockplanDetail>> collect = bizOutstockplanDetailList.stream().collect(Collectors.groupingBy(BizOutstockplanDetail::getOutRepositoryNo));
            List<BizOutstockorderDetail> bizOutstockorderDetailList = Lists.newArrayList();
            Date date = new Date();
            for (String outRepositoryNo : collect.keySet()) {
                List<BizOutstockplanDetail> bizOutstockplanDetails = collect.get(outRepositoryNo);
                BizOutstockplanDetail bizOutstockplanDetail = bizOutstockplanDetails.get(0);
                // 生成出库单号
                String outstockNo = null;
                StatusDto<String> outstockCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.C);
                if (outstockCode.getCode().equals(Constants.SUCCESS_CODE)) {
                    outstockNo = outstockCode.getData();
                } else {
                    return StatusDto.buildFailure("生成出库单编码失败！");
                }
                BizOutstockOrder bizOutstockOrder = new BizOutstockOrder();
                bizOutstockOrder.setOutstockorderNo(outstockNo);
                bizOutstockOrder.setOutRepositoryNo(outRepositoryNo);
                bizOutstockOrder.setOutstockOrgno(bizOutstockplanDetail.getOutOrgno());
                bizOutstockOrder.setOutstockOperator(userHolder.getLoggedUserId());
                bizOutstockOrder.setTradeDocno(applyNo);
                bizOutstockOrder.setOutstockType(bizOutstockplanDetail.getOutstockType());
                bizOutstockOrder.setOutstockTime(date);
                bizOutstockOrder.setChecked(Constants.LONG_FLAG_ONE);
                bizOutstockOrder.setCheckedTime(date);
                bizOutstockOrder.preInsert(userHolder.getLoggedUserId());
                int i = bizOutstockOrderDao.saveEntity(bizOutstockOrder);
                if (i == Constants.FAILURESTATUS) {
                    throw new CommonException("2001", "保存出库单失败！");
                }
                for (BizOutstockplanDetail outstockplanDetail : bizOutstockplanDetails) {
                    BizOutstockorderDetail bizOutstockorderDetail = new BizOutstockorderDetail();
                    bizOutstockorderDetail.setOutstockOrderno(outstockNo);
                    bizOutstockorderDetail.setOutstockPlanid(outstockplanDetail.getId());
                    bizOutstockorderDetail.setProductNo(outstockplanDetail.getProductNo());
                    bizOutstockorderDetail.setProductName(outstockplanDetail.getProductName());
                    bizOutstockorderDetail.setProductType(outstockplanDetail.getProductType());
                    bizOutstockorderDetail.setProductCategoryname(outstockplanDetail.getProductCategoryname());
                    bizOutstockorderDetail.setSupplierNo(outstockplanDetail.getSupplierNo());
                    bizOutstockorderDetail.setOutstockNum(outstockplanDetail.getPlanOutstocknum());
                    bizOutstockorderDetail.setStockType(outstockplanDetail.getStockType());
                    bizOutstockorderDetail.setUnit(outstockplanDetail.getProductUnit());
                    bizOutstockorderDetail.setCostPrice(outstockplanDetail.getCostPrice());
                    bizOutstockorderDetail.setActualPrice(outstockplanDetail.getSalesPrice());
                    bizOutstockorderDetail.preInsert(userHolder.getLoggedUserId());
                    bizOutstockorderDetailList.add(bizOutstockorderDetail);
                }
                List<Long> longs = outstockorderDetailService.batchBizOutstockOrderDetail(bizOutstockorderDetailList);
                if (null != longs && longs.size() == 0) {
                    throw new CommonException("2002", "保存出库单详单失败！");
                }
                // 更改库存
                // 根据出库单号查询出库单详单
                List<BizOutstockorderDetail> bizOutstockorderDetailList1 = outstockorderDetailService.queryByApplyNo(outstockNo);
                updateOccupyStock(bizOutstockorderDetailList1, bizOutstockplanDetailList);
                // 4、更新出库计划中的实际出库数量
                updateActualOutstocknum(bizOutstockorderDetailList1);
                // 5、更新出库计划中的状态和完成时间
                List<BizOutstockplanDetail> bizOutstockplanDetailList2 = outStockPlanService.queryOutstockplan(applyNo, StockPlanStatusEnum.DOING.toString(), outRepositoryNo);
                updatePlanStatus(bizOutstockplanDetailList2);
                // 6、更改申请单状态
//                if (applyNoType.equals(ApplyTypeEnum.APPLYORDER.name())) {
//                    List<BizOutstockplanDetail> bizOutstockplanDetailList3 = outStockPlanService.queryOutstockplan(applyNo, StockPlanStatusEnum.DOING.toString(), outRepositoryNo);
//                    updateApplyOrderStatus(applyNo, bizOutstockplanDetailList3);
//                }
            }
            return StatusDto.buildSuccessStatusDto("保存成功！");
        } catch (Exception e) {
            logger.error("生成出库单失败！", e.getMessage());
            throw e;
        }

    }


    /**
     * 保存出库单
     *
     * @param applyNo                    申请单号
     * @param transportorderNo           物流单号
     * @param bizOutstockorderDetailList 出库单详单
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-07 15:15:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto<String> saveOutstockOrder(String applyNo, String outRepositoryNo, String transportorderNo, List<BizOutstockorderDetail> bizOutstockorderDetailList) {
        try {
            // 生成出库单号
            String outstockNo = null;
            StatusDto<String> outstockCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.C);
            if (outstockCode.getCode().equals(Constants.SUCCESS_CODE)) {
                outstockNo = outstockCode.getData();
            } else {
                return StatusDto.buildFailure("生成出库单编码失败！");
            }
            // 1、保存出库单
            // 根据申请单号查询基本信息
            FindAllocateApplyDTO detail = allocateApplyService.findDetail(applyNo);
            // 查询出库计划
            List<BizOutstockplanDetail> bizOutstockplanDetailList = outStockPlanService.queryOutstockplan(applyNo, StockPlanStatusEnum.DOING.toString(), outRepositoryNo);
            int outstockorderStatus = saveOutstockOrder(outstockNo, applyNo, transportorderNo, bizOutstockplanDetailList.get(0).getOutstockType(), outRepositoryNo);
            if (outstockorderStatus == Constants.FAILURESTATUS) {
                throw new CommonException("2001", "生成出库单失败！");
            }
            // 2、保存出库单详单
            List<Long> longs = saveOutstockorderDetail(bizOutstockorderDetailList, outstockNo, bizOutstockplanDetailList);
            if (null != longs && longs.size() == 0) {
                throw new CommonException("2002", "保存出库单详单失败！");
            }
            // 复核库存和计划
            checkStockAndPlan(applyNo, outRepositoryNo, outstockNo, bizOutstockplanDetailList);
            return StatusDto.buildSuccessStatusDto("保存成功！");
        } catch (Exception e) {
            logger.error("生成出库单失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 复核库存和计划
     *
     * @param applyNo                   申请单号
     * @param outRepositoryNo           出库仓库编号
     * @param outstockNo                出库单编号
     * @param bizOutstockplanDetailList 出库计划集合
     * @author liuduo
     * @date 2018-08-20 13:57:35
     */
    private void checkStockAndPlan(String applyNo, String outRepositoryNo, String outstockNo, List<BizOutstockplanDetail> bizOutstockplanDetailList) {
        // 3、修改库存明细
        // 根据出库单号查询出库单详单
        List<BizOutstockorderDetail> bizOutstockorderDetailList1 = outstockorderDetailService.queryByApplyNo(outstockNo);
        updateOccupyStock(bizOutstockorderDetailList1, bizOutstockplanDetailList);
        // 4、更新出库计划中的实际出库数量
        updateActualOutstocknum(bizOutstockorderDetailList1);
        // 5、更新出库计划中的状态和完成时间
        List<BizOutstockplanDetail> bizOutstockplanDetailList2 = outStockPlanService.queryOutstockplan(applyNo, StockPlanStatusEnum.DOING.toString(), outRepositoryNo);
        updatePlanStatus(bizOutstockplanDetailList2);
        // 6、更改申请单状态
//        List<BizOutstockplanDetail> bizOutstockplanDetailList3 = outStockPlanService.queryOutstockplan(applyNo, StockPlanStatusEnum.DOING.toString(), outRepositoryNo);
//        updateApplyOrderStatus(applyNo, bizOutstockplanDetailList3);
        // 7、更新出库单的复核状态
        bizOutstockOrderDao.updateChecked(outstockNo, Constants.FLAG_ONE, new Date());
    }


    /**
     * 更改申请单状态
     *
     * @param applyNo                   申请单号
     * @param detail                    申请单
     * @param bizOutstockplanDetailList 出库计划
     * @author liuduo
     * @date 2018-08-11 13:05:07
     */
    private void updateApplyOrderStatus(String applyNo, FindAllocateApplyDTO detail, List<BizOutstockplanDetail> bizOutstockplanDetailList) {
        List<BizOutstockplanDetail> collect = bizOutstockplanDetailList.stream().filter(item -> item.getPlanStatus().equals(StockPlanStatusEnum.COMPLETE.name())).collect(Collectors.toList());
        String applyType = detail.getApplyType();
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        if (StringUtils.isNotBlank(applyType)) {
            // 判断本次交易的出库计划是否全部完成
            if (bizOutstockplanDetailList.size() == collect.size()) {
                switch (Enum.valueOf(BizAllocateApply.AllocateApplyTypeEnum.class, applyType)) {
                    case PLATFORMALLOCATE:
                    case PURCHASE:
                        allocateApplyService.updateApplyOrderStatus(applyNo, ApplyStatusEnum.WAITINGRECEIPT.toString());
                        break;
                    case DIRECTALLOCATE:
                    case SAMELEVEL:
                        allocateApplyService.updateApplyOrderStatus(applyNo, ApplyStatusEnum.WAITINGRECEIPT.toString());
                        break;
                    case BARTER:
                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REPLACEWAITIN.toString());
                        } else {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PRODRETURNED.toString());
                        }
                        break;
                    case REFUND:
                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REFUNDCOMPLETED.toString());
                        } else {
                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PRODRETURNED.toString());
                        }
                        break;
                }
            }

        }

    }

    /**
     * 查询等待发货的申请单
     * @param productType 商品类型
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-11 12:53:03
     */
    @Override
    public List<String> queryApplyNo(String productType) {
        return allocateApplyService.queryOutStockApplyNo(productType, userHolder.getLoggedUser().getOrganization().getOrgCode(), StockPlanStatusEnum.DOING.toString());
    }

    /**
     * 根据申请单号查询出库计划
     *
     * @param applyNo     申请单号
     * @param productType 商品类型
     * @return 出库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    @Override
    public List<BizOutstockplanDetail> queryOutstockplan(String applyNo, String outRepositoryNo, String productType) {
        return outStockPlanService.queryOutstockplanList(applyNo, outRepositoryNo, productType);
    }

    /**
     * 分页查询出库单列表
     *
     * @param productType  商品类型
     * @param outstockType 入库类型
     * @param outstockNo   入库单号
     * @param offset       起始数
     * @param pagesize     每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    @Override
    public Page<BizOutstockOrder> queryOutstockList(String productType, String outstockType, String outstockNo, Integer offset, Integer pagesize) {
        // 查询当前登录人的机构下的入库单
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        Page<BizOutstockOrder> bizOutstockOrderPage = bizOutstockOrderDao.queryOutstockList(productType, outstockType, outstockNo, orgCode, offset, pagesize);
        List<BizOutstockOrder> rows = bizOutstockOrderPage.getRows();
        List<String> outstockOperator = rows.stream().map(BizOutstockOrder::getOutstockOperator).collect(Collectors.toList());
        if (outstockOperator.size() == 0) {
            return new Page<>();
        }
        StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuidsDTOStatusDtoThriftList = innerUserInfoService.queryNameByUseruuids(outstockOperator);
        StatusDto<List<QueryNameByUseruuidsDTO>> resolve = StatusDtoThriftUtils.resolve(queryNameByUseruuidsDTOStatusDtoThriftList, QueryNameByUseruuidsDTO.class);
        List<QueryNameByUseruuidsDTO> userNames = resolve.getData();
        Map<String, String> collect = userNames.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, QueryNameByUseruuidsDTO::getName));
        rows.forEach(item -> {
            item.setOutstockOperatorName(collect.get(item.getOutstockOperator()));
        });

        return bizOutstockOrderPage;
    }

    /**
     * 根据出库单号查询出库单详情
     *
     * @param outstockNo 出库单号
     * @return 出库单详情
     * @author liuduo
     * @date 2018-08-13 10:23:03
     */
    @Override
    public BizOutstockOrderDTO getByOutstockNo(String outstockNo) {
        // 先查询出库单详情
        BizOutstockOrderDTO bizOutstockOrderDTO = bizOutstockOrderDao.getByOutstockNo(outstockNo);
        // 封装出库人的名字
        StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuidsDTOStatusDtoThriftList = innerUserInfoService.queryNameByUseruuids(Lists.newArrayList(bizOutstockOrderDTO.getOutstockOperator()));
        StatusDto<List<QueryNameByUseruuidsDTO>> resolve = StatusDtoThriftUtils.resolve(queryNameByUseruuidsDTOStatusDtoThriftList, QueryNameByUseruuidsDTO.class);
        List<QueryNameByUseruuidsDTO> userNames = resolve.getData();
        Map<String, String> collect = userNames.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, QueryNameByUseruuidsDTO::getName));
        bizOutstockOrderDTO.setOutstockOperatorName(collect.get(bizOutstockOrderDTO.getOutstockOperator()));
        // 查询出库单详单
        List<OutstockorderDetailDTO> outstockorderDetailDTOList = outstockorderDetailService.queryListByOutstockNo(outstockNo);
        // 封装出库仓库
        List<String> collect1 = outstockorderDetailDTOList.stream().map(OutstockorderDetailDTO::getOutRepositoryNo).collect(Collectors.toList());
        List<QueryStorehouseDTO> bizServiceStorehouseList = storeHouseService.queryByCode(collect1);
        Map<String, String> storeHouse = bizServiceStorehouseList.stream().collect(Collectors.toMap(QueryStorehouseDTO::getStorehouseCode, QueryStorehouseDTO::getStorehouseName));
        outstockorderDetailDTOList.forEach(item -> {
            item.setOutRepositoryName(storeHouse.get(item.getOutRepositoryNo()));
            item.setCostTotalPrice(item.getCostPrice().multiply(new BigDecimal(item.getOutstockNum())));
            item.setActualTotalPrice(item.getActualPrice().multiply(new BigDecimal(item.getOutstockNum())));
        });
        BigDecimal costTotalPrice = outstockorderDetailDTOList.stream().map(OutstockorderDetailDTO::getCostTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal actualTotalPrice = outstockorderDetailDTOList.stream().map(OutstockorderDetailDTO::getActualTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        bizOutstockOrderDTO.setCostTotalPrice(costTotalPrice);
        bizOutstockOrderDTO.setActualTotalPrice(actualTotalPrice);
        bizOutstockOrderDTO.setOutstockorderDetailDTOList(outstockorderDetailDTOList);

        return bizOutstockOrderDTO;
    }

    /**
     * 根据申请单号查询出库仓库
     *
     * @param applyNo 申请单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    @Override
    public List<String> getByApplyNo(String applyNo) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        return outStockPlanService.getByApplyNo(applyNo, orgCode);
    }

    /**
     * 更新出库计划中的状态和完成时间
     *
     * @param bizOutstockplanDetailList 出库计划
     * @author liuduo
     * @date 2018-08-10 17:46:26
     */
    private void updatePlanStatus(List<BizOutstockplanDetail> bizOutstockplanDetailList) {
        List<BizOutstockplanDetail> bizOutstockplanDetails = Lists.newArrayList();
        List<Long> ids = bizOutstockplanDetailList.stream().map(BizOutstockplanDetail::getId).collect(Collectors.toList());
        List<UpdatePlanStatusDTO> versionNoById = outStockPlanService.getVersionNoById(ids);
        Map<Long, UpdatePlanStatusDTO> collect = versionNoById.stream().collect(Collectors.toMap(UpdatePlanStatusDTO::getId, Function.identity()));
        bizOutstockplanDetailList.forEach(item -> {
            if (item.getActualOutstocknum() >= item.getPlanOutstocknum()) {
                UpdatePlanStatusDTO updatePlanStatusDTO = collect.get(item.getId());
                Long versionNo = updatePlanStatusDTO.getVersionNo() + Constants.LONG_FLAG_ONE;
                BizOutstockplanDetail bizOutstockplanDetail = new BizOutstockplanDetail();
                bizOutstockplanDetail.setId(item.getId());
                bizOutstockplanDetail.setPlanStatus(StockPlanStatusEnum.COMPLETE.name());
                bizOutstockplanDetail.setCompleteTime(new Date());
                bizOutstockplanDetail.setVersionNo(versionNo);
                bizOutstockplanDetail.preUpdate(userHolder.getLoggedUserId());
                bizOutstockplanDetails.add(bizOutstockplanDetail);
            }
        });
        outStockPlanService.updatePlanStatus(bizOutstockplanDetails);
    }

    /**
     * 更改出库计划的实际出库数量
     *
     * @param bizOutstockorderDetailList1 出库单详单
     * @author liuduo
     * @date 2018-08-10 16:48:48
     */
    private void updateActualOutstocknum(List<BizOutstockorderDetail> bizOutstockorderDetailList1) {
        List<BizOutstockplanDetail> bizOutstockplanDetails = Lists.newArrayList();
        List<Long> ids = bizOutstockorderDetailList1.stream().map(BizOutstockorderDetail::getOutstockPlanid).collect(Collectors.toList());
        List<UpdatePlanStatusDTO> versionNoById = outStockPlanService.getVersionNoById(ids);
        Map<Long, UpdatePlanStatusDTO> collect = versionNoById.stream().collect(Collectors.toMap(UpdatePlanStatusDTO::getId, Function.identity()));
        bizOutstockorderDetailList1.forEach(item -> {
            UpdatePlanStatusDTO updatePlanStatusDTO = collect.get(item.getOutstockPlanid());
            Long versionNo = updatePlanStatusDTO.getVersionNo() + Constants.LONG_FLAG_ONE;
            BizOutstockplanDetail bizOutstockplanDetail = new BizOutstockplanDetail();
            bizOutstockplanDetail.setId(item.getOutstockPlanid());
            bizOutstockplanDetail.setActualOutstocknum(item.getOutstockNum());
            bizOutstockplanDetail.setVersionNo(versionNo);
            bizOutstockplanDetail.preUpdate(userHolder.getLoggedUserId());
            bizOutstockplanDetails.add(bizOutstockplanDetail);
        });
        outStockPlanService.updateActualOutstocknum(bizOutstockplanDetails);
    }

    /**
     * 更改占用库存
     *
     * @param bizOutstockplanDetailList  出库计划
     * @param bizOutstockorderDetailList 出库单详单
     * @author liuduo
     * @date 2018-08-09 19:25:36
     */
    private void updateOccupyStock(List<BizOutstockorderDetail> bizOutstockorderDetailList, List<BizOutstockplanDetail> bizOutstockplanDetailList) {
        List<BizStockDetail> bizStockDetails = new ArrayList<>();
        // 获取出库计划的id和出库计划
        Map<Long, BizOutstockplanDetail> collect = bizOutstockplanDetailList.stream().collect(Collectors.toMap(BizOutstockplanDetail::getId, Function.identity()));
        // 获取到所有的库存明细id
        List<Long> collect1 = bizOutstockplanDetailList.stream().map(item -> item.getStockId()).collect(Collectors.toList());
        // 根据库存明细id查询所有库存明细的占用库存
        List<UpdateStockBizStockDetailDTO> updateStockBizStockDetailDTOList = stockDetailService.getOutstockDetail(collect1);
        // 库存明细
        Map<Long, UpdateStockBizStockDetailDTO> collect2 = updateStockBizStockDetailDTOList.stream().collect(Collectors.toMap(UpdateStockBizStockDetailDTO::getId, Function.identity()));
        bizOutstockorderDetailList.forEach(item -> {
            BizOutstockplanDetail bizOutstockplanDetail = collect.get(item.getOutstockPlanid());
            // 如果根据出库单中的出库计划id取到出库计划，那么则说明可以更改库存明细中的占用库存
            if (bizOutstockplanDetail != null) {
                UpdateStockBizStockDetailDTO updateStockBizStockDetailDTO = collect2.get(bizOutstockplanDetail.getStockId());
                long versionNo = updateStockBizStockDetailDTO.getVersionNo() + Constants.LONG_FLAG_ONE;
                // 拿出出库数量，与库存明细进行计算，并更新出库单的实际出库数量
                Long outstockNum = item.getOutstockNum();// 出库单上的出库数量
                Long occupyStock;
                if (updateStockBizStockDetailDTO != null) {
                    BizStockDetail bizStockDetail = new BizStockDetail();
                    if (item.getStockType().equals(BizStockDetail.StockTypeEnum.VALIDSTOCK.toString())) {
                        bizStockDetail.setId(updateStockBizStockDetailDTO.getId());
                        bizStockDetail.setOccupyStock(outstockNum);
                        bizStockDetail.setVersionNo(versionNo);
                        bizStockDetail.preUpdate(userHolder.getLoggedUserId());
                        bizStockDetails.add(bizStockDetail);
                    } else if (item.getStockType().equals(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.toString())) {
                        bizStockDetail.setId(updateStockBizStockDetailDTO.getId());
                        bizStockDetail.setProblemStock(outstockNum);
                        bizStockDetail.setVersionNo(versionNo);
                        bizStockDetail.preUpdate(userHolder.getLoggedUserId());
                        bizStockDetails.add(bizStockDetail);
                    }
                }

            }
        });
        // 更新库存明细中的占用库存
        stockDetailService.updateOccupyStock(bizStockDetails);
    }

    /**
     * 保存出库单详单
     *
     * @param bizOutstockorderDetailList 出库单详单
     * @param outstockNo                 出库单编号
     * @author liuduo
     * @date 2018-08-09 16:05:29
     */
    private List<Long> saveOutstockorderDetail(List<BizOutstockorderDetail> bizOutstockorderDetailList, String outstockNo, List<BizOutstockplanDetail> bizOutstockplanDetailList) {
        List<BizOutstockorderDetail> bizOutstockorderDetailList1 = new ArrayList<>();
        Map<Long, BizOutstockplanDetail> collect = bizOutstockplanDetailList.stream().collect(Collectors.toMap(BizOutstockplanDetail::getId, Function.identity()));
        bizOutstockorderDetailList.forEach(item -> {
            BizOutstockplanDetail bizOutstockplanDetail = collect.get(item.getOutstockPlanid());
            item.setCostPrice(bizOutstockplanDetail.getCostPrice());
            item.setActualPrice(bizOutstockplanDetail.getSalesPrice());
            item.setOutstockOrderno(outstockNo);
            item.preInsert(userHolder.getLoggedUserId());
            bizOutstockorderDetailList1.add(item);
        });
        return outstockorderDetailService.saveOutstockorderDetail(bizOutstockorderDetailList1);
    }

    /**
     * 保存出库单
     *
     * @param applyNo          申请单号
     * @param transportorderNo 物流号
     * @param outStockType           申请单类型
     * @param outRepositoryNo           出库仓库
     * @return
     * @author liuduo
     * @date 2018-08-09 14:57:11
     */
    private int saveOutstockOrder(String outstockNo, String applyNo, String transportorderNo, String outStockType, String outRepositoryNo) {
        BizOutstockOrder bizOutstockOrder = new BizOutstockOrder();
        Date date = new Date();
        bizOutstockOrder.setOutstockorderNo(outstockNo);
        bizOutstockOrder.setOutRepositoryNo(outRepositoryNo);
        // 根据仓库编号查询所在机构
        String orgCodeByStoreHouseCode = storeHouseService.getOrgCodeByStoreHouseCode(outRepositoryNo);
        bizOutstockOrder.setOutstockOrgno(orgCodeByStoreHouseCode);
        bizOutstockOrder.setOutstockOperator(userHolder.getLoggedUserId());
        bizOutstockOrder.setTradeDocno(applyNo);
        bizOutstockOrder.setOutstockType(applyHandleContext.getOutstockType(outStockType));
        bizOutstockOrder.setOutstockTime(date);
        bizOutstockOrder.setTransportorderNo(transportorderNo);
        bizOutstockOrder.setChecked(Constants.LONG_FLAG_ZERO);
        bizOutstockOrder.preInsert(userHolder.getLoggedUserId());
        return bizOutstockOrderDao.saveEntity(bizOutstockOrder);
    }
}
