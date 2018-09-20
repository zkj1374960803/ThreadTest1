package com.ccbuluo.business.platform.servicecenter.service;

import com.ccbuluo.account.BizFinanceAccountService;
import com.ccbuluo.account.BusinessItemTypeEnumThrift;
import com.ccbuluo.account.BusinessTypeEnumThrift;
import com.ccbuluo.account.OrganizationTypeEnumThrift;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.platform.label.dto.LabelServiceCenterDTO;
import com.ccbuluo.business.platform.label.service.LabelServiceCenterService;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.servicecenter.dto.SaveServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchListDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.servicecenterenum.ServiceCenterEnum;
import com.ccbuluo.business.platform.servicelog.service.ServiceLogService;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseServiceImpl;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.usercoreintf.dto.*;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.model.BasicUserWorkplace;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.BasicUserWorkplaceService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务中心service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-03 19:22:27
 */
@Service
public class ServiceCenterServiceImpl implements ServiceCenterService{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GenerateProjectCodeService generateProjectCodeService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private LabelServiceCenterService labelServiceCenterService;
    @Autowired
    private StoreHouseServiceImpl storeHouseService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserWorkplaceService workplaceService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService userService;
    @ThriftRPCClient("BasicWalletpaymentSerService")
    private BizFinanceAccountService bizFinanceAccountService;
    @Autowired
    private ServiceLogService serviceLogService;

    private static final String AFTERSALESERVICECENTER = "(售后服务中心所属职场)";
    private static final String SAVEFAILURE = "保存失败！";
    private static final String SAVESUCCESS = "保存成功！";

