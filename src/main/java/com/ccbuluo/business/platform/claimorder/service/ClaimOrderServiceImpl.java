package com.ccbuluo.business.platform.claimorder.service;

import com.ccbuluo.account.AccountTransactionDTO;
import com.ccbuluo.account.AccountTypeEnumThrift;
import com.ccbuluo.account.BizFinanceAccountService;
import com.ccbuluo.account.TransactionTypeEnumThrift;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.entity.BizServiceorderDetail;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelManageDao;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.business.platform.claimorder.dao.ClaimOrderDao;
import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.claimorder.dto.QueryClaimorderListDTO;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.dao.BizServiceorderDetailDao;
import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.QueryServiceCenterDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.BasicUserWorkplaceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangkangjian
 * @date 2018-09-08 10:42:41
 */
@Service
public class ClaimOrderServiceImpl implements ClaimOrderService{
    @Resource
    private BizServiceorderDetailDao bizServiceorderDetailDao;
    @Resource
    private GenerateDocCodeService generateDocCodeService;
    @Autowired
    private UserHolder userHolder;
    @Resource
    private ClaimOrderDao claimOrderDao;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;
    @Resource
    private BizServiceOrderDao bizServiceOrderDao;
    @Resource
    private BasicCarmodelManageService basicCarmodelManageService;
    @Autowired
    private BasicCarmodelManageDao basicCarmodelManageDao;
    @ThriftRPCClient("BasicWalletpaymentSerService")
    private BizFinanceAccountService bizFinanceAccountService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @ThriftRPCClient("UserCoreSerService")
    BasicUserWorkplaceService basicUserWorkplaceService;
    /**
     * 生成索赔单
     * @author zhangkangjian
     * @date 2018-09-08 10:43:18
     */
    @Override
    public void generateClaimForm(String serviceOrdno) {
        List<BizServiceClaimorder> bizServiceClaimorders = Lists.newArrayList();
        String loggedUserId = userHolder.getLoggedUserId();
        // 查询维修单详单数据
        BizServiceorderDetail queryBizServiceorderDetail = new BizServiceorderDetail();
        queryBizServiceorderDetail.setOrderNo(serviceOrdno);
        // 查询在保状态
        queryBizServiceorderDetail.setWarrantyType(BizServiceorderDetail.WarrantyTypeEnum.INSHELFLIFE.name());
        List<BizServiceorderDetail> bizServiceorderDetails = Optional.ofNullable(bizServiceorderDetailDao.queryListBizServiceorderDetail(queryBizServiceorderDetail)).orElse(new ArrayList<BizServiceorderDetail>());
        // 根据机构分组统计索赔单的总金额
        Map<String, Double> totalPriceMap = bizServiceorderDetails.stream()
            .collect(Collectors.groupingBy(BizServiceorderDetail::getServiceOrgno,
                Collectors.summingDouble(BizServiceorderDetail::getTotalPrice)));
        // 根据机构编号分组
        Map<String, List<BizServiceorderDetail>> bizServiceorderDetailMap = bizServiceorderDetails.stream().collect((Collectors.groupingBy(BizServiceorderDetail::getServiceOrgno)));
        // 遍历map，生成索赔单
        for (Map.Entry<String, List<BizServiceorderDetail>> entry : bizServiceorderDetailMap.entrySet()) {
            List<BizServiceorderDetail> value = entry.getValue();
            BizServiceorderDetail bizServiceorderDetail = value.get(0);
            String serviceOrgno = bizServiceorderDetail.getServiceOrgno();
            Double aDouble = totalPriceMap.get(serviceOrgno);
            BizServiceClaimorder bizServiceClaimorder = new BizServiceClaimorder();
            // 生成索赔单号
            StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.TP);
            bizServiceClaimorder.setClaimOrdno(stringStatusDto.getData());
            bizServiceClaimorder.setServiceOrdno(serviceOrdno);
            // 待接受
            bizServiceClaimorder.setDocStatus(BizServiceClaimorder.DocStatusEnum.PENDINGSUBMISSION.name());
            // 设置索赔机构编号和名称
            bizServiceClaimorder.setClaimOrgno(bizServiceorderDetail.getServiceOrgno());
            bizServiceClaimorder.setClaimOrgname(bizServiceorderDetail.getServiceOrgname());
            // 处理索赔单的机构
            bizServiceClaimorder.setProcessOrgno(OrganizationTypeEnum.PLATFORM.name());
            bizServiceClaimorder.setProcessOrgname(OrganizationTypeEnum.PLATFORM.getLabel());
            // 设置索赔的金额
            bizServiceClaimorder.setClaimAmount(aDouble);
            bizServiceClaimorder.setOperator(loggedUserId);
            bizServiceClaimorder.setCreator(loggedUserId);
            bizServiceClaimorders.add(bizServiceClaimorder);
        }
        // 批量保存索赔单
        claimOrderDao.batchSaveServiceClaimorders(bizServiceClaimorders);
    }

    /**
     * 提交索赔单
     * @param bizServiceClaimorder
     * @author zhangkangjian
     * @date 2018-09-08 14:26:55
     */
    @Override
    public void updateClaimOrder(BizServiceClaimorder bizServiceClaimorder) {
        // 设置索赔单的状态
        bizServiceClaimorder.setDocStatus(BizServiceClaimorder.DocStatusEnum.WAITACCEPTANCE.name());
        claimOrderDao.updateClaimOrder(bizServiceClaimorder);
    }

    /**
     * 查询索赔单的详情
     * @param bizServiceClaimorder
     * @return Map<String, Object>
     * @author zhangkangjian
     * @date 2018-09-08 14:40:12
     */
    @Override
    public BizServiceClaimorder findClaimOrderDetail(BizServiceClaimorder bizServiceClaimorder) {


        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        productDetailDTO.setServiceOrgno(orgCode);
        // 查询基础信息
        BizServiceClaimorder serviceClaimorderDetail = claimOrderDao.findClaimOrderDetailByClaimOrdno(bizServiceClaimorder);
        if(serviceClaimorderDetail == null){
            throw new CommonException(Constants.ERROR_CODE,"索赔单数据异常");
        }
        // 如果当前登陆人是平台
        if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(orgCode)){
            String claimOrgno = serviceClaimorderDetail.getClaimOrgno();
            productDetailDTO.setServiceOrgno(claimOrgno);
        }

        // 查询车牌号
        BizServiceOrder byOrderNo = bizServiceOrderDao.getByOrderNo(serviceClaimorderDetail.getServiceOrdno());
        String carNo = byOrderNo.getCarNo();
        serviceClaimorderDetail.setCarNo(carNo);
        // 查询 在保 工时信息
        productDetailDTO.setWarrantyType(BizServiceorderDetail.WarrantyTypeEnum.INSHELFLIFE.name());
        // 工时类型
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.MAINTAINITEM.name());
        productDetailDTO.setServiceOrderno(serviceClaimorderDetail.getServiceOrdno());
        List<ProductDetailDTO> maintainitemDetail = claimOrderDao.queryMaintainitemDetail(productDetailDTO);
        // 查询零配件信息
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.FITTING.name());
        // 查询零配件列表信息
        List<ProductDetailDTO> fittingDetail = queryFitingDetailList(productDetailDTO);
        serviceClaimorderDetail.setFittingDetail(fittingDetail);
        serviceClaimorderDetail.setMaintainitemDetail(maintainitemDetail);
        return serviceClaimorderDetail;
    }


     /**
      * 查询支付价格
      * @param serviceOrdno 维修单号
      * @param claimOrdno
      * @return  Map<String, Double>
      * @author zhangkangjian
      * @date 2018-09-12 14:02:21
      */
     @Override
     public Map<String, Double> findPaymentAmount(String serviceOrdno, String claimOrdno){
         BizServiceClaimorder bizServiceClaimorder = new BizServiceClaimorder();
         bizServiceClaimorder.setClaimOrdno(claimOrdno);
         BizServiceClaimorder claimOrderDetailByClaimOrdno = claimOrderDao.findClaimOrderDetailByClaimOrdno(bizServiceClaimorder);
         String claimOrgno = claimOrderDetailByClaimOrdno.getClaimOrgno();
         ProductDetailDTO productDetailDTO = new ProductDetailDTO();
         productDetailDTO.setServiceOrgno(claimOrgno);
         productDetailDTO.setWarrantyType(BizServiceorderDetail.WarrantyTypeEnum.INSHELFLIFE.name());
         productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.MAINTAINITEM.name());
         productDetailDTO.setServiceOrderno(serviceOrdno);
         List<ProductDetailDTO> maintainitemDetail = claimOrderDao.queryMaintainitemDetail(productDetailDTO);
         // 查询零配件信息
         productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.FITTING.name());
         // 查询零配件列表信息
         List<ProductDetailDTO> fittingDetail = queryFitingDetailList(productDetailDTO);
         // 统计价格
         double maintainitemPrice = maintainitemDetail.stream().mapToDouble(ProductDetailDTO::getTotalPrice).sum();
         double fittingPrice = fittingDetail.stream().mapToDouble(ProductDetailDTO::getTotalPrice).sum();
         HashMap<String, Double> map = Maps.newHashMap();
         map.put("maintainitemPrice", maintainitemPrice);
         map.put("fittingPrice", fittingPrice);
         map.put("maintainitemAndFittingPrice", maintainitemPrice + fittingPrice);
         return map;
     }

    /**
     * 查询维修单各种状态数据的数量
     * @return Map<String,Long>
     * @author zhangkangjian
     * @date 2018-09-19 17:10:39
     */
    @Override
    public Map<String, Long> countClaimorderStatusNum() {
        HashMap<String, Long> statusMap = Maps.newHashMap();
        statusMap.put(BizServiceClaimorder.DocStatusEnum.WAITACCEPTANCE.name(), 0L);
        statusMap.put(BizServiceClaimorder.DocStatusEnum.COMPLETED.name(), 0L);
        statusMap.put(BizServiceClaimorder.DocStatusEnum.PENDINGPAYMENT.name(), 0L);
        statusMap.put(BizServiceClaimorder.DocStatusEnum.PENDINGSUBMISSION.name(), 0L);
        Page<QueryClaimorderListDTO> queryClaimorderListDTOPage = queryClaimorderPage(null, null, null, 0, Integer.MAX_VALUE);
        List<QueryClaimorderListDTO> rows = queryClaimorderListDTOPage.getRows();
        if(rows != null && rows.size() > 0){
            Map<String,Long> map = rows.stream().collect(Collectors.groupingBy(QueryClaimorderListDTO::getDocStatus,Collectors.counting()));
            map.put("ALL", (long) rows.size());
            map.forEach(statusMap::put);
        }
        return statusMap;
    }


    /**
     * 查询零配件列表信息
     * @param productDetailDTO 查询条件
     * @return List<ProductDetailDTO>
     * @author zhangkangjian
     * @date 2018-09-10 16:36:14
     */
    @Override
    public List<ProductDetailDTO> queryFitingDetailList(ProductDetailDTO productDetailDTO) {
        List<ProductDetailDTO> fittingDetail = claimOrderDao.queryMaintainitemDetail(productDetailDTO);
        Optional.ofNullable(fittingDetail).ifPresent(a ->{
            List<String> productNoList = fittingDetail.stream().map(ProductDetailDTO::getProductNo).collect(Collectors.toList());
            QueryCarpartsProductDTO queryCarpartsProductDTO = new QueryCarpartsProductDTO();
            queryCarpartsProductDTO.setOffset(0);
            queryCarpartsProductDTO.setPageSize(Integer.MAX_VALUE);
            queryCarpartsProductDTO.setCarpartsCodeList(productNoList);
            StatusDtoThriftPage<BasicCarpartsProductDTO> basicCarpartsProductDTO =
                carpartsProductService.queryCarpartsProductListByPriceType(queryCarpartsProductDTO);
            StatusDto<Page<BasicCarpartsProductDTO>> basicCarpartsProductDTOResolve = StatusDtoThriftUtils.resolve(basicCarpartsProductDTO, BasicCarpartsProductDTO.class);
            List<BasicCarpartsProductDTO> basicCarpartsProductDTOList = basicCarpartsProductDTOResolve.getData().getRows();
            if(basicCarpartsProductDTOList != null){
                // 填充车型名称
                basicCarmodelManageService.buildCarModeName(basicCarpartsProductDTOList);
                Map<String, BasicCarpartsProductDTO> basicCarpartsProductMap = basicCarpartsProductDTOList.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, b -> b,(k1, k2)->k1));
                a.forEach(c->{
                    String productNo = c.getProductNo();
                    BasicCarpartsProductDTO carparts = basicCarpartsProductMap.get(productNo);
                    c.setProductName(carparts.getCarpartsName());
                    c.setCarModelName(carparts.getCarmodelName());
                    c.setProductCategoryname(carparts.getCategoryCodePath());
                    c.setProductUnit(carparts.getUnitName());
                });
            }
        });
        return fittingDetail;
    }

    /**
     * 查询索赔单的列表
     * @param claimOrdno 索赔单编号
     * @param docStatus 索赔单状态
     * @param offset 偏移量
     * @param pageSize 每页显示的数量
     * @return StatusDto<Page<QueryClaimorderListDTO>>
     * @author zhangkangjian
     * @date 2018-09-08 16:55:56
     */
    @Override
    public StatusDto<Page<QueryClaimorderListDTO>> queryClaimorderList(String claimOrdno, String keyword, String docStatus, int offset, int pageSize) {
        Page<QueryClaimorderListDTO> queryClaimorderListDTOPage = queryClaimorderPage(claimOrdno, keyword, docStatus, offset, pageSize);
        Optional.ofNullable(queryClaimorderListDTOPage.getRows()).ifPresent(a ->{
            StatusDtoThriftPage<QueryServiceCenterDTO> serviceCenterList = basicUserOrganizationService.queryServiceCenterList(null, null ,null ,null,null,0,Integer.MAX_VALUE);
            StatusDto<Page<QueryServiceCenterDTO>> queryServiceCenterDTOPage = StatusDtoThriftUtils.resolve(serviceCenterList, QueryServiceCenterDTO.class);
            Optional.ofNullable(queryServiceCenterDTOPage.getData().getRows()).ifPresent(b ->{
                Map<String, List<QueryServiceCenterDTO>> queryServiceCenterDTOMap = b.stream().collect(Collectors.groupingBy(QueryServiceCenterDTO::getServiceCenterCode));
                a.forEach(item ->{
                    List<QueryServiceCenterDTO> queryServiceCenterDTOS = queryServiceCenterDTOMap.get(item.getClaimOrgno());
                    if(queryServiceCenterDTOS != null && queryServiceCenterDTOS.size() > 0){
                        QueryServiceCenterDTO queryServiceCenterDTO = queryServiceCenterDTOS.get(0);
                        item.setOrgPhone(queryServiceCenterDTO.getPrincipalPhone());
                    }
                });
            });
        });
        return StatusDto.buildDataSuccessStatusDto(queryClaimorderListDTOPage);
    }

    /**
     * 查询索赔单的分页列表
     * @param claimOrdno 索赔单号
     * @param keyword 查询条件
     * @param docStatus 索赔单状态
     * @param offset 偏移量
     * @param pageSize 每页显示数量
     * @return Page<QueryClaimorderListDTO> 分页的索赔单信息
     * @author zhangkangjian
     * @date 2018-09-19 17:16:52
     */
    private Page<QueryClaimorderListDTO> queryClaimorderPage(String claimOrdno, String keyword, String docStatus, int offset, int pageSize) {
        BusinessUser loggedUser = userHolder.getLoggedUser();
        Organization organization = loggedUser.getOrganization();
        String orgCode = organization.getOrgCode();
        // 如果时平台的话，查询所有的索赔单
        if(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM.equals(orgCode)){
            orgCode = null;
        }
        return claimOrderDao.queryClaimorderList(claimOrdno, keyword, docStatus, orgCode, offset, pageSize);
    }

    /**
     * 更新索赔单状态
     * @param claimOrdno 索赔单号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    @Override
    public void updateDocStatus(String claimOrdno, String docStatus) {
        claimOrderDao.updateDocStatus(claimOrdno, docStatus);
    }

    /**
     * 更新索赔单状态和验收时间
     * @param claimOrdno 索赔单号
     * @param docStatus  索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    @Override
    public void updateDocStatusAndProcessTime(String claimOrdno, String docStatus) {
        claimOrderDao.updateDocStatusAndProcessTime(claimOrdno, docStatus);
    }

    /**
     * 索赔单付款
     * @param claimOrdno 索赔单号
     * @param docStatus  索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    @Override
    public StatusDto billOfPayment(String claimOrdno, String docStatus, BigDecimal actualAmount) {
        if(null == actualAmount){
            throw new CommonException("0", "请填写付款金额！");
        }
        BizServiceClaimorder claimorder = claimOrderDao.findClaimOrderDetail(claimOrdno);
        // 构建申请单
        List<AccountTransactionDTO> payments = buildClaimPayment(claimorder,actualAmount);
        // 支付
        StatusDto statusDto = bizFinanceAccountService.makeTrading(payments);
        // 如果支付成功
        if(statusDto.isSuccess()){
            // 更新索赔单状态和支付时间
            claimOrderDao.updateDocStatusAndRepayTime(claimOrdno, docStatus,actualAmount);
        }else{
            return statusDto;
        }
        return StatusDto.buildSuccessStatusDto("支付成功！");
    }

    /**
     *  构建维修单
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-12 10:07:36
     */
    private List<AccountTransactionDTO> buildClaimPayment(BizServiceClaimorder claimorder, BigDecimal actualAmount){
        List<AccountTransactionDTO> list = new ArrayList<AccountTransactionDTO>();
        //  付款方
        AccountTransactionDTO accountPayer = buildAccountTransactionDTO(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM,claimorder.getClaimOrdno());
        // 收款方
        AccountTransactionDTO accountReceive = buildAccountTransactionDTO(claimorder.getClaimOrgno(),claimorder.getClaimOrdno());
        // 付款金额
        accountPayer.setAmount(0 - actualAmount.doubleValue());
        accountReceive.setAmount(actualAmount.doubleValue());
        list.add(accountReceive);
        list.add(accountPayer);
        return list;
    }
    /**
     *  构建支付对象
     * @param orgNo 组织机构
     * @param applyNo 单号
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-12 10:50:36
     */
    private AccountTransactionDTO buildAccountTransactionDTO(String orgNo,String applyNo){
        AccountTransactionDTO transaction = new AccountTransactionDTO();
        // 账户
        transaction.setOrganizationCode(orgNo);
        // 账户类型
        transaction.setAccountTypeEnumThrift(AccountTypeEnumThrift.SMALL_CHANGE);
        // 业务单号
        transaction.setBusinessSourceDocumentNumber(applyNo);
        // 交易类型
        transaction.setTransactionTypeEnumThrift(TransactionTypeEnumThrift.THREE_PACK_SUPPLIER_REFUND);
        transaction.setCreator(userHolder.getLoggedUserId());
        return transaction;
    }
}
