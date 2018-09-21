package com.ccbuluo.business.platform.custmanager.service;

import com.ccbuluo.account.BizFinanceAccountService;
import com.ccbuluo.account.BusinessItemTypeEnumThrift;
import com.ccbuluo.account.BusinessTypeEnumThrift;
import com.ccbuluo.account.OrganizationTypeEnumThrift;
import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.constants.StockPlanStatusEnum;
import com.ccbuluo.business.custmanager.allocateapply.dto.QueryPendingMaterialsDTO;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.BizServiceStorehouse;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.QueryCustManagerListDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.carmanage.dto.CusmanagerCarCountDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.custmanager.dao.BizServiceCustmanagerDao;
import com.ccbuluo.business.platform.custmanager.dto.CustManagerDetailDTO;
import com.ccbuluo.business.platform.custmanager.dto.QueryUserListDTO;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.business.platform.maintaincar.dao.BizServiceMaintaincarDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.storehouse.dao.BizServiceStorehouseDao;
import com.ccbuluo.business.platform.storehouse.dto.QueryStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.dto.SaveBizServiceStorehouseDTO;
import com.ccbuluo.business.platform.storehouse.service.StoreHouseService;
import com.ccbuluo.business.platform.supplier.service.SupplierServiceImpl;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.json.JsonUtils;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.usercoreintf.dto.BasicUserOrganizationDTO;
import com.ccbuluo.usercoreintf.dto.QueryNameByUseruuidsDTO;
import com.ccbuluo.usercoreintf.dto.QueryServiceCenterListDTO;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.model.RelUserRole;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.BasicUserRoleService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 客户经理实现端
 * @author zhangkangjian
 * @date 2018-07-18 10:26:50
 */
@Service
public class CustmanagerServiceImpl implements CustmanagerService{
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;
    @Autowired
    UserHolder userHolder;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserRoleService basicUserRoleService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @Resource
    private BizServiceCustmanagerDao bizServiceCustmanagerDao;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;
    @Resource
    private SupplierServiceImpl supplierServiceImpl;
    Logger logger = LoggerFactory.getLogger(getClass());
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;
    @Resource
    private BizServiceMaintaincarDao bizServiceMaintaincarDao;
    @Resource
    BasicCarcoreInfoService basicCarcoreInfoService;
    @Resource
    private StoreHouseService storeHouseService;
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyService;
    @Resource
    private BizServiceStorehouseDao bizServiceStorehouseDao;
    @ThriftRPCClient("BasicWalletpaymentSerService")
    private BizFinanceAccountService bizFinanceAccountService;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;

    /**
     * 创建客户经理
     * @param userInfoDTO           用户的基本信息
     * @param bizServiceCustmanager 用户业务数据
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-07-18 10:30:22
     */
    @Override
    public StatusDto<String> createCustManager(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager) {
        // 参数校验
        checkedRoleCodeAndOrgCode(userInfoDTO);
        // 保存基础用户
        String useruuid = saveUserAndOrg(userInfoDTO, bizServiceCustmanager);
        // 保存客户经理信息
        saveCustManager(bizServiceCustmanager, useruuid);
        return StatusDto.buildSuccessStatusDto();
    }
    /**
     *  保存客户经理信息
     * @param bizServiceCustmanager 客户经理信息
     * @param useruuid 用户uuid
     * @exception CommonException 自定义异常
     * @author zhangkangjian
     * @date 2018-07-21 12:01:28
     */
    private void saveCustManager(BizServiceCustmanager bizServiceCustmanager, String useruuid) {
        // 保存客户经理信息
        String loggedUserId = userHolder.getLoggedUserId();
        try {
            bizServiceCustmanager.setCreator(loggedUserId);
            bizServiceCustmanager.setOperator(loggedUserId);
            bizServiceCustmanager.setUserUuid(useruuid);
            // 手机号校验
            compareRepeat(bizServiceCustmanager.getUserUuid(), bizServiceCustmanager.getOfficePhone(), "office_phone", "biz_service_custmanager", "客户经理办公SIM卡手机号重复！");
            bizServiceCustmanagerDao.saveBizServiceCustmanager(bizServiceCustmanager);
        }catch (CommonException e){
            // 删除用户信息
            e.printStackTrace();
            innerUserInfoService.deleteUserInfoByUseruuid(useruuid);
            throw new CommonException(Constants.ERROR_CODE, e.getMessage());
        }
    }

