package com.ccbuluo.business.platform.claimorder.service;

import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.entity.BizServiceorderDetail;
import com.ccbuluo.business.platform.claimorder.dao.ClaimOrderDao;
import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorderDTO;
import com.ccbuluo.business.platform.claimorder.dto.QueryClaimorderListDTO;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.dao.BizServiceorderDetailDao;
import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import com.ccbuluo.business.platform.order.dto.SaveMaintaintemDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        // 在保 过保
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
     * @param bizServiceClaimorder@exception
     * @author zhangkangjian
     * @date 2018-09-08 14:26:55
     */
    @Override
    public void updateClaimOrder(BizServiceClaimorder bizServiceClaimorder) {
        // 设置索赔单的状态
        // 待验收
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
        // 查询基础信息
        BizServiceClaimorder serviceClaimorderDetail = claimOrderDao.findClaimOrderDetailByClaimOrdno(bizServiceClaimorder);
        // 查询车牌号
        BizServiceOrder byOrderNo = bizServiceOrderDao.getByOrderNo(serviceClaimorderDetail.getServiceOrdno());
        String carNo = byOrderNo.getCarNo();
        serviceClaimorderDetail.setCarNo(carNo);
        // 查询 在保 工时信息
        productDetailDTO.setWarrantyType(BizServiceorderDetail.WarrantyTypeEnum.INSHELFLIFE.name());
        // 工时类型
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.MAINTAINITEM.name());
        List<ProductDetailDTO> maintainitemDetail = claimOrderDao.findMaintainitemDetail(productDetailDTO);
        // 查询零配件信息
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.FITTING.name());
        List<ProductDetailDTO> fittingDetail = claimOrderDao.findMaintainitemDetail(productDetailDTO);
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
            Map<String, BasicCarpartsProductDTO> basicCarpartsProductMap = basicCarpartsProductDTOList.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, b -> b,(k1,k2)->k1));
            a.forEach(c->{
                String productNo = c.getProductNo();
                BasicCarpartsProductDTO carparts = basicCarpartsProductMap.get(productNo);
                c.setProductName(carparts.getCarpartsName());
                c.setCarModelName(carparts.getFitCarmodel());
                c.setProductCategoryname(carparts.getCategoryCodePath());
                c.setProductUnit(carparts.getUnitName());
            });
        });
        serviceClaimorderDetail.setFittingDetail(fittingDetail);
        serviceClaimorderDetail.setMaintainitemDetail(maintainitemDetail);
        return serviceClaimorderDetail;
    }

    /**
     * 查询索赔单的列表
     * @param claimOrdno
     * @param offset
     * @param pageSize
     * @return
     * @throws
     * @author zhangkangjian
     * @date 2018-09-08 16:55:56
     */
    @Override
    public StatusDto<Page<QueryClaimorderListDTO>> queryClaimorderList(String claimOrdno, String docStatus, int offset, int pageSize) {
        BusinessUser loggedUser = userHolder.getLoggedUser();
        Organization organization = loggedUser.getOrganization();
        String orgCode = organization.getOrgCode();
        Page<QueryClaimorderListDTO> queryClaimorderListDTOPage = claimOrderDao.queryClaimorderList(claimOrdno, docStatus, orgCode, offset, pageSize);
        return StatusDto.buildDataSuccessStatusDto(queryClaimorderListDTOPage);
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
     *
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
     * 更新索赔单状态和支付时间
     * @param claimOrdno 索赔单号
     * @param docStatus  索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    @Override
    public void updateDocStatusAndRepayTime(String claimOrdno, String docStatus) {
        claimOrderDao.updateDocStatusAndRepayTime(claimOrdno, docStatus);
    }
}
