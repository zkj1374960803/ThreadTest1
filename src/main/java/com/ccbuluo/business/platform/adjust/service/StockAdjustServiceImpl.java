package com.ccbuluo.business.platform.adjust.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.constants.DocCodePrefixEnum;
import com.ccbuluo.business.entity.BizServiceEquiptype;
import com.ccbuluo.business.entity.BizStockAdjust;
import com.ccbuluo.business.entity.BizStockAdjustdetail;
import com.ccbuluo.business.entity.BizStockDetail;
import com.ccbuluo.business.platform.adjust.dao.BizStockAdjustDao;
import com.ccbuluo.business.platform.adjust.dao.BizStockAdjustdetailDao;
import com.ccbuluo.business.platform.adjust.dto.SaveBizStockAdjustDTO;
import com.ccbuluo.business.platform.adjust.dto.SearchStockAdjustListDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustDetailDTO;
import com.ccbuluo.business.platform.adjust.dto.StockAdjustListDTO;
import com.ccbuluo.business.platform.equipment.dto.DetailBizServiceEquipmentDTO;
import com.ccbuluo.business.platform.equipment.service.EquipmentService;
import com.ccbuluo.business.platform.equipment.service.EquiptypeService;
import com.ccbuluo.business.platform.projectcode.service.GenerateDocCodeService;
import com.ccbuluo.business.platform.stockdetail.service.StockDetailService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.json.JsonUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import com.ccbuluo.usercoreintf.dto.OrgWorkplaceDTO;
import com.ccbuluo.usercoreintf.dto.QueryNameByUseruuidsDTO;
import com.ccbuluo.usercoreintf.model.BasicUserOrganization;
import com.ccbuluo.usercoreintf.service.BasicUserOrganizationService;
import com.ccbuluo.usercoreintf.service.InnerUserInfoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 盘库service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-14 16:16:19
 */
@Service
public class StockAdjustServiceImpl implements StockAdjustService{

     Logger logger = LoggerFactory.getLogger(getClass());
     @Autowired
     private EquiptypeService equiptypeService;
     @Autowired
     private EquipmentService equipmentService;
     @Autowired
     private StockDetailService stockDetailService;
     @Autowired
     private GenerateDocCodeService generateDocCodeService;
     @Autowired
     private BizStockAdjustDao bizStockAdjustDao;
     @Autowired
     private UserHolder userHolder;
     @ThriftRPCClient("BasicMerchandiseSer")
     CarpartsProductService carpartsProductService;
     @ThriftRPCClient("UserCoreSerService")
     private BasicUserOrganizationService orgService;
    @ThriftRPCClient("UserCoreSerService")
    private InnerUserInfoService innerUserInfoService;
    @Autowired
    private BizStockAdjustdetailDao bizStockAdjustdetailDao;


     /**
      * 查询物料类型集合
      * @return 物料类型集合
      * @author liuduo
      * @date 2018-08-14 16:36:23
      */
     @Override
     public List<BizServiceEquiptype> queryEquipmentType() {
          return equiptypeService.queryList();
     }

     /**
      * 根据物料类型id查询物料
      * @param equipTypeId 物料类型id
      * @return 物料
      * @author liuduo
      * @date 2018-08-14 16:41:20
      */
     @Override
     public List<DetailBizServiceEquipmentDTO> queryEquipmentByType(Long equipTypeId) {
          return equipmentService.queryEqupmentByEquiptype(equipTypeId);
     }

     /**
      * 查询新增物料盘库时用的列表
      * @param equipTypeId 物料类型id
      * @param equipmentcode 物料code
      * @return 新增物料盘库时用的列表
      * @author liuduo
      * @date 2018-08-14 17:43:53
      */
     @Override
     public List<StockAdjustListDTO> queryAdjustList(Long equipTypeId, String equipmentcode, String productType) {
         String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
         // 若物料类型不为空，物料为空，则根据物料类型查询物料
          if (null != equipTypeId && StringUtils.isBlank(equipmentcode)) {
               List<DetailBizServiceEquipmentDTO> detailBizServiceEquipmentDTOS = queryEquipmentByType(equipTypeId);
               List<String> equipmentCodes = detailBizServiceEquipmentDTOS.stream().map(DetailBizServiceEquipmentDTO::getEquipCode).collect(Collectors.toList());
               if (null != equipmentCodes && equipmentCodes.size() > 0) {
                    return stockDetailService.queryAdjustList(equipmentCodes, orgCode, productType);
               }
               return Lists.newArrayList();
          } else if (StringUtils.isNotBlank(equipmentcode)) {
               return stockDetailService.queryAdjustList(Lists.newArrayList(equipmentcode), orgCode, productType);
          }
          return stockDetailService.queryAdjustList(Lists.newArrayList(), orgCode, productType);
     }