    /**
     * 重复校验根据id判断
     * 例子：compareRepeat("2" , "18761326500", "supplier_phone", "biz_service_supplier", "手机号重复！");
     * @param id
     * @param value 验重的值
     * @param fields 数据库字段，验重的字段
     * @param tableName 数据库表名
     * @param tip 失败提示语 成功不提示语
     * @author zhangkangjian
     * @date 2018-07-03 16:06:48
     */
    public void compareRepeat(String id, String value, String fields, String tableName, String tip){
        List<String> ids = bizServiceCustmanagerDao.queryIds(value, fields, tableName);
        // 查询离职的用户uuid
        StatusDtoThriftList<String> statusDtoThriftList = innerUserInfoService.queryUserUuidBySatatus(Constants.FAILURESTATUS);
        StatusDto<List<String>> resolve = StatusDtoThriftUtils.resolve(statusDtoThriftList, String.class);
        List<String> data = resolve.getData();
        // 过滤掉离职的用户
        ids.removeAll(data);
        compareRepeat(id, ids ,tip);
    }

    /**
     * 比较两个uuid是否有重复的
     * @param id 用户的id
     * @param ids 用户ids
     * @param tip 提示语
     * @author zhangkangjian
     * @date 2018-05-23 16:05:19
     * @version v1.0.0
     */
    private void compareRepeat(String id, List<String> ids, String tip){
        if(id != null){
            if(ids.size() == 1){
                if(!id.equals(ids.get(0))){
                    throw new CommonException(Constants.ERROR_CODE, tip);
                }
            }else if(ids.size() > 1){
                throw new CommonException(Constants.ERROR_CODE, "此数据已产生重复数据！ " + tip);
            }
        }else {
            if(ids.size() == 1){
                throw new CommonException(Constants.ERROR_CODE, tip);
            }else if(ids.size() > 1){
                throw new CommonException(Constants.ERROR_CODE, "此数据已产生重复数据！ " + tip);
            }
        }
    }

