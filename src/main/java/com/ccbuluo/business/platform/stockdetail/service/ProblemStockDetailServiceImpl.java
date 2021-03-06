package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.entity.BizOutstockplanDetail;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.allocateapply.service.AllocateApplyService;
import com.ccbuluo.business.platform.allocateapply.service.ProblemAllocateApplyImpl;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.inputstockplan.service.InputStockPlanServiceImpl;
import com.ccbuluo.business.platform.order.service.fifohandle.BarterStockInOutCallBack;
import com.ccbuluo.business.platform.outstockplan.dao.BizOutstockplanDetailDao;
import com.ccbuluo.business.platform.outstockplan.service.OutStockPlanServiceImpl;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.ProblemStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockDetailDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.weakref.jmx.internal.guava.collect.Lists.newArrayList;

/**
 * 功能描述（1）
 *
 * @author weijb
 * @version v1.0.0
 * @date 创建时间（2）
 */
@Service
public class ProblemStockDetailServiceImpl implements ProblemStockDetailService {

    @Resource
    private ProblemStockDetailDao problemStockDetailDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private BizServiceEquipmentDao bizServiceEquipmentDao;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService orgService;
    @Autowired
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Autowired
    private InputStockPlanServiceImpl inputStockPlanService;
    @Autowired
    private OutStockPlanServiceImpl outStockPlanService;
    @Autowired
    private AllocateApplyService allocateApplyService;
    @Autowired
    private BarterStockInOutCallBack barterStockInOutCallBack;
    @Autowired
    private ProblemAllocateApplyImpl problemAllocateApply;
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;

