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
import com.ccbuluo.business.platform.order.service.fifohandle.StockInOutCallBackContext;
import com.ccbuluo.business.platform.outstock.dto.UpdatePlanStatusDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.service.StockDetailService;
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
    @Autowired
    private StoreHouseService storeHouseService;
    @Autowired
    private StockInOutCallBackContext stockInOutCallBackContext;

    /**
     * 根据类型查询申请单
     *
     * @return 申请单号
     * @author liuduo
     * @date 2018-08-07 14:19:40
     */
    @Override
    public List<String> queryApplyNo(String productType) {
        return allocateApplyService.queryApplyNo(productType, userHolder.getLoggedUser().getOrganization().getOrgCode(), StockPlanStatusEnum.DOING.name());
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
            bizInstockorderDetail.setStockType(item.getStockType());
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
            // 根据申请单号查询申请单基本信息
            FindAllocateApplyDTO detail = allocateApplyService.findDetail(applyNo);
            // 查询入库计划
            List<BizInstockplanDetail> bizInstockplanDetails = inputStockPlanService.queryListByApplyNo(applyNo, StockPlanStatusEnum.DOING.toString(), inRepositoryNo);
            if (null == bizInstockplanDetails || bizInstockplanDetails.size() == 0) {
                throw new CommonException("1001", "入库数量与计划不符！");
            }

            // 校验是否有错误数据
            int status = checkInstockorderDetail(bizInstockorderDetailList, bizInstockplanDetails);
            if (status == Constants.FAILURESTATUS) {
                throw new CommonException("1001", "入库数量与计划不符！");
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
            int i = saveInstockOrder(instockNo, applyNo, inRepositoryNo, bizInstockplanDetails.get(0).getInstockType());
            if (i == Constants.FAILURESTATUS) {
                throw new CommonException("1002", "生成入库单失败！");
            }
            // 2、保存入库单详单
            List<Long> ids = saveInstockorderDetail(instockNo, bizInstockorderDetailList, bizInstockplanDetails);
            if (ids == null || ids.size() == 0) {
                throw new CommonException("1003", "生成入库单详单失败！");
            }
            // 复核库存和入库计划
            checkStockAndPlan(applyNo, inRepositoryNo, instockNo);
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
     * @param instockNo 入库单号
     * @author liuduo
     * @date 2018-08-20 11:32:13
     */
    private void checkStockAndPlan(String applyNo, String inRepositoryNo, String instockNo) {
        // 3、修改库存明细
        // 根据入库单号查询入库单详单
        List<BizInstockorderDetail> bizInstockorderDetailList1 = instockorderDetailService.queryListByinstockNo(instockNo);
        List<Long> stockIds = saveStockDetail(applyNo, bizInstockorderDetailList1, inRepositoryNo);
        // 4、更新入库计划明细中的实际入库数量
        updateInstockplan(bizInstockorderDetailList1);
        List<BizInstockplanDetail> bizInstockplanDetails2 = inputStockPlanService.queryListByApplyNo(applyNo, StockPlanStatusEnum.DOING.toString(), inRepositoryNo);
        // 5、更改入库计划的完成状态和完成时间
        updateCompleteStatus(bizInstockplanDetails2);
        // 6、如果是平台端，则把库存明细中的有效库存更新到占用库存
//        updateOccupyStockById(stockIds);
        // 7、如果是平台入库后则改变申请单状态为 平台待出库，如果是机构入库后则改变申请单的状态为  确认收货
//        List<BizInstockplanDetail> bizInstockplanDetails3 = inputStockPlanService.queryListByApplyNoAndInReNo(applyNo, inRepositoryNo);
//        List<BizInstockplanDetail> collect = bizInstockplanDetails3.stream().filter(item -> item.getCompleteStatus().equals(StockPlanStatusEnum.COMPLETE.name())).collect(Collectors.toList());
        stockInOutCallBackContext.inStockCallBack(applyNo,inRepositoryNo);
//        updateApplyStatus(applyNo, bizInstockplanDetails3, collect);
        // 8、更新入库单的复核状态
        bizInstockOrderDao.updateChecked(instockNo, Constants.FLAG_ONE, new Date());
    }

    /**
     * 更改申请单状态
     * @param applyNo 申请单号
     * @param detail 申请单详情
     * @param bizInstockplanDetails3 全部入库计划
     * @param collect 已经完成了的计划
     * @author liuduo
     * @date 2018-08-21 18:52:48
     */
//    private void updateApplyStatus(String applyNo, FindAllocateApplyDTO detail, List<BizInstockplanDetail> bizInstockplanDetails3, List<BizInstockplanDetail> collect) {
//        if (collect.size() == bizInstockplanDetails3.size()) {
//            String applyType = detail.getApplyType();
//            String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
//            if (StringUtils.isNotBlank(applyType)) {
//                // 更改申请单状态
//                switch (Enum.valueOf(BizAllocateApply.AllocateApplyTypeEnum.class, applyType)) {
//                    case PURCHASE:
//                    case PLATFORMALLOCATE:
//                    case SAMELEVEL:
//                    case DIRECTALLOCATE:
//                        allocateApplyService.updateApplyOrderStatus(applyNo, ApplyStatusEnum.CONFIRMRECEIPT.toString());
//                        break;
//                    case BARTER:
//                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
//                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PLATFORMOUTBOUND.toString());
//                        } else {
//                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REPLACECOMPLETED.toString());
//                        }
//                        break;
//                    case REFUND:
//                        if (orgCode.equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
//                            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.WAITINGREFUND.toString());
//                        }
//                        break;
//                }
//            }
//
//        }
//    }


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
            return new Page<>();
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
     * 修改入库计划的完成状态
     *
     * @param bizInstockplanDetails 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    private void updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetails) {
        List<BizInstockplanDetail> bizInstockplanDetailList = new ArrayList<>();
        List<Long> ids = bizInstockplanDetails.stream().map(BizInstockplanDetail::getId).collect(Collectors.toList());
        List<UpdatePlanStatusDTO> versionNoById = inputStockPlanService.getVersionNoByIds(ids);
        Map<Long, UpdatePlanStatusDTO> collect = versionNoById.stream().collect(Collectors.toMap(UpdatePlanStatusDTO::getId, Function.identity()));
        bizInstockplanDetails.forEach(item -> {
            if (item.getActualInstocknum() >= item.getPlanInstocknum()) {
                UpdatePlanStatusDTO updatePlanStatusDTO = collect.get(item.getId());
                Long versionNo = updatePlanStatusDTO.getVersionNo() + Constants.LONG_FLAG_ONE;
                BizInstockplanDetail bizInstockplanDetail = new BizInstockplanDetail();
                bizInstockplanDetail.setId(item.getId());
                bizInstockplanDetail.setCompleteStatus(StockPlanStatusEnum.COMPLETE.name());
                bizInstockplanDetail.setCompleteTime(new Date());
                bizInstockplanDetail.setVersionNo(versionNo);
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
        bizInstockorderDetailList.forEach(item -> {
            Long versionNo = inputStockPlanService.getVersionNoById(item.getInstockPlanid());
            BizInstockplanDetail bizInstockplanDetail = new BizInstockplanDetail();
            bizInstockplanDetail.setId(item.getInstockPlanid());
            bizInstockplanDetail.setActualInstocknum(item.getInstockNum());
            bizInstockplanDetail.setVersionNo(versionNo + Constants.LONG_FLAG_ONE);
            bizInstockplanDetail.setCostPrice(item.getCostPrice());
            bizInstockplanDetail.preUpdate(userHolder.getLoggedUserId());
            int i = inputStockPlanService.updateActualInstockNum(bizInstockplanDetail);
            if (i != Constants.STATUS_FLAG_ONE) {
                logger.error("修改入库计划的实际入库数量失败！");
                throw new CommonException("1002", "生成入库单失败！");
            }
        });
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
     * @return 库存ids
     * @author liuduo
     * @date 2018-08-08 15:41:54
     */
    private List<Long> saveStockDetail(String applyNo, List<BizInstockorderDetail> bizInstockorderDetailList,  String inRepositoryNo) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 根据入库详单的  供应商、商品、仓库、批次号  查询在库存中有无记录，有则更新，无则新增
        List<BizInstockorderDetail> bizInstockorderDetailList1 = Lists.newArrayList();
        // 根据仓库编号查询机构号
        String orgCodeByStoreHouseCode = storeHouseService.getOrgCodeByStoreHouseCode(inRepositoryNo);
        List<Long> stockIds = Lists.newArrayList();
        bizInstockorderDetailList.forEach(item -> {
            Long id = stockDetailService.getByinstockorderDeatil(item.getSupplierNo(), item.getProductNo(), item.getCostPrice(), inRepositoryNo, applyNo);
            if (null != id) {
                // 根据id查询乐观锁
                Integer versionNoById1 = stockDetailService.getVersionNoById(id);
                long versionNo = versionNoById1 + Constants.LONG_FLAG_ONE;
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setId(id);
                bizStockDetail.setVersionNo(versionNo);
                if (item.getStockType().equals(BizStockDetail.StockTypeEnum.VALIDSTOCK.toString())) {
                    bizStockDetail.setValidStock(item.getInstockNum());
                } else if (item.getStockType().equals(BizStockDetail.StockTypeEnum.PROBLEMSTOCK.toString())) {
                    bizStockDetail.setProblemStock(item.getInstockNum());
                }
                stockDetailService.updateValidStock(bizStockDetail);
                // 把库存id回填到入库详单中
                BizInstockorderDetail bizInstockorderDetail = new BizInstockorderDetail();
                bizInstockorderDetail.setId(item.getId());
                bizInstockorderDetail.setStockId(id);
                bizInstockorderDetail.preUpdate(userHolder.getLoggedUserId());
                bizInstockorderDetailList1.add(bizInstockorderDetail);
            } else {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setRepositoryNo(inRepositoryNo);
                bizStockDetail.setOrgNo(orgCodeByStoreHouseCode);
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
                bizStockDetail.setSellerOrgno(item.getSellerOrgno());
                bizStockDetail.setCostPrice(item.getCostPrice());
                bizStockDetail.setInstockPlanid(item.getInstockPlanid());
                bizStockDetail.setProductUnit(item.getUnit());
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
    private List<Long> saveInstockorderDetail(String instockOrderno, List<BizInstockorderDetail> bizInstockorderDetailList, List<BizInstockplanDetail> bizInstockplanDetails) {
        List<BizInstockorderDetail> bizInstockorderDetailList1 = new ArrayList<>();
        Map<Long, BizInstockplanDetail> collect = bizInstockplanDetails.stream().collect(Collectors.toMap(BizInstockplanDetail::getId, Function.identity()));
        bizInstockorderDetailList.forEach(item -> {
            BizInstockplanDetail bizInstockplanDetail = collect.get(item.getInstockPlanid());
            item.setCostPrice(bizInstockplanDetail.getCostPrice());
            item.setInstockOrderno(instockOrderno);
            item.preInsert(userHolder.getLoggedUserId());
            if (item.getInstockNum() > 0) {
                bizInstockorderDetailList1.add(item);
            }
        });
        return instockorderDetailService.save(bizInstockorderDetailList1);
    }

    /**
     * 保存入库单
     *
     * @param instockOrderno          入库单号
     * @param tradeDocno        申请单号
     * @param inRepositoryNo 入库仓库code
     * @param instockType 申请类型
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-08-08 10:56:05
     */
    private int saveInstockOrder(String instockOrderno, String tradeDocno, String inRepositoryNo, String instockType) {
        BizInstockOrder bizInstockOrder = new BizInstockOrder();
        Date date = new Date();
        bizInstockOrder.setInstockOrderno(instockOrderno);
        bizInstockOrder.setTradeDocno(tradeDocno);
        bizInstockOrder.setInRepositoryNo(inRepositoryNo);
        bizInstockOrder.setInstockOperator(userHolder.getLoggedUserId());
        // 根据仓库code查询机构code
        String orgCodeByStoreHouseCode = storeHouseService.getOrgCodeByStoreHouseCode(inRepositoryNo);
        bizInstockOrder.setInstockOrgno(orgCodeByStoreHouseCode);
        bizInstockOrder.setInstockType(applyHandleContext.getInstockType(instockType));
        bizInstockOrder.setInstockTime(date);
        bizInstockOrder.setChecked(Constants.LONG_FLAG_ZERO);
        bizInstockOrder.preInsert(userHolder.getLoggedUserId());
        return bizInstockOrderDao.saveEntity(bizInstockOrder);
    }


}