     /**
      * 保存盘库
      * @param saveBizStockAdjustDTO 盘库实体
      * @return 保存是否成功
      * @author liuduo
      * @date 2018-08-14 19:17:37
      */
     @Override
     @Transactional(rollbackFor = Exception.class)
     public StatusDto save(SaveBizStockAdjustDTO saveBizStockAdjustDTO) {
          try {
               // 1、保存盘库单
               // 生成盘库单号
               String adjustNo = null;
               StatusDto<String> outstockCode = generateDocCodeService.grantCodeByPrefix(DocCodePrefixEnum.PK);
               if (outstockCode.getCode().equals(Constants.SUCCESS_CODE)) {
                    adjustNo = outstockCode.getData();
               } else {
                    return StatusDto.buildFailure("生成盘库单编码失败！");
               }
              int saveAdjustStatus = saveStockAdjust(saveBizStockAdjustDTO, adjustNo);
               if (saveAdjustStatus == Constants.FAILURESTATUS) {
                   throw new CommonException("10001", "生成盘库单失败！");
               }
              // 2、保存盘库单详单
              List<Long> ids = saveAdjustDetail(saveBizStockAdjustDTO, adjustNo);
              if (null == ids && ids.size() == 0) {
                  return StatusDto.buildFailure("保存失败！");
              }
              return StatusDto.buildSuccessStatusDto("保存成功！");
          } catch (Exception e) {
               logger.error("保存盘库单失败！", e.getMessage());
               throw e;
          }

     }

    /**
     * 查询新增零配件盘库时用的列表
     * @param categoryCode 零配件分类code
     * @param productCode 商品code
     * @return 新增零配件盘库时用的列表
     * @author liuduo
     * @date 2018-08-15 09:23:53
     */
    @Override
    public List<StockAdjustListDTO> queryAdjustListByCategoryCode(String categoryCode, String productCode, String productType) {
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 分类不为空，零配件为空
        if (StringUtils.isNotBlank(categoryCode) && StringUtils.isBlank(productCode)) {
            List<BasicCarpartsProductDTO> basicCarpartsProductDTOS = carpartsProductService.queryCarpartsProductListByCategoryCode(categoryCode);
            List<String> carparts = basicCarpartsProductDTOS.stream().map(BasicCarpartsProductDTO::getCarpartsCode).collect(Collectors.toList());
            // 零配件为空，则直接根据分类code查询下面的商品进行查询
            if (null != carparts && carparts.size() > 0) {
                return stockDetailService.queryAdjustList(carparts, orgCode, productType);
            }
            return stockDetailService.queryAdjustList(Lists.newArrayList(), orgCode, productType);
        } else if (StringUtils.isNotBlank(productCode)) {
            return stockDetailService.queryAdjustList(Lists.newArrayList(productCode), orgCode, productType);
        }
        return stockDetailService.queryAdjustList(Lists.newArrayList(), orgCode, productType);
    }


    /**
     * 查询盘库单列表
     * @param adjustResult 盘库结果
     * @param adjustSource 盘库单来源
     * @param keyWord 关键字（盘库单号/服务中心/客户经理）
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 盘库单列表
     * @author liuduo
     * @date 2018-08-15 11:03:46
     */
    @Override
    public Page<SearchStockAdjustListDTO> queryAdjustStockList(Integer adjustResult, String adjustSource, String keyWord, String productType, Integer offset, Integer pageSize) {
        // 获取当前登录人的机构
        String orgCode = userHolder.getLoggedUser().getOrganization().getOrgCode();
        // 先根据keyword查询服务中心或者客户经理是否存在
        if (StringUtils.isNotBlank(keyWord)) {
            StatusDtoThriftList<String> stringStatusDtoThriftList = orgService.queryOrgNameByOrgCode(keyWord);
            List<String> codes = StatusDtoThriftUtils.resolve(stringStatusDtoThriftList, String.class).getData();
            String join = StringUtils.join(codes, ",");
            if (!codes.isEmpty()) {
                return editAdjustOrder(adjustResult, adjustSource, offset, pageSize, join, orgCode, productType);
            } else {
                return editAdjustOrder(adjustResult, adjustSource, offset, pageSize, keyWord, orgCode, productType);
            }
        }
        return editAdjustOrder(adjustResult, adjustSource, offset, pageSize, keyWord, orgCode, productType);
    }

