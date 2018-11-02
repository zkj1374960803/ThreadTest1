package com.ccbuluo.business.platform.maintain.service;

import com.ccbuluo.business.constants.CommonStatusEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintaingroup;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.BizServiceorderDetail;
import com.ccbuluo.business.platform.claimorder.dao.ClaimOrderDao;
import com.ccbuluo.business.platform.maintain.dao.BizServiceMaintaingroupDao;
import com.ccbuluo.business.platform.maintain.dto.SaveBizServiceMaintaingroup;
import com.ccbuluo.business.platform.maintain.dto.SaveMaintaintemDTO;
import com.ccbuluo.business.platform.maintain.dto.SaveMerchandiseDTO;
import com.ccbuluo.business.platform.maintain.dto.SearchBizServiceMaintaingroup;
import com.ccbuluo.business.platform.order.dao.BizServiceorderDetailDao;
import com.ccbuluo.business.platform.order.dto.ProductDetailDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 保养service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-10-30 10:13:44
 */
@Service
public class MainTainServiceImpl implements MainTainService{

    @Autowired
    private GenerateProjectCodeService generateProjectCodeService;
    @Autowired
    private BizServiceMaintaingroupDao bizServiceMaintaingroupDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private BizServiceorderDetailDao bizServiceorderDetailDao;
    @Autowired
    private ClaimOrderDao  claimOrderDao;

    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 保存保养单
     * @param bizServiceMaintaingroup 保养单信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-10-30 10:34:10
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto saveMainTain(SaveBizServiceMaintaingroup bizServiceMaintaingroup) {
        try {
            // 生成保养单号
            String code = null;
            StatusDto<String> order = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FF);
            if (order.getCode().equals(Constants.SUCCESS_CODE)) {
                code = order.getData();
            } else {
                return StatusDto.buildFailure("生成保养单编码失败！");
            }
            // 保存保养单
            bizServiceMaintaingroup.setGroupCode(code);
            bizServiceMaintaingroup.setGroupStatus(CommonStatusEnum.ENABLE.name());
            bizServiceMaintaingroup.preInsert(userHolder.getLoggedUserId());
            bizServiceMaintaingroupDao.saveEntity(bizServiceMaintaingroup);
            // 保存工时
            saveMaintaintem(bizServiceMaintaingroup);
            // 保存零配件
            saveMerchandise(bizServiceMaintaingroup);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("保存保养套餐失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 编辑保养单
     * @param bizServiceMaintaingroup 保养单信息
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-10-30 10:34:10
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editMainTain(SaveBizServiceMaintaingroup bizServiceMaintaingroup) {
        try {
            // 编辑保养套餐
            bizServiceMaintaingroup.preUpdate(userHolder.getLoggedUserId());
            bizServiceMaintaingroupDao.update(bizServiceMaintaingroup);
            // 删除原有工时和零配件
            bizServiceorderDetailDao.deleteByOrderNo(bizServiceMaintaingroup.getGroupCode());
            // 保存工时
            saveMaintaintem(bizServiceMaintaingroup);
            // 保存零配件
            saveMerchandise(bizServiceMaintaingroup);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("编辑保养套餐失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 修改保修套餐状态
     * @param groupNo 保修套餐编号
     * @param groupStatus 保修套餐状态
     * @return 修改是否成功
     * @author liuduo
     * @date 2018-10-30 14:20:22
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatusDto editStatus(String groupNo, String groupStatus) {
        try {
            bizServiceMaintaingroupDao.editStatus(groupNo, groupStatus);
            return StatusDto.buildSuccessStatusDto();
        } catch (Exception e) {
            logger.error("编辑保养套餐失败！", e.getMessage());
            throw e;
        }
    }

    /**
     * 查询保养套餐列表
     * @param groupStatus 保养套餐状态
     * @param keyword 保养套餐编号或名字
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 保养列表
     * @author liuduo
     * @date 2018-10-30 14:32:21
     */
    @Override
    public StatusDto<Page<BizServiceMaintaingroup>> list(String groupStatus, String keyword, Integer offset, Integer pageSize) {
        return StatusDto.buildDataSuccessStatusDto(bizServiceMaintaingroupDao.list(groupStatus, keyword, offset, pageSize));
    }

    /**
     * 根据保养套餐编号查询保养套餐详情
     * @param groupNo 保养套餐编号
     * @return 保养详情
     * @author liuduo
     * @date 2018-10-30 15:16:26
     */
    @Override
    public StatusDto<SearchBizServiceMaintaingroup> getDetailByGroupNo(String groupNo) {
        // 查询保养套餐
        SaveBizServiceMaintaingroup byGroupNo = bizServiceMaintaingroupDao.getByGroupNo(groupNo);
        SearchBizServiceMaintaingroup searchBizServiceMaintaingroup = new SearchBizServiceMaintaingroup();
        searchBizServiceMaintaingroup.setGroupCode(byGroupNo.getGroupCode());
        searchBizServiceMaintaingroup.setGroupName(byGroupNo.getGroupName());
        searchBizServiceMaintaingroup.setGroupPrice(byGroupNo.getGroupPrice());
        searchBizServiceMaintaingroup.setGroupStatus(byGroupNo.getGroupStatus());
        searchBizServiceMaintaingroup.setGroupImage(byGroupNo.getGroupImage());
        // 查询工时
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setProductType(BizServiceorderDetail.ProductTypeEnum.MAINTAINITEM.name());
        productDetailDTO.setServiceOrderno(groupNo);
        List<ProductDetailDTO> maintainitemDetail = claimOrderDao.queryMaintainitemDetail(productDetailDTO);
        searchBizServiceMaintaingroup.setSaveMaintaintemDTOS(maintainitemDetail);
        // 查询零配件
        ProductDetailDTO productDetailDTO2 = new ProductDetailDTO();
        productDetailDTO2.setProductType(BizServiceorderDetail.ProductTypeEnum.FITTING.name());
        productDetailDTO2.setServiceOrderno(groupNo);
        List<ProductDetailDTO> maintainitemDetail2 = claimOrderDao.queryMaintainitemDetail(productDetailDTO2);
        searchBizServiceMaintaingroup.setSaveMerchandiseDTOS(maintainitemDetail2);
        return StatusDto.buildDataSuccessStatusDto(searchBizServiceMaintaingroup);
    }


    /**
     * 保存工时
     * @param bizServiceMaintaingroup 保养单
     * @author liuduo
     * @date 2018-10-30 11:23:37
     */
    private void saveMaintaintem(SaveBizServiceMaintaingroup bizServiceMaintaingroup) {
        List<SaveMaintaintemDTO> saveMaintaintemDTOS = bizServiceMaintaingroup.getSaveMaintaintemDTOS();
        if (!saveMaintaintemDTOS.isEmpty()) {
            List<SaveMaintaintemDTO> saveMaintaintemDTOS1 = Lists.newArrayList();
            saveMaintaintemDTOS.forEach(item -> {
                if (StringUtils.isBlank(item.getServiceOrgno())) {
                    item.setServiceOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
                    item.setServiceOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
                    item.setServiceUserid(userHolder.getLoggedUserId());
                    item.setServiceUsername(userHolder.getLoggedUser().getName());
                }
                item.preInsert(userHolder.getLoggedUserId());
                item.setServiceOrderno(bizServiceMaintaingroup.getGroupCode());
                saveMaintaintemDTOS1.add(item);
            });
            bizServiceorderDetailDao.saveMaintaintem(saveMaintaintemDTOS1);
        }
    }

    /**
     * 保存零配件
     * @param bizServiceMaintaingroup 保养单
     * @author liuduo
     * @date 2018-10-30 11:40:44
     */
    private void saveMerchandise(SaveBizServiceMaintaingroup bizServiceMaintaingroup) {
        List<SaveMerchandiseDTO> saveMerchandiseDTOS = bizServiceMaintaingroup.getSaveMerchandiseDTOS();
        if (!saveMerchandiseDTOS.isEmpty()) {
            List<SaveMerchandiseDTO> saveMerchandiseDTOS1 = Lists.newArrayList();
            saveMerchandiseDTOS.forEach(item -> {
                if (StringUtils.isBlank(item.getServiceOrgno())) {
                    item.setServiceOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
                    item.setServiceOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
                    item.setServiceUserid(userHolder.getLoggedUserId());
                    item.setServiceUsername(userHolder.getLoggedUser().getName());
                }
                item.preInsert(userHolder.getLoggedUserId());
                item.setServiceOrderno(bizServiceMaintaingroup.getGroupCode());
                saveMerchandiseDTOS1.add(item);
            });
            bizServiceorderDetailDao.saveMerchandise(saveMerchandiseDTOS1);
        }
    }
}
