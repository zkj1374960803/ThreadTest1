package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelManageDao;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelManage;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.business.platform.carparts.dao.CarpartsProductPriceDao;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.OSSClientProperties;
import com.ccbuluo.core.entity.UploadFileInfo;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.service.UploadService;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.*;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 零配件实现类
 * @author zhangkangjian
 * @date 2018-09-06 16:17:58
 */
@Service
public class CarpartsProductPriceServiceImpl implements CarpartsProductPriceService {

    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;
    @Resource
    private UserHolder userHolder;
    @Resource
    private BasicCarmodelManageService basicCarmodelManageService;
    @Resource
    private CarpartsProductPriceDao carpartsProductPriceDao;
    @Resource
    private BizAllocateApplyDao bizAllocateApplyDao;
    @Resource
    private UploadService uploadService;
    @Resource
    private BasicCarmodelManageDao basicCarmodelManageDao;
    @Resource(name = "carpartsProductPriceServiceImpl")
    @Lazy
    private CarpartsProductPriceService carpartsProductServiceImpl;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private BizServiceSupplierDao bizServiceSupplierDao;
    @Resource
    private BizServiceOrderDao bizServiceOrderDao;
    @Resource
    private BizServiceEquipmentDao bizServiceEquipmentDao;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganization;
    @Autowired
    private OSSClientProperties ossClientProperties;


    /**
     * 查询零配件的信息和价格
     * @param queryCarpartsProductDTO 查询零配件条件
     * @return StatusDto<Page < BasicCarpartsProductDTO>>
     * @author zhangkangjian
     * @date 2018-09-06 16:25:05
     */
    @Override
    public StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductPriceList(QueryCarpartsProductDTO queryCarpartsProductDTO) {
        // 查询已有价格的零配件
        List<RelProductPrice> relProductPrice =  carpartsProductPriceDao.queryCarpartsProductList(Constants.PRODUCT_TYPE_FITTINGS);
        Optional.ofNullable(relProductPrice).ifPresent(a ->{
            List<String> productNoList = a.stream().map(RelProductPrice::getProductNo).collect(Collectors.toList());
            queryCarpartsProductDTO.setCarpartsCodeList(productNoList);
        });
        return getPageStatusDto(queryCarpartsProductDTO, relProductPrice);
    }

    /**
     * 设置价格
     * @param relProductPrice 价格信息
     * @author zhangkangjian
     * @date 2018-09-06 19:27:11
     */
    @Override
    public void saveProductPrice(List<RelProductPrice> relProductPrice) {
        // 根据类型查询机构编号
        QueryOrgDTO queryOrgDTO = new QueryOrgDTO();
        List<String> name = List.of(OrganizationTypeEnum.CUSTMANAGER.name(), OrganizationTypeEnum.SERVICECENTER.name());
        queryOrgDTO.setOrgTypeList(name);
        StatusDtoThriftList<QueryOrgDTO> queryOrgDTOStatusDtoThriftList = basicUserOrganization.queryOrgAndWorkInfo(queryOrgDTO);
        StatusDto<List<QueryOrgDTO>> resolve = StatusDtoThriftUtils.resolve(queryOrgDTOStatusDtoThriftList, QueryOrgDTO.class);
        List<String> applyStatusList = List.of(BizAllocateApply.ApplyStatusEnum.PENDING.name(), BizAllocateApply.ApplyStatusEnum.WAITINGPAYMENT.name());
        List<String> custInStockNoList =  resolve.getData().stream().filter(b -> OrganizationTypeEnum.CUSTMANAGER.name().equals(b.getOrgType())).map(QueryOrgDTO::getOrgCode).collect(Collectors.toList());
        List<String> serInStockNoList =  resolve.getData().stream().filter(b -> OrganizationTypeEnum.SERVICECENTER.name().equals(b.getOrgType())).map(QueryOrgDTO::getOrgCode).collect(Collectors.toList());
        List<String> custApplyNoList = bizAllocateapplyDetailDao.queryApplyNo(applyStatusList, BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.name(), custInStockNoList);
        List<String> serApplyNoList = bizAllocateapplyDetailDao.queryApplyNo(applyStatusList, BizAllocateApply.AllocateApplyTypeEnum.SAMELEVEL.name(), serInStockNoList);
        // 查询商品最新一条的价格，并更新结束时间
        relProductPrice.forEach(item ->{
            carpartsProductPriceDao.updateProductEndTime(item);
            String loggedUserId = userHolder.getLoggedUserId();
            item.setOperator(loggedUserId);
            item.setCreator(loggedUserId);
            item.setStartTime(new Date());
            carpartsProductPriceDao.save(item);
            String priceLevelName = RelProductPrice.PriceLevelEnum.map.get(item.getPriceLevel()).name();
            if(OrganizationTypeEnum.CUSTMANAGER.name().equals(priceLevelName)){
                updateApplySellPrice(item.getProductNo(), item.getSuggestedPrice(), custApplyNoList);
            }else {
                updateApplySellPrice(item.getProductNo(), item.getSuggestedPrice(), serApplyNoList);
            }
        });
    }

