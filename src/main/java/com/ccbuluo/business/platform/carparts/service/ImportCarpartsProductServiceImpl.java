package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.OrganizationTypeEnum;
import com.ccbuluo.business.entity.BizAllocateApply;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateapplyDetailDao;
import com.ccbuluo.business.platform.carconfiguration.dao.BasicCarmodelManageDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.constants.SystemPropertyHolder;
import com.ccbuluo.core.entity.OSSClientProperties;
import com.ccbuluo.core.entity.UploadFileInfo;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.service.UploadService;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.excel.imports.ExcelReaderUtils;
import com.ccbuluo.excel.imports.ExcelRowReaderBean;
import com.ccbuluo.excel.readpic.ExcelPictruePos;
import com.ccbuluo.excel.readpic.ExcelPictruesUtils;
import com.ccbuluo.excel.readpic.ExcelShapeSaveAliyunOSS;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author zhangkangjian
 * @date 2018-11-21 15:12:25
 */
@Service
public class ImportCarpartsProductServiceImpl implements ImportCarpartsProductService{

    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;
    @Resource
    private GenerateProjectCodeService generateProjectCodeService;
    @Resource
    private UploadService uploadService;
    @Resource
    private BasicCarmodelManageDao basicCarmodelManageDao;
    @Resource(name = "carpartsProductPriceServiceImpl")
    private CarpartsProductPriceService carpartsProductServiceImpl;
    @Resource
    private BizAllocateapplyDetailDao bizAllocateapplyDetailDao;
    @ThriftRPCClient("UserCoreSerService")
    private BasicUserOrganizationService basicUserOrganization;
    @Autowired
    private OSSClientProperties ossClientProperties;
    @Resource
    private UserHolder userHolder;

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
        // 1.导入的文件上传阿里云
        StatusDto<UploadFileInfo> carpartsexcel = uploadService.simpleUpload(multipartFile, "carpartsexcel");
        if(!Constants.SUCCESS_CODE.equals(carpartsexcel.getCode())){
            throw new CommonException(Constants.ERROR_CODE, "上传失败");
        }
        // 2.上传文件到本地（注： ExcelReaderUtils.readExcel(readerBean, filepath) 导入需要filePath）
        File file = uploadFilesToLocal(multipartFile);
        String filepath = file.getPath();

