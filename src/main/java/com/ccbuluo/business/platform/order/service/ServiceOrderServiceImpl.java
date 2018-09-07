package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dto.CheckStockQuantityDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProductStockInfoDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.carmanage.dto.CarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.business.platform.carposition.dao.BizCarPositionDao;
import com.ccbuluo.business.platform.custmanager.dao.BizServiceCustmanagerDao;
import com.ccbuluo.business.platform.custmanager.entity.BizServiceCustmanager;
import com.ccbuluo.business.platform.custmanager.service.CustmanagerService;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.service.MaintainitemService;
import com.ccbuluo.business.platform.maintainitem.service.MultiplepriceService;
import com.ccbuluo.business.platform.order.dao.BizAllocateTradeorderDao;
import com.ccbuluo.business.platform.order.dao.BizServiceDispatchDao;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.dao.BizServiceorderDetailDao;
import com.ccbuluo.business.platform.order.dto.*;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.servicecenter.dao.BizServiceCenterDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.ServiceCenterDTO;
import com.ccbuluo.usercoreintf.dto.ServiceCenterWorkplaceDTO;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.BasicUserWorkplaceService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述 服务订单service
 * @author liuduo
 * @date 2018-09-03 17:35:19
 * @version V1.0.0
 */
@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BizServiceOrderDao bizServiceOrderDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private GenerateDocCodeService generateDocCodeService;
    @Autowired
    private BasicCarcoreInfoService basicCarcoreInfoService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserWorkplaceService basicUserWorkplaceService;
    @Autowired
    private BizCarPositionDao bizCarPositionDao;
    @Autowired
    private CustmanagerService custmanagerService;
    @Autowired
    private BizServiceCenterDao bizServiceCenterDao;
    @Autowired
    private BizServiceDispatchDao bizServiceDispatchDao;
    @Autowired
    private MaintainitemService maintainitemService;
    @Autowired
    private MultiplepriceService multiplepriceService;
    @Autowired
    private BizServiceCustmanagerDao bizServiceCustmanagerDao;
    @Autowired
    private AllocateApplyService allocateApplyService;
    @Autowired
    private BizStockDetailDao bizStockDetailDao;
    @Autowired
    private BizAllocateTradeorderDao bizAllocateTradeorderDao;
    @Autowired
    private BizServiceorderDetailDao bizServiceorderDetailDao;


    /**
     * 描述 新增服务订单
     * @param serviceOrderDTO
     * @return com.ccbuluo.http.StatusDto<java.lang.String>
     * @author liuduo
     * @date 2018-09-03 18:54:01
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto<String> saveOrder(SaveServiceOrderDTO serviceOrderDTO) {
        try {
            // 生成订单号
            String orderCode = null;
            StatusDto<String> order = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.DD);
            if (order.getCode().equals(Constants.SUCCESS_CODE)) {
                orderCode = order.getData();
            } else {
                return StatusDto.buildFailure("生成订单编码失败！");
            }
            BusinessUser loggedUser = userHolder.getLoggedUser();
            // 保存订单
            buildSaveBizServiceOrder(serviceOrderDTO, orderCode, loggedUser);
            // 保存车辆停放位置
            buildSaveBizCarPosition(serviceOrderDTO, loggedUser);
            // 保存服务单派发
            buildBizServiceDispatch(orderCode, loggedUser);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("保存订单失败！", e.getMessage());
            throw e;
        }
    }




    /**
     * 编辑订单
     * @param editServiceOrderDTO 订单要保存的信息
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-09-04 14:06:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editOrder(EditServiceOrderDTO editServiceOrderDTO) {
        try {
            // 编辑订单
            buildEditBizServiceOrder(editServiceOrderDTO);
            // 编辑车辆停放位置
            buildEditBizCarPosition(editServiceOrderDTO);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("编辑订单失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 查询订单列表
     * @param orderStatus 订单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 订单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    @Override
    public StatusDto<Page<BizServiceOrder>> queryList(String orderStatus, String serviceType, String keyword, Integer offset, Integer pagesize) {
        return StatusDto.buildDataSuccessStatusDto(bizServiceOrderDao.queryList(orderStatus, serviceType, keyword, offset, pagesize));
    }


    /**
     * 修改订单状态
     * @param serviceOrderno 订单编号
     * @param orderStatus 订单状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-09-04 15:37:36
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editStatus(String serviceOrderno, String orderStatus) {
        try {
            // 如果是 待完善 状态，则还需要更新服务单派发表的数据
            if (StringUtils.isNotBlank(orderStatus) && orderStatus.equals(BizServiceOrder.OrderStatusEnum.WAITING_PERFECTION.name())) {
                bizServiceDispatchDao.updateConfirmed(serviceOrderno);
            }
            bizServiceOrderDao.editStatus(serviceOrderno, orderStatus);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("状态修改失败！", e.getMessage());
            throw e;
        }
    }


    /**
     * 查询车牌号下拉框
     * @return 所有车牌号
     * @author liuduo
     * @date 2018-09-04 17:34:08
     */
    @Override
    public StatusDto<List<String>> queryCarNoList() {
        BusinessUser loggedUser = userHolder.getLoggedUser();
        // 是门店，查询该门店下已分配的车辆
        if (BizServiceOrder.ProcessorOrgtypeEnum.STORE.name().equals(loggedUser.getOrganization().getOrgType())) {
            return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.queryCarNoList(loggedUser.getOrganization().getOrgCode(), Constants.STATUS_FLAG_ZERO));
        }
        // 是客户经理，查询客户经理下的所有车辆
        return StatusDto.buildDataSuccessStatusDto(basicCarcoreInfoService.queryCarNoList(loggedUser.getUserId(),Constants.STATUS_FLAG_ONE));
    }

    /**
     * 根据订单编号查询订单详情
     * @param serviceOrderno 订单编号
     * @return 订单详情
     * @author liuduo
     * @date 2018-09-05 09:54:40
     */
    @Override
    public StatusDto<DetailServiceOrderDTO> getDetailByOrderNo(String serviceOrderno) {
        // 根据订单编号查询车牌号
        String carNo = bizServiceOrderDao.getCarNoByServiceOrderno(serviceOrderno);
        // 根据车牌号查询车辆基本信息
        CarcoreInfoDTO data = basicCarcoreInfoService.getCarByCarNo(carNo).getData();
        BizServiceOrder byOrderNo = bizServiceOrderDao.getByOrderNo(serviceOrderno);
        String carVin = byOrderNo.getCarVin();
        // 根据车辆vin码查询车辆停放地址
        BizCarPosition bizCarPosition = bizCarPositionDao.getByCarVin(carVin);
        DetailServiceOrderDTO detailServiceOrderDTO = new DetailServiceOrderDTO();
        detailServiceOrderDTO.setServiceOrderno(serviceOrderno);
        detailServiceOrderDTO.setBizCarPosition(bizCarPosition);
        detailServiceOrderDTO.setBizServiceOrder(byOrderNo);
        detailServiceOrderDTO.setCarcoreInfoDTO(data);
        return StatusDto.buildDataSuccessStatusDto(detailServiceOrderDTO);
    }

    /**
     * 查询服务中心和客户经理（分配用）
     * @param province 省
     * @param city 市
     * @param area 区
     * @param orgType 机构类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 可以分配的客户经理和服务中心
     * @author liuduo
     * @date 2018-09-05 10:40:35
     */
    @Override
    public StatusDto<Page<ServiceCenterDTO>> serviceCenterList(String province, String city, String area, String orgType, String keyword, Integer offset, Integer pagesize) {
        StatusDtoThriftPage<ServiceCenterDTO> serviceCenterDTOStatusDtoThriftPage = basicUserOrganizationService.queryAfterSaleServiceCenter(province, city, area, orgType, keyword, offset, pagesize);
        Page<ServiceCenterDTO> data = StatusDtoThriftUtils.resolve(serviceCenterDTOStatusDtoThriftPage, ServiceCenterDTO.class).getData();
        if (null != data && null != data.getRows()) {
            List<ServiceCenterDTO> rows = data.getRows();
            // 获取服务中心的code
            List<String> orgCodes = rows.stream().map(item -> item.getOrgCode()).distinct().collect(Collectors.toList());
            if (orgCodes.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(new Page<>());
            }
            // 查询客户经理
            if (StringUtils.isNotBlank(orgType) && orgType.equals(BizServiceOrder.ProcessorOrgtypeEnum.CUSTMANAGER.name())) {
                List<BizServiceCustmanager> bizServiceCustmanagers = custmanagerService.queryCustManagerListByOrgCode(orgCodes);
                Map<String, BizServiceCustmanager> collect = bizServiceCustmanagers.stream().collect(Collectors.toMap(a -> a.getServicecenterCode(), Function.identity()));
                rows.forEach(item -> {
                    BizServiceCustmanager bizServiceCustmanager = collect.get(item.getOrgCode());
                    item.setContact(bizServiceCustmanager.getName());
                    item.setContactPhone(bizServiceCustmanager.getOfficePhone());
                });
            }
        }
        return StatusDto.buildDataSuccessStatusDto(data);
    }

    /**
     * 分配订单
     * @param serviceOrderno 订单编号
     * @param orgCodeOrUuid 机构编号或者客户经理uuid
     * @return 订单是否分配成功
     * @author liuduo
     * @date 2018-09-05 18:32:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto orderAllocation(String serviceOrderno, String orgCodeOrUuid) {
        try {
            BusinessUser loggedUser = userHolder.getLoggedUser();
            // 先更改本次分配单的 是否当前处理人
            Long id = bizServiceDispatchDao.getByServiceOrderno(serviceOrderno, loggedUser.getUserId());
            BizServiceDispatch bizServiceDispatch = new BizServiceDispatch();
            bizServiceDispatch.setId(id);
            bizServiceDispatch.setCurrentFlag(Constants.STATUS_FLAG_ZERO);
            int status = bizServiceDispatchDao.updateCurrentFlag(bizServiceDispatch);
            if (status == 0) {
                throw new CommonException("3002", "分配失败！");
            }
            // 创建新的服务单详情并更新旧的
            BizServiceDispatch bizServiceDispatch2 = new BizServiceDispatch();
            bizServiceDispatch2.setServiceOrderno(serviceOrderno);
            bizServiceDispatch2.setCurrentFlag(Constants.FLAG_ONE);
            bizServiceDispatch2.setProcessorOrgno(loggedUser.getOrganization().getOrgCode());
            bizServiceDispatch2.setProcessorOrgtype(loggedUser.getOrganization().getOrgType());
            bizServiceDispatch2.setProcessorUuid(loggedUser.getUserId());
            bizServiceDispatch2.setConfirmed(Constants.STATUS_FLAG_ZERO);
            // todo 分配暂停
//            if () {
//
//            }
            bizServiceDispatch2.preInsert(loggedUser.getUserId());
            return null;
        } catch (Exception e) {
            logger.error("分配失败", e.getMessage());
            throw e;
        }
    }

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     *  @param pagesize 每页数
     * @return 工时列表
     * @author liuduo
     * @date 2018-09-06 10:39:33
     */
    @Override
    public StatusDto<Page<DetailBizServiceMaintainitemDTO>> queryMaintainitem(String keyword, Integer offset, Integer pagesize) {
        Page<DetailBizServiceMaintainitemDTO> detailBizServiceMaintainitemDTOPage = maintainitemService.queryList(keyword, offset, pagesize);
        if (null != detailBizServiceMaintainitemDTOPage && null != detailBizServiceMaintainitemDTOPage.getRows()) {
            List<DetailBizServiceMaintainitemDTO> rows = detailBizServiceMaintainitemDTOPage.getRows();
            List<String> codes = rows.stream().map(DetailBizServiceMaintainitemDTO::getMaintainitemCode).collect(Collectors.toList());
            if (codes.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(new Page<>());
            }
            // 获取当前登录人的地址
            StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(userHolder.getLoggedUserId());
            UserInfoDTO data = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class).getData();
            String province = data.getProvince();
            String city = data.getCity();
            // 查询地区倍数
            List<BizServiceMultipleprice> bizServiceMultiplepriceList = multiplepriceService.queryMultiple(codes, province, city);
            Map<String, BizServiceMultipleprice> collect = bizServiceMultiplepriceList.stream().collect(Collectors.toMap(BizServiceMultipleprice::getMaintainitemCode, Function.identity()));
            rows.forEach(item -> {
                BizServiceMultipleprice bizServiceMultipleprice = collect.get(item.getMaintainitemCode());
                item.setUnitPrice(item.getUnitPrice().multiply(new BigDecimal(bizServiceMultipleprice.getMultiple())));
            });
        }
        return StatusDto.buildDataSuccessStatusDto(detailBizServiceMaintainitemDTOPage);
    }


    /**
     * 保存维修单详单
     * @param saveOrderDetailDTO 维修单详单
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-09-06 17:04:02
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto saveOrderDetail(SaveOrderDetailDTO saveOrderDetailDTO) {
        try {
            // 删除原有工时和零配件
            bizServiceorderDetailDao.deleteByOrderNo(saveOrderDetailDTO.getServiceOrderno());
            // 保存工时
            List<SaveMaintaintemDTO> saveMaintaintemDTOS = saveOrderDetailDTO.getSaveMaintaintemDTOS();
            if (!saveMaintaintemDTOS.isEmpty()) {
                saveMaintaintemDTOS.forEach(item -> {
                    if (StringUtils.isBlank(item.getServiceOrgno())) {
                        item.setServiceOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
                        item.setServiceOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
                        item.setServiceUserid(userHolder.getLoggedUserId());
                        item.setServiceUsername(userHolder.getLoggedUser().getUsername());
                    }
                    item.preInsert(userHolder.getLoggedUserId());
                    item.setServiceOrderno(saveOrderDetailDTO.getServiceOrderno());
                });
//                bizServiceorderDetailDao.saveEntity()
            }
return null;
        } catch (Exception e) {
            logger.error("3003", "保存失败！");
            throw e;
        }
    }


    /**
     * 组装服务单派发
     * @param orderCode 订单编号
     * @param loggedUser 用户信息
     * @author liuduo
     * @date 2018-09-05 16:46:28
     */
    private void buildBizServiceDispatch(String orderCode, BusinessUser loggedUser) {
        BizServiceDispatch bizServiceDispatch = new BizServiceDispatch();
        bizServiceDispatch.setServiceOrderno(orderCode);
        bizServiceDispatch.setCurrentFlag(Constants.FLAG_ONE);
        bizServiceDispatch.setProcessorOrgno(loggedUser.getOrganization().getOrgCode());
        bizServiceDispatch.setProcessorOrgtype(loggedUser.getOrganization().getOrgType());
        bizServiceDispatch.setProcessorUuid(loggedUser.getUserId());
        bizServiceDispatch.setConfirmed(Constants.STATUS_FLAG_ZERO);
        bizServiceDispatch.preInsert(loggedUser.getUserId());
        bizServiceDispatchDao.saveBizServiceDispatch(bizServiceDispatch);
    }


    /**
     * 组装编辑用的车辆停放位置
     * @param editServiceOrderDTO 订单信息
     * @author liuduo
     * @date 2018-09-04 14:53:15
     */
    private void buildEditBizCarPosition(EditServiceOrderDTO editServiceOrderDTO) {
        // 保存省市区
        BizCarPosition bizCarPosition = new BizCarPosition();
        bizCarPosition.setCarVin(editServiceOrderDTO.getCarVin());
        bizCarPosition.setDetailAddress(editServiceOrderDTO.getDetailAddress());
        bizCarPosition.setProvinceCode(editServiceOrderDTO.getProvinceCode());
        bizCarPosition.setProvinceName(editServiceOrderDTO.getProvinceName());
        bizCarPosition.setCityCode(editServiceOrderDTO.getCityCode());
        bizCarPosition.setCityName(editServiceOrderDTO.getCityName());
        bizCarPosition.setAreaCode(editServiceOrderDTO.getAreaCode());
        bizCarPosition.setAreaName(editServiceOrderDTO.getAreaName());
        bizCarPosition.preUpdate(userHolder.getLoggedUserId());
        bizCarPositionDao.updateBizCarPosition(bizCarPosition);
    }


    /**
     * 组装编辑用的订单信息
     * @param editServiceOrderDTO 订单信息
     * @author liuduo
     * @date 2018-09-04 14:39:36
     */
    private void buildEditBizServiceOrder(EditServiceOrderDTO editServiceOrderDTO) {
        BizServiceOrder bizServiceOrder = new BizServiceOrder();
        bizServiceOrder.setServiceOrderno(editServiceOrderDTO.getServiceOrderno());
        bizServiceOrder.setCarNo(editServiceOrderDTO.getCarNo());
        bizServiceOrder.setCarVin(editServiceOrderDTO.getCarVin());
        bizServiceOrder.setServiceType(editServiceOrderDTO.getServiceType());
        bizServiceOrder.setCustomerName(editServiceOrderDTO.getCustomerName());
        bizServiceOrder.setCustomerPhone(editServiceOrderDTO.getCustomerPhone());
        bizServiceOrder.setReserveContacter(editServiceOrderDTO.getReserveContacter());
        bizServiceOrder.setReservePhone(editServiceOrderDTO.getReservePhone());
        // 根据车牌号查询该车属于哪个机构
        String uuid = basicCarcoreInfoService.getUuidByPlateNum(editServiceOrderDTO.getCarNo());
        if (StringUtils.isBlank(uuid)) {
            throw new CommonException("3001", "该车辆没有被分配到客户经理，请核对！");
        }
        bizServiceOrder.setCurProcessor(uuid);
        // 根据用户uuid查询用户机构类型
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(uuid);
        UserInfoDTO data = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class).getData();
        bizServiceOrder.setProcessorOrgtype(data.getOrgType());
        bizServiceOrder.setServiceTime(editServiceOrderDTO.getServiceTime());
        bizServiceOrder.setProblemContent(editServiceOrderDTO.getProblemContent());
        bizServiceOrder.preUpdate(userHolder.getLoggedUserId());
        bizServiceOrderDao.updateBizServiceOrder(bizServiceOrder);
    }


    /**
     * 组装车辆停放位置
     * @param serviceOrderDTO 订单信息
     * @param loggedUser  当前登录人
     * @author liuduo
     * @date 2018-09-04 11:59:33
     */
    private void buildSaveBizCarPosition(SaveServiceOrderDTO serviceOrderDTO, BusinessUser loggedUser) {
        // 保存省市区
        BizCarPosition bizCarPosition = new BizCarPosition();
        bizCarPosition.setCarVin(serviceOrderDTO.getCarVin());
        bizCarPosition.setDetailAddress(serviceOrderDTO.getDetailAddress());
        bizCarPosition.setProvinceCode(serviceOrderDTO.getProvinceCode());
        bizCarPosition.setProvinceName(serviceOrderDTO.getProvinceName());
        bizCarPosition.setCityCode(serviceOrderDTO.getCityCode());
        bizCarPosition.setCityName(serviceOrderDTO.getCityName());
        bizCarPosition.setAreaCode(serviceOrderDTO.getAreaCode());
        bizCarPosition.setAreaName(serviceOrderDTO.getAreaName());
        bizCarPosition.preInsert(loggedUser.getUserId());
        bizCarPositionDao.saveBizCarPosition(bizCarPosition);
    }


    /**
     * 组装订单
     * @param serviceOrderDTO 订单信息
     * @param orderCode 订单编号
     * @param loggedUser 当前登录人
     * @author liuduo
     * @date 2018-09-04 11:58:20
     */
    private void buildSaveBizServiceOrder(SaveServiceOrderDTO serviceOrderDTO, String orderCode, BusinessUser loggedUser) {
        BizServiceOrder bizServiceOrder = new BizServiceOrder();
        bizServiceOrder.setCarNo(serviceOrderDTO.getCarNo());
        bizServiceOrder.setServiceOrderno(orderCode);
        bizServiceOrder.setCarVin(serviceOrderDTO.getCarVin());
        bizServiceOrder.setServiceType(serviceOrderDTO.getServiceType());
        bizServiceOrder.setReportOrgno(loggedUser.getUserId());
        bizServiceOrder.setReportOrgtype(loggedUser.getOrganization().getOrgType());
        bizServiceOrder.setReportTime(new Date());
        bizServiceOrder.setCustomerName(serviceOrderDTO.getCustomerName());
        bizServiceOrder.setCustomerPhone(serviceOrderDTO.getCustomerPhone());
        bizServiceOrder.setReserveContacter(serviceOrderDTO.getReserveContacter());
        bizServiceOrder.setReservePhone(serviceOrderDTO.getReservePhone());
        bizServiceOrder.setOrderStatus(BizServiceOrder.OrderStatusEnum.WAITING_CHECKING.name());
        bizServiceOrder.setDispatchTimes(Constants.LONG_FLAG_ONE);
        // 根据车牌号查询该车属于哪个机构
        String uuid = basicCarcoreInfoService.getUuidByPlateNum(serviceOrderDTO.getCarNo());
        if (StringUtils.isBlank(uuid)) {
            throw new CommonException("3001", "该车辆没有被分配到客户经理，请核对！");
        }
        bizServiceOrder.setCurProcessor(uuid);
        // 根据用户uuid查询用户机构类型
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(uuid);
        UserInfoDTO data = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class).getData();
        bizServiceOrder.setProcessorOrgtype(data.getOrgType());
        bizServiceOrder.setServiceTime(serviceOrderDTO.getServiceTime());
        bizServiceOrder.setProblemContent(serviceOrderDTO.getProblemContent());
        bizServiceOrder.preInsert(loggedUser.getUserId());
        bizServiceOrderDao.saveBizServiceOrder(bizServiceOrder);
    }


}
