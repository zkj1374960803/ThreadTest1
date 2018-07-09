package com.ccbuluo.business.platform.servicecenter.service;

import com.auth0.jwt.internal.com.fasterxml.jackson.core.type.TypeReference;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceLabel;
import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.platform.label.dto.LabelServiceCenterDTO;
import com.ccbuluo.business.platform.label.service.LabelServiceCenterService;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.servicecenter.dto.SaveServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchListDTO;
import com.ccbuluo.business.platform.servicecenter.dto.SearchServiceCenterDTO;
import com.ccbuluo.business.platform.servicecenter.servicecenterenum.ServiceCenterEnum;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseServiceImpl;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.usercoreintf.dto.EditServiceCenterDTO;
import com.ccbuluo.usercoreintf.dto.OrgWorkplaceDTO;
import com.ccbuluo.usercoreintf.dto.ServiceCenterWorkplaceDTO;
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

import java.io.IOException;
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

    private static final String AFTERSALESERVICECENTER = "(售后服务中心所属职场)";

    /**
     * 保存服务中心
     * @param saveServiceCenterDTO 服务中心实体
     * @return 保存状态
     * @author liuduo
     * @date 2018-07-04 09:40:53
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveServiceCenter(SaveServiceCenterDTO saveServiceCenterDTO)  throws TException{
        try {
            // 生成服务中心code
            String serviceCenterCode = generateProjectCodeService.grantCode(CodePrefixEnum.FW);
            // 保存服务中心
            Long serviceCenterId = createServiceCenter(saveServiceCenterDTO, serviceCenterCode);
            if (serviceCenterId == null) {
                return Constants.FAILURESTATUS;
            } else if (serviceCenterId == Constants.LONG_FLAG_DEFAULT) {
                return Constants.FAILURE_TWO;
            } else if (serviceCenterId == Constants.LONG_ORG_ERROR) {
                return Constants.ORG_ERROR;
            }
            // 保存职场
            int workplaceStatus = crteateWorkplace(saveServiceCenterDTO, serviceCenterCode);
            if (workplaceStatus == Constants.FAILURESTATUS) {
                return Constants.FAILURESTATUS;
            }
            // 保存标签
            int[] ids = saveLableServiceCenter(saveServiceCenterDTO, serviceCenterCode);
            if (ids.length == 0) {
                return Constants.FAILURESTATUS;
            }
            // 保存仓库
            int status = saveStoreHouse(saveServiceCenterDTO, serviceCenterCode);
            return status;
        } catch (Exception e) {
            logger.error("保存失败！", e);
            throw e;
        }
    }

    private int crteateWorkplace(SaveServiceCenterDTO saveServiceCenterDTO, String serviceCenterCode) throws TException {
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
    public SearchServiceCenterDTO getByCode(String serviceCenterCode) throws TException {
        // 查询服务中心和职场
        OrgWorkplaceDTO byCode = orgService.getByCode(serviceCenterCode);
        // 查询标签
        List<BizServiceLabel> bizServiceLabelList = labelServiceCenterService.getLabelServiceCenterByCode(serviceCenterCode);
        // 查询仓库
        List<BizServiceStorehouse> bizServiceStorehouseLis = storeHouseService.getStorehousrByCode(serviceCenterCode);
        SearchServiceCenterDTO searchServiceCenterDTO = new SearchServiceCenterDTO();
        searchServiceCenterDTO.setServiceCenterName(byCode.getOrgName());
        searchServiceCenterDTO.setServiceCenterCode(byCode.getOrgCode());
        searchServiceCenterDTO.setServiceCenterStatus(byCode.getOrgStatus());
        searchServiceCenterDTO.setPrincipalPhone(byCode.getPrincipalPhone());
        searchServiceCenterDTO.setSignTime(byCode.getSignTime());
        searchServiceCenterDTO.setSignoutTime(byCode.getSignoutTime());
        searchServiceCenterDTO.setAddress(byCode.getAddress());
        searchServiceCenterDTO.setRemark(byCode.getRemark());
        searchServiceCenterDTO.setLabels(bizServiceLabelList);
        searchServiceCenterDTO.setStorehouseList(bizServiceStorehouseLis);

        return searchServiceCenterDTO;
    }

    /**
     * 编辑服务中心
     * @param serviceCenterCode 服务中心code
     * @param serviceCenterName 服务中心名称
     * @param labels 标签ids
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-07-05 11:10:30
     */
    @Override
    public int editServiceCenter(String serviceCenterCode, String serviceCenterName, String labels) throws TException {
        EditServiceCenterDTO editServiceCenterDTO = new EditServiceCenterDTO();
        editServiceCenterDTO.setServiceCenterCode(serviceCenterCode);
        editServiceCenterDTO.setServiceCenterName(serviceCenterName);
        editServiceCenterDTO.setOperateTime(System.currentTimeMillis());
        editServiceCenterDTO.setOperator(userHolder.getLoggedUserId());
        // 修改服务中心名称
        int status = orgService.editServiceCenter(editServiceCenterDTO);
        if (status == Constants.SUCCESSSTATUS) {
            // 修改标签与服务中心关联关系
            int affected = labelServiceCenterService.editLabelServiceCenter(serviceCenterCode, labels);
            if (affected == Constants.FAILURESTATUS) {
                return Constants.FAILURESTATUS;
            }
        }
        return status;
    }

    /**
     * 根据服务中心code查询职场
     * @param serviceCenterCode 服务中心code
     * @return 职场信息
     * @author liuduo
     * @date 2018-07-05 13:49:42
     */
    @Override
    public ServiceCenterWorkplaceDTO getWorkplaceByCode(String serviceCenterCode) throws TException {
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
    public int editWorkplace(ServiceCenterWorkplaceDTO serviceCenterWorkplaceDTO) throws TException {
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
    public Map<String, Object> queryList(SearchListDTO searchListDTO) throws TException, IOException {
        String serviceCenterList = orgService.queryServiceCenterList(searchListDTO.getProvince(),
                                                                         searchListDTO.getCity() ,
                                                                         searchListDTO.getArea() ,
                                                                         searchListDTO.getStatus(),
                                                                         searchListDTO.getKeyword(),
                                                                         searchListDTO.getOffset(),
                                                                         searchListDTO.getPagesize());
        ObjectMapper mapper = new ObjectMapper();
        // 要返回的对象
        Map<String, Object> obj = mapper.readValue(serviceCenterList, new TypeReference<Map<String, Object>>() {});
        // 服务中心集合
        List<Map<String, Object>> rows = (List<Map<String, Object>>)obj.get("rows");
        List<Long> ids = rows.stream().map(a -> ((Integer)a.get("id")).longValue()).collect(Collectors.toList());

        if (ids.size() == 0) {
            return Collections.emptyMap();
        }
        // 统计门店下用户的数量
        Map<Long, Long> storeUserNumber = userService.queryCountUserNumberByOrgId(ids);
        for (Map<String, Object> map : rows) {
            Long id = ((Integer) map.get("id")).longValue();
            map.put("serviceCenterUserNumber", storeUserNumber.get(id));
        }
        return obj;
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
    public int editOrgStatus(String serviceCenterCode, Integer serviceCenterStatus) throws TException {
        return orgService.editOrgStatus(serviceCenterCode, serviceCenterStatus, userHolder.getLoggedUserId());
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
        if (StringUtils.isBlank(labelIds)) {
            return new int[0];
        }
        String[] split = labelIds.split(Constants.COMMA);
        for (String lable : split) {
            lables.add(Long.valueOf(lable));
        }
        List<LabelServiceCenterDTO> lableList = new ArrayList<>();
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
    private Long createServiceCenter(SaveServiceCenterDTO saveServiceCenterDTO, String serviceCenterCode) throws TException {
        // 保存服务中心
        BasicUserOrganization basicUserOrganization = new BasicUserOrganization();
        basicUserOrganization.setOrgCode(serviceCenterCode);
        basicUserOrganization.setOrgName(saveServiceCenterDTO.getServiceCenterName());
        basicUserOrganization.setOrgType(ServiceCenterEnum.SERVICECENTER.toString());
        basicUserOrganization.setCreator(userHolder.getLoggedUserId());
        basicUserOrganization.setOperator(userHolder.getLoggedUserId());
        basicUserOrganization.setTopOrgCode(BusinessPropertyHolder.TOP_SERVICECENTER);
        Long id = orgService.save(basicUserOrganization);
        return id;
    }
}