    /**
     * 保存服务中心
     * @param saveServiceCenterDTO 服务中心实体
     * @return 保存状态
     * @author liuduo
     * @date 2018-07-04 09:40:53
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto<String> saveServiceCenter(SaveServiceCenterDTO saveServiceCenterDTO) throws Exception{
        try {
            // 生成服务中心code
            String serviceCenterCode = null;
            StatusDto<String> serviceCenterDTO = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FW);
            if (serviceCenterDTO.getCode().equals(Constants.SUCCESS_CODE)) {
                serviceCenterCode = serviceCenterDTO.getData();
            } else {
                return serviceCenterDTO;
            }
            // 保存标签
            int[] ids = saveLableServiceCenter(saveServiceCenterDTO, serviceCenterCode);
            if (ids.length == Constants.FAILURESTATUS) {
                return StatusDto.buildFailure(SAVEFAILURE);
            }
            // 保存仓库
            int status = saveStoreHouse(saveServiceCenterDTO, serviceCenterCode);
            if (status == Constants.FAILURE_ONE) {
                return StatusDto.buildFailure("仓库名字已存在，请核对！");
            } else if (status == Constants.FAILURESTATUS) {
                return StatusDto.buildFailure(SAVEFAILURE);
            }

            // 保存服务中心
            StatusDtoThriftLong<Long> serviceCenterId = createServiceCenter(saveServiceCenterDTO, serviceCenterCode);
            if (serviceCenterId.getCode().equals(Constants.ERROR_CODE)) {
                throw new IllegalAccessException(serviceCenterId.getMessage());
            }

            // 保存职场
            StatusDto<String> workplaceStatus = crteateWorkplace(saveServiceCenterDTO, serviceCenterCode);
            if (workplaceStatus.getCode().equals(Constants.ERROR_CODE)) {
                throw new IllegalAccessException(SAVEFAILURE);
            }

            // 生成支付账号
            StatusDto<String> stringStatusDto = bizFinanceAccountService.saveBizFinanceAccount(saveServiceCenterDTO.getServiceCenterName(),
                                                                                                BusinessTypeEnumThrift.AFTER_SALE,
                                                                                                BusinessItemTypeEnumThrift.SERVICE_CENTER,
                                                                                                serviceCenterCode,
                                                                                                saveServiceCenterDTO.getServiceCenterName(),
                                                                                                OrganizationTypeEnumThrift.ORGANIZATION,
                                                                                                userHolder.getLoggedUserId());
            String code = stringStatusDto.getCode();
            if (null != code && code.equals(Constants.ERROR_CODE)) {
                throw new IllegalAccessException(stringStatusDto.getMessage());
            }
            BizServiceLog bizServiceLog = new BizServiceLog();
            bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
            bizServiceLog.setAction(BizServiceLog.actionEnum.SAVE.name());
            bizServiceLog.setSubjectType("ServiceCenterServiceImpl");
            bizServiceLog.setSubjectKeyvalue(serviceCenterCode);
            bizServiceLog.setLogContent("保存服务中心");
            bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
            bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
            bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
            serviceLogService.create(bizServiceLog);
            return StatusDto.buildSuccessStatusDto(SAVESUCCESS);
        } catch (Exception e) {
            logger.error(SAVEFAILURE, e);
            throw e;
        }
    }

    private StatusDto<String> crteateWorkplace(SaveServiceCenterDTO saveServiceCenterDTO, String serviceCenterCode) {
        // 保存职场
        BasicUserWorkplace basicUserWorkplace = new BasicUserWorkplace();
        basicUserWorkplace.setWorkplaceName(saveServiceCenterDTO.getServiceCenterName()+AFTERSALESERVICECENTER);
        basicUserWorkplace.setOrgCode(serviceCenterCode);
        basicUserWorkplace.setWorkplaceStatus(Constants.FLAG_ONE);
        basicUserWorkplace.setPlaceType(ServiceCenterEnum.ADDED.toString());
        basicUserWorkplace.setProvince(saveServiceCenterDTO.getProvince());
        basicUserWorkplace.setCity(saveServiceCenterDTO.getCity());
        basicUserWorkplace.setArea(saveServiceCenterDTO.getArea());
        basicUserWorkplace.setAddress(saveServiceCenterDTO.getAddress());
        basicUserWorkplace.setLongitude(saveServiceCenterDTO.getLongitude());
        basicUserWorkplace.setLatitude(saveServiceCenterDTO.getLatitude());
        basicUserWorkplace.setCreator(userHolder.getLoggedUserId());
        basicUserWorkplace.setOperator(userHolder.getLoggedUserId());
        return workplaceService.save(basicUserWorkplace);
    }

    /**
     * 服务中心详情
     * @param serviceCenterCode 服务中心code
     * @return 服务中心详情
     * @author liuduo
     * @date 2018-07-05 09:15:47
     */
    @Override
    public SearchServiceCenterDTO getByCode(String serviceCenterCode) {
        // 查询服务中心和职场
        StatusDtoThriftBean<OrgWorkplaceDTO> byCode = orgService.getByCode(serviceCenterCode);
        StatusDto<OrgWorkplaceDTO> resolve = StatusDtoThriftUtils.resolve(byCode, OrgWorkplaceDTO.class);
        OrgWorkplaceDTO data = resolve.getData();
        // 查询标签
        List<BizServiceLabel> bizServiceLabelList = labelServiceCenterService.getLabelServiceCenterByCode(serviceCenterCode);
        // 查询仓库
        List<BizServiceStorehouse> bizServiceStorehouseLis = storeHouseService.getStorehousrByCode(serviceCenterCode);
        SearchServiceCenterDTO searchServiceCenterDTO = new SearchServiceCenterDTO();
        searchServiceCenterDTO.setServiceCenterName(data.getOrgName());
        searchServiceCenterDTO.setServiceCenterCode(data.getOrgCode());
        searchServiceCenterDTO.setServiceCenterStatus(data.getOrgStatus());
        searchServiceCenterDTO.setPrincipalPhone(data.getPrincipalPhone());
        searchServiceCenterDTO.setSignTime(data.getSignTime());
        searchServiceCenterDTO.setSignoutTime(data.getSignoutTime());
        searchServiceCenterDTO.setAddress(data.getAddress());
        searchServiceCenterDTO.setRemark(data.getRemark());
        searchServiceCenterDTO.setLabels(bizServiceLabelList);
        searchServiceCenterDTO.setStorehouseList(bizServiceStorehouseLis);

        return searchServiceCenterDTO;
    }

    /**
     * 编辑服务中心
     * @param serviceCenterCode 服务中心code
     * @param serviceCenterName 服务中心名称
     * @param labelIds 标签ids
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-05 11:10:30
     */
    @Override
    public StatusDto<String> editServiceCenter(String serviceCenterCode, String serviceCenterName, String labelIds)  {
        EditServiceCenterDTO editServiceCenterDTO = new EditServiceCenterDTO();
        editServiceCenterDTO.setServiceCenterCode(serviceCenterCode);
        editServiceCenterDTO.setServiceCenterName(serviceCenterName);
        editServiceCenterDTO.setOperateTime(System.currentTimeMillis());
        editServiceCenterDTO.setOperator(userHolder.getLoggedUserId());
        // 修改服务中心名称
        StatusDto<String> status = orgService.editServiceCenter(editServiceCenterDTO);
        if (status.getCode().equals(Constants.SUCCESS_CODE)) {
            // 修改标签与服务中心关联关系
            int affected = labelServiceCenterService.editLabelServiceCenter(serviceCenterCode, labelIds);
            if (affected == Constants.FAILURESTATUS) {
                return StatusDto.buildFailure("编辑失败！");
            }
        }
        if (status.getCode().equals(Constants.ERROR_CODE)) {
            return StatusDto.buildFailure(status.getMessage());
        }

        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
        bizServiceLog.setSubjectType("ServiceCenterServiceImpl");
        bizServiceLog.setSubjectKeyvalue(serviceCenterCode);
        bizServiceLog.setLogContent("编辑服务中心");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return StatusDto.buildSuccessStatusDto("编辑成功！");
    }

