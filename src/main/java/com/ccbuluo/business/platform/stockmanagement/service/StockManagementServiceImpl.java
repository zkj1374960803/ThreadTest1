package com.ccbuluo.business.platform.stockmanagement.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.platform.stockmanagement.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.stockmanagement.dto.FindProductDetailDTO;
import com.ccbuluo.business.platform.stockmanagement.dto.FindStockDetailDTO;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-08-20 11:12:01
 */
@Service
public class StockManagementServiceImpl implements StockManagementService {
    @Resource
    BizStockDetailDao bizStockDetailDao;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganizationService;
    /**
     * 查看库存详情
     *
     * @param productNo   商品的编号
     * @param productType 商品类型
     * @return FindStockDetailDTO
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    @Override
    public FindStockDetailDTO findStockProductDetail(String productNo, String productType, String type) {
        FindStockDetailDTO findStockDetailDTO = bizStockDetailDao.findStockDetail(productNo, productType);
        // 根据类型查询机构的编号
        List<QueryOrgDTO> orgDTOList = getQueryOrgDTOByOrgType(type);
        // 查询正常件
        FindProductDetailDTO findProductDetailDTO = bizStockDetailDao.findProductDetail(productNo, productType, orgDTOList);
        // 查询可调拨库存的数量
        Long transferInventoryNUm = bizStockDetailDao.findTransferInventory(productNo, productType, orgDTOList, BusinessPropertyHolder.ORGCODE_TOP_SERVICECENTER);
        findProductDetailDTO.setTransferInventory(transferInventoryNUm);
        // 查询问题件
        FindProductDetailDTO findProblemStock = bizStockDetailDao.findProblemStock(productNo, productType, orgDTOList);
        // 查询损坏件
        FindProductDetailDTO findDamagedStock = bizStockDetailDao.findDamagedStock(productNo, productType, orgDTOList);
        findStockDetailDTO.setNormalPiece(findProductDetailDTO);
        findStockDetailDTO.setProblemPiece(findProblemStock);
        findStockDetailDTO.setDamagedPiece(findDamagedStock);
        return findStockDetailDTO;
    }
    /**
     *  根据机构类型查询机构的编号
     * @param type
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-20 14:56:54
     */
    private List<QueryOrgDTO> getQueryOrgDTOByOrgType(String type) {
        QueryOrgDTO orgDTO = new QueryOrgDTO();
        orgDTO.setOrgType(type);
        orgDTO.setStatus(Constants.STATUS_FLAG_ONE);
        StatusDtoThriftList<QueryOrgDTO> queryOrgDTO =
            basicUserOrganizationService.queryOrgAndWorkInfo(orgDTO);
        StatusDto<List<QueryOrgDTO>> queryOrgDTOResolve = StatusDtoThriftUtils.resolve(queryOrgDTO, QueryOrgDTO.class);
        return queryOrgDTOResolve.getData();
    }
}
