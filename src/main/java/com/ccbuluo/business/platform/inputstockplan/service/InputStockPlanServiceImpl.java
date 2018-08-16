package com.ccbuluo.business.platform.inputstockplan.service;


import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.outstock.dto.updatePlanStatusDTO;
import com.ccbuluo.http.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 入库计划
 *
 * @author weijb
 * @version v1.0.0
 * @date 2018-08-08 10:45:41
 */
@Service
public class InputStockPlanServiceImpl implements InputStockPlanService {

    @Autowired
    private BizInstockplanDetailDao bizInstockplanDetailDao;

    /**
     * 根据申请单编号查询入库计划
     * @param applyNo 申请单编号
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-08 11:14:56
     */
    @Override
    public List<BizInstockplanDetail> queryListByApplyNo(String applyNo, String inRepositoryNo) {
        return bizInstockplanDetailDao.queryListByApplyNo(applyNo, inRepositoryNo);
    }

    /**
     * 根据入库计划id查询版本号
     * @param ids 入库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    @Override
    public List<updatePlanStatusDTO> getVersionNoById(List<Long> ids) {
        return bizInstockplanDetailDao.getVersionNoById(ids);
    }

    /**
     * 更新入库佳话中的实际入库数量
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-08 20:17:42
     */
    @Override
    public void updateActualInstockNum(List<BizInstockplanDetail> bizInstockplanDetailList) {
        bizInstockplanDetailDao.updateActualInstockNum(bizInstockplanDetailList);
    }

    /**
     * 修改入库计划的完成状态
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    @Override
    public void updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetailList) {
        bizInstockplanDetailDao.updateCompleteStatus(bizInstockplanDetailList);
    }

    /**
     * 根据申请单号查询入库计划
     * @param applyNo 申请单号
     * @param productType 商品类型
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    @Override
    public List<BizInstockplanDetail> queryInstockplan(String applyNo, String productType) {
        return bizInstockplanDetailDao.queryInstockplan(applyNo, productType);
    }

    /**
     *  更改入出库计划状态
     * @param applyNo 申请单编号
     * @param completeStatus 状态
     * @param instockRepositoryNo 入库仓库编号
     * @author weijb
     * @date 2018-08-11 12:55:41
     */
    @Override
    public int updateInStockPlanStatus(String applyNo, String completeStatus, String instockRepositoryNo){
        return bizInstockplanDetailDao.updateInStockPlanStatus(applyNo, completeStatus,instockRepositoryNo);
    }
}
