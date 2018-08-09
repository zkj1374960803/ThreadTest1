package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindAllocateApplyDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateapplyDetailDTO;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateApply;
import com.ccbuluo.business.platform.allocateapply.entity.BizAllocateapplyDetail;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.entity.Organization;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
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
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
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
        StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.SW);
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
        // 如果是采购类型的，入库机构是平台
        String processType = bizAllocateApply.getProcessType();
        if(Constants.PROCESS_TYPE_PURCHASE.equals(processType)){
            bizAllocateApply.setApplyorgNo(BusinessPropertyHolder.TOP_SERVICECENTER);
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

    /**
     * 查询申请列表
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @Override
    public Page<QueryAllocateApplyListDTO> findApplyList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize) {
        // 获取用户的组织机构
        String userOrgCode = getUserOrgCode();
        // 查询分页的申请列表
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findApplyList(processType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        return page;
    }

    /**
     * 获取用户的组织机构
     * @return String 用户的orgCode
     * @author zhangkangjian
     * @date 2018-08-09 15:38:41
     */
    private String getUserOrgCode() {
        BusinessUser loggedUser = userHolder.getLoggedUser();
        if(loggedUser != null){
            Organization organization = loggedUser.getOrganization();
            if(organization == null){
                throw new CommonException(Constants.ERROR_CODE, loggedUser.getName() + ":组织架构数据异常");
            }
            String orgCode = organization.getOrgCode();
            if(StringUtils.isBlank(orgCode)){
                throw new CommonException(Constants.ERROR_CODE, loggedUser.getName() + ":组织架构数据异常");
            }
            return orgCode;
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询处理申请列表
     *
     * @param processType
     * @param applyStatus
     * @param applyNo
     * @param offset
     * @param pageSize
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    @Override
    public Page<QueryAllocateApplyListDTO> findProcessApplyList(String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize) {
        String userOrgCode = getUserOrgCode();
        // 查询分页的申请列表
        Page<QueryAllocateApplyListDTO> page = bizAllocateApplyDao.findProcessApplyList(processType, applyStatus, applyNo, offset, pageSize, userOrgCode);
        return page;
    }
}
