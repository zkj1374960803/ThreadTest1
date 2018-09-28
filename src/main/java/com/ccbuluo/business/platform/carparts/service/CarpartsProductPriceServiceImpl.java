package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.allocateapply.dao.BizAllocateApplyDao;
import com.ccbuluo.business.platform.carconfiguration.service.BasicCarmodelManageService;
import com.ccbuluo.business.platform.carparts.dao.CarpartsProductPriceDao;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public void saveProductPrice(RelProductPrice relProductPrice) {
        // 查询商品最新一条的价格，并更新结束时间
        carpartsProductPriceDao.updateProductEndTime(relProductPrice);
        String loggedUserId = userHolder.getLoggedUserId();
        relProductPrice.setOperator(loggedUserId);
        relProductPrice.setCreator(loggedUserId);
        relProductPrice.setStartTime(new Date());
        carpartsProductPriceDao.save(relProductPrice);
    }

    /**
     * 查询维修单的零配件列表
     *
     * @param queryCarpartsProductDTO 查询条件
     * @return StatusDto<Page < BasicCarpartsProductDTO>> 查询分页信息
     * @author zhangkangjian
     * @date 2018-09-28 10:31:47
     */
    @Override
    public StatusDto<Page<BasicCarpartsProductDTO>> queryServiceProductList(QueryCarpartsProductDTO queryCarpartsProductDTO) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 查询当前客户经理已有库存的商品
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
     * 填充销售价格和车型名称
     * @param queryCarpartsProductDTO
     * @param relProductPrice
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-28 11:27:10
     */
    private StatusDto<Page<BasicCarpartsProductDTO>> getPageStatusDto(QueryCarpartsProductDTO queryCarpartsProductDTO, List<RelProductPrice> relProductPrice) {
        StatusDtoThriftPage<BasicCarpartsProductDTO> basicCarpartsProductDTO =
            carpartsProductService.queryCarpartsProductListByPriceType(queryCarpartsProductDTO);
        StatusDto<Page<BasicCarpartsProductDTO>> basicCarpartsProductDTOResolve = StatusDtoThriftUtils.resolve(basicCarpartsProductDTO, BasicCarpartsProductDTO.class);
        List<BasicCarpartsProductDTO> rows = basicCarpartsProductDTOResolve.getData().getRows();

        Optional.ofNullable(relProductPrice).ifPresent(a ->{
            Map<String, RelProductPrice> relProductPriceMap = a.stream().collect(Collectors.toMap(RelProductPrice::getProductNo, b -> b,(k1, k2)->k1));
            if(rows != null && rows.size() > 0){
                rows.forEach(item ->{
                    RelProductPrice relProductPrice1 = relProductPriceMap.get(item.getCarpartsCode());
                    if(relProductPrice1 != null){
                        item.setCarpartsPrice(String.valueOf(relProductPrice1.getSuggestedPrice()));
                    }
                });
            }
        });
        basicCarmodelManageService.buildCarModeName(basicCarpartsProductDTOResolve.getData().getRows());
        return basicCarpartsProductDTOResolve;
    }
}
