package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateApplyDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;

import java.util.List;

/**
 * @author zhangkangjian
 * @date 2018-08-07 14:29:38
 */
public interface AllocateApplyService {
    /**
     * 创建物料或者零配件申请
     * @param allocateApplyDTO 申请单实体
     * @author zhangkangjian
     * @date 2018-08-07 20:54:24
     */
    void createAllocateApply(AllocateApplyDTO allocateApplyDTO);
    /**
     * 查询申请单详情
     * @param applyNo 申请单号
     * @return BizAllocateApply 申请单详情
     * @author zhangkangjian
     * @date 2018-08-08 17:19:17
     */
    FindAllocateApplyDTO findDetail(String applyNo);
    /**
     * 查询申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    Page<QueryAllocateApplyListDTO> findApplyList(String productType,String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
    /**
     * 查询处理申请列表
     * @param
     * @exception
     * @return Page<QueryAllocateApplyListDTO> 分页的信息
     * @author zhangkangjian
     * @date 2018-08-09 10:36:34
     */
    Page<QueryAllocateApplyListDTO> findProcessApplyList(String productType,String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
    /**
     * 处理申请单
     * @param
     * @exception
     * @return
     * @author zhangkangjian
     * @date 2018-08-10 11:24:53
     */
    void processApply(ProcessApplyDTO processApplyDTO);
    /**
     * 查询可调拨库存列表
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    Page<FindStockListDTO> findStockList(FindStockListDTO findStockListDTO);

    /**
     * 修改申请单状态
     * @param applyNo 申请单号
     * @param status 申请单状态
     * @author liuduo
     * @date 2018-08-10 13:44:37
     */
    void updateApplyOrderStatus(String applyNo, String status);

    /**
     * 根据申请单状态查询申请单
     * @param applyNoStatus 申请单状态
     * @return 状态为等待收货的申请单
     * @author liuduo
     * @date 2018-08-11 12:56:39
     */
    List<String> queryApplyNo(String applyNoStatus, String orgCode, String productType);
    /**
     * 查询可调拨库存列表
     * @param orgDTO 查询条件
     * @return StatusDtoThriftPage<QueryOrgDTO>
     * @author zhangkangjian
     * @date 2018-08-13 17:19:54
     */
    Page<QueryOrgDTO> queryTransferStock(QueryOrgDTO orgDTO,Integer offset,Integer pageSize);
    /**
     *  
     * @param 
     * @exception 
     * @return 
     * @author zhangkangjian
     * @date 2018-08-15 14:10:42
     */
    StatusDto<List<ProductStockInfoDTO>> checkStockQuantity(CheckStockQuantityDTO checkStockQuantityDTO);

    /**
     * 撤销申请单
     * @param applyNo 申请单号
     * @return StatusDto
     * @author weijb
     * @date 2018-08-20 12:02:58
     */
    StatusDto cancelApply(String applyNo);
    /**
     *  根据类型查询服务中心的code
     * @param type 机构的类型
     * @return List<String> 机构的code
     * @author zhangkangjian
     * @date 2018-08-20 16:17:55
     */
    List<String> getOrgCodesByOrgType(String type);
    /**
     * 问题件申请查询(创建问题件，查询问题件列表)
     * @param orgCode 机构的code
     * @return StatusDto<List<StockBizStockDetailDTO>>
     * @author zhangkangjian
     * @date 2018-08-22 14:37:40
     */
    List<StockBizStockDetailDTO> queryProblemStockList(String orgCode, String productType);
    /**
     * 查询售后平台的信息
     * @return StatusDto<BasicUserOrganization>
     * @author zhangkangjian
     * @date 2018-08-23 11:12:47
     */
    List<BasicUserOrganization> queryTopPlatform();
}