    /**
     * 根据服务中心code查询职场
     * @param serviceCenterCode 服务中心code
     * @return 职场信息
     * @author liuduo
     * @date 2018-07-05 13:49:42
     */
    @Override
    public StatusDtoThriftBean<ServiceCenterWorkplaceDTO> getWorkplaceByCode(String serviceCenterCode) {
        return workplaceService.getWorkplaceByCode(serviceCenterCode);
    }

    /**
     * 编辑职场
     * @param serviceCenterWorkplaceDTO 职场实体
     * @return 编辑状态
     * @author liuduo
     * @date 2018-07-05 14:09:09
     */
    @Override
    public StatusDto<String> editWorkplace(ServiceCenterWorkplaceDTO serviceCenterWorkplaceDTO) {
        serviceCenterWorkplaceDTO.setOperator(userHolder.getLoggedUserId());
        serviceCenterWorkplaceDTO.setOperateTime(System.currentTimeMillis());
        return workplaceService.editServiceCenterWorkplace(serviceCenterWorkplaceDTO);
    }

    /**
     * 查询服务中心列表
     * @param searchListDTO 查询服务中心列表参数
     * @return 服务中心列表
     * @author liuduo
     * @date 2018-07-03 14:27:11
     */
    @Override
    public StatusDtoThriftPage<QueryServiceCenterDTO> queryList(SearchListDTO searchListDTO) {
        StatusDtoThriftPage<QueryServiceCenterDTO> serviceCenterList = orgService.queryServiceCenterList(searchListDTO.getProvince(),
                                                                         searchListDTO.getCity() ,
                                                                         searchListDTO.getArea() ,
                                                                         searchListDTO.getStatus(),
                                                                         searchListDTO.getKeyword(),
                                                                         searchListDTO.getOffset(),
                                                                         searchListDTO.getPagesize());
        StatusDto<Page<QueryServiceCenterDTO>> resolve = StatusDtoThriftUtils.resolve(serviceCenterList, QueryServiceCenterDTO.class);
        Page<QueryServiceCenterDTO> data = resolve.getData();
        List<QueryServiceCenterDTO> rows = data.getRows();
        List<Long> ids = rows.stream().map(a -> a.getId()).collect(Collectors.toList());

        if (ids.size() == 0) {
            return StatusDtoThriftUtils.build(null);
        }
        // 统计门店下用户的数量
        StatusDtoThriftList<QueryCountUserNumberDTO> storeUserNumber = userService.queryCountUserNumberByOrgId(ids);
        StatusDto<List<QueryCountUserNumberDTO>> resolve1 = StatusDtoThriftUtils.resolve(storeUserNumber, QueryCountUserNumberDTO.class);
        List<QueryCountUserNumberDTO> data1 = resolve1.getData();
        Map<Long, Long> collect = data1.stream().collect(Collectors.toMap(a -> a.getStoreId(), b -> b.getCountNum()));
        for (QueryServiceCenterDTO serviceCenterDTO : rows) {
            serviceCenterDTO.setServiceCenterUserNumber(collect.get(serviceCenterDTO.getId()));
        }
        return StatusDtoThriftUtils.buildSuccess(data);
    }

    /**
     * 服务中心启停
     * @param serviceCenterCode 服务中心code
     * @param serviceCenterStatus 服务中心状态
     * @return 操作是否成功
     * @author liuduo
     * @date 2018-07-06 10:11:00
     */
    @Override
    public StatusDto<String> editOrgStatus(String serviceCenterCode, Integer serviceCenterStatus) {
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
        bizServiceLog.setSubjectType("ServiceCenterServiceImpl");
        bizServiceLog.setSubjectKeyvalue(serviceCenterCode);
        bizServiceLog.setLogContent("服务中心启停");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return orgService.editOrgStatus(serviceCenterCode, serviceCenterStatus, userHolder.getLoggedUserId());
    }

