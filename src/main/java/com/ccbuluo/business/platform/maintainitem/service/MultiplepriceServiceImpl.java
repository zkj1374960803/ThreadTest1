package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceLog;
import com.ccbuluo.business.entity.BizServiceMultipleprice;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMultiplepriceDao;
import com.ccbuluo.business.platform.maintainitem.dto.CorrespondAreaDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMultiplepriceDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.servicelog.service.ServiceLogService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地区倍数service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-07-18 10:23:24
 */
@Service
public class MultiplepriceServiceImpl implements MultiplepriceService{

    @Autowired
    private BizServiceMultiplepriceDao bizServiceMultiplepriceDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private GenerateProjectCodeService generateProjectCodeService;
    @Autowired
    private ServiceLogService serviceLogService;

    /**
     * 保存地区倍数
     * @param saveBizServiceMultiplepriceDTO 地区倍数dto
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 13:59:55
     */
    @Override
    public int save(SaveBizServiceMultiplepriceDTO saveBizServiceMultiplepriceDTO) {
        List<BizServiceMultipleprice> bizServiceMultiplepriceList = new ArrayList<>();
        // 根据服务项code查询已有的地区
        List<String> cityCodeList = bizServiceMultiplepriceDao.queryAreaByCode(saveBizServiceMultiplepriceDTO.getMaintainitemCode());
        if (cityCodeList.size() > 0
            && saveBizServiceMultiplepriceDTO != null
            && saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().size() > 0) {
            List<CorrespondAreaDTO> multiplepriceDTOList = saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList();
            // 获取新的地区
            List<String> newCityCodeList = multiplepriceDTOList.stream().map(a -> a.getCityCode()).collect(Collectors.toList());
            // 获取交集（需要修改的）
            List<String> needUpdate = cityCodeList.stream().filter(item -> newCityCodeList.contains(item)).collect(Collectors.toList());
            // 获取差集（需要新增的）
            List<String> needInsert = newCityCodeList.stream().filter(item -> !cityCodeList.contains(item)).collect(Collectors.toList());
            // 更新
            for (String cityCode : needUpdate) {
                for (CorrespondAreaDTO correspondAreaDTO : multiplepriceDTOList) {
                    if (cityCode.equals(correspondAreaDTO.getCityCode())) {
                        BizServiceMultipleprice bizServiceMultipleprice = new BizServiceMultipleprice();
                        bizServiceMultipleprice.setMultiple(saveBizServiceMultiplepriceDTO.getMultiple());
                        bizServiceMultipleprice.setCityCode(correspondAreaDTO.getCityCode());
                        bizServiceMultipleprice.setMaintainitemCode(saveBizServiceMultiplepriceDTO.getMaintainitemCode());
                        bizServiceMultipleprice.setDeleteFlag(Constants.DELETE_FLAG_NORMAL);
                        bizServiceMultiplepriceDao.update(bizServiceMultipleprice);
                    }
                }
            }
            // 新增
            for (String cityCode : needInsert) {
                for (CorrespondAreaDTO correspondAreaDTO : multiplepriceDTOList) {
                    if (cityCode.equals(correspondAreaDTO.getCityCode())) {
                        BizServiceMultipleprice bizServiceMultipleprice = new BizServiceMultipleprice();
                        bizServiceMultipleprice.setMaintainitemCode(saveBizServiceMultiplepriceDTO.getMaintainitemCode());
                        bizServiceMultipleprice.setMultiple(saveBizServiceMultiplepriceDTO.getMultiple());
                        bizServiceMultipleprice.setProvinceCode(correspondAreaDTO.getProvinceCode());
                        bizServiceMultipleprice.setProvinceName(correspondAreaDTO.getProvinceName());
                        bizServiceMultipleprice.setCityCode(correspondAreaDTO.getCityCode());
                        bizServiceMultipleprice.setCityName(correspondAreaDTO.getCityName());
                        bizServiceMultipleprice.preInsert(userHolder.getLoggedUserId());
                        bizServiceMultiplepriceList.add(bizServiceMultipleprice);
                    }
                }
            }
            int[] ints = bizServiceMultiplepriceDao.saveMultipleprice(bizServiceMultiplepriceList);
            if (ints.length > 0) {
                return Constants.SUCCESSSTATUS;
            }
            return Constants.FAILURESTATUS;
        }
        // 没有查询到已有地区，直接新增
        for (int i = 0; i < saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().size(); i++) {
            BizServiceMultipleprice bizServiceMultipleprice = new BizServiceMultipleprice();
            bizServiceMultipleprice.setMaintainitemCode(saveBizServiceMultiplepriceDTO.getMaintainitemCode());
            bizServiceMultipleprice.setMultiple(saveBizServiceMultiplepriceDTO.getMultiple());
            bizServiceMultipleprice.setProvinceCode(saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().get(i).getProvinceCode());
            bizServiceMultipleprice.setProvinceName(saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().get(i).getProvinceName());
            bizServiceMultipleprice.setCityCode(saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().get(i).getCityCode());
            bizServiceMultipleprice.setCityName(saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().get(i).getCityName());
            bizServiceMultipleprice.preInsert(userHolder.getLoggedUserId());
            bizServiceMultiplepriceList.add(bizServiceMultipleprice);
        }
        int[] ints = bizServiceMultiplepriceDao.saveMultipleprice(bizServiceMultiplepriceList);
        if (ints.length > 0) {
            return Constants.SUCCESSSTATUS;
        }
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.SAVE.name());
        bizServiceLog.setSubjectType("MultiplepriceServiceImpl");
        bizServiceLog.setSubjectKeyvalue(saveBizServiceMultiplepriceDTO.getMaintainitemCode());
        bizServiceLog.setLogContent("保存地区倍数");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return Constants.FAILURESTATUS;
    }

    /**
     * 查询地区倍数列表
     * @param maintainitemCode 服务项code
     * @param provinceCode 省code
     * @param cityCode 市code
     * @param offset 起始数
     * @param pageSize 每页数
     * @return 地区倍数列表
     * @author liuduo
     * @date 2018-07-18 15:35:18
     */
    @Override
    public Page<BizServiceMultipleprice> queryList(String maintainitemCode, String provinceCode, String cityCode, Integer offset, Integer pageSize) {
        return bizServiceMultiplepriceDao.queryList(maintainitemCode,provinceCode,cityCode,offset,pageSize);
    }

    /**
     * 根据id删除地区倍数
     * @param id 地区倍数id
     * @return 删除是否成功
     * @author liuduo
     * @date 2018-07-18 16:14:46
     */
    @Override
    public int deleteById(Long id) {
        BizServiceLog bizServiceLog = new BizServiceLog();
        bizServiceLog.setModel(BizServiceLog.modelEnum.BASIC.name());
        bizServiceLog.setAction(BizServiceLog.actionEnum.DELETE.name());
        bizServiceLog.setSubjectType("MultiplepriceServiceImpl");
        bizServiceLog.setSubjectKeyvalue(String.valueOf(id));
        bizServiceLog.setLogContent("删除地区倍数");
        bizServiceLog.setOwnerOrgno(userHolder.getLoggedUser().getOrganization().getOrgCode());
        bizServiceLog.setOwnerOrgname(userHolder.getLoggedUser().getOrganization().getOrgName());
        bizServiceLog.preInsert(userHolder.getLoggedUser().getUserId());
        serviceLogService.create(bizServiceLog);
        return bizServiceMultiplepriceDao.deleteById(id);
    }


    /**
     * 根据地区和工时查询地区倍数
     * @param codes 工时编码
     * @param province 省
     * @param city 市
     * @return 地区倍数
     * @author liuduo
     * @date 2018-09-06 11:58:07
     */
    @Override
    public List<BizServiceMultipleprice> queryMultiple(List<String> codes, String province, String city) {
        return bizServiceMultiplepriceDao.queryMultiple(codes, province, city);
    }


}
