package com.ccbuluo.business.platform.instock.service;

import com.ccbuluo.business.entity.BizInstockorderDetail;
import com.ccbuluo.business.platform.instock.dao.BizInstockorderDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 入库单详情单service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-07 20:23:41
 */
@Service
public class InstockorderDetailServiceImpl implements InstockorderDetailService{

    @Autowired
    private BizInstockorderDetailDao bizInstockorderDetailDao;
    /**
     * 保存入库单详单
     * @param bizInstockorderDetailList 入库单详单
     * @return 保存时否成功
     * @author liuduo
     * @date 2018-08-07 20:25:37
     */
    @Override
    public List<Long> save(List<BizInstockorderDetail> bizInstockorderDetailList) {
        return bizInstockorderDetailDao.saveInstockorderDetail(bizInstockorderDetailList);
    }
}
