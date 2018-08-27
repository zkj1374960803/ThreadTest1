package com.ccbuluo.business.platform.stockdetail.service;

import com.ccbuluo.business.platform.allocateapply.dto.QueryAllocateApplyListDTO;
import com.ccbuluo.business.platform.equipment.dao.BizServiceEquipmentDao;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.stockdetail.dao.ProblemStockDetailDao;
import com.ccbuluo.business.platform.stockdetail.dto.ProblemStockBizStockDetailDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 带条件分页查询所有零配件的问题库存
     * @param type 物料或是零配件
     * @param productCategory 物料类型
     * @param productList 商品
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author weijb
     * @date 2018-08-14 21:59:51
     */
    @Override
    public Page<StockBizStockDetailDTO> queryStockBizStockDetailDTOList(boolean category, String type, String productCategory, List<BasicCarpartsProductDTO> productList, String keyword, Integer offset, Integer pageSize){
        List<String> productNames = null;
        if(null != productList && productList.size() > 0){
            productNames = productList.stream().map(BasicCarpartsProductDTO::getCarpartsName).collect(Collectors.toList());
        }

        if(StringUtils.isNotBlank(productCategory)){
            List<DetailBizServiceEquipmentDTO> equis = bizServiceEquipmentDao.queryEqupmentByEquiptype(Long.valueOf(productCategory));
            productNames = equis.stream().map(DetailBizServiceEquipmentDTO::getEquipName).collect(Collectors.toList());
        }
        return problemStockDetailDao.queryStockBizStockDetailDTOList(category, type,"", productNames, keyword,offset, pageSize);
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
        return problemStockDetailDao.queryStockBizStockDetailDTOList(category, type,orgCode, productNames, keyword,offset, pageSize);
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
        psd.setProblemDetailList(problemStockDetailDao.queryProblemStockBizStockList(orgCode, psd.getProductNo()));
        return psd;
    }
}
