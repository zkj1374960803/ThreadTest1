package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.BizAllocateApply.ApplyStatusEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.allocateapply.service.applyhandle.ApplyHandleContext;
import com.ccbuluo.business.platform.inputstockplan.service.InputStockPlanService;
import com.ccbuluo.business.platform.instock.dao.BizInstockOrderDao;
import com.ccbuluo.business.platform.instock.dto.BizInstockOrderDTO;
import com.ccbuluo.business.platform.instock.dto.InstockorderDetailDTO;
import com.ccbuluo.business.platform.outstock.dto.updatePlanStatusDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.service.StockDetailService;
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
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
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
    private BizInstockOrderDao bizInstockOrderDao;
    @Autowired
    private InstockorderDetailService instockorderDetailService;
    @Autowired
    private GenerateDocCodeService generateDocCodeService;
    @Autowired
    private InputStockPlanService inputStockPlanService;
    @Autowired
    private StockDetailService stockDetailService;
    @Autowired
    private AllocateApplyService allocateApplyService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private ApplyHandleContext applyHandleContext;

    /**
     * 根据申请单号状态查询申请单号集合
     *
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    @Override
    public List<String> queryApplyNo() {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
            return allocateApplyService.queryApplyNo(ApplyStatusEnum.INSTORE.toString(), orgCode);
        }
        return allocateApplyService.queryApplyNo(ApplyStatusEnum.WAITINGRECEIPT.toString(), orgCode);
    }

    /**
     * 自动入库
     *
     * @param applyNo               申请单编号
     * @param bizInstockplanDetails 入库计划集合
     * @return 是否保存成功
     * @author liuduo
     * @date 2018-08-15 16:14:04
     */
    @Override
    public StatusDto<String> autoSaveInstockOrder(String applyNo, String inRepositoryNo, List<BizInstockplanDetail> bizInstockplanDetails) {
        List<BizInstockorderDetail> bizInstockorderDetailList1 = Lists.newArrayList();
        bizInstockplanDetails.forEach(item -> {
            BizInstockorderDetail bizInstockorderDetail = new BizInstockorderDetail();
            bizInstockorderDetail.setInstockPlanid(item.getId());
            bizInstockorderDetail.setProductNo(item.getProductNo());
            bizInstockorderDetail.setProductName(item.getProductName());
            bizInstockorderDetail.setProductType(item.getProductType());
            bizInstockorderDetail.setProductCategoryname(item.getProductCategoryname());
            bizInstockorderDetail.setSupplierNo(item.getSupplierNo());
            bizInstockorderDetail.setInstockNum(item.getActualInstocknum());
            bizInstockorderDetail.setStockType(item.getInstockType());
            bizInstockorderDetail.setUnit(item.getProductUnit());
            bizInstockorderDetail.setCostPrice(item.getCostPrice());
            bizInstockorderDetailList1.add(bizInstockorderDetail);
        });
        return saveInstockOrder(applyNo, inRepositoryNo, bizInstockorderDetailList1);
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
    public StatusDto<String> saveInstockOrder(String applyNo, String inRepositoryNo, List<BizInstockorderDetail> bizInstockorderDetailList) {
        try {
            String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
            // 根据申请单号查询申请单基本信息
            FindAllocateApplyDTO detail = allocateApplyService.findDetail(applyNo);
            // 查询入库计划
            List<BizInstockplanDetail> bizInstockplanDetails = inputStockPlanService.queryListByApplyNo(applyNo, StockPlanStatusEnum.DOING.toString(), inRepositoryNo);
            if (null == bizInstockplanDetails || bizInstockplanDetails.size() == 0) {
                throw new CommonException("10001", "入库数量与计划不符！");
            }

            // 校验是否有错误数据
            int status = checkInstockorderDetail(bizInstockorderDetailList, bizInstockplanDetails);
            if (status == Constants.FAILURESTATUS) {
                throw new CommonException("10001", "入库数量与计划不符！");
            }
            // 1、保存入库单
            // 生成入库单编号
            String instockNo = null;
            StatusDto<String> instockOrderCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.R);
            if (instockOrderCode.getCode().equals(Constants.SUCCESS_CODE)) {
                instockNo = instockOrderCode.getData();
            } else {
                return StatusDto.buildFailure("生成入库单编码失败！");
            }
            int i = saveInstockOrder(instockNo, detail, detail.getInstockOrgno());
            if (i == Constants.FAILURESTATUS) {
                throw new CommonException("10002", "生成入库单失败！");
            }
            // 2、保存入库单详单
            List<Long> ids = saveInstockorderDetail(instockNo, bizInstockorderDetailList);
            if (ids == null || ids.size() == 0) {
                throw new CommonException("10003", "生成入库单详单失败！");
            }
            // 复核库存和入库计划
            checkStockAndPlan(applyNo, inRepositoryNo, detail, instockNo);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("生成入库单失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 复核库存和入库计划
     * @param applyNo 申请单号
     * @param inRepositoryNo 入库仓库编号
     * @param detail 申请单详情
     * @param instockNo 入库单号
     * @author liuduo
     * @date 2018-08-20 11:32:13
     */
    private void checkStockAndPlan(String applyNo, String inRepositoryNo, FindAllocateApplyDTO detail, String instockNo) {
        // 3、修改库存明细
        // 根据入库单号查询入库单详单
        List<BizInstockorderDetail> bizInstockorderDetailList1 = instockorderDetailService.queryListByinstockNo(instockNo);
        List<Long> stockIds = saveStockDetail(applyNo, bizInstockorderDetailList1, detail, inRepositoryNo);
        // 4、更新入库计划明细中的实际入库数量
        updateInstockplan(bizInstockorderDetailList1);
        List<BizInstockplanDetail> bizInstockplanDetails2 = inputStockPlanService.queryListByApplyNo(applyNo, StockPlanStatusEnum.DOING.toString(), inRepositoryNo);
        // 5、更改入库计划的完成状态和完成时间
        updateCompleteStatus(bizInstockplanDetails2);
        // 6、如果是平台端，则把库存明细中的有效库存更新到占用库存
        updateOccupyStockById(stockIds);
        // 7、如果是平台入库后则改变申请单状态为 平台待出库，如果是机构入库后则改变申请单的状态为  确认收货
        List<BizInstockplanDetail> bizInstockplanDetails3 = inputStockPlanService.queryListByApplyNo(applyNo, StockPlanStatusEnum.COMPLETE.name(), inRepositoryNo);
        List<BizInstockplanDetail> collect = bizInstockplanDetails3.stream().filter(item -> item.getCompleteStatus().equals(StockPlanStatusEnum.COMPLETE.name())).collect(Collectors.toList());
        if (collect.size() == bizInstockplanDetails3.size()) {
            // todo 回调彪哥的更改库存的方法
            if (userHolder.getLoggedUser().getOrganization().getOrgCode().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                allocateApplyService.updateApplyOrderStatus(applyNo, ApplyStatusEnum.OUTSTORE.toString());
            } else {
                allocateApplyService.updateApplyOrderStatus(applyNo, ApplyStatusEnum.CONFIRMRECEIPT.toString());
            }
        }
    }


    /**
     * 根据申请单号查询入库计划
     *
     * @param applyNo     申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    @Override
    public List<BizInstockplanDetail> queryInstockplan(String applyNo, String inRepositoryNo, String productType) {
        return inputStockPlanService.queryInstockplan(applyNo, inRepositoryNo, productType);
    }

    /**
     * 分页查询入库单列表
     *
     * @param productType 商品类型
     * @param instockType 入库类型
     * @param instockNo   入库单号
     * @param offset      起始数
     * @param pagesize    每页数
     * @return 入库单列表
     * @author liuduo
     * @date 2018-08-11 16:43:19
     */
    @Override
    public Page<BizInstockOrder> queryInstockList(String productType, String instockType, String instockNo, Integer offset, Integer pagesize) {
        // 查询当前登录人的机构下的入库单
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        Page<BizInstockOrder> bizInstockOrderPage = bizInstockOrderDao.queryInstockList(productType, instockType, instockNo, orgCode, offset, pagesize);
        List<BizInstockOrder> rows = bizInstockOrderPage.getRows();
        List<String> instockOperator = rows.stream().map(BizInstockOrder::getInstockOperator).distinct().collect(Collectors.toList());
        if (instockOperator.size() == 0) {
            return null;
        }
        StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuidsDTOStatusDtoThriftList = innerUserInfoService.queryNameByUseruuids(instockOperator);
        StatusDto<List<QueryNameByUseruuidsDTO>> resolve = StatusDtoThriftUtils.resolve(queryNameByUseruuidsDTOStatusDtoThriftList, QueryNameByUseruuidsDTO.class);
        List<QueryNameByUseruuidsDTO> userNames = resolve.getData();
        Map<String, String> collect = userNames.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, QueryNameByUseruuidsDTO::getName));
        rows.forEach(item -> {
            item.setInstockOperatorName(collect.get(item.getInstockOperator()));
        });

        return bizInstockOrderPage;
    }

    /**
     * 根据入库单号查询入库单详情
     *
     * @param instockNo 入库单号
     * @return 入库单详情
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    @Override
    public BizInstockOrderDTO getByInstockNo(String instockNo) {
        // 查询入库单
        BizInstockOrderDTO bizInstockOrderDTO = bizInstockOrderDao.getByInstockNo(instockNo);
        // 封装入库人
        StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuidsDTOStatusDtoThriftList = innerUserInfoService.queryNameByUseruuids(Lists.newArrayList(bizInstockOrderDTO.getInstockOperator()));
        StatusDto<List<QueryNameByUseruuidsDTO>> resolve = StatusDtoThriftUtils.resolve(queryNameByUseruuidsDTOStatusDtoThriftList, QueryNameByUseruuidsDTO.class);
        List<QueryNameByUseruuidsDTO> userNames = resolve.getData();
        Map<String, String> collect = userNames.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, QueryNameByUseruuidsDTO::getName));
        bizInstockOrderDTO.setInstockOperatorName(collect.get(bizInstockOrderDTO.getInstockOperator()));
        // 查询入库单详单
        List<InstockorderDetailDTO> instockorderDetailDTOS = instockorderDetailService.getByInstockNo(instockNo);
        instockorderDetailDTOS.forEach(item -> {
            item.setCostTotalPrice(item.getCostPrice().multiply(new BigDecimal(item.getInstockNum())));
        });
        BigDecimal reduce = instockorderDetailDTOS.stream().map(InstockorderDetailDTO::getCostTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        bizInstockOrderDTO.setCostTotalPrice(reduce);
        bizInstockOrderDTO.setInstockorderDetailDTOList(instockorderDetailDTOS);

        return bizInstockOrderDTO;
    }

    /**
     * 根据申请单号查询入库仓库
     * @param applyNo 入库单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    @Override
    public List<String> getByApplyNo(String applyNo) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        return inputStockPlanService.getByApplyNo(applyNo, orgCode);
    }

    /**
     * 如果是平台端，则把库存明细中的有效库存更新到占用库存
     * @param stockIds 库存ids
     * @author liuduo
     * @date 2018-08-10 15:31:50
     */
    private void updateOccupyStockById(List<Long> stockIds) {
        if (userHolder.getLoggedUser().getOrganization().getOrgCode().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
            // 根据库存明细id查询到控制的版本号（乐观锁使用）
            List<Pair<Long, Long>> versionNos = stockDetailService.queryVersionNoById(stockIds);
            List<BizStockDetail> bizStockDetails = Lists.newArrayList();
            for (Pair<Long, Long> versionNo : versionNos) {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setId(versionNo.getKey());
                bizStockDetail.setVersionNo(versionNo.getValue() + Constants.FLAG_ONE);
                bizStockDetail.preUpdate(userHolder.getLoggedUserId());
                bizStockDetails.add(bizStockDetail);
            }
            stockDetailService.updateOccupyStockById(bizStockDetails);
        }
    }


    /**
     * 修改入库计划的完成状态
     *
     * @param bizInstockplanDetails 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    private void updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetails) {
        List<BizInstockplanDetail> bizInstockplanDetailList = new ArrayList<>();
        List<Long> ids = bizInstockplanDetails.stream().map(BizInstockplanDetail::getId).collect(Collectors.toList());
        List<updatePlanStatusDTO> versionNoById = inputStockPlanService.getVersionNoById(ids);
        Map<Long, updatePlanStatusDTO> collect = versionNoById.stream().collect(Collectors.toMap(updatePlanStatusDTO::getId, Function.identity()));
        bizInstockplanDetails.forEach(item -> {
            if (item.getActualInstocknum() >= item.getPlanInstocknum()) {
                updatePlanStatusDTO updatePlanStatusDTO = collect.get(item.getId());
                BizInstockplanDetail bizInstockplanDetail = new BizInstockplanDetail();
                bizInstockplanDetail.setId(item.getId());
                bizInstockplanDetail.setCompleteStatus(StockPlanStatusEnum.COMPLETE.name());
                bizInstockplanDetail.setCompleteTime(new Date());
                bizInstockplanDetail.setVersionNo(updatePlanStatusDTO.getVersionNo() + Constants.LONG_FLAG_ONE);
                bizInstockplanDetail.preUpdate(userHolder.getLoggedUserId());
                bizInstockplanDetailList.add(bizInstockplanDetail);
            }
        });
        if (!bizInstockplanDetailList.isEmpty()) {
            inputStockPlanService.updateCompleteStatus(bizInstockplanDetailList);
        }
    }

    /**
     * 修改入库计划中的实际入库数量
     *
     * @param bizInstockorderDetailList 入库单详单
     * @author liuduo
     * @date 2018-08-08 20:29:20
     */
    private void updateInstockplan(List<BizInstockorderDetail> bizInstockorderDetailList) {
        List<BizInstockplanDetail> bizInstockplanDetailList = new ArrayList<>();
        List<Long> ids = bizInstockorderDetailList.stream().map(BizInstockorderDetail::getInstockPlanid).collect(Collectors.toList());
        List<updatePlanStatusDTO> versionNoById = inputStockPlanService.getVersionNoById(ids);
        Map<Long, updatePlanStatusDTO> collect = versionNoById.stream().collect(Collectors.toMap(updatePlanStatusDTO::getId, Function.identity()));
        bizInstockorderDetailList.forEach(item -> {
            updatePlanStatusDTO updatePlanStatusDTO = collect.get(item.getInstockPlanid());
            BizInstockplanDetail bizInstockplanDetail = new BizInstockplanDetail();
            bizInstockplanDetail.setId(item.getInstockPlanid());
            bizInstockplanDetail.setActualInstocknum(item.getInstockNum());
            bizInstockplanDetail.setVersionNo(updatePlanStatusDTO.getVersionNo() + Constants.LONG_FLAG_ONE);
            bizInstockplanDetail.preUpdate(userHolder.getLoggedUserId());
            bizInstockplanDetailList.add(bizInstockplanDetail);
        });
        if (!bizInstockplanDetailList.isEmpty()) {
            inputStockPlanService.updateActualInstockNum(bizInstockplanDetailList);
        }
    }

    /**
     * 校验是否有错误数据
     *
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
            if (null != bizInstockplanDetail && null != item.getInstockNum()) {
                // 本次实际入库数量
                Long actualNum = item.getInstockNum();
                // 计划入库数量
                long planNum = bizInstockplanDetail.getPlanInstocknum() - bizInstockplanDetail.getActualInstocknum();
                if (actualNum > planNum) {
                    bizInstockorderDetailList1.add(item);
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
     * @param applyNo                   申请单号
     * @param bizInstockorderDetailList 入库单详单
     * @param bizAllocateApply          申请单
     * @return 库存ids
     * @author liuduo
     * @date 2018-08-08 15:41:54
     */
    private List<Long> saveStockDetail(String applyNo, List<BizInstockorderDetail> bizInstockorderDetailList, FindAllocateApplyDTO bizAllocateApply, String inRepositoryNo) {
        // 根据入库详单的  供应商、商品、仓库、批次号  查询在库存中有无记录，有则更新，无则新增
        List<BizInstockorderDetail> bizInstockorderDetailList1 = Lists.newArrayList();
        List<Long> ids = bizInstockorderDetailList.stream().map(BizInstockorderDetail::getInstockPlanid).collect(Collectors.toList());
        List<updatePlanStatusDTO> versionNoById = inputStockPlanService.getVersionNoById(ids);
        Map<Long, updatePlanStatusDTO> collect = versionNoById.stream().collect(Collectors.toMap(updatePlanStatusDTO::getId, Function.identity()));
        List<Long> stockIds = Lists.newArrayList();
        bizInstockorderDetailList.forEach(item -> {
            updatePlanStatusDTO updatePlanStatusDTO = collect.get(item.getInstockPlanid());
            Long id = stockDetailService.getByinstockorderDeatil(item.getSupplierNo(), item.getProductNo(), inRepositoryNo, applyNo);
            if (null != id) {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setId(id);
                bizStockDetail.setValidStock(item.getInstockNum());
                stockDetailService.updateValidStock(bizStockDetail, updatePlanStatusDTO.getVersionNo());
            } else {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setRepositoryNo(inRepositoryNo);
                bizStockDetail.setOrgNo(bizAllocateApply.getInstockOrgno());
                bizStockDetail.setProductNo(item.getProductNo());
                bizStockDetail.setProductName(item.getProductName());
                bizStockDetail.setProductType(item.getProductType());
                bizStockDetail.setProductCategoryname(item.getProductCategoryname());
                bizStockDetail.setTradeNo(applyNo);
                bizStockDetail.setSupplierNo(item.getSupplierNo());
                if (item.getStockType().equals(BizStockDetail.StockTypeEnum.VALIDSTOCK.toString())) {
                    bizStockDetail.setValidStock(item.getInstockNum());
                } else if (item.getStockType().equals(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.toString())) {
                    bizStockDetail.setProblemStock(item.getInstockNum());
                }
                bizStockDetail.setSellerOrgno(bizAllocateApply.getOutstockOrgno());
                bizStockDetail.setCostPrice(item.getCostPrice());
                bizStockDetail.setInstockPlanid(item.getInstockPlanid());
                bizStockDetail.preInsert(userHolder.getLoggedUserId());
                Long stockDetailId = stockDetailService.saveStockDetail(bizStockDetail);
                stockIds.add(stockDetailId);
                // 把库存id回填到入库详单中
                BizInstockorderDetail bizInstockorderDetail = new BizInstockorderDetail();
                bizInstockorderDetail.setId(item.getId());
                bizInstockorderDetail.setStockId(stockDetailId);
                bizInstockorderDetail.preUpdate(userHolder.getLoggedUserId());
                bizInstockorderDetailList1.add(bizInstockorderDetail);
            }
        });
        // 修改入库单详单的库存明细id
        instockorderDetailService.updateInstockorderStockId(bizInstockorderDetailList1);
        return stockIds;
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
        List<BizInstockorderDetail> bizInstockorderDetailList1 = new ArrayList<>();
        bizInstockorderDetailList.forEach(item -> {
            item.setInstockOrderno(instockOrderno);
            item.preInsert(userHolder.getLoggedUserId());
            bizInstockorderDetailList1.add(item);
        });
        return instockorderDetailService.save(bizInstockorderDetailList1);
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
    private int saveInstockOrder(String instockOrderno, FindAllocateApplyDTO bizAllocateApply, String orgCodeByStoreHouseCode) {
        BizInstockOrder bizInstockOrder = new BizInstockOrder();
        Date date = new Date();
        bizInstockOrder.setInstockOrderno(instockOrderno);
        bizInstockOrder.setTradeDocno(bizAllocateApply.getApplyNo());
        bizInstockOrder.setInRepositoryNo(bizAllocateApply.getInRepositoryNo());
        bizInstockOrder.setInstockOrgno(orgCodeByStoreHouseCode);
        bizInstockOrder.setInstockOperator(userHolder.getLoggedUserId());
        bizInstockOrder.setInstockOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizInstockOrder.setInstockType(applyHandleContext.getInstockType(bizAllocateApply.getApplyType()));
        bizInstockOrder.setInstockTime(date);
        bizInstockOrder.setChecked(Constants.LONG_FLAG_ZERO);
        bizInstockOrder.setCheckedTime(date);
        bizInstockOrder.preInsert(userHolder.getLoggedUserId());
        return bizInstockOrderDao.saveEntity(bizInstockOrder);
    }


}
