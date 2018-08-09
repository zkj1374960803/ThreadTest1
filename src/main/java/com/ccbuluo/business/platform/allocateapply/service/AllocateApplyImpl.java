package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:29:54
 */
@Service
public class AllocateApplyImpl implements AllocateApply{
    @Resource
    BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    UserHolder userHolder;
    @Resource
    BizServiceStorehouseDao bizServiceStorehouseDao;
    @Resource
    GenerateDocCodeService generateDocCodeService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
//    BasicMerchandiseSer
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;

    /**
     * 创建物料或者零配件申请
     * @param bizAllocateApply 申请单实体
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    @Override
    public void createAllocateApply(BizAllocateApply bizAllocateApply) {
        String loggedUserId = userHolder.getLoggedUserId();
        bizAllocateApply.setOperator(loggedUserId);
        bizAllocateApply.setCreator(loggedUserId);
        bizAllocateApply.setApplyer(loggedUserId);
        String orgCode = bizServiceStorehouseDao.getOrgCodeByStoreHouseCode(bizAllocateApply.getInRepositoryNo());
        bizAllocateApply.setInstockOrgno(orgCode);
        StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(CodePrefixEnum.SW);
        bizAllocateApply.setApplyNo(stringStatusDto.getData());
        BusinessUser loggedUser = userHolder.getLoggedUser();
        if(loggedUser != null){
            Organization organization = loggedUser.getOrganization();
            if(organization != null){
                String orgCodeUser = organization.getOrgCode();
                if(orgCodeUser != null){
                    bizAllocateApply.setApplyorgNo(orgCodeUser);
                    if(orgCodeUser.equals(BusinessPropertyHolder.TOP_SERVICECENTER)){
                        // 等待付款状态
                        bizAllocateApply.setApplyStatus("20");
                    }
                    // 待处理
                    bizAllocateApply.setApplyStatus("10");
                }
            }
        }
        // 组织架构类型
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(bizAllocateApply.getOutstockOrgno());
        if(orgByCode.isSuccess()){
            StatusDto<BasicUserOrganization> resolve = StatusDtoThriftUtils.resolve(orgByCode, BasicUserOrganization.class);
            BasicUserOrganization data = resolve.getData();
            if(data != null){
                bizAllocateApply.setOutstockOrgtype(data.getOrgType());
            }
        }

        // 保存申请单基础数据
        bizAllocateApplyDao.saveEntity(bizAllocateApply);
        // 保存申请单详情数据
        List<BizAllocateapplyDetail> allocateapplyDetailList = bizAllocateApply.getAllocateapplyDetailList();
        allocateapplyDetailList.stream().forEach(a -> {
            a.setApplyNo(bizAllocateApply.getApplyNo());
            a.setOperator(loggedUserId);
            a.setCreator(loggedUserId);
        });
        bizAllocateApplyDao.batchInsertForapplyDetailList(allocateapplyDetailList);

    }

    /**
     * 查询申请单详情
     *
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    @Override
    public FindAllocateApplyDTO findDetail(String applyNo) {
        FindAllocateApplyDTO allocateApplyDTO = bizAllocateApplyDao.findDetail(applyNo);
        // 查询组织架构的名字
        StatusDtoThriftBean<BasicUserOrganization> outstockOrgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getOutstockOrgno());
        StatusDtoThriftBean<BasicUserOrganization> instockOrgName = basicUserOrganizationService.findOrgByCode(allocateApplyDTO.getInstockOrgno());
        StatusDto<BasicUserOrganization> outstockOrgNameresolve = StatusDtoThriftUtils.resolve(outstockOrgName, BasicUserOrganization.class);
        StatusDto<BasicUserOrganization> instockOrgNameresolve = StatusDtoThriftUtils.resolve(instockOrgName, BasicUserOrganization.class);
        BasicUserOrganization outstockOrgdata = outstockOrgNameresolve.getData();
        BasicUserOrganization instockOrgdata = instockOrgNameresolve.getData();
        if (outstockOrgdata != null) {
            String orgName = outstockOrgdata.getOrgName();
            allocateApplyDTO.setOutstockOrgName(orgName);
        }
        if (instockOrgdata != null) {
            String orgName = instockOrgdata.getOrgName();
            allocateApplyDTO.setInstockOrgName(orgName);
        }
        // 查询申请单的详单
        List<QueryAllocateapplyDetailDTO> queryAllocateapplyDetailDTOS = bizAllocateApplyDao.queryAllocateapplyDetail(applyNo);
        if (queryAllocateapplyDetailDTOS != null) {
            String productType = queryAllocateapplyDetailDTOS.get(0).getProductType();
            if (!Constants.PRODUCT_TYPE_EQUIPMENT.equals(productType)) {
                // 查询零配件的名称
                List<String> productNo = queryAllocateapplyDetailDTOS.stream().map(QueryAllocateapplyDetailDTO::getProductNo).collect(Collectors.toList());
                StatusDtoThriftList<BasicCarpartsProductDTO> basicCarpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCarpartsCodes(productNo);
                if (basicCarpartsProductDTOList.isSuccess()) {
                    StatusDto<List<BasicCarpartsProductDTO>> resolve = StatusDtoThriftUtils.resolve(basicCarpartsProductDTOList, BasicCarpartsProductDTO.class);
                    List<BasicCarpartsProductDTO> basicCarpartsProductList = resolve.getData();
                    if (basicCarpartsProductList != null) {
                        Map<String, BasicCarpartsProductDTO> basicCarpartsProductDTOMap = basicCarpartsProductList.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, a -> a, (k1, k2) -> k1));
                        queryAllocateapplyDetailDTOS.stream().forEach(a -> {
                            BasicCarpartsProductDTO basicCarpartsProductDTO = basicCarpartsProductDTOMap.get(a.getProductNo());
                            if (basicCarpartsProductDTO != null) {
                                a.setProductName(basicCarpartsProductDTO.getCarpartsName());
                            }
                        });
                    }
                }
            }
        }
        allocateApplyDTO.setQueryAllocateapplyDetailDTO(queryAllocateapplyDetailDTOS);
        return allocateApplyDTO;
    }
}
