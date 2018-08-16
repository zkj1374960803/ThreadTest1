package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 带条件分页查询所有零配件的问题库存
     * @param productType 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(String productType, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize){
        List<String> codes = new ArrayList<String>();
        if(null != productList && productList.size() > 0){
            codes = getCodes(productList);
        }
        return problemStockDetailDao.queryStockBizStockDetailDTOList("", productType, codes, keyword,offset, pageSize);
    }
    /**
     * 带条件分页查询本机构所有零配件的问题库存
     * @param productType 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> querySelfStockBizStockDetailDTOList(String productType, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize){
        List<String> codes = new ArrayList<String>();
        if(null != productList && productList.size() > 0){
            codes = getCodes(productList);
        }
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        return problemStockDetailDao.queryStockBizStockDetailDTOList(orgCode, productType, codes, keyword,offset, pageSize);
    }

    /**
     * 根据物料code查询某个物料的问题件库存
     * @param productNo 商品编号
     * @param offset 起始数
     * @param pageSize 每页数量
     * @return
     * @exception
     * @author weijb
     * @date 2018-08-15 08:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> getProdectStockBizStockDetailByCode(String productNo, Integer offset, Integer pageSize){
        return problemStockDetailDao.getProdectStockBizStockDetailByCode("", productNo, offset, pageSize);
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
        return problemStockDetailDao.getProdectStockBizStockDetailByCode(orgCode, productNo, offset, pageSize);
    }

    /**
     *  获取商品codes
     */
    private List<String> getCodes(List<BasicCarpartsProductDTO> productList){
        List<String> codes = new ArrayList<String>();
        for(BasicCarpartsProductDTO bp : productList){
            codes.add(bp.getCarpartsCode());
        }
        return codes;
    }
}
