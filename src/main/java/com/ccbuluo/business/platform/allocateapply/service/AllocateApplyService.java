package com.ccbuluo.business.platform.allocateapply.service;

import com.ccbuluo.business.custmanager.allocateapply.dto.QueryPendingMaterialsDTO;
import com.ccbuluo.business.platform.allocateapply.dto.*;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateApplyDTO;
import com.ccbuluo.business.platform.stockdetail.dto.StockBizStockDetailDTO;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.usercoreintf.dto.QueryOrgDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    Page<QueryAllocateApplyListDTO> findApplyList(String productType, String orgType, String processType, String applyStatus, String applyNo, Integer offset, Integer pageSize);
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
    List<String> queryApplyNo(List<String> status, String orgCode, String productType, Integer stockType);
    /**
     * 查询可调拨库存列表
     * @param orgDTO 查询条件
     * @param productNo
     * @return StatusDtoThriftPage<QueryOrgDTO>
     * @author zhangkangjian
     * @date 2018-08-13 17:19:54
     */
    Page<QueryOrgDTO> queryTransferStock(QueryOrgDTO orgDTO, String productNo, Integer offset, Integer pageSize);
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

    /**
     * 根据物料code查询物料是否被申请
     * @param equipCode 物料code
     * @return 物料是否被申请
     * @author liuduo
     * @date 2018-08-23 16:01:38
     */
    Boolean getEquipMent(String equipCode);
    /**
     * 查询客户经理关联的服务中心
     * @param useruuid
     * @return StatusDto<Map<String,String>>
     * @author zhangkangjian
     * @date 2018-08-24 17:37:13
     */
    Map<String, String> findCustManagerServiceCenter(String useruuid) throws IOException;

    /**
     * 查询客户经理待领取的物料
     * @param completeStatus 状态（DOING待领取，COMPLETE已领取）
     * @param keyword 物料编号/物料名称
     * @param offset 偏移量
     * @param pageSize 每页显示的数量
     * @return Page<QueryPendingMaterialsDTO>
     * @author zhangkangjian
     * @date 2018-08-25 20:40:35
     */
    Page<QueryPendingMaterialsDTO> queryPendingMaterials(String completeStatus, String keyword, Integer offset, Integer pageSize);

    /**
     * 客户经理领取物料
     * @param id 入库计划的id
     * @param productNo 商品的编号
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-08-27 14:53:10
     */
    void receivingmaterials(Long id, String productNo);
    /**
     * 查询当前登陆人的物料库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-31 14:47:54
     */
    Page<FindStockListDTO> findStockListByOrgCode(FindStockListDTO findStockListDTO);

    /**
     * 查看所有零配件调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-10 15:45:56
     */
    Page<FindStockListDTO> findAllStockList(FindStockListDTO findStockListDTO);

    /**
     * 查看所有物料调拨库存
     * @param findStockListDTO 查询条件
     * @return StatusDto<Page<FindStockListDTO>>
     * @author zhangkangjian
     * @date 2018-08-31 14:47:54
     */
    Page<FindStockListDTO> findAllEquipmentStockList(FindStockListDTO findStockListDTO);
    /**
     *  维修单检查库存
     * @param checkRepairOrderStockDTO
     * @return StatusDto<List<ProductStockInfoDTO>>
     * @author zhangkangjian
     * @date 2018-09-11 16:21:56
     */
    StatusDto<List<ProductStockInfoDTO>> checkRepairOrderStock(CheckRepairOrderStockDTO checkRepairOrderStockDTO);
    /**
     * 保存申请单数据
     * @param processApplyDTO 申请单实体
     * @author zhangkangjian
     * @date 2018-09-12 16:07:20
     */
    void saveProcessApply(List<ProcessApplyDetailDTO> processApplyDTO);
    /**
     * 驳回申请
     * @param applyNo 申请单号
     * @param processMemo 驳回理由
     * @author zhangkangjian
     * @date 2018-09-12 16:16:17
     */
    void rejectApply(String applyNo, String processMemo);
    /**
     * 处理申请
     * @param applyNo 申请单号
     * @param outstockOrgno 出库机构
     * @author zhangkangjian
     * @date 2018-09-12 17:47:14
     */
    void processOutStockOrg(String applyNo, String outstockOrgno);
    /**
     * 查询采购列表
     * @param queryPurchaseListDTO 查询条件
     * @return Page<QueryPurchaseListDTO>
     * @author zhangkangjian
     * @date 2018-09-13 09:58:35
     */
    Page<QueryPurchaseListDTO> queryPurchaseLise(QueryPurchaseListDTO queryPurchaseListDTO);

    /**
     * 创建采购单
     * @param createPurchaseBillDTO
     * @author zhangkangjian
     * @date 2018-09-13 13:58:37
     */
    void createPurchaseBill(CreatePurchaseBillDTO createPurchaseBillDTO);
}
