package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.entity.BizServiceMultipleprice;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMaintainitemDao;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMultiplepriceDao;
import com.ccbuluo.business.platform.maintainitem.dto.CorrespondAreaDTO;
import com.ccbuluo.business.platform.maintainitem.dto.DetailBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMaintainitemDTO;
import com.ccbuluo.business.platform.maintainitem.dto.SaveBizServiceMultiplepriceDTO;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

    /**
     * 保存地区倍数
     * @param saveBizServiceMaintainitemDTO 工时dto
     * @param maintainitemCode 工时code
     * @return 保存是否成功
     * @author liuduo
     * @date 2018-07-18 13:59:55
     */
    @Override
    public int save(String maintainitemCode, SaveBizServiceMaintainitemDTO saveBizServiceMaintainitemDTO) {
        List<BizServiceMultipleprice> bizServiceMultiplepriceList = new ArrayList<>();
       // 删除原来的地区
        bizServiceMultiplepriceDao.deleteOld(maintainitemCode);
        // 新增
        for (int i = 0; i < saveBizServiceMaintainitemDTO.getCorrespondAreaDTOList().size(); i++) {
            BizServiceMultipleprice bizServiceMultipleprice = new BizServiceMultipleprice();
            bizServiceMultipleprice.setMaintainitemCode(maintainitemCode);
            bizServiceMultipleprice.setMultiple(saveBizServiceMaintainitemDTO.getCorrespondAreaDTOList().get(i).getMultiple());
            bizServiceMultipleprice.setProvinceCode(saveBizServiceMaintainitemDTO.getCorrespondAreaDTOList().get(i).getProvinceCode());
            bizServiceMultipleprice.setProvinceName(saveBizServiceMaintainitemDTO.getCorrespondAreaDTOList().get(i).getProvinceName());
            bizServiceMultipleprice.setCityCode(saveBizServiceMaintainitemDTO.getCorrespondAreaDTOList().get(i).getCityCode());
            bizServiceMultipleprice.setCityName(saveBizServiceMaintainitemDTO.getCorrespondAreaDTOList().get(i).getCityName());
            bizServiceMultipleprice.preInsert(userHolder.getLoggedUserId());
            bizServiceMultiplepriceList.add(bizServiceMultipleprice);
        }
        int[] ints = bizServiceMultiplepriceDao.saveMultipleprice(bizServiceMultiplepriceList);
        if (ints.length > 0) {
            return Constants.SUCCESSSTATUS;
        }
        return Constants.FAILURESTATUS;
    }

    /**
     * 查询地区倍数列表
     * @param maintainitemCode 服务项code
     * @param provinceCode 省code
     * @param cityCode 市code
     * @param offset 起始数
     * @param pagesize 每页数
     * @return 地区倍数列表
     * @author liuduo
     * @date 2018-07-18 15:35:18
     */
    @Override
    public Page<BizServiceMultipleprice> queryList(String maintainitemCode, String provinceCode, String cityCode, Integer offset, Integer pagesize) {
        return bizServiceMultiplepriceDao.queryList(maintainitemCode,provinceCode,cityCode,offset,pagesize);
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
        return bizServiceMultiplepriceDao.deleteById(id);
    }


}
