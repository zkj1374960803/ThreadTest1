package com.ccbuluo.business.platform.maintainitem.service;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceMaintainitem;
import com.ccbuluo.business.entity.BizServiceMultipleprice;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMaintainitemDao;
import com.ccbuluo.business.platform.maintainitem.dao.BizServiceMultiplepriceDao;
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
import java.util.List;

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
        if (saveBizServiceMultiplepriceDTO != null && saveBizServiceMultiplepriceDTO.getMultiplepriceDTOList().size() > 0) {
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
        }
//        bizServiceMultiplepriceDao.saveMultipleprice();
        return 0;
    }
}
