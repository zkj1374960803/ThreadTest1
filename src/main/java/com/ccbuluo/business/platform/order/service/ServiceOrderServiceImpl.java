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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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
            buildSaveBizCarPosition(serviceOrderDTO, loggedUser);
            // 保存服务单派发
            buildBizServiceDispatch(orderCode, loggedUser);
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
     * @param pagesize 每页数
     * @return 服务单列表
     * @author liuduo
     * @date 2018-09-04 15:06:55
     */
    @Override
    public StatusDto<Page<BizServiceOrder>> queryList(String orderStatus, String serviceType, String keyword, Integer offset, Integer pagesize) {
        return StatusDto.buildDataSuccessStatusDto(bizServiceOrderDao.queryList(orderStatus, serviceType, keyword, offset, pagesize));
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
            // 如果是 待完善 状态，则还需要更新服务单派发表的数据
            if (StringUtils.isNotBlank(orderStatus) && orderStatus.equals(BizServiceOrder.OrderStatusEnum.WAITING_PERFECTION.name())) {
                bizServiceDispatchDao.updateConfirmed(serviceOrderno);
                BizServiceLog bizServiceLog = new BizServiceLog();
                bizServiceLog.setModel(BizServiceLog.modelEnum.SERVICE.name());
                bizServiceLog.setAction(BizServiceLog.actionEnum.UPDATE.name());
                bizServiceLog.setSubjectType("ServiceOrderServiceImpl");
                bizServiceLog.setSubjectKeyvalue(serviceOrderno);
                if (userHolder.getLoggedUser().getOrganization().getOrgType().equals(BizServiceOrder.ProcessorOrgtypeEnum.CUSTMANAGER.name())) {
                    bizServiceLog.setLogContent("客户经理接单");
                } else {
                    bizServiceLog.setLogContent("服务中心接单");
                }
                bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
                bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
                bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
                serviceLogService.create(bizServiceLog);
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
            // 查询该服务单被分配的次数，如果大于1则不能分配
            BizServiceOrder byOrderNo = bizServiceOrderDao.getByOrderNo(serviceOrderno);
            if (byOrderNo.getDispatchTimes() > Constants.LONG_FLAG_ONE) {
                throw new CommonException("3006", "该服务单已被分配，请核对！");
            }
            if (StringUtils.isNotBlank(orgType)) {
                // 创建新的服务单详情并更新旧的
                BizServiceDispatch bizServiceDispatch2 = new BizServiceDispatch();
                bizServiceDispatch2.setServiceOrderno(serviceOrderno);
                bizServiceDispatch2.setPreviousId(id);
                bizServiceDispatch2.setCurrentFlag(Constants.FLAG_ONE);
                bizServiceDispatch2.setProcessorOrgno(loggedUser.getOrganization().getOrgCode());
                bizServiceDispatch2.setProcessorOrgtype(loggedUser.getOrganization().getOrgType());
                bizServiceDispatch2.setProcessorUuid(loggedUser.getUserId());
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
                bizServiceOrderDao.updateDispatchTimes(serviceOrderno, Constants.LONG_FLAG_TWO);
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
            StatusDtoThriftBean<ServiceCenterWorkplaceDTO> workplaceByCode = basicUserWorkplaceService.getWorkplaceByCode(data.getOrgCode());
            ServiceCenterWorkplaceDTO data1 = StatusDtoThriftUtils.resolve(workplaceByCode, ServiceCenterWorkplaceDTO.class).getData();
            String province = data1.getProvince();
            String city = data1.getCity();
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
                    item.setServiceUsername(userHolder.getLoggedUser().getUsername());
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
            List<BizStockDetail> bizStockDetailList = Lists.newArrayList();
            List<RelOrdstockOccupy> relOrdstockOccupyList = Lists.newArrayList();
            for (SaveMerchandiseDTO saveMerchandiseDTO : saveMerchandiseDTOS) {
                // 根据商品编号和使用人查询库存
                String orgNo = "";
                if (StringUtils.isNotBlank(saveMerchandiseDTO.getServiceOrgno())) {
                    orgNo = saveMerchandiseDTO.getServiceOrgno();
                } else {
                    orgNo = userHolder.getLoggedUser().getOrganization().getOrgCode();
                }
                List<BizStockDetail> stockDetailByOrder = bizStockDetailDao.getStockDetailByOrder(saveMerchandiseDTO.getProductNo(), orgNo);
                long count = stockDetailByOrder.stream().map(BizStockDetail::getValidStock).count();
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
        List<RelOrdstockOccupy> relOrdstockOccupyByApplyNo = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(saveOrderDetailDTO.getServiceOrderno());
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
                    item.setServiceUsername(userHolder.getLoggedUser().getUsername());
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
     * @param editServiceOrderDTO 服务单信息
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
     * 组装编辑用的服务单信息
     * @param editServiceOrderDTO 服务单信息
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
            bizServiceLog.setLogContent("提交保修");
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
    private void buildOutstockplanAndOut(String orderNo){
        // 查询服务单详情
        List<BizServiceorderDetail> orderDetails = bizServiceorderDetailDao.queryServiceorderDetailList(orderNo);
        if(null == orderDetails || orderDetails.size() == 0){
            return;
        }
        List<String> codeList = getProductList(orderDetails);
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 根据卖方code和商品code（list）查出库存列表
        List<BizStockDetail> stockDetails = bizStockDetailDao.getStockDetailListByOrgAndProduct(orgCode, codeList);
        // 查询占用关系
        List<RelOrdstockOccupy> relOrdstockOccupies = bizAllocateTradeorderDao.getRelOrdstockOccupyByApplyNo(orderNo);
        // 生成出库计划
        List<BizOutstockplanDetail> outStpcks = buildOutstockplan(orderDetails, stockDetails, relOrdstockOccupies);
        // 批量保存出库计划详情
        bizOutstockplanDetailDao.batchOutstockplanDetail(outStpcks);
        // 查询出库计划
        List<BizOutstockplanDetail> outstockPlans = bizOutstockplanDetailDao.getOutstockplansByApplyNo(orderNo,orgCode);
        // 调用自动出库
        outstockOrderService.autoSaveOutstockOrder(orderNo, outstockPlans,ApplyTypeEnum.SERVICEORDER.name());
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
                    outStockList.add(outPlan);
                    continue;
                }
            }
        }
        return outStockList;
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
            // 验收完成
            bizServiceOrderDao.editStatus(serviceOrderno, BizServiceOrder.OrderStatusEnum.COMPLETED.name());
            // 修改维修单状态
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
        return StatusDto.buildDataSuccessStatusDto(bizServiceLogDao.orderLog(serviceOrderno, subjectType));
    }

}