    /**
     *  保存基础用户信息。创建用户，机构（服务中心），仓库，支付钱包
     * @param userInfoDTO 用户的信息
     * @param bizServiceCustmanager 客户经理信息
     * @exception CommonException 自定义异常
     * @return StatusDto<String> 状态
     * @author zhangkangjian
     * @date 2018-07-21 12:02:26
     */
    private String saveUserAndOrg(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager) {
        // 填充用户的默认信息
        String loggedUserId = userHolder.getLoggedUserId();
        userInfoDTO.setUserType(Constants.USER_TYPE_INNER);
        userInfoDTO.setUserSource(Constants.USER_SOURCE_INNER);
        userInfoDTO.setUserStatus(Constants.USER_STATUS_INNER);
        userInfoDTO.setFreezeStatus(Constants.FREEZE_STATUS_YES);
        userInfoDTO.setOperator(loggedUserId);
        userInfoDTO.setOperateTime(System.currentTimeMillis() / 1000);
        userInfoDTO.setCreator(loggedUserId);
        userInfoDTO.setUserTypeInAndOut(Constants.USER_STATUS_INNER);
        // 保存基础用户信息和创建组织架构
        BasicUserOrganization buo = new BasicUserOrganization();
        buo.setCreator(loggedUserId);
        buo.setOperator(loggedUserId);
        buo.setCreateTime(System.currentTimeMillis());
        buo.setOperateTime(System.currentTimeMillis());
        // 查询组织架构信息
        StatusDtoThriftBean<BasicUserOrganization> orgcode = basicUserOrganizationService.findOrgByCode(BusinessPropertyHolder.ORGCODE_TOP_CUSMANAGER);
        StatusDto<BasicUserOrganization> resolve = StatusDtoThriftUtils.resolve(orgcode, BasicUserOrganization.class);
        // 拿到父级组织架构的id
        buo.setParentId(resolve.getData().getId());
        buo.setOrgName(bizServiceCustmanager.getName() + "经理");
        buo.setOrgType(Constants.ORG_TYPE);
        // 生成客户经理组织架构的code
        StatusDto<String> custManagerCode = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FO);
        if(!custManagerCode.isSuccess()){
            throw new CommonException(custManagerCode.getCode(), "生成客户经理组织架构编号失败！");
        }
        String code = custManagerCode.getData();
        buo.setOrgCode(code);
        // 创建用户和组织架构
        userInfoDTO.setOrgCode(code);
        StatusDto<String> orgAndUser = innerUserInfoService.createOrgAndUser(buo, userInfoDTO);
        if(!orgAndUser.isSuccess()){
            throw new CommonException(orgAndUser.getCode(), orgAndUser.getMessage());
        }
        // 创建支付钱包
        StatusDto<String> financeAccountStatusDto = bizFinanceAccountService.saveBizFinanceAccount(buo.getOrgName(),
            BusinessTypeEnumThrift.AFTER_SALE,
            BusinessItemTypeEnumThrift.ACCOUNT_MANAGER,
            code,
            buo.getOrgName(),
            OrganizationTypeEnumThrift.PERSONAL,
            userHolder.getLoggedUserId());
        if(!financeAccountStatusDto.isSuccess()){
            throw new CommonException(financeAccountStatusDto.getCode(), financeAccountStatusDto.getMessage());
        }
        userInfoDTO.setUseruuid(orgAndUser.getData());
        // 给用户添加角色
        StatusDto<String> stringStatusDto = innerUserInfoService.addUserRole(userInfoDTO);
        if(!stringStatusDto.isSuccess()){
            throw new CommonException(stringStatusDto.getCode(), stringStatusDto.getMessage());
        }
        String useruuid = orgAndUser.getData();
        userInfoDTO.setUseruuid(useruuid);
        // 更新用户的信息
        StatusDto<String> updateUser = innerUserInfoService.updateUser(userInfoDTO);
        if(!updateUser.isSuccess()){
            throw new CommonException(updateUser.getCode(), updateUser.getMessage());
        }
        SaveBizServiceStorehouseDTO save = new SaveBizServiceStorehouseDTO();
        StatusDtoThriftBean<UserInfoDTO> userInfo = innerUserInfoService.findUserDetail(orgAndUser.getData());
        StatusDto<UserInfoDTO> userInfoDTOResolve = StatusDtoThriftUtils.resolve(userInfo, UserInfoDTO.class);
        UserInfoDTO data = userInfoDTOResolve.getData();
        save.setStorehouseName(bizServiceCustmanager.getName() + "仓库");
        save.setServicecenterCode(data.getOrgCode());
        save.setStorehouseAddress(bizServiceCustmanager.getReceivingAddress());
        save.setStorehouseStatus(Constants.LONG_FLAG_ONE);
        storeHouseService.saveStoreHouse(save);
        return useruuid;
    }

    /**
     * 用户信息校验
     * @param userInfoDTO 用户信息
     * @return StatusDto<String> 状态dto
     * @author zhangkangjian
     * @date 2018-07-19 11:53:11
     */
    private void checkedRoleCodeAndOrgCode(UserInfoDTO userInfoDTO) {
        // 角色校验
        StatusDtoThriftList<Long> statusDtoRole = basicUserRoleService.queryRoleByRoleCode(BusinessPropertyHolder.ROLECODE_CUSMANAGER);
        if(!statusDtoRole.isSuccess()){
            throw new CommonException(statusDtoRole.getCode(), "客户经理角色不存在！");
        }
        // 组织架构校验
        // userInfoDTO.setOrgCode(BusinessPropertyHolder.ORGCODE_TOP_CUSMANAGER);
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(userInfoDTO.getOrgCode());
        if(!orgByCode.isSuccess()){
            throw new CommonException(statusDtoRole.getCode(), "组织架构不存在！");
        }
        StatusDto<List<Long>> resolve = StatusDtoThriftUtils.resolve(statusDtoRole, Long.class);
        userInfoDTO.setRoles(resolve.getData());
    }

    /**
     * 查询用户列表
     * @param userInfoDTO 查询条件
     * @return StatusDtoThriftList
     * @author zhangkangjian
     * @date 2018-07-18 16:55:03
     */
    @Override
    public StatusDto<Page<QueryUserListDTO>> queryUserList(UserInfoDTO userInfoDTO) {
        userInfoDTO.setAppId(SystemPropertyHolder.getBaseAppid());
        userInfoDTO.setSecretId(SystemPropertyHolder.getBaseSecret());
        List<String> orgCodeList = allocateApplyService.getOrgCodesByOrgType(OrganizationTypeEnum.CUSTMANAGER.name());
        userInfoDTO.setOrgCodes(orgCodeList);
        // 排序字段
        userInfoDTO.setSortField(Constants.SORT_FIELD_OPERATE);
        //查询用户信息
        StatusDtoThriftPage<UserInfoDTO> userInfoDTOStatusDtoThriftPage = innerUserInfoService.queryUserList(userInfoDTO);
        StatusDto<Page<UserInfoDTO>> userInfoStatusDto = StatusDtoThriftUtils.resolve(userInfoDTOStatusDtoThriftPage, UserInfoDTO.class);
        // List<UserInfoDTO> 转换成 List<QueryUserListDTO>
        StatusDto<List<QueryUserListDTO>> custManagerList = userInfoDTOConversionQueryUserListDTO(userInfoStatusDto.getData().getRows());
        // 查询维修车
        if(!custManagerList.isSuccess()){
            return StatusDto.buildFailure(custManagerList.getMessage());
        }
        List<QueryUserListDTO> data = custManagerList.getData();
        if(data == null || data.size() == 0){
            Page<QueryUserListDTO> page = buildCustManagerData(userInfoStatusDto, custManagerList);
            return StatusDto.buildDataSuccessStatusDto(page);
        }
        List<String> useruudis = data.stream().map(QueryUserListDTO::getUseruuid).collect(Collectors.toList());
        // 查询维修车编号
        List<BizServiceCustmanager> vinList = bizServiceMaintaincarDao.queryVinNumberByuuid(useruudis);
        // 查询管理车辆数量
        List<CusmanagerCarCountDTO> carList = basicCarcoreInfoService.queryCarNumByCusmanagerUuid(useruudis);
        // list 转 map
        Map<String, CusmanagerCarCountDTO> carMap = Collections.emptyMap();
        if(carList != null && carList.size() > 0){
            carMap = carList.stream().collect(Collectors.toMap(CusmanagerCarCountDTO::getCusmanagerUuid, a -> a,(k1,k2)->k1));
        }
        // list 转 map
        Map<String, BizServiceCustmanager> vinMap = Collections.emptyMap();
        if(vinList != null && vinList.size() > 0) {
            vinMap = vinList.stream().collect(Collectors.toMap(BizServiceCustmanager::getUserUuid, a -> a, (k1, k2) -> k1));
        }
        for (int i = 0; i < data.size(); i++) {
            QueryUserListDTO queryUserListDTO = data.get(i);
            BizServiceCustmanager bizServiceCustmanager = vinMap.get(queryUserListDTO.getUseruuid());
            CusmanagerCarCountDTO cusmanagerCarCountDTO = carMap.get(queryUserListDTO.getUseruuid());
            if(bizServiceCustmanager != null){
                queryUserListDTO.setVinNumber(bizServiceCustmanager.getVinNumber());
                queryUserListDTO.setVinId(bizServiceCustmanager.getId());
            }
            if(cusmanagerCarCountDTO != null){
                queryUserListDTO.setCarsNumber(Long.valueOf(cusmanagerCarCountDTO.getCarNum()));
            }
        }
        // 组装分页信息
        Page<QueryUserListDTO> page = buildCustManagerData(userInfoStatusDto, custManagerList);
        // 统计客户经理数量
        List<QueryPendingMaterialsDTO> queryPendingMaterialsDTOS = bizAllocateapplyDetailDao.queryCustReceiveMaterials(useruudis, Constants.PRODUCT_TYPE_EQUIPMENT, StockPlanStatusEnum.COMPLETE.name());
        Optional.ofNullable(queryPendingMaterialsDTOS).ifPresent(a ->{
            Map<String, List<QueryPendingMaterialsDTO>> collect = a.stream().collect(Collectors.groupingBy(QueryPendingMaterialsDTO::getApplyeruuid));
            Optional.ofNullable(page.getRows()).ifPresent(b ->{
                b.forEach(item ->{
                    List<QueryPendingMaterialsDTO> queryPendingMaterialsDTOS1 = collect.get(item.getUseruuid());
                    if(queryPendingMaterialsDTOS1 != null && queryPendingMaterialsDTOS1.size() > 0){
                        item.setMaterialsNumber(Long.valueOf(queryPendingMaterialsDTOS1.size()));
                    }
                });
            });
        });
        return StatusDto.buildDataSuccessStatusDto(page);
    }

    /**
     * 编辑客户经理
     * @param userInfoDTO           用户的基本信息
     * @param bizServiceCustmanager 用户业务数据
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-18 10:30:22
     */
    @Override
    public StatusDto<String> editUser(UserInfoDTO userInfoDTO, BizServiceCustmanager bizServiceCustmanager) {
        checkedRoleCodeAndOrgCode(userInfoDTO);
        compareRepeat(userInfoDTO.getUseruuid(), bizServiceCustmanager.getOfficePhone(), "office_phone", "biz_service_custmanager", "客户经理办公SIM卡手机号重复！");
        String loggedUserId = userHolder.getLoggedUserId();
        long timeMillis = System.currentTimeMillis();
        userInfoDTO.setOperateTime(timeMillis);
        userInfoDTO.setOperator(loggedUserId);
        StatusDto<String> updateUser = innerUserInfoService.updateUser(userInfoDTO);
        if(!updateUser.isSuccess()){
            return StatusDto.buildFailure(updateUser.getMessage());
        }
        bizServiceCustmanager.setUserUuid(userInfoDTO.getUseruuid());
        updateCustManager(bizServiceCustmanager);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 更新客户经理信息
     * @param bizServiceCustmanager  客户经理信息
     * @author zhangkangjian
     * @date 2018-07-25 14:32:04
     */
    private void updateCustManagerDetail(BizServiceCustmanager bizServiceCustmanager) {
        compareRepeat(bizServiceCustmanager.getUserUuid(), bizServiceCustmanager.getOfficePhone(), "office_phone", "biz_service_custmanager", "客户经理办公SIM卡手机号重复！");
        String loggedUserId = userHolder.getLoggedUserId();
        bizServiceCustmanager.setOperator(loggedUserId);
        bizServiceCustmanager.setOperateTime(new Date());
        bizServiceCustmanagerDao.updateBizServiceCustmanager(bizServiceCustmanager);
    }

    /**
     * 查询用户的详情
     * @param useruuid 用户的uuid
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-19 14:47:07
     */
    @Override
    public StatusDto<CustManagerDetailDTO> custManagerDetail(String useruuid) throws IOException {
        // 查询用户的基础信息和角色信息
        StatusDto<UserInfoDTO> resolve = findUserAndRoleDetail(useruuid);
        // 类型转换 UserInfoDTO ——> CustManagerDetailDTO
        CustManagerDetailDTO custManagerDetailDTO = userInfoDTOConversionCustManagerDetailDTO(resolve);
        // 查询客户经理的信息
        findCustManagerDetail(useruuid, custManagerDetailDTO);
        return StatusDto.buildDataSuccessStatusDto(custManagerDetailDTO);
    }

    /**
     * 更新客户经理信息
     * @param bizServiceCustmanager  客户经理信息
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-07-25 14:14:11
     */
    @Override
    public StatusDto<String> updateCustManager(BizServiceCustmanager bizServiceCustmanager) {
        updateCustManagerDetail(bizServiceCustmanager);
        // 更新维修车
        bizServiceMaintaincarDao.updateStatusbyUuid(bizServiceCustmanager.getUserUuid(), Constants.DELETE_FLAG_NORMAL);
        bizServiceMaintaincarDao.updateCustmanager(bizServiceCustmanager);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 获取组织架构子节点
     * @param parentId 组织架构父级id
     * @param isSearchClose 是否获取关闭的组织架构
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-05-04 14:58:20
     */
    @Override
    public StatusDto queryOrgList(int parentId, boolean isSearchClose) {
        StatusDtoThriftBean<BasicUserOrganization> orgByCode = basicUserOrganizationService.findOrgByCode(BusinessPropertyHolder.ORGCODE_TOP_CUSMANAGER);
        StatusDto<BasicUserOrganization> orgByCodeStatusDto = StatusDtoThriftUtils.resolve(orgByCode, BasicUserOrganization.class);
        StatusDtoThriftList<BasicUserOrganizationDTO> basicUserOrganizationDTO = orgService.queryOrganizationByParentId(orgByCodeStatusDto.getData().getParentId(), isSearchClose);
        StatusDto<List<BasicUserOrganizationDTO>> resolve = StatusDtoThriftUtils.resolve(basicUserOrganizationDTO, BasicUserOrganizationDTO.class);
        List<BasicUserOrganizationDTO> data = resolve.getData();
        BasicUserOrganizationDTO basicUserOrganizationDTO1 = data.stream().filter(a -> BusinessPropertyHolder.ORGCODE_TOP_CUSMANAGER.equals(a.getOrgCode())).findFirst().get();
        basicUserOrganizationDTO1.setLeaf(true);
        return StatusDto.buildDataSuccessStatusDto(basicUserOrganizationDTO1);
    }

    /**
     * 查询客户经理列表(创建申请)
     * @param queryCustManagerListDTO 查询条件
     * @return Page<QueryCustManagerListDTO> 分页的客户经理列表
     * @author zhangkangjian
     * @date 2018-08-09 19:03:17
     */
    @Override
    public Page<QueryCustManagerListDTO> queryCustManagerList(QueryCustManagerListDTO queryCustManagerListDTO) {
        // 查询客户经理的信息
        Page<QueryCustManagerListDTO> queryCustManagerListDTOPage = bizServiceCustmanagerDao.queryCustManagerList(queryCustManagerListDTO);
        List<QueryCustManagerListDTO> rows = queryCustManagerListDTOPage.getRows();
        Map<String, BasicUserOrganization> organizationMap;
        if(rows != null){
            // 查询客户经理的信息
            List<String> useruudis = rows.stream().map(QueryCustManagerListDTO::getUseruuid).collect(Collectors.toList());
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setUseruuids(useruudis);
            StatusDtoThriftList<UserInfoDTO> userInfoDTOList = innerUserInfoService.queryUserListByOrgCode(userInfoDTO);
            StatusDto<List<UserInfoDTO>> userInfoStatusDto = StatusDtoThriftUtils.resolve(userInfoDTOList, UserInfoDTO.class);
            List<UserInfoDTO> data = userInfoStatusDto.getData();
            List<UserInfoDTO> userInfoList = Optional.ofNullable(data).orElse(Collections.EMPTY_LIST);
            List<String> collect = userInfoList.stream().map(UserInfoDTO::getOrgCode).collect(Collectors.toList());
            Map<String, UserInfoDTO> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfoDTO::getUseruuid, a -> a,(k1,k2)->k1));
            List<String> orgCodeList = rows.stream().filter(a -> null != a.getServiceCenter()).map(QueryCustManagerListDTO::getServiceCenter).collect(Collectors.toList());
            organizationMap = orgService.queryOrganizationByOrgCodes(orgCodeList);
            List<BizServiceStorehouse> queryStorehouseDTOList = bizServiceStorehouseDao.queryStorehouseByServiceCenterCode(collect);
            rows.forEach(c -> {
                UserInfoDTO userInfoDTO1 = userInfoMap.get(c.getUseruuid());
                if(userInfoDTO1 != null){
                    c.setOrgCode(userInfoDTO1.getOrgCode());
                }
                BasicUserOrganization basicUserOrganization = organizationMap.get(c.getServiceCenter());
                if (basicUserOrganization != null) {
                    c.setServiceCenterName(basicUserOrganization.getOrgName());
                }
            });

            Optional.ofNullable(queryStorehouseDTOList).ifPresent(a ->{
                Map<String, BizServiceStorehouse> queryStorehouseDTOMap = a.stream().collect(Collectors.toMap(BizServiceStorehouse::getServicecenterCode, b -> b,(k1,k2)->k1));
                rows.forEach(c ->{
                    String orgCode = c.getOrgCode();
                    BizServiceStorehouse queryStorehouseDTO = queryStorehouseDTOMap.get(orgCode);
                    if(queryStorehouseDTO != null){
                        c.setInRepositoryNo(queryStorehouseDTO.getStorehouseCode());
                    }
                });
            });

        }
        return queryCustManagerListDTOPage;
    }

    /**
     * 根据机构编号查询客户经理
     * @param orgCodes 机构编号
     * @return 客户经理
     * @author liuduo
     * @date 2018-09-05 15:54:48
     */
    @Override
    public List<BizServiceCustmanager> queryCustManagerListByOrgCode(List<String> orgCodes) {
        return bizServiceCustmanagerDao.queryCustManagerListByOrgCode(orgCodes);
    }

    /**
     * 查询客户经理物料的信息
     * @param useruuid 客户经理uuid
     * @return List<QueryUserListDTO> 物料列表
     * @author zhangkangjian
     * @date 2018-09-17 13:58:30
     */
    @Override
    public List<QueryPendingMaterialsDTO> queryCustMaterials(String useruuid) {
        // 统计客户经理数量
        return bizAllocateapplyDetailDao.queryCustReceiveMaterials(List.of(useruuid), Constants.PRODUCT_TYPE_EQUIPMENT, StockPlanStatusEnum.COMPLETE.name());
    }

    /**
     *  查询客户经理并填充信息
     * @param useruuid 用户的uuid
     * @param custManagerDetailDTO 客户经理的信息
     * @author zhangkangjian
     * @date 2018-07-21 17:28:53
     */
    private void findCustManagerDetail(String useruuid, CustManagerDetailDTO custManagerDetailDTO) {
        BizServiceCustmanager bizServiceCustmanager = bizServiceCustmanagerDao.queryCustManagerByUuid(useruuid);
        // 查询客户经理维修车的vin
        List<BizServiceCustmanager> bizServiceCustmanagers = bizServiceMaintaincarDao.queryVinNumberByuuid(List.of(useruuid));
        if(bizServiceCustmanagers != null && bizServiceCustmanagers.size() > 0){
            String vinNumber = bizServiceCustmanagers.get(0).getVinNumber();
            custManagerDetailDTO.setVinNumber(vinNumber);
        }
        if(bizServiceCustmanager != null){
            custManagerDetailDTO.setOfficePhone(bizServiceCustmanager.getOfficePhone());
            custManagerDetailDTO.setReceivingAddress(bizServiceCustmanager.getReceivingAddress());
            custManagerDetailDTO.setServicecenterCode(bizServiceCustmanager.getServicecenterCode());
        }
        int age = getAgeByBirth(new Date(custManagerDetailDTO.getBirthday() * 1000));
        custManagerDetailDTO.setAge(age);
    }

    /**
     *  类型转换 UserInfoDTO ——> CustManagerDetailDTO
     * @param resolve 用户信息
     * @exception IOException
     * @return CustManagerDetailDTO 客户经理dto
     * @author zhangkangjian
     * @date 2018-07-21 17:26:59
     */
    private CustManagerDetailDTO userInfoDTOConversionCustManagerDetailDTO(StatusDto<UserInfoDTO> resolve) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = JsonUtils.writeValue(resolve.getData());
        return mapper.readValue(json, CustManagerDetailDTO.class);
    }

    /**
     * 查询用户信息和角色信息
     * @param useruuid 用户的uuid
     * @return StatusDto<UserInfoDTO> 用户的信息
     * @author zhangkangjian
     * @date 2018-07-21 17:17:20
     */
    private StatusDto<UserInfoDTO> findUserAndRoleDetail(String useruuid) {
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(useruuid);
        StatusDto<UserInfoDTO> resolve = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class);
        List<RelUserRole> userRoles = resolve.getData().getUserRoles();
        if(userRoles != null && userRoles.size() > 0){
            List<RelUserRole> temp = userRoles.stream().filter(a -> BusinessPropertyHolder.ROLECODE_CUSMANAGER.equals(a.getRoleCode())).collect(Collectors.toList());
            if( temp != null && temp.size() > 0){
                RelUserRole relUserRole = temp.get(0);
                resolve.getData().setRoleName(relUserRole.getRoleName());
            }
        }
        return resolve;
    }

    /**
     * 组装客户经理信息
     * @param resolve 用户的分页信息
     * @exception
     * @return Page<QueryUserListDTO>
     * @author zhangkangjian
     * @date 2018-07-19 11:22:26
     */
    private Page<QueryUserListDTO> buildCustManagerData(StatusDto<Page<UserInfoDTO>> resolve, StatusDto<List<QueryUserListDTO>> custManagerList) {
        // 数据组装
        List<UserInfoDTO> rows = resolve.getData().getRows();
        List<QueryUserListDTO> data = custManagerList.getData();
        List<String> useruudis = rows.stream().map(UserInfoDTO::getUseruuid).collect(Collectors.toList());
        // 查询客户经理的信息
        List<QueryUserListDTO> userList = bizServiceCustmanagerDao.queryCustManager(useruudis);
        if(data != null && data.size() > 0){
            data.stream().forEach(a ->{
                    String str = a.getUseruuid();
                    userList.stream().forEach(b ->{
                        String uuid = b.getUseruuid();
                        if(str.equals(uuid)){
                            a.setOfficePhone(b.getOfficePhone());
                        }
                    });
                }
            );
        }
        Page<QueryUserListDTO> page = new Page<QueryUserListDTO>();
        Page<UserInfoDTO> pageUser = resolve.getData();
        page.setRows(data);
        page.setLimit(pageUser.getLimit());
        page.setOffset(pageUser.getOffset());
        page.setTotal(pageUser.getTotal());
        page.setTotalPage(pageUser.getTotalPage());
        return page;
    }

    /**
     * UserInfoDTO 类型转换 QueryUserListDTO
     * @param rows 用户信息
     * @return StatusDto<List<QueryUserListDTO>>
     * @author zhangkangjian
     * @date 2018-07-18 19:45:07
     */
    private StatusDto<List<QueryUserListDTO>> userInfoDTOConversionQueryUserListDTO(List<UserInfoDTO> rows) {
        List<QueryUserListDTO> userList = null;
        if(rows != null && rows.size() > 0){
            String s = JsonUtils.writeValue(rows);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(
                List.class, QueryUserListDTO.class);
            try {
                userList = mapper.readValue(s, javaType);
            }catch (Exception e){
                e.printStackTrace();
                return StatusDto.buildFailure("查询错误");
            }
        }
        return StatusDto.buildDataSuccessStatusDto(userList);
    }
    /**
     *  根据生日计算年龄
     * @param birthday 生日
     * @return int 岁数
     * @author zhangkangjian
     * @date 2018-07-19 17:08:52
     */
    private int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);
            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            //异常后返回数据
            e.printStackTrace();
            return 0;
        }
    }

}