        // 3.读取excel，获取list的数据
        List<BasicCarpartsProductDTO> productList = geCarpartsProductList(filepath);
        if(productList == null || productList.size() == 0){
            return StatusDto.buildFailureStatusDto("数据为空导入失败！");
        }
        // 4.车型名称转换成车型id
        conventCarModelNameById(productList);
        // 5.上传图片并把相对路径填充到 productList中
        uploadImgeAndSetImgePath(filepath, productList);
        // 6.批量保存或者更新零配件数据（如数据库中存在的零配件和productList中的零配件代码相同的数据，则更新数据，否则插入数据）
        batchSaveOrUpdateProductList(productList);
        // 7.删除本地项目的文件
        new File(filepath).delete();
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 保存文件到本地项目
     * @param multipartFile 文件
     * @return  File
     * @author zhangkangjian
     * @date 2018-11-20 20:09:40
     */
    private File uploadFilesToLocal(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        String property = System.getProperty("user.dir") + "\\src\\main\\resources\\";
        File file = new File(property + System.currentTimeMillis() + ".xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            byte temp[] = new byte[1024];
            int size = -1;
            while ((size = inputStream.read(temp)) != -1) { // 每次读取1KB，直至读完
                outputStream.write(temp, 0, size);
            }
        }finally {
            inputStream.close();
            outputStream.close();
        }
        return file;
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
                Boolean boo = true;
                if(!(curRow >= 1)){
                    boo = false;
                }
                try {
                    String usedAmount = rowlist.get(4);
                    if(usedAmount != null){
                        Long.parseLong(usedAmount);
                    }
                    String serverCarpartsPrice = rowlist.get(7);
                    if(StringUtils.isNotBlank(serverCarpartsPrice)){
                        Double.parseDouble(serverCarpartsPrice);
                    }
                    String custCarpartsPrice = rowlist.get(8);
                    if(StringUtils.isNotBlank(custCarpartsPrice)){
                        Double.parseDouble(custCarpartsPrice);
                    }
                    String carpartsPrice = rowlist.get(9);
                    if(StringUtils.isNotBlank(carpartsPrice)){
                        Double.parseDouble(carpartsPrice);
                    }
                }catch (Exception e){
                    boo = false;
//                    e.printStackTrace();
                }
                return boo;
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
     *  车型名称转换成车型id
     * @param list 零配件列表
     * @author zhangkangjian
     * @date 2018-11-16 16:17:55
     */
    private void conventCarModelNameById(List<BasicCarpartsProductDTO> list){
        // 查出所有的车型
        List<Map<String, Object>> maps = basicCarmodelManageDao.queryAll();
        Map<String, Integer> map = Maps.newHashMap();
        maps.forEach(item->{
            String name = (String)item.get("name");
            Integer id = (Integer)item.get("id");
            map.put(name, id);
        });
        ArrayList<Integer> objects = Lists.newArrayList();
        list.forEach(item->{
            String carpartsName = item.getCarpartsName();
            if(StringUtils.isNotBlank(carpartsName)){
                item.setCarpartsPinyinname(PinyinTool.getPinYinHeadChar(carpartsName));
            }
            String carmodelName = item.getCarmodelName();
            if(StringUtils.isNotBlank(carmodelName)){
                String[] split = StringUtils.split(carmodelName, Constants.COMMA);
                List<String> splitList = Arrays.asList(split);
                splitList.forEach(str->{
                    Integer id = (Integer)map.get(str);
                    if(id != null){
                        objects.add(id);
                    }
                });
                String fitCarmodelId = StringUtils.join(objects, Constants.COMMA);
                item.setFitCarmodel(fitCarmodelId);
                objects.clear();
            }
        });
    }

    /**
     *  上传图片并把相对路径填充到 productList中
     * @param filepath excel路径
     * @param productList 零配件的列表
     * @return List<RelProductPrice> 零配件的价格列表
     * @author zhangkangjian
     * @date 2018-11-16 16:35:55
     */
    private void uploadImgeAndSetImgePath(String filepath, List<BasicCarpartsProductDTO> productList) {
        // 上传图片并获取的图片的路径
        String accessKeyId = ossClientProperties.getAccessKeyId();
        String accessKeySecret = ossClientProperties.getAccessKeySecret();
        String bucketName = ossClientProperties.getBucketName();
        String endpoint = ossClientProperties.getEndpoint();
        String baseAppid = SystemPropertyHolder.getBaseAppid();
        ExcelShapeSaveAliyunOSS excelShapeSaveAliyunOSS = new ExcelShapeSaveAliyunOSS(accessKeyId, accessKeySecret, bucketName, endpoint, baseAppid);

        Map<String, Collection<ExcelPictruePos>> pictrueListInfo = ExcelPictruesUtils.getAllDate(filepath, 1, excelShapeSaveAliyunOSS);
        String loggedUserId = userHolder.getLoggedUserId();
        productList.forEach(a ->{
            a.setCreator(loggedUserId);
            a.setOperator(loggedUserId);
            String carpartsMarkno = a.getCarpartsMarkno();
            Collection<ExcelPictruePos> excelPictruePosColl = pictrueListInfo.get(carpartsMarkno);
            if(excelPictruePosColl != null){
                List<ExcelPictruePos> excelPictruePosList = new ArrayList<ExcelPictruePos>(excelPictruePosColl);
                ExcelPictruePos excelPictruePos = excelPictruePosList.get(0);
                String ossKey = excelPictruePos.getOssKey();
                a.setCarpartsImage(ossKey);
            }
        });
    }

    /**
     * 批量保存或者更新零配件数据（如果有更新数据，没有插入数据）
     * @param productList 零配件的列表
     * @author zhangkangjian
     * @date 2018-11-20 10:42:24
     */
    private void batchSaveOrUpdateProductList(List<BasicCarpartsProductDTO> productList) {
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

        // 查询所有的零配件
        List<BasicCarpartsProductDTO> basicCarpartsProductDTOS = carpartsProductService.queryCarpartsProductListByCategoryCode(null);
        Optional.ofNullable(basicCarpartsProductDTOS).ifPresent(carpartsProductItem ->{
            Map<String, BasicCarpartsProductDTO> carpartsProductMap = carpartsProductItem.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsMarkno, a -> a,(k1, k2)->k1));
            Optional.of(productList).ifPresent(productItem ->{
                List<BasicCarpartsProductDTO> saveProductList = Lists.newArrayList();
                List<BasicCarpartsProductDTO> updateProductList = Lists.newArrayList();
                List<RelProductPrice> updateRelProductPriceList = Lists.newArrayList();
                List<RelProductPrice> saveRelProductPriceList = Lists.newArrayList();
                productItem.forEach(a ->{
                    String carpartsMarkno = a.getCarpartsMarkno();
                    BasicCarpartsProductDTO basicCarpartsProductDTO = carpartsProductMap.get(carpartsMarkno);
                    // 数据库中已存在该数据，更新即可。如不存在，需要插入新数据
                    if(basicCarpartsProductDTO != null){
                        String carpartsCode = basicCarpartsProductDTO.getCarpartsCode();
                        a.setCarpartsCode(carpartsCode);
                        updateProductList.add(a);
                        buildProductPrice(serApplyNoList, custApplyNoList, updateRelProductPriceList, a, carpartsCode);
                    }else {
                        // 生成零配件编号
                        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FP);
                        String code = stringStatusDto.getData();
                        if(StringUtils.isBlank(code)){
                            throw new CommonException(Constants.ERROR_CODE, "生成编号失败！");
                        }
                        a.setCarpartsCode(code);
                        saveProductList.add(a);
                        buildProductPrice(serApplyNoList, custApplyNoList, saveRelProductPriceList, a, code);
                    }
                });

                // 批量插入，保存零配件
                carpartsProductService.batchSaveCarpartsProduct(saveProductList);
                // 批量更新，更新零配件
                carpartsProductService.batchUpdateCarpartsProduct(updateProductList);
                // 批量更新零配件价格时间
                carpartsProductServiceImpl.batchUpdateProductPrice(updateRelProductPriceList);
                // 更新申请单详单的价格
                List<RelProductPrice> collect = updateRelProductPriceList.stream().filter(a -> {
                    return a.getPriceLevel() != 4L;

                }).collect(Collectors.toList());
                bizAllocateapplyDetailDao.batchUpdateAllocateapplyDetail(collect);
                // 更新入库计划
                bizAllocateapplyDetailDao.batchUpdateInstockorderDetail(collect);
                // 更新出库计划
                bizAllocateapplyDetailDao.batchUpdateOutstockorderDetail(collect);
                saveRelProductPriceList.addAll(updateRelProductPriceList);
                // 批量插入零配件价格列表
                carpartsProductServiceImpl.batchSaveProductPrice(saveRelProductPriceList);
            });
        });
    }

    /**
     * 构建价格实体
     *
     * @param serApplyNoList 服务中心的申请编号
     * @param custApplyNoList 客户经理的申请编号
     * @param relProductPriceList 阶梯价格
     * @author zhangkangjian
     * @date 2018-11-20 20:11:14
     */
    private void buildProductPrice(List<String> serApplyNoList, List<String> custApplyNoList, List<RelProductPrice> relProductPriceList, BasicCarpartsProductDTO basicCarpartsProductDTO, String productCode) {
        // 构建阶梯价格
        RelProductPrice custProductPrice = new RelProductPrice(productCode,Constants.PRODUCT_TYPE_FITTINGS, basicCarpartsProductDTO.getCustCarpartsPrice(), 3L);
        String substring = getApplyNoString(custApplyNoList);
        custProductPrice.setApplyNoList(substring);
        custProductPrice.setStartTime(new Date());
        relProductPriceList.add(custProductPrice);

        RelProductPrice serProductPrice = new RelProductPrice(productCode,Constants.PRODUCT_TYPE_FITTINGS, basicCarpartsProductDTO.getServerCarpartsPrice(), 2L);
        String applyNoString = getApplyNoString(serApplyNoList);
        serProductPrice.setApplyNoList(applyNoString);
        serProductPrice.setStartTime(new Date());
        relProductPriceList.add(serProductPrice);

        RelProductPrice userProductPrice = new RelProductPrice(productCode,Constants.PRODUCT_TYPE_FITTINGS, basicCarpartsProductDTO.getCarpartsPrice(), 4L);
        userProductPrice.setStartTime(new Date());
        relProductPriceList.add(userProductPrice);
    }

    private String getApplyNoString(List<String> custApplyNoList) {
        StringBuilder sb = new StringBuilder();
        for (String str :  custApplyNoList) {
            sb.append("'").append(str).append("'").append(",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }
}
