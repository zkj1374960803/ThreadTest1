package com.ccbuluo.business.platform.outstock.service;

import com.ccbuluo.business.entity.BizOutstockorderDetail;
import com.ccbuluo.business.platform.outstock.dao.BizOutstockorderDetailDao;
import com.ccbuluo.business.platform.outstock.dto.OutstockorderDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 出库单详单service实现
 * @author liuduo
 * @version v1.0.0
 * @date 2018-08-09 11:36:07
 */
@Service
public class OutstockorderDetailServiceImpl implements OutstockorderDetailService{
    @Autowired
    private BizOutstockorderDetailDao bizOutstockorderDetailDao;

    /**
     * 保存出库单详单
     * @param bizOutstockorderDetailList1 出库单详单
     * @date 2018-08-09 15:58:17
     */
    @Override
    public List<Long> saveOutstockorderDetail(List<BizOutstockorderDetail> bizOutstockorderDetailList1) {
        return bizOutstockorderDetailDao.saveOutstockorderDetail(bizOutstockorderDetailList1);
    }

    /**
     * 根据申请单号查询出库单详单
     * @param outstockNo 申请单号
     * @return 出库单详单
     * @author liuduo
     * @date 2018-08-10 14:16:32
     */
    @Override
    public List<BizOutstockorderDetail> queryByApplyNo(String outstockNo) {
        return bizOutstockorderDetailDao.queryByApplyNo(outstockNo);
    }

    /**
     * 根据出库单号查询出库单详单
     * @param outstockNo 出库单号
     * @return 出库单详单
     * @author liuduo
     * @date 2018-08-13 11:26:32
     */
    @Override
    public List<OutstockorderDetailDTO> queryListByOutstockNo(String outstockNo) {
        return bizOutstockorderDetailDao.queryListByOutstockNo(outstockNo);
    }

    /**
     * 保存出库单详单
     * @param bizOutstockorderDetailList 出库单详单
     * @return ids
     * @author liuduo
     * @date 2018-08-24 18:40:19
     */
    @Override
    public List<Long> batchBizOutstockOrderDetail(List<BizOutstockorderDetail> bizOutstockorderDetailList) {
        return bizOutstockorderDetailDao.batchBizOutstockOrderDetail(bizOutstockorderDetailList);
    }
}