    /**
     * 更新申请单，出入库计划单价格
     * @param productNo 申请单编号
     * @param sellPrice 销售的价格
     * @author zhangkangjian
     * @date 2018-11-19 17:21:52
     */
    private void updateApplySellPrice(String productNo, Double sellPrice, List<String> applyNoList) {
        Optional.ofNullable(applyNoList).ifPresent(a ->{
            // 查询等待付款前的申请单，修改价格
            if(StringUtils.isNotBlank(productNo) && sellPrice != null && applyNoList.size() > 0){

                // 更新申请单详单的价格
                bizAllocateapplyDetailDao.updateAllocateapplyDetail(applyNoList, productNo, sellPrice);
                // 更新入库计划
                bizAllocateapplyDetailDao.updateInstockorderDetail(applyNoList, productNo, sellPrice);
                // 更新出库计划
                bizAllocateapplyDetailDao.updateOutstockorderDetail(applyNoList, productNo, sellPrice);
            }
        });
    }

    /**
     * 查询维修单的零配件列表
     * @param queryCarpartsProductDTO 查询条件
     * @return StatusDto<Page < BasicCarpartsProductDTO>> 查询分页信息
     * @author zhangkangjian
     * @date 2018-09-28 10:31:47
     */
    @Override
    public StatusDto<Page<BasicCarpartsProductDTO>> queryServiceProductList(QueryCarpartsProductDTO queryCarpartsProductDTO) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 查询当前机构库存的商品
        Map<String, Object> map = bizAllocateApplyDao.queryStockQuantity(orgCode, null);
        List<RelProductPrice> relProductPrice =  carpartsProductPriceDao.queryCarpartsProductList(Constants.PRODUCT_TYPE_FITTINGS);
        Optional.ofNullable(relProductPrice).ifPresent(a ->{
            ArrayList<String> strList = Lists.newArrayList();
            a.forEach(item -> {
                String productNo = item.getProductNo();
                Object obj = map.get(productNo);
                if(obj != null){
                    BigDecimal bd = (BigDecimal) obj;
                    if(bd.longValue() > 0){
                        strList.add(productNo);
                    }
                }
            });
            queryCarpartsProductDTO.setCarpartsCodeList(strList);
        });
        return getPageStatusDto(queryCarpartsProductDTO, relProductPrice);
    }

    /**
     * 查询当前机构下所有的零配件（不限制数量，不限制是否设置价格）
     * @param queryCarpartsProductDTO 查询的条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 分页的零配件列表
     * @author zhangkangjian
     * @date 2018-11-05 15:40:42
     */
    @Override
    public StatusDto<Page<BasicCarpartsProductDTO>> queryAllServiceProductList(QueryCarpartsProductDTO queryCarpartsProductDTO) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        List<RelProductPrice> relProductPrices = carpartsProductPriceDao.queryCarpartsProductList(Constants.PRODUCT_TYPE_FITTINGS);
        // 查询当前机构库存的商品
        List<RelProductPrice> newRelProductPrices = Optional.ofNullable(relProductPrices).orElse(new ArrayList<>());
        Map<String, List<RelProductPrice>> relProductPriceMap = newRelProductPrices.stream().collect(Collectors.groupingBy(RelProductPrice::getProductNo));
        Map<String, Object> mapProductNo = bizAllocateApplyDao.queryStockQuantity(orgCode, null);
        Set<String> keyProductNo = mapProductNo.keySet();
        List<String> productNoList = new ArrayList<>(keyProductNo);
        queryCarpartsProductDTO.setCarpartsCodeList(productNoList);
        List<RelProductPrice> relProductPriceList = Lists.newArrayList();
        productNoList.forEach(b ->{
            List<RelProductPrice> getRelProduct = relProductPriceMap.get(b);
            if(getRelProduct != null){
                relProductPriceList.addAll(getRelProduct);
            }else {
                RelProductPrice relProductPrice = new RelProductPrice();
                relProductPrice.setProductNo(b);
                relProductPriceList.add(relProductPrice);
            }
        });

        return getPageStatusDto(queryCarpartsProductDTO, relProductPriceList);
    }

    /**
     * 查询零配件的详情
     * @param carpartsCode 零配件code
     * @return StatusDtoThriftBean<EditBasicCarpartsProductDTO>
     * @author zhangkangjian
     * @date 2018-11-05 16:56:30
     */
    @Override
    public StatusDto<SaveBasicCarpartsProductDTO> findCarpartsProductdetail(String carpartsCode) {
        StatusDtoThriftBean<SaveBasicCarpartsProductDTO> carpartsProductdetail = carpartsProductService.findCarpartsProductdetail(carpartsCode);
        StatusDto<SaveBasicCarpartsProductDTO> carpartsProductStatusDto = StatusDtoThriftUtils.resolve(carpartsProductdetail, SaveBasicCarpartsProductDTO.class);
        SaveBasicCarpartsProductDTO carpartsProductDTO = carpartsProductStatusDto.getData();
        String fitCarmodel = carpartsProductDTO.getFitCarmodel();
        // 查询车型的名称
        if(StringUtils.isBlank(fitCarmodel)){
            return carpartsProductStatusDto;
        }
        List<String> fitCarmodelStrIds = Arrays.asList(fitCarmodel.split(Constants.COMMA));
        List<Long> fitCarmodelLongIds = fitCarmodelStrIds.stream().filter(StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toList());
        if(fitCarmodelLongIds != null && fitCarmodelLongIds.size() > 0){
            List<CarmodelManage> carmodelManages = basicCarmodelManageDao.queryPartModel(fitCarmodelLongIds);
            String fitCarmodelName = StringUtils.join(carmodelManages.stream().map(CarmodelManage::getCarmodelName).toArray(), Constants.COMMA);
            carpartsProductDTO.setCarmodelName(fitCarmodelName);
        }
        return carpartsProductStatusDto;
    }

    /**
     * 保存零配件
     *
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-11-12 14:37:33
     */
    @Override
    public StatusDto<String> saveCarpartsProduct(SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO) {
        // 生成编码
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FP);
        // 获取code失败
        if(!Constants.SUCCESS_CODE.equals(stringStatusDto.getCode())){
            return stringStatusDto;
        }
        saveBasicCarpartsProductDTO.setCarpartsCode(stringStatusDto.getData());
        saveBasicCarpartsProductDTO.setCreator(userHolder.getLoggedUserId());
        saveBasicCarpartsProductDTO.setOperator(userHolder.getLoggedUserId());
        if(StringUtils.isNotBlank(saveBasicCarpartsProductDTO.getCarpartsName())){
            String pinYinHeadChar = PinyinTool.getPinYinHeadChar(saveBasicCarpartsProductDTO.getCarpartsName());
            saveBasicCarpartsProductDTO.setCarpartsPinyinname(pinYinHeadChar);
        }
        return carpartsProductService.saveCarpartsProduct(saveBasicCarpartsProductDTO);
    }

    /**
     * 编辑零部件
     *
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return StatusDto<String>
     * @author weijb
     * @date 2018-07-02 18:52:40
     */
    @Override
    public StatusDto<String> editCarpartsProduct(SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO) {
        if(StringUtils.isNotBlank(saveBasicCarpartsProductDTO.getCarpartsName())){
            String pinYinHeadChar = PinyinTool.getPinYinHeadChar(saveBasicCarpartsProductDTO.getCarpartsName());
            saveBasicCarpartsProductDTO.setCarpartsPinyinname(pinYinHeadChar);
        }
        return carpartsProductService.editCarpartsProduct(saveBasicCarpartsProductDTO);
    }

    /**
     * 零配件列表分页查询
     *
     * @param keyword  零部件名称
     * @param offset   起始数
     * @param pageSize 每页数量
     * @author zhangkangjian
     * @date 2018-11-12 19:52:44
     */
    @Override
    public StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductList(String keyword, Integer offset, Integer pageSize) {
        StatusDto<Page<BasicCarpartsProductDTO>> basicCarpartsProductDTO = StatusDtoThriftUtils.resolve(carpartsProductService.queryCarpartsProductList(keyword, offset, pageSize),BasicCarpartsProductDTO.class);
        // 把车型id转换成车型名字
        if(basicCarpartsProductDTO != null) {
            Optional.ofNullable(basicCarpartsProductDTO.getData().getRows()).ifPresent(a ->{
                a.forEach(item->{
                    String fitCarmodel = item.getFitCarmodel();
                    if(StringUtils.isBlank(fitCarmodel)){
                        return;
                    }
                    List<String> fitCarmodelStrIds = Arrays.asList(fitCarmodel.split(Constants.COMMA));
                    List<Long> fitCarmodelLongIds = fitCarmodelStrIds.stream().filter(StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toList());
                    if(fitCarmodelLongIds != null && fitCarmodelLongIds.size() > 0){
                        List<CarmodelManage> carmodelManages = basicCarmodelManageDao.queryPartModel(fitCarmodelLongIds);
                        String fitCarmodelName = StringUtils.join(carmodelManages.stream().map(CarmodelManage::getCarmodelName).toArray(), Constants.COMMA);
                        item.setCarmodelName(fitCarmodelName);
                    }
                });
            });
        }
        return basicCarpartsProductDTO;
    }

    /**
     * 查询零配件的信息
     * @param keyword 查询的条件
     * @return StatusDto<List<BasicCarpartsProductDTO>
     * @author zhangkangjian
     * @date 2018-11-13 10:07:23
     */
    @Override
    public StatusDto<List<BasicCarpartsProductDTO>> queryCarparts(String keyword, RelProductPrice.PriceLevelEnum priceLevelEnum) {
        StatusDto<Page<BasicCarpartsProductDTO>> list = StatusDtoThriftUtils.resolve(carpartsProductService.queryCarpartsProductList(keyword, 0, Integer.MAX_VALUE),BasicCarpartsProductDTO.class);
        Page<BasicCarpartsProductDTO> data = list.getData();
        List<BasicCarpartsProductDTO> basicCarpartsProductDTOS = Lists.newArrayList();
        if(data != null){
            basicCarpartsProductDTOS = data.getRows();
        }
        // 查找所有有价格的
        if(priceLevelEnum.getPriceLevel() == 0){
            return StatusDto.buildDataSuccessStatusDto(basicCarpartsProductDTOS);
        }
        Map<String, Object> suggestedPriceMap = bizServiceEquipmentDao.findSuggestedPrice(null , priceLevelEnum.getPriceLevel());
        List<BasicCarpartsProductDTO> collect = basicCarpartsProductDTOS.stream().filter(a -> {
            String carpartsCode = a.getCarpartsCode();
            Object obj = suggestedPriceMap.get(carpartsCode);
            return !(obj == null);
        }).collect(Collectors.toList());
        return StatusDto.buildDataSuccessStatusDto(collect);

    }

    /**
     * 删除零配件
     * @param productNo 零配件code
     * @author zhangkangjian
     * @date 2018-11-14 15:42:34
     */
    @Override
    public StatusDto<String> deleteCarpartsProduct(String productNo) {

        // 查询维修单详单
        Boolean resultRelOrder = bizServiceOrderDao.checkProductRelOrder(productNo);
        if(resultRelOrder){
            return StatusDto.buildFailureStatusDto("该零配件已与维修单建立关联关系！");
        }

        Boolean resultRelStock =  bizAllocateApplyDao.checkProductRelStock(productNo);
        // 查询库存
        if(resultRelStock){
            return StatusDto.buildFailureStatusDto("该零配件已与库存建立关联关系！");
        }

        // 查询申请
        Boolean resultApply = bizAllocateapplyDetailDao.checkProductRelApply(productNo);
        if(resultApply){
            return StatusDto.buildFailureStatusDto("该零配件已与申请建立关联关系！");
        }

        // 查询关联供应商
        Boolean resultRelSupplier = bizServiceSupplierDao.checkProductRelSupplier(productNo);
        if(resultRelSupplier){
            return StatusDto.buildFailureStatusDto("该零配件已与供应商建立关联关系！");
        }
        carpartsProductService.deleteCarpartsProduct(productNo);
        return StatusDto.buildSuccessStatusDto();

    }

    /**
     * 上传图片
     * @param base64   图片base64编码
     * @return StatusDto<UploadFileInfo>
     * @author zhangkangjian
     * @date 2018-10-31 10:45:26
     */
    @Override
    public StatusDto<UploadFileInfo> uploadImage(String base64) throws UnsupportedEncodingException {
        return uploadService.simpleUpload(new BASE64DecodedMultipartFile(base64), "carpartsimage");
    }

    /**
     * 填充销售价格和车型名称
     * @param queryCarpartsProductDTO 零配件
     * @param relProductPriceList 价格
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 分页零配件
     * @author zhangkangjian
     * @date 2018-09-28 11:27:10
     */
    private StatusDto<Page<BasicCarpartsProductDTO>> getPageStatusDto(QueryCarpartsProductDTO queryCarpartsProductDTO, List<RelProductPrice> relProductPriceList) {
        StatusDtoThriftPage<BasicCarpartsProductDTO> basicCarpartsProductDTO =
            carpartsProductService.queryCarpartsProductListByPriceType(queryCarpartsProductDTO);
        StatusDto<Page<BasicCarpartsProductDTO>> basicCarpartsProductDTOResolve = StatusDtoThriftUtils.resolve(basicCarpartsProductDTO, BasicCarpartsProductDTO.class);
        List<BasicCarpartsProductDTO> rows = basicCarpartsProductDTOResolve.getData().getRows();
        Optional.ofNullable(relProductPriceList).ifPresent(a ->{
            Map<String, List<RelProductPrice>> relProductPriceMap = a.stream().collect(Collectors.groupingBy(RelProductPrice::getProductNo));
            if(rows != null && rows.size() > 0){
                rows.forEach(item ->{
                    // 设置价格
                    buildCarpartsPrice(relProductPriceMap, item);
                    // 把车型id转换成车型名字
                    String fitCarmodel = item.getFitCarmodel();
                    if(StringUtils.isBlank(fitCarmodel)){
                        return;
                    }
                    List<String> fitCarmodelStrIds = Arrays.asList(fitCarmodel.split(Constants.COMMA));
                    List<Long> fitCarmodelLongIds = fitCarmodelStrIds.stream().filter(StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toList());
                    if(fitCarmodelLongIds != null && fitCarmodelLongIds.size() > 0){
                        List<CarmodelManage> carmodelManages = basicCarmodelManageDao.queryPartModel(fitCarmodelLongIds);
                        String fitCarmodelName = StringUtils.join(carmodelManages.stream().map(CarmodelManage::getCarmodelName).toArray(), Constants.COMMA);
                        item.setCarmodelName(fitCarmodelName);
                    }
                });
            }
        });
        return basicCarpartsProductDTOResolve;
    }

    /**
     * 填充零配件的价格
     * @param relProductPriceMap 价格信息
     * @param basicCarpartsProductDTO 零配件信息
     * @author zhangkangjian
     * @date 2018-10-29 15:46:18
     */
    private void buildCarpartsPrice(Map<String, List<RelProductPrice>> relProductPriceMap, BasicCarpartsProductDTO basicCarpartsProductDTO) {
        List<RelProductPrice> relProductPrice = relProductPriceMap.get(basicCarpartsProductDTO.getCarpartsCode());
        if(relProductPrice != null && relProductPrice.size() > 0){
            for (RelProductPrice priceItem : relProductPrice) {
                long priceLevel = priceItem.getPriceLevel();
                double suggestedPrice = priceItem.getSuggestedPrice();
                if(priceLevel == Constants.LADDER_PRICE_2){
                    basicCarpartsProductDTO.setServerCarpartsPrice(suggestedPrice);
                }
                if(priceLevel == Constants.LADDER_PRICE_3){
                    basicCarpartsProductDTO.setCustCarpartsPrice(suggestedPrice);
                }
                if(priceLevel == Constants.LADDER_PRICE_4){
                    basicCarpartsProductDTO.setCarpartsPrice(suggestedPrice);
                }
            }
        }
    }


    /**
     * 批量更新零配件价格结束时间
     * @param updateRelProductPriceList 零配件价格列表
     * @author zhangkangjian
     * @date 2018-11-21 13:45:40
     */
    @Override
    @Async
    public void batchUpdateProductPrice(List<RelProductPrice> updateRelProductPriceList) {
//        if(true){
//            throw new RuntimeException();
//        }
        // 批量更新零配件价格结束时间
        carpartsProductPriceDao.batchUpdateProductPrice(updateRelProductPriceList);

    }

    /**
     * 批量插入零件价格列表
     *
     * @param saveRelProductPriceList 零配件价格列表
     * @author zhangkangjian
     * @date 2018-11-21 14:30:32
     */
    @Override
    @Async
    public Future<String> batchSaveProductPrice(List<RelProductPrice> saveRelProductPriceList) {
        if(true){
            throw new RuntimeException();
        }
        // 批量插入零件价格列表
        carpartsProductPriceDao.batchSaveProductPrice(saveRelProductPriceList);


        return new AsyncResult<String>("发送消息用了"+1+"秒");
    }


}