    /**
     * 带条件分页查询所有零配件的问题库存

     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<FindStockListDTO> queryStockBizStockDetailDTOList(FindStockListDTO findStockListDTO){
        List<BasicCarpartsProductDTO> carpartsProductDTOList;
        if(Constants.PRODUCT_TYPE_EQUIPMENT.equals(findStockListDTO.getProductType())){
            // 查询类型下所有的code
            carpartsProductDTOList  = bizAllocateApplyDao.findEquipmentCode(findStockListDTO.getEquiptypeId());
        }else {
            // 查询零配件
            carpartsProductDTOList = carpartsProductService.queryCarpartsProductListByCategoryCode(findStockListDTO.getKeyword());
        }
        List<String> productCode = carpartsProductDTOList.stream().map(BasicCarpartsProductDTO::getCarpartsCode).collect(Collectors.toList());
        if(productCode == null || productCode.size() == 0){
            return new Page<FindStockListDTO>(findStockListDTO.getOffset(), findStockListDTO.getPageSize());
        }
        String orgNo = findStockListDTO.getOrgNo();
        List<String> orgCodeList = orgNo == null ? null : List.of(orgNo);
        Page<FindStockListDTO> stockPage = bizAllocateApplyDao.findProblemStockList(findStockListDTO, productCode, orgCodeList);
        buildStockPage(findStockListDTO, carpartsProductDTOList, stockPage);
        return stockPage;
    }

    /**
     * 构建库存（填充零配件信息）
     * @param findStockListDTO 配件基础信息条件
     * @param carpartsProductDTOList 配件信息
     * @param stockPage 库存分页信息
     * @author zhangkangjian
     * @date 2018-11-15 16:31:07
     */
    public void buildStockPage(FindStockListDTO findStockListDTO, List<BasicCarpartsProductDTO> carpartsProductDTOList, Page<FindStockListDTO> stockPage) {
        if(Constants.PRODUCT_TYPE_FITTINGS.equals(findStockListDTO.getProductType())) {
            List<FindStockListDTO> rows = stockPage.getRows();
            if (rows != null) {
                Map<String, BasicCarpartsProductDTO> productMap = carpartsProductDTOList.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, a -> a, (k1, k2) -> k1));
                rows.forEach(item -> {
                    String productNo = item.getProductNo();
                    BasicCarpartsProductDTO basicCarparts = productMap.get(productNo);
                    item.setCarpartsImage(basicCarparts.getCarpartsImage());
                    item.setCarpartsMarkno(basicCarparts.getCarpartsMarkno());
                    item.setProductName(basicCarparts.getCarpartsName());
                    item.setUnit(basicCarparts.getUnitName());
                });
            }
        }
    }

    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param type 物料或是零配件
     * @param productCategory 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> querySelfStockBizStockDetailDTOList(boolean category, String type, String productCategory, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize){
        List<String> productNames = null;
        if(null != productList && productList.size() > 0){
            productNames = productList.stream().map(BasicCarpartsProductDTO::getCarpartsName).collect(Collectors.toList());
        }
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        if(StringUtils.isNotBlank(productCategory)){
            List<DetailBizServiceEquipmentDTO> equis = bizServiceEquipmentDao.queryEqupmentByEquiptype(Long.valueOf(productCategory));
            productNames = equis.stream().map(DetailBizServiceEquipmentDTO::getEquipName).collect(Collectors.toList());;
        }
        // TODO: 2018/11/15
//        return problemStockDetailDao.queryStockBizStockDetailDTOList(category, type,orgCode, productNames, keyword,offset, pageSize);
        return null;
    }

    /**
     * 根据物料code查询某个物料的问题件库存
     *  @param type 物料或是零配件
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> getProdectStockBizStockDetailByCode(String type, String productNo, Integer offset, Integer pageSize){
        return problemStockDetailDao.getProdectStockBizStockDetailByCode(type, "", productNo, offset, pageSize);
    }

    /**
     * 根据物料code查询某个物料在当前登录机构的问题件库存
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> getSelfProdectStockBizStockDetailByCode(String productNo, Integer offset, Integer pageSize){
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        return problemStockDetailDao.getProdectStockBizStockDetailByCode(null,orgCode, productNo, offset, pageSize);
    }

    /**
     * 查询问题库存详情
     * @param id 库存批次id
     * @return StatusDto
     * @author weijb
     * @date 2018-08-23 16:02:58
     */
    @Override
    public ProblemStockBizStockDetailDTO getProblemStockDetail(Long id){
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        ProblemStockBizStockDetailDTO psd = problemStockDetailDao.getProblemStockDetail(id);
        // 查询本机构下面，本条记录所对应的商品的所有问题库存列表
        psd.setProblemDetailList(problemStockDetailDao.queryProblemStockBizStockList(orgCode, psd.getProductNo()));
        // 对psd过滤计算下，问题件的个数，然后赋值
        computerProblemProductCount(psd);
        return psd;
    }

    private void computerProblemProductCount(ProblemStockBizStockDetailDTO psd){
        if(null == psd || null == psd.getProblemDetailList() || psd.getProblemDetailList().size() == 0){
            return;
        }
        Long count = 0L;
        for(StockDetailDTO stock : psd.getProblemDetailList()){
            count += stock.getProblemStock();
        }
        psd.setProblemStock(count);
    }

    /**
     * 查询问题库存详情(平台用)
     * @param id 库存批次id
     * @return StatusDto
     * @author weijb
     * @date 2018-08-23 16:02:58
     */
    @Override
    public ProblemStockBizStockDetailDTO getProblemStockDetailById(Long id){
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        ProblemStockBizStockDetailDTO psd = problemStockDetailDao.getProblemStockDetail(id);
        // 查询本机构下面，本条记录所对应的商品的所有问题库存列表
        psd.setProblemDetailList(problemStockDetailDao.queryProblemStockBizStockList(orgCode, psd.getProductNo()));
        computerProblemProductCount(psd);
        StatusDtoThriftBean<SaveBasicCarpartsProductDTO> carpartsProductdetail = carpartsProductService.findCarpartsProductdetail(psd.getProductNo());
        StatusDto<SaveBasicCarpartsProductDTO> resolve = StatusDtoThriftUtils.resolve(carpartsProductdetail, SaveBasicCarpartsProductDTO.class);
        SaveBasicCarpartsProductDTO data = resolve.getData();
        if(data != null){
            psd.setCarpartsMarkno(data.getCarpartsMarkno());
            psd.setCarpartsImage(data.getCarpartsImage());
            psd.setProductName(data.getCarpartsName());
        }
        return psd;
    }

    /**
     * 根据商品类型和商品编号查询详情
     * @param procudtType 商品类型
     * @param productNo 商品编号
     * @return 问题件详情
     * @author liuduo
     * @date 2018-10-29 14:05:14
     */
    @Override
    public ProblemStockBizStockDetailDTO findByProductno(String procudtType, String productNo) {
        // 根据商品编号查询基本信息
        ProblemStockBizStockDetailDTO problemStockBizStockDetailDTO = problemStockDetailDao.getByProductNo(productNo);
        StatusDtoThriftBean<SaveBasicCarpartsProductDTO> carpartsProductdetail =
            carpartsProductService.findCarpartsProductdetail(problemStockBizStockDetailDTO.getProductNo());
        StatusDto<SaveBasicCarpartsProductDTO> resolve = StatusDtoThriftUtils.resolve(carpartsProductdetail, SaveBasicCarpartsProductDTO.class);
        SaveBasicCarpartsProductDTO data = resolve.getData();
        if(data != null){
            problemStockBizStockDetailDTO.setCarpartsImage(data.getCarpartsImage());
            problemStockBizStockDetailDTO.setCarpartsMarkno(data.getCarpartsMarkno());
            problemStockBizStockDetailDTO.setProductName(data.getCarpartsName());
        }
        //　查询该商品所有的库存
        List<StockDetailDTO> stockDetailDTOS = problemStockDetailDao.queryProblemStockByProduct(procudtType, productNo);
        // 取出问题件库存大于0的商品
        List<StockDetailDTO> collect = stockDetailDTOS.stream().filter(item -> item.getProblemStock() > 0).collect(Collectors.toList());
        // 取出所有机构
        List<String> orgtNos = collect.stream().map(StockDetailDTO::getOrgNo).collect(Collectors.toList());
        // 根据机构编号查询机构名字
        Map<String, BasicUserOrganization> stringBasicUserOrganizationMap = orgService.queryOrganizationByOrgCodes(orgtNos);
        for (StockDetailDTO stockDetailDTO : collect) {
            BasicUserOrganization basicUserOrganization = stringBasicUserOrganizationMap.get(stockDetailDTO.getOrgNo());
            stockDetailDTO.setOrgName(basicUserOrganization.getOrgName());
            stockDetailDTO.setOrgType(basicUserOrganization.getOrgType());
        }
        // 按照机构和供应商分组
        Map<String, List<StockDetailDTO>> collect1 = collect.stream().collect(Collectors.groupingBy(StockDetailDTO::getOrgNoAndSupplierNo));
        List<StockDetailDTO> stockDetailDTOS1 = Lists.newArrayList();
        for (Map.Entry<String, List<StockDetailDTO>> entry : collect1.entrySet()) {
            List<StockDetailDTO> value = entry.getValue();
            StockDetailDTO stockDetailDTO1 = value.get(0);
            long count = value.stream().map(StockDetailDTO::getProblemStock).reduce((sum, item) -> sum + item).get();
            StockDetailDTO stockDetailDTO = new StockDetailDTO();
            stockDetailDTO.setOrgType(stockDetailDTO1.getOrgType());
            stockDetailDTO.setOrgName(stockDetailDTO1.getOrgName());
            stockDetailDTO.setProblemStock(count);
            stockDetailDTO.setSupplierName(stockDetailDTO1.getSupplierName());
            stockDetailDTO.setProductUnit(stockDetailDTO1.getProductUnit());
            stockDetailDTO.setProductNo(stockDetailDTO1.getProductNo());
            stockDetailDTOS1.add(stockDetailDTO);
        }
        problemStockBizStockDetailDTO.setProblemDetailList(stockDetailDTOS1);
        return problemStockBizStockDetailDTO;
    }

    /**
     * 根据申请单号修改退换类型
     * @param applyNo 申请单号
     * @param recedeType 退换类型
     * @author liuduo
     * @date 2018-10-29 16:59:30
     */
    @Override
    public StatusDto problemHandle(String applyNo, String recedeType) {
        // 查询申请单的申请机构, 并判断修改的是平台还是机构
        BizAllocateApply apply = bizAllocateApplyDao.getByNo(applyNo);
        // 1、判断要修改为的类型是什么
        // 如果要修改为退款，说明目前是换货
        if (BizAllocateApply.AllocateApplyTypeEnum.PLATFORMREFUND.name().equals(recedeType)
            || BizAllocateApply.AllocateApplyTypeEnum.REFUND.name().equals(recedeType) ) {
            //　更改申请单申请类型
            if (apply.getApplyorgNo().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
                bizAllocateApplyDao.updateApplyType(applyNo, BizAllocateApply.AllocateApplyTypeEnum.PLATFORMREFUND.name());
            } else {
                bizAllocateApplyDao.updateApplyType(applyNo, BizAllocateApply.AllocateApplyTypeEnum.REFUND.name());
            }
            // 删除所有的出入库计划
//            inputStockPlanService.deleteInStockPlan(applyNo);
//            outStockPlanService.deleteOutStockPlan(applyNo);
            // 修改申请单为 -等待退款,
           allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.WAITINGREFUND.name());
           return StatusDto.buildSuccessStatusDto();
        }
        //　更改申请单申请类型
        if (apply.getApplyorgNo().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
            bizAllocateApplyDao.updateApplyType(applyNo, BizAllocateApply.AllocateApplyTypeEnum.PLATFORMBARTER.name());
        } else {
            bizAllocateApplyDao.updateApplyType(applyNo, BizAllocateApply.AllocateApplyTypeEnum.BARTER.name());
        }

        // 要修改为换货，说明目前是退款
        // 如果修改的是平台，则直接修改申请单为 -等待入库
        if (apply.getApplyorgNo().equals(BusinessPropertyHolder.ORGCODE_AFTERSALE_PLATFORM)) {
            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.REPLACEWAITIN.name());
        } else {
            //　不是平台，修改申请单类型为  等待出库
            allocateApplyService.updateApplyOrderStatus(applyNo, BizAllocateApply.ReturnApplyStatusEnum.PLATFORMOUTBOUND.name());
        }
        return StatusDto.buildSuccessStatusDto();
    }
}
