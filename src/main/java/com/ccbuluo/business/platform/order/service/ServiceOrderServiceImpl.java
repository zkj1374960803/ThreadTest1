package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.constants.*;
import com.ccbuluo.business.entity.*;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailBO;
import com.ccbuluo.business.platform.allocateapply.dto.CheckStockQuantityDTO;
import com.ccbuluo.business.platform.allocateapply.dto.ProductStockInfoDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.carmanage.dto.CarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.business.platform.carposition.dao.BizCarPositionDao;
import com.ccbuluo.business.platform.claimorder.dao.ClaimOrderDao;
import com.ccbuluo.business.platform.claimorder.service.ClaimOrderService;
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
import com.ccbuluo.business.platform.outstock.service.OutstockOrderService;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.servicecenter.dao.BizServiceCenterDao;
import com.ccbuluo.business.platform.servicelog.dao.BizServiceLogDao;
import com.ccbuluo.business.platform.servicelog.service.ServiceLogService;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.usercoreintf.dto.QueryNameByUseruuidsDTO;
import com.ccbuluo.usercoreintf.dto.ServiceCenterDTO;
import com.ccbuluo.usercoreintf.dto.ServiceCenterWorkplaceDTO;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.BasicUserWorkplaceService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 描述 服务服务单service
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
    @Autowired
    private BizOutstockplanDetailDao bizOutstockplanDetailDao;
    @Autowired
    OutstockOrderService outstockOrderService;
    @Autowired
    private ServiceLogService serviceLogService;
    @Autowired
    private BizServiceLogDao bizServiceLogDao;
    @Autowired
    private ClaimOrderDao claimOrderDao;
    @Resource(name = "claimOrderServiceImpl")
    private ClaimOrderService claimOrderServiceImpl;
    @Autowired
    private ClaimOrderService claimOrderService;


    /**
     * 描述 新增服务服务单
     * @param serviceOrderDTO
     * @return com.ccbuluo.http.StatusDto<java.lang.String>
     * @author liuduo
     * @date 2018-09-03 18:54:01
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto<String> saveOrder(SaveServiceOrderDTO serviceOrderDTO) {
        try {
            // 生成服务单号
            String orderCode = null;
            StatusDto<String> order = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.DD);
            if (order.getCode().equals(Constants.SUCCESS_CODE)) {
                orderCode = order.getData();
            } else {
                return StatusDto.buildFailure("生成服务单编码失败！");
            }
            BusinessUser loggedUser = userHolder.getLoggedUser();
            // 保存服务单
            buildSaveBizServiceOrder(serviceOrderDTO, orderCode, loggedUser);
            // 保存车辆停放位置
            buildSaveBizCarPosition(serviceOrderDTO, loggedUser, orderCode);
            // 保存服务单派发
            buildBizServiceDispatch(serviceOrderDTO, orderCode, loggedUser);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("保存服务单失败！", e.getMessage());
            throw e;
        }
    }




    /**
     * 编辑服务单
     * @param editServiceOrderDTO 服务单要保存的信息
     * @return 编辑是否成功
     * @author liuduo
     * @date 2018-09-04 14:06:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editOrder(EditServiceOrderDTO editServiceOrderDTO) {
        try {
            // 编辑服务单
            buildEditBizServiceOrder(editServiceOrderDTO);
            // 编辑车辆停放位置
            buildEditBizCarPosition(editServiceOrderDTO);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("编辑服务单失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 查询服务单列表
     * @param orderStatus 服务单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 服务单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    @Override
    public StatusDto<Page<BizServiceOrder>> queryList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(bizServiceOrderDao.queryList(orderStatus, serviceType, reportOrgno, keyword, offset, pageSize));
    }


    /**
     * 修改服务单状态
     * @param serviceOrderno 服务单编号
     * @param orderStatus 服务单状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-09-04 15:37:36
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editStatus(String serviceOrderno, String orderStatus) {
        try {
            BizServiceLog bizServiceLog = new BizServiceLog();
            bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
            bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
            bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
            bizServiceLog.setSubjectKeyvalue(serviceOrderno);
            // 如果是 待完善 状态，则还需要更新服务单派发表的数据
            if (StringUtils.isNotBlank(orderStatus) ) {
                if (orderStatus.equals(BizServiceOrder.OrderStatusEnum.WAITING_PERFECTION.name())) {
                    bizServiceDispatchDao.updateConfirmed(serviceOrderno);
                    bizServiceLog.setLogContent("维修单接单");
                } else if (orderStatus.equals(BizServiceOrder.OrderStatusEnum.WAITING_RECEIVE.name())) {
                    bizServiceLog.setLogContent("提交维修单");
                } else if (orderStatus.equals(BizServiceOrder.OrderStatusEnum.PROCESSING.name())) {
                    bizServiceLog.setLogContent("开始处理维修单");
                } else if (orderStatus.equals(BizServiceOrder.OrderStatusEnum.WAITING_CHECKING.name())) {
                    bizServiceLog.setLogContent("处理完成，待验收");
                } else if (orderStatus.equals(BizServiceOrder.OrderStatusEnum.WAITING_PAYMENT.name())) {
                    bizServiceLog.setLogContent("验收完成，待付款");
                } else if (orderStatus.equals(BizServiceOrder.OrderStatusEnum.COMPLETED.name())) {
                    bizServiceLog.setLogContent("已完成");
                } else {
                    bizServiceLog.setLogContent("已取消");
                }
            }
            bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
            bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
            bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
            serviceLogService.create(bizServiceLog);
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
     * 根据服务单编号查询服务单详情
     * @param serviceOrderno 服务单编号
     * @return 服务单详情
     * @author liuduo
     * @date 2018-09-05 09:54:40
     */
    @Override
    public StatusDto<DetailServiceOrderDTO> getDetailByOrderNo(String serviceOrderno) {
        // 根据服务单编号查询车牌号
        String carNo = bizServiceOrderDao.getCarNoByServiceOrderno(serviceOrderno);
        // 根据车牌号查询车辆基本信息
        CarcoreInfoDTO data = basicCarcoreInfoService.getCarByCarNo(carNo).getData();
        BizServiceOrder byOrderNo = bizServiceOrderDao.getByOrderNo(serviceOrderno);
        String carVin = byOrderNo.getCarVin();
        // 根据车辆vin码查询车辆停放地址
        BizCarPosition bizCarPosition = bizCarPositionDao.getByCarVin(carVin, serviceOrderno);
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
     * @param pageSize 每页数
     * @return 可以分配的客户经理和服务中心
     * @author liuduo
     * @date 2018-09-05 10:40:35
     */
    @Override
    public StatusDto<Page<ServiceCenterDTO>> serviceCenterList(String province, String city, String area, String orgType, String keyword, Integer offset, Integer pageSize) {
        StatusDtoThriftPage<ServiceCenterDTO> serviceCenterDTOStatusDtoThriftPage = basicUserOrganizationService.queryAfterSaleServiceCenter(province, city, area, orgType, keyword, offset, pageSize);
        Page<ServiceCenterDTO> data = StatusDtoThriftUtils.resolve(serviceCenterDTOStatusDtoThriftPage, ServiceCenterDTO.class).getData();
        if (null != data && null != data.getRows()) {
            List<ServiceCenterDTO> rows = data.getRows();
            // 获取服务中心的code
            List<String> orgCodes = rows.stream().map(item -> item.getOrgCode()).distinct().collect(Collectors.toList());
            if (orgCodes.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(new Page<>());
            }
            // 查询客户经理
            if (StringUtils.isNotBlank(orgType) && BizServiceOrder.ProcessorOrgtypeEnum.CUSTMANAGER.name().equals(orgType)) {
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
     * 分配服务单
     * @param serviceOrderno 服务单编号
     * @param orgType 类型（服务中心还是客户经理）
     * @param orgCodeOrUuid 机构编号或者客户经理uuid
     * @return 服务单是否分配成功
     * @author liuduo
     * @date 2018-09-05 18:32:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto orderAllocation(String serviceOrderno, String orgType, String orgCodeOrUuid) {
        try {
            if (!orgType.equals(BizServiceOrder.ProcessorOrgtypeEnum.SERVICECENTER.name())) {
                throw new CommonException("3009", "不能分配到非服务中心的其他机构！");
            }
            // 查询该服务单被分配的次数，如果大于1则不能分配
            BizServiceOrder byOrderNo = bizServiceOrderDao.getByOrderNo(serviceOrderno);
            if (byOrderNo.getServiceType().equals(BizServiceOrder.ServiceTypeEnum.MAINTAIN.name())) {
                throw new CommonException("3010", "该服务单是保养类型，无法分配，请核对！");
            }
            if (byOrderNo.getDispatchTimes() > Constants.LONG_FLAG_ONE) {
                throw new CommonException("3006", "该服务单已被分配，请核对！");
            }
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
            if (StringUtils.isNotBlank(orgType)) {
                // 创建新的服务单详情并更新旧的
                BizServiceDispatch bizServiceDispatch2 = new BizServiceDispatch();
                bizServiceDispatch2.setServiceOrderno(serviceOrderno);
                bizServiceDispatch2.setPreviousId(id);
                bizServiceDispatch2.setCurrentFlag(Constants.FLAG_ONE);
                bizServiceDispatch2.setProcessorOrgno(orgCodeOrUuid);
                bizServiceDispatch2.setProcessorOrgtype(orgType);
                bizServiceDispatch2.setConfirmed(Constants.STATUS_FLAG_ZERO);
                bizServiceDispatch2.preInsert(loggedUser.getUserId());
                bizServiceDispatchDao.saveBizServiceDispatch(bizServiceDispatch2);
                BizServiceDispatch updateBizServiceDispatch = new BizServiceDispatch();
                updateBizServiceDispatch.setId(id);
                updateBizServiceDispatch.setReplaceOrgno(orgCodeOrUuid);
                updateBizServiceDispatch.setReplaceOrgtype(orgType);
                updateBizServiceDispatch.setDispatchTime(new Date());
                int i = bizServiceDispatchDao.updateReplaceOrgNo(updateBizServiceDispatch);
                if (i == Constants.FAILURESTATUS) {
                    throw new CommonException("3007", "原服务单状态修改失败，请联系管理员！");
                }
                // 分配之后修改该服务单的分配次数
                BizServiceOrder bizServiceOrder = new BizServiceOrder();
                bizServiceOrder.setProcessorOrgno(orgCodeOrUuid);
                bizServiceOrder.setProcessorOrgtype(BizServiceOrder.ProcessorOrgtypeEnum.SERVICECENTER.name());
                bizServiceOrder.setServiceOrderno(serviceOrderno);
                bizServiceOrder.setDispatchTimes(Constants.LONG_FLAG_TWO);
                bizServiceOrderDao.updateDispatchTimes(bizServiceOrder);
            }
            BizServiceLog bizServiceLog = new BizServiceLog();
            bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
            bizServiceLog.setAction(BizServiceLog.actionEnum.SAVE.name());
            bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
            bizServiceLog.setSubjectKeyvalue(serviceOrderno);
            bizServiceLog.setLogContent("客户经理分配");
            bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
            bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
            bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
            serviceLogService.create(bizServiceLog);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("分配失败", e.getMessage());
            throw e;
        }
    }

    /**
     * 查询工时列表
     * @param keyword 关键字
     * @param offset 起始数
     *  @param pageSize 每页数
     * @return 工时列表
     * @author liuduo
     * @date 2018-09-06 10:39:33
     */
    @Override
    public StatusDto<Page<DetailBizServiceMaintainitemDTO>> queryMaintainitem(String keyword, Integer offset, Integer pageSize) {
        Page<DetailBizServiceMaintainitemDTO> detailBizServiceMaintainitemDTOPage = maintainitemService.queryList(keyword, offset, pageSize);
        if (null != detailBizServiceMaintainitemDTOPage && null != detailBizServiceMaintainitemDTOPage.getRows()) {
            List<DetailBizServiceMaintainitemDTO> rows = detailBizServiceMaintainitemDTOPage.getRows();
            List<String> codes = rows.stream().map(DetailBizServiceMaintainitemDTO::getMaintainitemCode).collect(Collectors.toList());
            if (codes.isEmpty()) {
                return StatusDto.buildDataSuccessStatusDto(new Page<>());
            }
            // 获取当前登录人的地址
            String province = "";
            String city = "";
            if (BizServiceOrder.ProcessorOrgtypeEnum.CUSTMANAGER.name().equals(userHolder.getLoggedUser().getOrganization().getOrgType())) {
                BizServiceCustmanager bizServiceCustmanager = bizServiceCustmanagerDao.queryCustManagerByUuid(userHolder.getLoggedUserId());
                StatusDtoThriftBean<ServiceCenterWorkplaceDTO> workplaceByCode = basicUserWorkplaceService.getWorkplaceByCode(bizServiceCustmanager.getServicecenterCode());
                ServiceCenterWorkplaceDTO data1 = StatusDtoThriftUtils.resolve(workplaceByCode, ServiceCenterWorkplaceDTO.class).getData();
                province = data1.getProvince();
                city = data1.getCity();
            } else {
                StatusDtoThriftBean<ServiceCenterWorkplaceDTO> workplaceByCode = basicUserWorkplaceService.getWorkplaceByCode(userHolder.getLoggedUser().getOrganization().getOrgCode());
                ServiceCenterWorkplaceDTO data1 = StatusDtoThriftUtils.resolve(workplaceByCode, ServiceCenterWorkplaceDTO.class).getData();
                province = data1.getProvince();
                city = data1.getCity();
            }
            // 查询地区倍数
            List<BizServiceMultipleprice> bizServiceMultiplepriceList = multiplepriceService.queryMultiple(codes, province, city);
            Map<String, BizServiceMultipleprice> collect = bizServiceMultiplepriceList.stream().collect(Collectors.toMap(BizServiceMultipleprice::getMaintainitemCode, Function.identity()));
            rows.forEach(item -> {
                BizServiceMultipleprice bizServiceMultipleprice = collect.get(item.getMaintainitemCode());
                if (null != bizServiceMultipleprice && null != bizServiceMultipleprice.getMultiple()) {
                    item.setUnitPrice(item.getUnitPrice().multiply(new BigDecimal(bizServiceMultipleprice.getMultiple())));
                } else {
                    item.setUnitPrice(item.getUnitPrice().multiply(new BigDecimal(Constants.FLAG_ONE)));
                }
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
            // 1、删除原有工时和零配件
            bizServiceorderDetailDao.deleteByOrderNo(saveOrderDetailDTO.getServiceOrderno());
            // 2、保存工时
            saveMaintaintem(saveOrderDetailDTO);
            // 3、还库存
            returnStock(saveOrderDetailDTO);
            // 4、占库存
            occupyStock(saveOrderDetailDTO);
            // 5、保存零配件
            saveMerchandise(saveOrderDetailDTO);
            BizServiceLog bizServiceLog = new BizServiceLog();
            bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
            bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
            bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
            bizServiceLog.setSubjectKeyvalue(saveOrderDetailDTO.getServiceOrderno());
            bizServiceLog.setLogContent("开始处理维修");
            bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
            bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
            bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
            serviceLogService.create(bizServiceLog);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("3003", "保存失败！");
            throw e;
        }
    }

    /**
     * 保存零配件
     * @param saveOrderDetailDTO 维修单详单
     * @author liuduo
     * @date 2018-09-07 15:40:44
     */
    private void saveMerchandise(SaveOrderDetailDTO saveOrderDetailDTO) {
        List<SaveMerchandiseDTO> saveMerchandiseDTOS = saveOrderDetailDTO.getSaveMerchandiseDTOS();
        if (!saveMerchandiseDTOS.isEmpty()) {
            List<SaveMerchandiseDTO> saveMerchandiseDTOS1 = Lists.newArrayList();
            saveMerchandiseDTOS.forEach(item -> {
                if (StringUtils.isBlank(item.getServiceOrgno())) {
                    item.setServiceOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
                    item.setServiceOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
                    item.setServiceUserid(userHolder.getLoggedUserId());
                    item.setServiceUsername(userHolder.getLoggedUser().getName());
                }
                item.preInsert(userHolder.getLoggedUserId());
                item.setServiceOrderno(saveOrderDetailDTO.getServiceOrderno());
                saveMerchandiseDTOS1.add(item);
            });
            bizServiceorderDetailDao.saveMerchandise(saveMerchandiseDTOS1);
        }
    }


    /**
     * 占库存
     * @param saveOrderDetailDTO 维修单详单
     * @author liuduo
     * @date 2018-09-07 15:21:56
     */
    private void occupyStock(SaveOrderDetailDTO saveOrderDetailDTO) {
        List<SaveMerchandiseDTO> saveMerchandiseDTOS = saveOrderDetailDTO.getSaveMerchandiseDTOS();
        if (!saveMerchandiseDTOS.isEmpty()) {
            String orgCode = "";
            for (SaveMerchandiseDTO saveMerchandiseDTO : saveMerchandiseDTOS) {
                List<RelOrdstockOccupy> relOrdstockOccupyList = Lists.newArrayList();
                List<BizStockDetail> bizStockDetailList = Lists.newArrayList();
                // 根据商品编号和使用人查询库存
                String orgNo = "";
                if (StringUtils.isNotBlank(saveMerchandiseDTO.getServiceOrgno())) {
                    orgNo = saveMerchandiseDTO.getServiceOrgno();
                } else {
                    orgNo = userHolder.getLoggedUser().getOrganization().getOrgCode();
                }
                List<BizStockDetail> stockDetailByOrder = bizStockDetailDao.getStockDetailByOrder(saveMerchandiseDTO.getProductNo(), orgNo);
                long count = stockDetailByOrder.stream().collect(Collectors.summarizingLong(BizStockDetail::getValidStock)).getSum();
                if (saveMerchandiseDTO.getAmount() > count) {
                    throw new CommonException("3008", "现有库存小于使用库存，请核对！");
                }
                // 使用的数量
                Long amount = saveMerchandiseDTO.getAmount();
                // 使用的数量减去实际的数量后得到的数量
                Long lastNum = 0L;
                for (BizStockDetail bizStockDetail : stockDetailByOrder) {
                    // 库存的实际数量
                    Long validStock = bizStockDetail.getValidStock();
                    lastNum = amount - validStock;
                    if (lastNum <= Constants.LONG_FLAG_ZERO) {
                        // 占用库存，关联库存关系
                        bizStockDetail.setValidStock(amount);
                        bizStockDetail.setOccupyStock(amount);
                        bizStockDetailList.add(bizStockDetail);
                        RelOrdstockOccupy relOrdstockOccupy = new RelOrdstockOccupy();
                        relOrdstockOccupy.setOrderType(BizServiceOrder.ServiceTypeEnum.REPAIR.name());
                        relOrdstockOccupy.setDocNo(saveOrderDetailDTO.getServiceOrderno());
                        relOrdstockOccupy.setStockId(bizStockDetail.getId());
                        relOrdstockOccupy.setOccupyNum(amount);
                        relOrdstockOccupy.setOccupyStarttime(new Date());
                        relOrdstockOccupy.preInsert(userHolder.getLoggedUserId());
                        relOrdstockOccupyList.add(relOrdstockOccupy);
                        break;
                    } else {
                        // 占用库存，关联库存关系
                        bizStockDetail.setValidStock(validStock);
                        bizStockDetail.setOccupyStock(validStock);
                        bizStockDetailList.add(bizStockDetail);
                        RelOrdstockOccupy relOrdstockOccupy = new RelOrdstockOccupy();
                        relOrdstockOccupy.setOrderType(BizServiceOrder.ServiceTypeEnum.REPAIR.name());
                        relOrdstockOccupy.setDocNo(saveOrderDetailDTO.getServiceOrderno());
                        relOrdstockOccupy.setStockId(bizStockDetail.getId());
                        relOrdstockOccupy.setOccupyNum(validStock);
                        relOrdstockOccupy.setOccupyStarttime(new Date());
                        relOrdstockOccupy.preInsert(userHolder.getLoggedUserId());
                        relOrdstockOccupyList.add(relOrdstockOccupy);
                        amount = lastNum;
                    }
                }
                // 占用库存
                int i = bizStockDetailDao.batchUpdateValidStock(bizStockDetailList);
                if (i != bizStockDetailList.size()) {
                    throw new CommonException("3005", "库存占用失败！");
                }
                // 关联库存关系
                bizAllocateTradeorderDao.batchInsertRelOrdstockOccupy(relOrdstockOccupyList);
            }
        }
    }


    /**
     * 还库存
     * @param saveOrderDetailDTO 维修单详单
     * @author liuduo
     * @date 2018-09-07 14:23:37
     */
    private void returnStock(SaveOrderDetailDTO saveOrderDetailDTO) {
        // 查询关联关系
        List<RelOrdstockOccupy> relOrdstockOccupyByApplyNo = bizAllocateTradeorderDao.getRelOrdstockOccupy(saveOrderDetailDTO.getServiceOrderno(), userHolder.getLoggedUser().getOrganization().getOrgCode());
        List<BizStockDetail> bizStockDetailList = Lists.newArrayList();
        if (!relOrdstockOccupyByApplyNo.isEmpty()) {
            relOrdstockOccupyByApplyNo.forEach(item -> {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setId(item.getStockId());
                bizStockDetail.setValidStock(item.getOccupyNum());
                bizStockDetail.setOccupyStock(item.getOccupyNum());
                bizStockDetailList.add(bizStockDetail);
            });
            // 还库存
            int i = bizStockDetailDao.updateValidAndOccupy(bizStockDetailList);
            if (i != bizStockDetailList.size()) {
                throw new CommonException("3004", "库存归还失败！");
            }
            // 删除关联关系
            bizAllocateTradeorderDao.deleteRelation(saveOrderDetailDTO.getServiceOrderno());
        }
    }


    /**
     * 保存工时
     * @param saveOrderDetailDTO 维修单详单
     * @author liuduo
     * @date 2018-09-07 14:23:37
     */
    private void saveMaintaintem(SaveOrderDetailDTO saveOrderDetailDTO) {
        List<SaveMaintaintemDTO> saveMaintaintemDTOS = saveOrderDetailDTO.getSaveMaintaintemDTOS();
        if (!saveMaintaintemDTOS.isEmpty()) {
            List<SaveMaintaintemDTO> saveMaintaintemDTOS1 = Lists.newArrayList();
            saveMaintaintemDTOS.forEach(item -> {
                if (StringUtils.isBlank(item.getServiceOrgno())) {
                    item.setServiceOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
                    item.setServiceOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
                    item.setServiceUserid(userHolder.getLoggedUserId());
                    item.setServiceUsername(userHolder.getLoggedUser().getName());
                }
                item.preInsert(userHolder.getLoggedUserId());
                item.setServiceOrderno(saveOrderDetailDTO.getServiceOrderno());
                saveMaintaintemDTOS1.add(item);
            });
            bizServiceorderDetailDao.saveMaintaintem(saveMaintaintemDTOS1);
        }
    }


    /**
     * 组装服务单派发
     * @param orderCode 服务单编号
     * @param loggedUser 用户信息
     * @author liuduo
     * @date 2018-09-05 16:46:28
     */
    private void buildBizServiceDispatch(SaveServiceOrderDTO serviceOrderDTO, String orderCode, BusinessUser loggedUser) {
        BizServiceDispatch bizServiceDispatch = new BizServiceDispatch();
        bizServiceDispatch.setServiceOrderno(orderCode);
        bizServiceDispatch.setCurrentFlag(Constants.FLAG_ONE);
        // 根据车牌号查询该车属于哪个机构
        String uuid = basicCarcoreInfoService.getUuidByPlateNum(serviceOrderDTO.getCarNo());
        if (StringUtils.isBlank(uuid)) {
            throw new CommonException("3001", "该车辆没有被分配到客户经理，请核对！");
        }
        bizServiceDispatch.setProcessorUuid(uuid);
        // 根据用户uuid查询用户机构类型
        StatusDtoThriftBean<UserInfoDTO> userDetail = innerUserInfoService.findUserDetail(uuid);
        UserInfoDTO data = StatusDtoThriftUtils.resolve(userDetail, UserInfoDTO.class).getData();
        bizServiceDispatch.setProcessorOrgtype(data.getOrgType());
        bizServiceDispatch.setProcessorOrgno(data.getOrgCode());
        bizServiceDispatch.setConfirmed(Constants.STATUS_FLAG_ZERO);
        bizServiceDispatch.preInsert(loggedUser.getUserId());
        bizServiceDispatchDao.saveBizServiceDispatch(bizServiceDispatch);
    }


    /**
     * 组装编辑用的车辆停放位置
     * @param editServiceOrderDTO 服务单信息
     * @author liuduo
     * @date 2018-09-04 14:53:15
     */
    private void buildEditBizCarPosition(EditServiceOrderDTO editServiceOrderDTO) {
        // 保存省市区
        BizCarPosition bizCarPosition = new BizCarPosition();
        bizCarPosition.setServiceOrderno(editServiceOrderDTO.getServiceOrderno());
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
     * 组装编辑用的服务单信息
     * @param editServiceOrderDTO 服务单信息
     * @author liuduo
     * @date 2018-09-04 14:39:36
     */
    private void buildEditBizServiceOrder(EditServiceOrderDTO editServiceOrderDTO) {
        // 根据单号查询维修单
        BizServiceOrder bizServiceOrder = bizServiceOrderDao.getByOrderNo(editServiceOrderDTO.getServiceOrderno());
        if (null == bizServiceOrder) {
            throw new CommonException("3011", "未查询到该维修单，请核对！");
        } else {
            bizServiceOrder.setServiceOrderno(editServiceOrderDTO.getServiceOrderno());
            bizServiceOrder.setCarNo(editServiceOrderDTO.getCarNo());
            bizServiceOrder.setCarVin(editServiceOrderDTO.getCarVin());
            bizServiceOrder.setServiceType(editServiceOrderDTO.getServiceType());
            bizServiceOrder.setCustomerName(editServiceOrderDTO.getCustomerName());
            bizServiceOrder.setCustomerPhone(editServiceOrderDTO.getCustomerPhone());
            bizServiceOrder.setCustomerOrgno(editServiceOrderDTO.getCustomerOrgno());
            bizServiceOrder.setReserveContacter(editServiceOrderDTO.getReserveContacter());
            bizServiceOrder.setReservePhone(editServiceOrderDTO.getReservePhone());
            bizServiceOrder.setServiceTime(editServiceOrderDTO.getServiceTime());
            bizServiceOrder.setProblemContent(editServiceOrderDTO.getProblemContent());
            bizServiceOrder.preUpdate(userHolder.getLoggedUserId());
            bizServiceOrderDao.updateBizServiceOrder(bizServiceOrder);
        }
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
        bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
        bizServiceLog.setSubjectKeyvalue(editServiceOrderDTO.getServiceOrderno());
        bizServiceLog.setLogContent("编辑维修单");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
    }


    /**
     * 组装车辆停放位置
     * @param serviceOrderDTO 服务单信息
     * @param loggedUser  当前登录人
     * @author liuduo
     * @date 2018-09-04 11:59:33
     */
    private void buildSaveBizCarPosition(SaveServiceOrderDTO serviceOrderDTO, BusinessUser loggedUser, String serviceOrderNo) {
        // 保存省市区
        BizCarPosition bizCarPosition = new BizCarPosition();
        bizCarPosition.setServiceOrderno(serviceOrderNo);
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
     * 组装服务单
     * @param serviceOrderDTO 服务单信息
     * @param orderCode 服务单编号
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
        bizServiceOrder.setReportOrgno(loggedUser.getOrganization().getOrgCode());
        bizServiceOrder.setReportOrgtype(loggedUser.getOrganization().getOrgType());
        bizServiceOrder.setReportTime(new Date());
        bizServiceOrder.setCustomerName(serviceOrderDTO.getCustomerName());
        bizServiceOrder.setCustomerPhone(serviceOrderDTO.getCustomerPhone());
        bizServiceOrder.setCustomerOrgno(serviceOrderDTO.getCustomerOrgno());
        bizServiceOrder.setReserveContacter(serviceOrderDTO.getReserveContacter());
        bizServiceOrder.setReservePhone(serviceOrderDTO.getReservePhone());
        if (loggedUser.getOrganization().getOrgType().equals(BizServiceOrder.ProcessorOrgtypeEnum.STORE.name())) {
            bizServiceOrder.setOrderStatus(BizServiceOrder.OrderStatusEnum.DRAFT.name());
        } else {
            bizServiceOrder.setOrderStatus(BizServiceOrder.OrderStatusEnum.WAITING_RECEIVE.name());
        }
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
        bizServiceOrder.setProcessorOrgno(data.getOrgCode());
        bizServiceOrder.setServiceTime(serviceOrderDTO.getServiceTime());
        bizServiceOrder.setProblemContent(serviceOrderDTO.getProblemContent());
        bizServiceOrder.preInsert(loggedUser.getUserId());
        bizServiceOrderDao.saveBizServiceOrder(bizServiceOrder);
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.SAVE.name());
        bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
        bizServiceLog.setSubjectKeyvalue(orderCode);
        if (loggedUser.getOrganization().getOrgType().equals(BizServiceOrder.ProcessorOrgtypeEnum.STORE.name())) {
            bizServiceLog.setLogContent("提交报修");
        } else {
            bizServiceLog.setLogContent("新增维修单");
        }
        bizServiceLog.setOwnerOrgno(loggedUser.getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(loggedUser.getOrganization().getOrgName());
        bizServiceLog.preInsert(loggedUser.getUserId());
        serviceLogService.create(bizServiceLog);
    }

    /**
     * 提交订单
     * @param serviceOrderno 订单编号
     * @return 订单是否提交成功
     * @author weijb
     * @date 2018-09-07 17:32:52
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto ordersubmit(String serviceOrderno){
        try {
            // 生成出库计划并出库
            buildOutstockplanAndOut(serviceOrderno);
            // 修改维修单状态(待验收)
            bizServiceOrderDao.editStatus(serviceOrderno, BizServiceOrder.OrderStatusEnum.WAITING_CHECKING.name());
            // 记录日志
            addlog(serviceOrderno,"提交验收",BizServiceLog.actionEnum.UPDATE.name());
            return StatusDto.buildSuccessStatusDto("提交成功");
        } catch (Exception e) {
            throw new CommonException("0", "提交失败！");
        }
    }

    /**
     *  生成出库计划并出库
     * @param orderNo 服务单号
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-07 14:10:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void buildOutstockplanAndOut(String orderNo){
        // 查询服务单详情
        List<BizServiceorderDetail> orderDetails = bizServiceorderDetailDao.queryServiceorderDetailList(orderNo);
        // 有可能只有工时没有使用零配件
        if(null != orderDetails && orderDetails.size() != 0){
            List<String> orderOrgNo = getServiceOrgNo(orderDetails);
            // 有多个维修点情况
            for (String orgCode : orderOrgNo){
                List<String> codeList = getProductList(orderDetails);
                // 根据卖方code和商品code（list）查出库存列表
                List<BizStockDetail> stockDetails = bizStockDetailDao.getStockDetailList(orgCode, codeList);
                // 查询占用关系
                List<RelOrdstockOccupy> relOrdstockOccupies = bizAllocateTradeorderDao.getRelOrdstockOccupyBy(orderNo,orgCode);
                // 生成出库计划
                List<BizOutstockplanDetail> outStpcks = buildOutstockplan(orderDetails, stockDetails, relOrdstockOccupies);
                // 批量保存出库计划详情
                bizOutstockplanDetailDao.batchOutstockplanDetail(outStpcks);
            }
            // 查询出库计划
            List<BizOutstockplanDetail> outstockPlans = bizOutstockplanDetailDao.getOutstockplansByApplyNo(orderNo,null);
            // 调用自动出库
            outstockOrderService.autoSaveOutstockOrder(orderNo, outstockPlans,ApplyTypeEnum.SERVICEORDER.name());
        }
    }
    private List<String> getServiceOrgNo(List<BizServiceorderDetail> orderDetails){
        List<String> list = new ArrayList<String>();
        for(BizServiceorderDetail order : orderDetails){
            if(! list.contains(order.getServiceOrgno())){
                list.add(order.getServiceOrgno());
            }
        }
        return list;
    }

    /**
     *  生成出库计划并出库
     * @param orderDetails 服务单详情
     * @param stockDetails 库存详细
     * @param relOrdstockOccupies 占用关系
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-07 14:10:11
     */
    private List<BizOutstockplanDetail> buildOutstockplan(List<BizServiceorderDetail> orderDetails, List<BizStockDetail> stockDetails, List<RelOrdstockOccupy> relOrdstockOccupies){
        List<BizOutstockplanDetail> outStockList = new ArrayList<BizOutstockplanDetail>();
        for(RelOrdstockOccupy ro : relOrdstockOccupies){
            if(distinctOutStockPlan(ro,outStockList)){
                continue;
            }
            BizOutstockplanDetail outPlan = new BizOutstockplanDetail();
            for(BizStockDetail stock : stockDetails){
                if(ro.getStockId().intValue() == stock.getId().intValue()){// 关系库存批次id和库存批次id相等
                    BizServiceorderDetail order = new BizServiceorderDetail();
                    Optional<BizServiceorderDetail> orderFilter = orderDetails.stream() .filter(orderDetail -> stock.getProductNo().equals(orderDetail.getProductNo())) .findFirst();
                    if (orderFilter.isPresent()) {
                        order = orderFilter.get();
                    }
                    outPlan.setProductNo(order.getProductNo());
                    outPlan.setProductType(stock.getProductType());
                    outPlan.setProductCategoryname(stock.getProductCategoryname());
                    outPlan.setProductName(stock.getProductName());
                    outPlan.setProductUnit(stock.getProductUnit());
                    // 交易批次号（服务单编号）
                    outPlan.setTradeNo(String.valueOf(order.getOrderNo()));
                    outPlan.setSupplierNo(stock.getSupplierNo());
                    outPlan.setApplyDetailId(order.getId());
                    // 销售价
                    outPlan.setSalesPrice(BigDecimal.valueOf(order.getUnitPrice()));
                    // 出库计划的状态（计划执行中）
                    outPlan.setPlanStatus(StockPlanStatusEnum.DOING.toString());
                    outPlan.preInsert(userHolder.getLoggedUserId());
                    outPlan.setStockType(BizStockDetail.StockTypeEnum.VALIDSTOCK.name());
                    outPlan.setPlanOutstocknum(ro.getOccupyNum());// 计划出库数量applyNum
                    // 库存所在机构编号
                    outPlan.setOutOrgno(stock.getOrgNo());
                    // 库存所在机构的仓库编号
                    outPlan.setOutRepositoryNo(stock.getRepositoryNo());
                    // 库存编号id
                    outPlan.setStockId(stock.getId());
                    outPlan.setCostPrice(stock.getCostPrice());
                    outPlan.setOutstockType(OutstockTypeEnum.SERVICEORDER.name());
                    outStockList.add(outPlan);
                    continue;
                }
            }
        }
        return outStockList;
    }

    /**
     *  对出库计划合并
     * @param
     * @exception
     * @return
     * @author weijb
     * @date 2018-09-17 15:31:45
     */
    private boolean distinctOutStockPlan(RelOrdstockOccupy ro, List<BizOutstockplanDetail> outStockList){
        boolean flag = false;
        if(null == outStockList || outStockList.size() == 0){
            return flag;
        }
        for(BizOutstockplanDetail outPlan : outStockList){
            boolean status = ro.getProductNo().equals(outPlan.getProductNo()) && ro.getSupplierNo().equals(outPlan.getSupplierNo()) && ro.getCostPrice().compareTo(outPlan.getCostPrice()) == 0;
            // 如果同一个商品的供应商和价格都相同，那么就合并
            if(status){
                outPlan.setPlanOutstocknum(outPlan.getPlanOutstocknum() + ro.getOccupyNum());
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取商品code
     * @param orderDetails 订单详细
     * @author weijb
     * @date 2018-08-11 13:35:41
     */
    private static List<String> getProductList(List<BizServiceorderDetail> orderDetails){
        List<String> list = new ArrayList<String>();
        for(BizServiceorderDetail detail : orderDetails){
            list.add(detail.getProductNo());
        }
        return list;
    }

    /**
     * 验收
     * @param serviceOrderno 订单编号
     * @return 订单是否验收成功
     * @author weijb
     * @date 2018-09-07 17:32:52
     */
    @Override
    public StatusDto acceptance(String serviceOrderno){
        try {
            // 调用生成索赔单(支付成功)
            claimOrderService.generateClaimForm(serviceOrderno);
            // 验收完成
            bizServiceOrderDao.editStatus(serviceOrderno, BizServiceOrder.OrderStatusEnum.WAITING_PAYMENT.name());
            // 修改维修单状态
            // 记录日志
            addlog(serviceOrderno,"验收通过",BizServiceLog.actionEnum.UPDATE.name());
            return StatusDto.buildSuccessStatusDto("验收成功");
        } catch (Exception e) {
            throw new CommonException("0", "验收失败！");
        }
    }

    /**
     * 查询维修单日志
     * @param serviceOrderno 维修单编号
     * @return 维修单日志
     * @author liuduo
     * @date 2018-09-10 14:45:37
     */
    @Override
    public StatusDto<List<BizServiceLog>> orderLog(String serviceOrderno) {
        // 操作的主体的类型
        String subjectType = "ServiceOrderServiceImpl";
        List<BizServiceLog> bizServiceLogs = bizServiceLogDao.orderLog(serviceOrderno, subjectType);
        List<String> uuids = bizServiceLogs.stream().map(BizServiceLog::getCreator).collect(Collectors.toList());
        if (uuids.isEmpty()) {
            return StatusDto.buildDataSuccessStatusDto(Lists.newArrayList());
        }
        StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuids = innerUserInfoService.queryNameByUseruuids(uuids);
        List<QueryNameByUseruuidsDTO> data = StatusDtoThriftUtils.resolve(queryNameByUseruuids, QueryNameByUseruuidsDTO.class).getData();
        Map<String, QueryNameByUseruuidsDTO> collect = data.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, Function.identity()));
        bizServiceLogs.forEach(item -> {
            QueryNameByUseruuidsDTO queryNameByUseruuidsDTO = collect.get(item.getCreator());
            item.setOperator(queryNameByUseruuidsDTO.getName());
        });
        return StatusDto.buildDataSuccessStatusDto(bizServiceLogs);
    }

    /**BasicCarcoreInfoServiceImpl
     * 查询维修单的工时详情和零配件详情
     * @param serviceOrderno 维修单的编号
     * @return Map<String , List < ProductDetailDTO>>
     * @author zhangkangjian
     * @date 2018-09-10 17:41:01
     */
    @Override
    public Map<String, List<ProductDetailDTO>> querymaintainitemAndFittingDetail(String serviceOrderno) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setServiceOrderno(serviceOrderno);
        // 工时类型
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.MAINTAINITEM.name());
        List<ProductDetailDTO> maintainitemDetail = claimOrderDao.queryMaintainitemDetail(productDetailDTO);
        // 查询零配件信息
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.FITTING.name());
        // 查询零配件列表信息
        List<ProductDetailDTO> fittingDetail = claimOrderServiceImpl.queryFitingDetailList(productDetailDTO);
        HashMap<String, List<ProductDetailDTO>> map = Maps.newHashMap();
        map.put("maintainitemDetail", maintainitemDetail);
        map.put("fittingDetail", fittingDetail);
        return map;
    }

    /**
     * 查询维修单列表(门店用)
     * @param orderStatus 维修单状态
     * @param serviceType 服务类型
     * @param keyword 关键字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 维修单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    @Override
    public StatusDto<Page<BizServiceOrder>> queryStoreList(String orderStatus, String serviceType, String reportOrgno, String keyword, Integer offset, Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(bizServiceOrderDao.queryStoreList(orderStatus, serviceType, reportOrgno, keyword, offset, pageSize));
    }

    /**
     * 根据工时code查询公司是否被引用
     * @param maintainitemCode 工时code
     * @return 工时是否被引用
     * @author liuduo
     * @date 2018-09-18 14:36:12
     */
    @Override
    public Boolean getByProductCode(String maintainitemCode) {
        return bizServiceOrderDao.getByProductCode(maintainitemCode);
    }

    /**
     * 查询维修单状态数量
     * @return 维修单各种状态的数量
     * @author liuduo
     * @date 2018-09-19 17:21:44
     */
    @Override
    public Map<String, Long> queryOrderStatusNum(String reportOrgno) {
        List<BizServiceOrder> statusList = bizServiceOrderDao.queryOrderStatusNum(reportOrgno);
        Map<String, Long> map = buildMap();
        if (!statusList.isEmpty()) {
            Map<String, Long> collect = statusList.stream().collect(Collectors.groupingBy(BizServiceOrder::getOrderStatus, Collectors.counting()));
            collect.forEach(map::put);
            map.put("all", (long) statusList.size());
        }
        return map;
    }


    /**
     * 查询维修单状态数量(门店用)
     * @return 维修单各种状态的数量
     * @author liuduo
     * @date 2018-09-19 17:21:44
     */
    @Override
    public Map<String, Long> queryStoreOrderStatusNum(String reportOrgno) {
        List<BizServiceOrder> statusList = bizServiceOrderDao.queryStoreOrderStatusNum(reportOrgno);
        Map<String, Long> map = buildMap();
        if (!statusList.isEmpty()) {
            Map<String, Long> collect = statusList.stream().collect(Collectors.groupingBy(BizServiceOrder::getOrderStatus, Collectors.counting()));
            collect.forEach(map::put);
            map.put("all", (long) statusList.size());
        }
        return map;
    }

    private Map<String, Long> buildMap() {
        Map<String, Long> map = Maps.newHashMap();
        map.put(BizServiceOrder.OrderStatusEnum.DRAFT.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.WAITING_RECEIVE.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.WAITING_PERFECTION.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.PROCESSING.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.WAITING_CHECKING.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.WAITING_PAYMENT.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.COMPLETED.name(), 0L);
        map.put(BizServiceOrder.OrderStatusEnum.CANCELED.name(), 0L);
        map.put("all", 0L);
        return map;
    }

    /**
     * 维修单取消
     * @param serviceOrderno 维修单编号
     * @return 修改是否成功
     * @author weijb
     * @date 2018-09-22 18:41:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto cancelApply(String serviceOrderno){
        BizServiceOrder serviceOrder = bizServiceOrderDao.getByOrderNo(serviceOrderno);
        // 取消防护
        boolean flag = false;
        flag = serviceOrder.getOrderStatus().equals(BizServiceOrder.OrderStatusEnum.WAITING_RECEIVE.name())
                || serviceOrder.getOrderStatus().equals(BizServiceOrder.OrderStatusEnum.WAITING_PERFECTION.name())
                || serviceOrder.getOrderStatus().equals(BizServiceOrder.OrderStatusEnum.PROCESSING.name());
        if (! flag) {
            BizServiceOrder.OrderStatusEnum statusEnum = BizServiceOrder.OrderStatusEnum.valueOf(serviceOrder.getOrderStatus());
            throw new CommonException("0", statusEnum.getLabel()+"状态不能取消！");
        }
        // 恢复库存
        regainStock(serviceOrderno);
        // 删除关联关系
        bizAllocateTradeorderDao.deleteRelation(serviceOrderno);
        // 更改维修单状态
        bizServiceOrderDao.editStatus(serviceOrderno, BizServiceOrder.OrderStatusEnum.CANCELED.name());
        return StatusDto.buildSuccessStatusDto("取消成功！");
    }

    /**
     * 查询工时列表(平台端添加保养套餐使用)
     * @param keyword 关键字
     * @param offset 起始数
     *  @param pageSize 每页数
     * @return 工时列表
     * @author liuduo
     * @date 2018-09-06 10:39:33
     */
    @Override
    public StatusDto<Page<DetailBizServiceMaintainitemDTO>> platformQueryMaintainitem(String keyword, Integer offset, Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(maintainitemService.queryList(keyword, offset, pageSize));
    }

    private void regainStock(String serviceOrderno){
        // 查询关联关系
        List<RelOrdstockOccupy> relOrdstockOccupyByApplyNo = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(serviceOrderno);
        List<BizStockDetail> bizStockDetailList = Lists.newArrayList();
        if (!relOrdstockOccupyByApplyNo.isEmpty()) {
            relOrdstockOccupyByApplyNo.forEach(item -> {
                BizStockDetail bizStockDetail = new BizStockDetail();
                bizStockDetail.setId(item.getStockId());
                bizStockDetail.setValidStock(item.getOccupyNum());
                bizStockDetail.setOccupyStock(item.getOccupyNum());
                bizStockDetailList.add(bizStockDetail);
            });
            // 还库存
            int i = bizStockDetailDao.updateValidAndOccupy(bizStockDetailList);
            if (i != bizStockDetailList.size()) {
                throw new CommonException("3004", "库存归还失败！");
            }
        }
    }

    /**
     * 记录日志
     * @param applyNo 申请单号
     * @param content 日志内容
     * @param action 动作
     */
    private void addlog(String applyNo,String content,String action){
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
        bizServiceLog.setAction(action);
        bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
        bizServiceLog.setSubjectKeyvalue(applyNo);
        bizServiceLog.setLogContent(content);
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
    }

}
