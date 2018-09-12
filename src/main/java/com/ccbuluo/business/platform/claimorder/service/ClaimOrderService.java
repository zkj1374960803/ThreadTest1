package com.ccbuluo.business.platform.claimorder.service;

import com.ccbuluo.business.platform.claimorder.dto.BizServiceClaimorder;
import com.ccbuluo.business.platform.claimorder.dto.QueryClaimorderListDTO;
import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;

import java.util.List;
import java.util.Map;

/**
 * @author zhangkangjian
 * @date 2018-09-08 10:42:14
 */
public interface ClaimOrderService {
    /**
     * 生成索赔单
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-08 10:43:18
     */
    void generateClaimForm(String serviceOrdno);
    /**
     *  提交索赔单
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-08 14:26:55
     */
    void updateClaimOrder(BizServiceClaimorder bizServiceClaimorder);
    /**
     *  查询索赔单的详情
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-08 14:40:12
     */
    BizServiceClaimorder findClaimOrderDetail(BizServiceClaimorder bizServiceClaimorder);
    /**
     * 查询索赔单的列表
     * @param claimOrdno
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-09-08 16:55:56
     */
    StatusDto<Page<QueryClaimorderListDTO>> queryClaimorderList(String claimOrdno, String docStatus, int offset, int pageSize);
    /**
     * 更新索赔单状态
     * @param claimOrdno 索赔单号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    void updateDocStatus(String claimOrdno, String docStatus);
    /**
     * 更新索赔单状态和验收时间
     * @param claimOrdno 索赔单号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    void updateDocStatusAndProcessTime(String claimOrdno, String docStatus);
    /**
     * 更新索赔单状态和支付时间
     * @param claimOrdno 索赔单号
     * @param docStatus 索赔单状态
     * @author zhangkangjian
     * @date 2018-09-10 10:43:33
     */
    StatusDto updateDocStatusAndRepayTime(String claimOrdno, String docStatus);
    /**
     * 查询零配件列表信息
     * @param productDetailDTO 查询条件
     * @return List<ProductDetailDTO>
     * @author zhangkangjian
     * @date 2018-09-10 16:36:14
     */
    List<ProductDetailDTO> queryFitingDetailList(ProductDetailDTO productDetailDTO);
    /**
     * 查询支付价格
     * @param serviceOrdno 维修单号
     * @return  Map<String, Double>
     * @author zhangkangjian
     * @date 2018-09-12 14:02:21
     */
    Map<String, Double> findPaymentAmount(String serviceOrdno);
}