    /**
     * 根据盘库单号查询盘库详情
     * @param adjustNo 盘库单号
     * @return 盘库详情
     * @author liuduo
     * @date 2018-08-15 14:37:37
     */
    @Override
    public StockAdjustDetailDTO getByAdjustNo(String adjustNo) {
        // 查询盘库单
        StockAdjustDetailDTO stockAdjustDetailDTO = bizStockAdjustDao.getByAdjustNo(adjustNo);
        String adjustOrgno = stockAdjustDetailDTO.getAdjustOrgno();
        String adjustUserid = stockAdjustDetailDTO.getAdjustUserid();
        StatusDtoThriftBean<OrgWorkplaceDTO> byCode = orgService.getByCode(adjustOrgno);
        OrgWorkplaceDTO data = StatusDtoThriftUtils.resolve(byCode, OrgWorkplaceDTO.class).getData();
        // 设置机构
        stockAdjustDetailDTO.setAdjustDocName(data.getOrgName());
        StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuids = innerUserInfoService.queryNameByUseruuids(Lists.newArrayList(adjustUserid));
        List<QueryNameByUseruuidsDTO> data1 = StatusDtoThriftUtils.resolve(queryNameByUseruuids, QueryNameByUseruuidsDTO.class).getData();
        Map<String, String> collect = data1.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, QueryNameByUseruuidsDTO::getName));
        // 设置盘库人
        stockAdjustDetailDTO.setAdjustName(collect.get(stockAdjustDetailDTO.getAdjustUserid()));
        // 查询详情
        List<StockAdjustListDTO> stockAdjustListDTOList = bizStockAdjustdetailDao.getByAdjustNo(adjustNo);
        stockAdjustListDTOList.forEach(item -> {
            if (item.getActualNum().equals(item.getDueNum())) {
                item.setDifferenceNum(0);
            } else {
                item.setDifferenceNum(item.getActualNum() - item.getDueNum());
            }
        });
        stockAdjustDetailDTO.setStockAdjustListDTOList(stockAdjustListDTOList);
        return stockAdjustDetailDTO;
    }

    /**
     * 盘库单列表数据组装
     * @param adjustResult 盘库结果
     * @param adjustSource 盘库单来源
     * @param keyWord 关键字（盘库单号/服务中心/客户经理）
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 盘库单列表
     * @author liuduo
     * @date 2018-08-15 11:03:46
     */
    private Page<SearchStockAdjustListDTO> editAdjustOrder(Integer adjustResult, String adjustSource, Integer offset, Integer pageSize, String keyWord, String orgCode, String productType) {
        Page<SearchStockAdjustListDTO> adjustListDTOPage = bizStockAdjustDao.queryAdjustStockList(adjustResult, keyWord, offset, pageSize, orgCode, productType);
        if (null != adjustListDTOPage && null != adjustListDTOPage.getRows()) {
            List<SearchStockAdjustListDTO> rows = adjustListDTOPage.getRows();
            List<String> orgCodes = rows.stream().map(SearchStockAdjustListDTO::getAdjustOrgno).distinct().collect(Collectors.toList());
            List<String> uuids = rows.stream().map(SearchStockAdjustListDTO::getAdjustUserid).distinct().collect(Collectors.toList());
            if (orgCodes.size() == 0 || uuids.size() == 0) {
                return new Page<>();
            }
            // 获取组织机构或者客户经理名字
            Map<String, BasicUserOrganization> stringBasicUserOrganizationMap = orgService.queryOrganizationByOrgCodes(orgCodes);
            StatusDtoThriftList<QueryNameByUseruuidsDTO> queryNameByUseruuidsDTOStatusDtoThriftList = innerUserInfoService.queryNameByUseruuids(uuids);
            List<QueryNameByUseruuidsDTO> userList = StatusDtoThriftUtils.resolve(queryNameByUseruuidsDTOStatusDtoThriftList, QueryNameByUseruuidsDTO.class).getData();
            Map<String, String> userMap = userList.stream().collect(Collectors.toMap(QueryNameByUseruuidsDTO::getUseruuid, QueryNameByUseruuidsDTO::getName));
            rows.forEach(item -> {
                item.setAdjustDocName(stringBasicUserOrganizationMap.get(item.getAdjustOrgno()).getOrgName());
                item.setAdjustName(userMap.get(item.getAdjustUserid()));
                item.setAdjustSourceType(stringBasicUserOrganizationMap.get(item.getAdjustOrgno()).getOrgType());
            });
            if (StringUtils.isNotBlank(adjustSource)) {
                List<SearchStockAdjustListDTO> adjustList = Lists.newArrayList();
                Integer total = 0;
                for (SearchStockAdjustListDTO row : rows) {
                    if (adjustSource.equals(row.getAdjustSourceType())) {
                        adjustList.add(row);
                        total ++;
                    }
                }
                adjustListDTOPage.setRows(adjustList);
                adjustListDTOPage.setTotal(total);
                int totalPage = total / pageSize;
                if (total % pageSize != 0) {
                    ++totalPage;
                }
                adjustListDTOPage.setTotalPage((long)totalPage);
            }
            return adjustListDTOPage;
        }
        return new Page<>();
    }

    /**
     * 保存盘库单详单
     * @param saveBizStockAdjustDTO 盘库单
     * @param adjustNo 盘库单号
     * @return 保存成功过的id
     * @author liuduo
     * @date 2018-08-15 09:25:13
     */
    private List<Long> saveAdjustDetail(SaveBizStockAdjustDTO saveBizStockAdjustDTO, String adjustNo) {
        List<BizStockAdjustdetail> bizStockAdjustdetailList = saveBizStockAdjustDTO.getBizStockAdjustdetailList();
        List<BizStockDetail> bizStockDetailList1 = Lists.newArrayList();
        bizStockAdjustdetailList.forEach(item -> {
            if (!item.getPerfectNum().equals(item.getActualNum())) {
                List<Map<String, Long>> list = Lists.newArrayList();
                // 根据商品编号查询库存，和盘库后的实际数量做对比，进行记录
                List<BizStockDetail> bizStockDetailList = stockDetailService.queryStockDetailByProductNo(item.getProductNo(), userHolder.getLoggedUser().getOrganization().getOrgCode());
                // 相差的数量=实际数量-应有数量
                long differenceNum = item.getActualNum() - item.getPerfectNum();
                for (BizStockDetail bizStockDetail : bizStockDetailList) {
                    BizStockDetail bizStockDetail1 = new BizStockDetail();
                    // 库存中的有效数量
                    Long validStock = bizStockDetail.getValidStock();
                    // 如果现有库存大于盘库之后输入的库存
                    if ((validStock + differenceNum) >= 0) {
                        Map<String, Long> map = Maps.newHashMap();
                        map.put("stockId", bizStockDetail.getId());
                        map.put("differenceNum", differenceNum);
                        list.add(map);
                        //　影响库存为 validStock + differenceNum
                        bizStockDetail1.setId(bizStockDetail.getId());
                        bizStockDetail1.setValidStock(validStock + differenceNum);
                        bizStockDetail1.setVersionNo(bizStockDetail.getVersionNo() + Constants.LONG_FLAG_ONE);
                        bizStockDetail1.preUpdate(userHolder.getLoggedUserId());
                        bizStockDetailList1.add(bizStockDetail1);
                        break;
                    } else {
                        Map<String, Long> map = Maps.newHashMap();
                        map.put("stockId", bizStockDetail.getId());
                        map.put("differenceNum", validStock * -1);
                        list.add(map);
                        differenceNum = differenceNum + validStock;
                        // 影响库存 为 0
                        bizStockDetail1.setId(bizStockDetail.getId());
                        bizStockDetail1.setValidStock(Constants.LONG_FLAG_ZERO);
                        bizStockDetail1.setVersionNo(bizStockDetail.getVersionNo() + Constants.LONG_FLAG_ONE);
                        bizStockDetail1.preUpdate(userHolder.getLoggedUserId());
                        bizStockDetailList1.add(bizStockDetail1);
                    }
                }
                // 把list转为json 作为  影响的库存情况  的值
                String affectStockid = JsonUtils.writeValue(list);
                item.setAffectStockid(affectStockid);
            }
            item.setAdjustDocno(adjustNo);
            item.preInsert(userHolder.getLoggedUserId());
        });
        // 修改库存中的有效库存
        stockDetailService.updateAdjustValidStock(bizStockDetailList1);
        // 保存盘库详单
        return bizStockAdjustdetailDao.saveAdjustDetail(bizStockAdjustdetailList);
    }


    /**
     *
     * @param saveBizStockAdjustDTO 盘库单的盘库单详单
     * @param adjustNo 盘库单号
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-08-14 20:04:55
     */
    private int saveStockAdjust(SaveBizStockAdjustDTO saveBizStockAdjustDTO, String adjustNo) {
        BizStockAdjust bizStockAdjust = new BizStockAdjust();
        bizStockAdjust.setAdjustDocno(adjustNo);
        bizStockAdjust.setAdjustOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizStockAdjust.setAdjustUserid(userHolder.getLoggedUserId());
        bizStockAdjust.setAdjustTime(new Date());
        bizStockAdjust.setAdjustReson(saveBizStockAdjustDTO.getAdjustReson());
        bizStockAdjust.preInsert(userHolder.getLoggedUserId());
        List<BizStockAdjustdetail> bizStockAdjustdetailList = saveBizStockAdjustDTO.getBizStockAdjustdetailList();
        if (null != bizStockAdjustdetailList && bizStockAdjustdetailList.size() > 0) {
            Long count = bizStockAdjustdetailList.stream().filter(item -> !item.getActualNum().equals(item.getPerfectNum())).count();
            bizStockAdjust.setAdjustResult(count);
        }
        return bizStockAdjustDao.saveEntity(bizStockAdjust);
    }

}
