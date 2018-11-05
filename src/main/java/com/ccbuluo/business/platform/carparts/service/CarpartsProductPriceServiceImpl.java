package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.business.platform.carparts.dao.CarpartsProductPriceDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.codec.Base64;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.entity.UploadFileInfo;
import com.ccbuluo.core.service.UploadService;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
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
        // 查询当前机构库存的商品
        Map<String, Object> mapProductNo = bizAllocateApplyDao.queryStockQuantity(orgCode, null);
        Set<String> keyProductNo = mapProductNo.keySet();
        List<String> productNoList = new ArrayList<>(keyProductNo);
        queryCarpartsProductDTO.setCarpartsCodeList(productNoList);
        List<RelProductPrice> relProductPriceList = Lists.newArrayList();
        productNoList.forEach(a ->{
            RelProductPrice relProductPrice = new RelProductPrice();
            relProductPrice.setProductNo(a);
            relProductPriceList.add(relProductPrice);
        });
        return getPageStatusDto(queryCarpartsProductDTO, relProductPriceList);
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
                    String categoryCodePath = item.getCategoryCodePath();
                    if(StringUtils.isNotBlank(categoryCodePath)){
                        int i = categoryCodePath.indexOf("-") + 1;
                        String substring = categoryCodePath.substring(i, categoryCodePath.length());
                        item.setCategoryCodePath(substring);
                    }
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
                if(priceLevel == 2){
                    basicCarpartsProductDTO.setServerCarpartsPrice(suggestedPrice);
                }
                if(priceLevel == 3){
                    basicCarpartsProductDTO.setCustCarpartsPrice(suggestedPrice);
                }
                if(priceLevel == 4){
                    basicCarpartsProductDTO.setCarpartsPrice(suggestedPrice);
                }
            }
        }
    }
}
