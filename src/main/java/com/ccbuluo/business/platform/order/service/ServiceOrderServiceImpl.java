package com.ccbuluo.business.platform.order.service;

import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.BizServiceOrder;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.order.dto.SaveServiceOrderDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.BusinessUser;
import com.ccbuluo.http.StatusDto;

import javax.annotation.Resource;

/**
 * 描述 服务订单service
 * @author baoweiding
 * @date 2018-09-03 17:35:19
 * @version V1.0.0
 */
public class ServiceOrderServiceImpl implements ServiceOrderService {
    @Resource
    private BizServiceOrderDao bizServiceOrderDao;
    @Resource
    private UserHolder userHolder;
    @Resource
    private GenerateDocCodeService generateDocCodeService;

    /**
     * 描述 新增服务订单
     * @param serviceOrderDTO
     * @return com.ccbuluo.http.StatusDto<java.lang.String>
     * @author baoweiding
     * @date 2018-09-03 18:54:01
     */
    @Override
    public StatusDto<String> saveOrder(SaveServiceOrderDTO serviceOrderDTO) {
        BusinessUser loggedUser = userHolder.getLoggedUser();

        return null;
    }

    private BizServiceOrder buildBizServiceOrder(SaveServiceOrderDTO serviceOrderDTO,BusinessUser user) {
        BizServiceOrder bizServiceOrder = new BizServiceOrder();
        // 生成订单号
        StatusDto<String> stringStatusDto = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.DD);
        bizServiceOrder.setServiceOrderno(stringStatusDto.getData());
        bizServiceOrder.setCarNo(serviceOrderDTO.getCarNo());
        bizServiceOrder.setCarVin(serviceOrderDTO.getCarVin());
        bizServiceOrder.setServiceType(serviceOrderDTO.getServiceType());
        if (null != user.getOrganization()) {
            bizServiceOrder.setReportOrgno(user.getOrganization().getOrgCode());
        }
        bizServiceOrder.setReportOrgtype(serviceOrderDTO.getReportOrgtype());
        bizServiceOrder.setReportTime(serviceOrderDTO.getReportTime());
        bizServiceOrder.setCustomerName(serviceOrderDTO.getCustomerName());
        bizServiceOrder.setCustomerPhone(serviceOrderDTO.getCustomerPhone());
        bizServiceOrder.setReserveContacter(serviceOrderDTO.getReserveContacter());
        bizServiceOrder.setReservePhone(serviceOrderDTO.getReservePhone());
       // if (serviceOrderDTO.getReportOrgtype().equals())

        return bizServiceOrder;
    }
}
