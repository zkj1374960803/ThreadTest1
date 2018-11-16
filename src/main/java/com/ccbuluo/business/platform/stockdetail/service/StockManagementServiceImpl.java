package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.ProductUnitEnum;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.stockdetail.dao.BizStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.FindBatchStockListDTO;
import com.ccbuluo.business.platform.stockdetail.dto.FindProductDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.FindStockDetailDTO;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.category.dto.RelSupplierProductDTO;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.EditBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;

    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;

    @Resource
    private BizServiceEquipmentDao bizServiceEquipmentDao;
    @Resource(name = "allocateApplyServiceImpl")
    private AllocateApplyService allocateApplyServiceImpl;

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
    public FindStockDetailDTO findStockProductDetail(String productNo, String productType, String type, String code) {
        // 根据类型查询机构的编号
        List<String> orgDTOList = allocateApplyServiceImpl.getOrgCodesByOrgType(type);
        FindStockDetailDTO findStockDetailDTO = bizStockDetailDao.findStockDetail(productNo, productType, orgDTOList, code);
        if(findStockDetailDTO == null){
            // 物料
            if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(productType)){
                return findEqupmentStockDetail(productNo);
            }else {
                // 零配件
                return findFtingStockDetailDTO(productNo);
            }
        }
        if(Constants.PRODUCT_TYPE_FITTINGS.equals(productType)){
            StatusDtoThriftBean<SaveBasicCarpartsProductDTO> carpartsProductdetail = carpartsProductService.findCarpartsProductdetail(productNo);
            StatusDto<SaveBasicCarpartsProductDTO> resolve = StatusDtoThriftUtils.resolve(carpartsProductdetail, SaveBasicCarpartsProductDTO.class);
            SaveBasicCarpartsProductDTO carpartsProductDTO = resolve.getData();
            if(carpartsProductDTO != null){
                findStockDetailDTO.setProductName(carpartsProductDTO.getCarpartsName());
                findStockDetailDTO.setCarpartsMarkno(carpartsProductDTO.getCarpartsMarkno());
                findStockDetailDTO.setUnit(carpartsProductDTO.getCarpartsUnit());
                findStockDetailDTO.setCarpartsImage(carpartsProductDTO.getCarpartsImage());
            }
        }
        // 查询正常件
        FindProductDetailDTO findProductDetailDTO = bizStockDetailDao.findProductDetail(productNo, productType, orgDTOList, code);
        // 查询问题件
        FindProductDetailDTO findProblemStock = bizStockDetailDao.findProblemStock(productNo, productType, orgDTOList, code);
        // 查询损坏件
        FindProductDetailDTO findDamagedStock = bizStockDetailDao.findDamagedStock(productNo, productType, orgDTOList, code);
        findStockDetailDTO.setNormalPiece(findProductDetailDTO);
        findStockDetailDTO.setProblemPiece(findProblemStock);
        findStockDetailDTO.setDamagedPiece(findDamagedStock);
        return findStockDetailDTO;
    }
    /**
     * 查询零配件的详情
     * @param productNo 商品的编号
     * @return FindStockDetailDTO
     * @author zhangkangjian
     * @date 2018-09-04 10:02:30
     */
    private FindStockDetailDTO findFtingStockDetailDTO(String productNo) {
        // 零备件
        StatusDtoThriftBean<SaveBasicCarpartsProductDTO> carpartsProductdetail = carpartsProductService.findCarpartsProductdetail(productNo);
        StatusDto<SaveBasicCarpartsProductDTO> resolve = StatusDtoThriftUtils.resolve(carpartsProductdetail, SaveBasicCarpartsProductDTO.class);
        SaveBasicCarpartsProductDTO data = resolve.getData();
        if(data != null){
            FindStockDetailDTO findStockDetailDTOs = new FindStockDetailDTO();
            findStockDetailDTOs.setProductNo(data.getCarpartsCode());
            findStockDetailDTOs.setProductName(data.getCarpartsName());
            findStockDetailDTOs.setCarpartsImage(data.getCarpartsImage());
            findStockDetailDTOs.setCarpartsMarkno(data.getCarpartsMarkno());
            findStockDetailDTOs.setUnit(data.getCarpartsUnit());
            findStockDetailDTOs.setDamagedPiece(new FindProductDetailDTO());
            findStockDetailDTOs.setNormalPiece(new FindProductDetailDTO());
            findStockDetailDTOs.setProblemPiece(new FindProductDetailDTO());
            return findStockDetailDTOs;
        }
        return new FindStockDetailDTO();
    }

    /**
     *  查询物料的详情
     * @param productNo 商品的编号
     * @return FindStockDetailDTO
     * @author zhangkangjian
     * @date 2018-09-04 10:03:11
     */
    private FindStockDetailDTO findEqupmentStockDetail(String productNo) {
        FindStockListDTO equpmentDetail = bizServiceEquipmentDao.findEqupmentDetail(productNo);
        FindStockDetailDTO findStockDetailDTOs = new FindStockDetailDTO();
        findStockDetailDTOs.setProductNo(equpmentDetail.getProductNo());
        findStockDetailDTOs.setProductName(equpmentDetail.getProductName());
        String unit = equpmentDetail.getUnit();
        if(StringUtils.isNotBlank(unit)){
            findStockDetailDTOs.setUnit(ProductUnitEnum.valueOf(unit).getLabel());
        }else {
            findStockDetailDTOs.setUnit(unit);
        }
        findStockDetailDTOs.setProductCategoryname(equpmentDetail.getProductCategoryname());
        findStockDetailDTOs.setDamagedPiece(new FindProductDetailDTO());
        findStockDetailDTOs.setNormalPiece(new FindProductDetailDTO());
        findStockDetailDTOs.setProblemPiece(new FindProductDetailDTO());
        return findStockDetailDTOs;
    }

    /**
     * 查看库存详情（批次库存列表查询）
     *
     * @param findStockListDTO
     * @return Page<FindBatchStockListDTO>
     * @author zhangkangjian
     * @date 2018-08-21 10:53:48
     */
    @Override
    public Page<FindBatchStockListDTO> findBatchStockList(FindStockListDTO findStockListDTO) {
        List<String> orgDTOList = allocateApplyServiceImpl.getOrgCodesByOrgType(findStockListDTO.getType());
        Page<FindBatchStockListDTO> page = bizStockDetailDao.findBatchStockList(findStockListDTO, orgDTOList);
        return page;
    }

    /**
     *  根据机构类型查询机构的编号
     * @param type
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-20 14:56:54
     */
    private List<String> getQueryOrgDTOByOrgType(String type) {
        QueryOrgDTO orgDTO = new QueryOrgDTO();
        orgDTO.setOrgType(type);
        orgDTO.setStatus(Constants.STATUS_FLAG_ONE);
        StatusDtoThriftList<QueryOrgDTO> queryOrgDTO =
            basicUserOrganizationService.queryOrgAndWorkInfo(orgDTO);
        StatusDto<List<QueryOrgDTO>> queryOrgDTOResolve = StatusDtoThriftUtils.resolve(queryOrgDTO, QueryOrgDTO.class);
        List<QueryOrgDTO> data = queryOrgDTOResolve.getData();
        List<String> orgcode = data.stream().map(QueryOrgDTO::getOrgCode).collect(Collectors.toList());
        return orgcode;
    }
}
