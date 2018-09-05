package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.BizCarPosition;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.carmanage.dto.CarcoreInfoDTO;
import com.ccbuluo.business.platform.carmanage.service.BasicCarcoreInfoService;
import com.ccbuluo.business.platform.carposition.dao.BizCarPositionDao;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.dto.DetailServiceOrderDTO;
import com.ccbuluo.business.platform.order.dto.EditServiceOrderDTO;
import com.ccbuluo.business.platform.order.dto.SaveServiceOrderDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.UserInfoDTO;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private BizCarPositionDao bizCarPositionDao;


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
    public StatusDto serviceCenterList(String province, String city, String area, String orgType, String keyword, Integer offset, Integer pagesize) {
        return null;
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
        bizServiceOrder.setReportOrgtype(loggedUser.getOrganization());
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
