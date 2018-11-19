package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BasicCarpartsProduct;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.export.ExportSingleUtils;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.allocateapply.dto.FindStockListDTO;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelManageDao;
import com.ccbuluo.business.platform.carconfiguration.entity.CarmodelManage;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.business.platform.carparts.dao.CarpartsProductPriceDao;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.order.dao.BizServiceOrderDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.UploadFileInfo;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.service.UploadService;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.excel.imports.ExcelReaderUtils;
import com.ccbuluo.excel.imports.ExcelRowReaderBean;
import com.ccbuluo.excel.readpic.ExcelPictruePos;
import com.ccbuluo.excel.readpic.ExcelPictruesUtils;
import com.ccbuluo.excel.readpic.ExcelShapeSaveLocal;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.QueryNameByUseruuidsDTO;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
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
    private CarpartsProductPriceService carpartsProductServiceImpl;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @Resource
    private BizServiceSupplierDao bizServiceSupplierDao;
    @Resource
    private BizServiceOrderDao bizServiceOrderDao;
    @Resource
    private BizServiceEquipmentDao bizServiceEquipmentDao;

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
     * @param relProductPrice
     * @author zhangkangjian
     * @date 2018-09-06 19:27:11
     */
    @Override
    public void saveProductPrice(List<RelProductPrice> relProductPrice) {
        // 查询商品最新一条的价格，并更新结束时间
        relProductPrice.forEach(item ->{
            carpartsProductPriceDao.updateProductEndTime(item);
            String loggedUserId = userHolder.getLoggedUserId();
            item.setOperator(loggedUserId);
            item.setCreator(loggedUserId);
            item.setStartTime(new Date());
            carpartsProductPriceDao.save(item);
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
        // 查询维修单详单
        Boolean resultRelOrder = bizServiceOrderDao.checkProductRelOrder(productNo);
        if(resultRelOrder){
            return StatusDto.buildFailureStatusDto("该零配件已与维修单建立关联关系！");
        }
        carpartsProductService.deleteCarpartsProduct(productNo);
        return StatusDto.buildSuccessStatusDto();

    }

    /**
     * 导入零配件
     *
     * @param multipartFile
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-11-16 11:46:02
     */
    @Override
    public StatusDto<String> importCarparts(MultipartFile multipartFile) throws Exception {
        StatusDto<UploadFileInfo> carpartsexcel = uploadService.simpleUpload(multipartFile, "carpartsexcel");
        if(Constants.SUCCESS_CODE.equals(carpartsexcel.getCode())){
            throw new CommonException(Constants.ERROR_CODE, "上传失败");
        }
        UploadFileInfo uploadFileInfo = carpartsexcel.getData();
        if(uploadFileInfo == null){
            throw new CommonException(Constants.ERROR_CODE, "上传文件后，数据异常");
        }
        String filepath = uploadFileInfo.getPath();
        // 上传图片并获取的图片的路径
        Map<String, Collection<ExcelPictruePos>> allDate = ExcelPictruesUtils.getAllDate(filepath, 1, new ExcelShapeSaveLocal("C:\\Users\\Ezreal\\Desktop\\haha"));
        // 读取excel，获取list的数据
        List<BasicCarpartsProductDTO> productList = geCarpartsProductList(filepath);
        if(productList == null || productList.size() == 0){
            return StatusDto.buildFailureStatusDto("数据为空导入失败！");
        }
        // 名称转换成id
        conventNameById(productList);
        // 保存零配件
        carpartsProductService.batchSaveCarpartsProduct(productList);
        // 构建阶梯价格信息
        List<RelProductPrice> relProductPriceList = buildProductList(allDate, productList);
        // 保存阶梯价格
        carpartsProductServiceImpl.saveProductPrice(relProductPriceList);
        return StatusDto.buildSuccessStatusDto();
    }
    /**
     * 读取excel文件转换为list<BasicCarpartsProductDTO>
     * @param filepath excel的路径
     * @return List<BasicCarpartsProductDTO> 零配件的列表
     * @author zhangkangjian
     * @date 2018-11-16 16:41:02
     */
    private List<BasicCarpartsProductDTO> geCarpartsProductList(String filepath) throws Exception {
        Map<String, Integer> columnIndexMapperBeanField = Maps.newHashMap();
        // 列映射，实体字段，对应的excel列索引
        columnIndexMapperBeanField.put("carpartsMarkno", 1);
        columnIndexMapperBeanField.put("carpartsName", 2);
        columnIndexMapperBeanField.put("carpartsUnit", 3);
        columnIndexMapperBeanField.put("usedAmount", 4);
        columnIndexMapperBeanField.put("carmodelName", 6);
        columnIndexMapperBeanField.put("serverCarpartsPrice", 7);
        columnIndexMapperBeanField.put("custCarpartsPrice", 8);
        columnIndexMapperBeanField.put("carpartsPrice", 9);
        // 列映射，实体字段，对应的excel列索引
        ExcelRowReaderBean<BasicCarpartsProductDTO> readerBean = new ExcelRowReaderBean<BasicCarpartsProductDTO>(BasicCarpartsProductDTO.class, columnIndexMapperBeanField) {
            @Override
            public boolean checkRow(int sheetIndex, int curRow, List<String> rowlist) {
                // 标题行放过
                return curRow >= 1;
            }
        };

        // 设置开始读取的行，之前的行被跳过
        readerBean.setStartRow(2);
        // 调协列的宽度，小于宽度的列被跳过
        readerBean.setTargetColLength(4);
        // 读取excel
        ExcelReaderUtils.readExcel(readerBean, filepath);
        // 获取读取的结果结果
        return readerBean.getData();
    }

    /**
     *  构建零配件信息和零配件价格信息
     * @param pictrueListInfo 图片列表的信息
     * @param productList 零配件的列表
     * @return List<RelProductPrice> 零配件的价格列表
     * @author zhangkangjian
     * @date 2018-11-16 16:35:55
     */
    private List<RelProductPrice> buildProductList(Map<String, Collection<ExcelPictruePos>> pictrueListInfo, List<BasicCarpartsProductDTO> productList) {
        List<RelProductPrice> relProductPriceList = Lists.newArrayList();
        productList.forEach(a ->{
            String carpartsMarkno = a.getCarpartsMarkno();
            Collection<ExcelPictruePos> excelPictruePos = pictrueListInfo.get(carpartsMarkno);
            List<ExcelPictruePos> list = new ArrayList<ExcelPictruePos>(excelPictruePos);
            ExcelPictruePos excelPictruePos1 = list.get(0);
            String path = excelPictruePos1.getPath();
            a.setCarpartsImage(path);
            // 生成零配件编号
            StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FP);
            String data = stringStatusDto.getData();
            if(StringUtils.isNotBlank(data)){
                throw new CommonException(Constants.ERROR_CODE, "生成编号失败！");
            }
            a.setCarpartsCode(data);
            // 构建阶梯价格
            RelProductPrice custProductPrice = new RelProductPrice(data,Constants.PRODUCT_TYPE_FITTINGS, a.getCustCarpartsPrice(), 3L);
            relProductPriceList.add(custProductPrice);

            RelProductPrice serProductPrice = new RelProductPrice(data,Constants.PRODUCT_TYPE_FITTINGS, a.getServerCarpartsPrice(), 2L);
            relProductPriceList.add(serProductPrice);

            RelProductPrice userProductPrice = new RelProductPrice(data,Constants.PRODUCT_TYPE_FITTINGS, a.getCarpartsPrice(), 4L);
            relProductPriceList.add(userProductPrice);
        });
        return relProductPriceList;
    }


    /**
     *  车型名称转换成车型id
     * @param list 零配件列表
     * @author zhangkangjian
     * @date 2018-11-16 16:17:55
     */
    private void conventNameById(List<BasicCarpartsProductDTO> list){
        // 查出所有的车型
        List<Map<String, Object>> maps = basicCarmodelManageDao.queryAll();
        Map<String, Object> map = Maps.newHashMap();
        maps.forEach(item->{
            String name = (String)item.get("name");
            Long id = (Long)item.get("id");
            map.put(name, id);
        });
        ArrayList<Long> objects = Lists.newArrayList();
        list.forEach(item->{
            String carmodelName = item.getCarmodelName();
            if(StringUtils.isNotBlank(carmodelName)){
                String[] split = carmodelName.split(Constants.COMMA);
                List<String> strings = Arrays.asList(split);
                strings.forEach(str->{
                    Long id = (Long)map.get(str);
                    objects.add(id);
                });
                String fitCarmodelId = StringUtils.join(objects, Constants.COMMA);
                item.setFitCarmodel(fitCarmodelId);
            }
        });



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
                    buildCarpartsPrice(relProductPriceMap, item);
                });
            }
        });
        basicCarmodelManageService.buildCarModeName(basicCarpartsProductDTOResolve.getData().getRows());
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

    @Override
    public StatusDto exportCarparts() throws IOException {
        // 查询所有零配件
//        StatusDtoThriftPage<BasicCarpartsProductDTO> basicCarpartsProductDTOStatusDtoThriftPage = carpartsProductService.queryCarpartsProductList(null, 0, Integer.MAX_VALUE);
//        StatusDto<Page<BasicCarpartsProductDTO>> list = StatusDtoThriftUtils.resolve(basicCarpartsProductDTOStatusDtoThriftPage, BasicCarpartsProductDTO.class);
//        if (null == list.getData()) {
//            return StatusDto.buildFailure("没有查询到零配件!");
//        }
//        // 查询零配件的价格
//        QueryCarpartsProductDTO queryCarpartsProductDTO = new QueryCarpartsProductDTO();
//        queryCarpartsProductDTO.setOffset(0);
//        queryCarpartsProductDTO.setPageSize(Integer.MAX_VALUE);
//        StatusDto<Page<BasicCarpartsProductDTO>> pageStatusDto = carpartsProductServiceImpl.queryCarpartsProductPriceList(queryCarpartsProductDTO);
//        Page<BasicCarpartsProductDTO> data = pageStatusDto.getData();
//        List<BasicCarpartsProductDTO> rows = data.getRows();
//        Map<String, BasicCarpartsProductDTO> carpartsProductMap = rows.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, Function.identity()));
//        Page<BasicCarpartsProductDTO> data1 = list.getData();
//        List<BasicCarpartsProductDTO> exportData = data1.getRows();
//        for (BasicCarpartsProductDTO exportDatum : exportData) {
//            BasicCarpartsProductDTO basicCarpartsProductDTO = carpartsProductMap.get(exportDatum.getCarpartsCode());
//            exportDatum.setServerCarpartsPrice(basicCarpartsProductDTO.getServerCarpartsPrice());
//            exportDatum.setCarpartsPrice(basicCarpartsProductDTO.getCarpartsPrice());
//            exportDatum.setCustCarpartsPrice(basicCarpartsProductDTO.getCustCarpartsPrice());
//            // 图片
//
//        }



        String fileFullPath = "d:/test.xls";
        LinkedHashMap<String, String> headerMapper = com.google.common.collect.Maps.newLinkedHashMap();
        List<Map<String, Object>> list = Lists.newArrayList();
        ExportSingleUtils<Map<String, Object>> ex = new ExportSingleUtils<Map<String, Object>>(fileFullPath,
            headerMapper, list);
        int darwRow = ex.darwRow(0, new String[] { "序号", "件号", "名称", "计量单位","单车用量","图片","适用车型","服务中心价格","客户经理价格","用户销售价格" });
        List<BasicCarpartsProduct> basicCarpartsProductList = Lists.newArrayList();
        BasicCarpartsProduct basicCarpartsProduct = new BasicCarpartsProduct();
        basicCarpartsProduct.setCarpartsMarkno("0261231204");
        basicCarpartsProduct.setCarpartsName("爆震传感器");
        basicCarpartsProduct.setCarpartsUnit("个");
        basicCarpartsProduct.setUsedAmount(4L);
        basicCarpartsProduct.setCarpartsImage("faqq");
        basicCarpartsProduct.setCarmodelName("A型");
        basicCarpartsProduct.setCustmanagerPrice(new BigDecimal(123));
        basicCarpartsProduct.setServercenterPrice(new BigDecimal(123));
        basicCarpartsProduct.setSellPrice(new BigDecimal(123));
        BasicCarpartsProduct basicCarpartsProduct2 = new BasicCarpartsProduct();
        basicCarpartsProduct2.setCarpartsMarkno("1001011KHAC");
        basicCarpartsProduct2.setCarpartsName("左支架");
        basicCarpartsProduct2.setCarpartsUnit("个");
        basicCarpartsProduct2.setUsedAmount(1L);
        basicCarpartsProduct2.setCarpartsImage("fa12131awseeqq");
        basicCarpartsProduct2.setCarmodelName("B型");
        basicCarpartsProduct2.setCustmanagerPrice(new BigDecimal(3423432));
        basicCarpartsProduct2.setServercenterPrice(new BigDecimal(122343323));
        basicCarpartsProduct2.setSellPrice(new BigDecimal(1243243223));
        BasicCarpartsProduct basicCarpartsProduct3 = new BasicCarpartsProduct();
        basicCarpartsProduct3.setCarpartsMarkno("0280750101");
        basicCarpartsProduct3.setCarpartsName("电子节气门体");
        basicCarpartsProduct3.setCarpartsUnit("个");
        basicCarpartsProduct3.setUsedAmount(6L);
        basicCarpartsProduct3.setCarpartsImage("fasdadasdqq");
        basicCarpartsProduct3.setCarmodelName("C型");
        basicCarpartsProduct3.setCustmanagerPrice(new BigDecimal(2133));
        basicCarpartsProduct3.setServercenterPrice(new BigDecimal(3123123));
        basicCarpartsProduct3.setSellPrice(new BigDecimal(11231323));
        basicCarpartsProductList.add(basicCarpartsProduct3);
        basicCarpartsProductList.add(basicCarpartsProduct2);
        basicCarpartsProductList.add(basicCarpartsProduct);
        for (int i = 1; i <= basicCarpartsProductList.size(); i++) {
            BasicCarpartsProduct carpartsProduct = basicCarpartsProductList.get(i - 1);
            ex.darwRow(i , new String[] { String.valueOf(i), carpartsProduct.getCarpartsMarkno(), carpartsProduct.getCarpartsName()
                , carpartsProduct.getCarpartsUnit(),carpartsProduct.getUsedAmount().toString(),carpartsProduct.getCarpartsImage()
                ,carpartsProduct.getCarmodelName(),carpartsProduct.getServercenterPrice().toString(),carpartsProduct.getCustmanagerPrice().toString()
                ,carpartsProduct.getSellPrice().toString() });
        }
        ex.build(true);
        export("test.xls", "d:/test.xls");


        return null;
    }

    public ResponseEntity<byte[]> export(String fileName, String filePath) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        File file = new File(filePath);

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
            headers, HttpStatus.CREATED);
    }


}