    /**
     * 查询可用的服务中心和平台
     * @param province 省
     * @param city 市
     * @param area 区
     * @param orgType 机构类型
     * @param name 服务中心名字
     * @return 可用的服务中心
     * @author liuduo
     * @date 2018-07-06 10:00:52
     */
    @Override
    public List<QueryServiceCenterListDTO> findUsableServiceCenterAndPlatform(String province, String city, String area, String orgType, String name) {
        StatusDtoThriftList<QueryServiceCenterListDTO> queryServiceCenterListDTOStatusDtoThriftList = orgService.queryServiceCenter(province, city, area, name, orgType);
        return StatusDtoThriftUtils.resolve(queryServiceCenterListDTOStatusDtoThriftList, QueryServiceCenterListDTO.class).getData();
    }


    /**
     * 保存仓库
     * @param saveServiceCenterDTO 服务中心实体
     * @param serviceCenterCode 服务中心code
     * @return 保存状态
     * @author liuduo
     * @date 2018-07-04 19:55:03
     */
    private int saveStoreHouse(SaveServiceCenterDTO saveServiceCenterDTO, String serviceCenterCode) throws TException {
        SaveBizServiceStorehouseDTO saveBizServiceStorehouseDTO = new SaveBizServiceStorehouseDTO();
        saveBizServiceStorehouseDTO.setStorehouseName(saveServiceCenterDTO.getStorehouseName());
        saveBizServiceStorehouseDTO.setStorehouseAcreage(saveServiceCenterDTO.getStorehouseAcreage());
        saveBizServiceStorehouseDTO.setServicecenterCode(serviceCenterCode);
        saveBizServiceStorehouseDTO.setStorehouseStatus(Constants.LONG_FLAG_ONE);
        saveBizServiceStorehouseDTO.setProvinceName(saveServiceCenterDTO.getProvince());
        saveBizServiceStorehouseDTO.setCityName(saveServiceCenterDTO.getCity());
        saveBizServiceStorehouseDTO.setAreaName(saveServiceCenterDTO.getArea());
        saveBizServiceStorehouseDTO.setLongitude(saveServiceCenterDTO.getLongitude());
        saveBizServiceStorehouseDTO.setLatitude(saveServiceCenterDTO.getLatitude());
        saveBizServiceStorehouseDTO.setStorehouseAddress(saveServiceCenterDTO.getAddress());
        return storeHouseService.saveStoreHouse(saveBizServiceStorehouseDTO);
    }

    /**
     * 保存标签与服务中心关联关系
     * @param saveServiceCenterDTO 服务中心
     * @param serviceCenterCode 服务中心code
     * @return 保存成功的ids
     * @author liuduo
     * @date 2018-07-04 18:33:51
     */
    public int[] saveLableServiceCenter(SaveServiceCenterDTO saveServiceCenterDTO, String serviceCenterCode) {
        List<Long> lables = new ArrayList<>();
        String labelIds = saveServiceCenterDTO.getLabelIds();
        List<LabelServiceCenterDTO> lableList = new ArrayList<>();
        if (StringUtils.isBlank(labelIds)) {
            return new int[1];
        }
        String[] split = labelIds.split(Constants.COMMA);
        for (String lable : split) {
            lables.add(Long.valueOf(lable));
        }
        for (Long lableId : lables) {
            LabelServiceCenterDTO labelServiceCenterDTO = new LabelServiceCenterDTO();
            labelServiceCenterDTO.setLabelId(lableId);
            labelServiceCenterDTO.setServiceCenterCode(serviceCenterCode);
            lableList.add(labelServiceCenterDTO);
        }
        return labelServiceCenterService.save(lableList);
    }


    /**
     * 保存服务中心
     * @param saveServiceCenterDTO 服务中心实体dto
     * @return 服务中心id
     * @author liuduo
     * @date 2018-07-04 10:33:43
     */
    private StatusDtoThriftLong<Long> createServiceCenter(SaveServiceCenterDTO saveServiceCenterDTO, String serviceCenterCode)  {
        // 保存服务中心
        BasicUserOrganization basicUserOrganization = new BasicUserOrganization();
        basicUserOrganization.setOrgCode(serviceCenterCode);
        basicUserOrganization.setOrgName(saveServiceCenterDTO.getServiceCenterName());
        basicUserOrganization.setOrgType(ServiceCenterEnum.SERVICECENTER.toString());
        basicUserOrganization.setCreator(userHolder.getLoggedUserId());
        basicUserOrganization.setOperator(userHolder.getLoggedUserId());
        basicUserOrganization.setTopOrgCode(BusinessPropertyHolder.ORGCODE_TOP_SERVICECENTER);
        StatusDtoThriftLong<Long> id = orgService.save(basicUserOrganization);
        return id;
    }
}
