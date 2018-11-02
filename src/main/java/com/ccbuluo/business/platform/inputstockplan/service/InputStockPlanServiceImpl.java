package com.ccbuluo.business.platform.inputstockplan.service;


import com.ccbuluo.business.entity.BizInstockplanDetail;
import com.ccbuluo.business.platform.inputstockplan.dao.BizInstockplanDetailDao;
import com.ccbuluo.business.platform.outstock.dto.UpdatePlanStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<BizInstockplanDetail> queryListByApplyNo(String applyNo, String status, String inRepositoryNo) {
        return bizInstockplanDetailDao.queryListByApplyNo(applyNo, status, inRepositoryNo);
    }

    /**
     * 根据入库计划id查询版本号
     * @param id 入库计划id
     * @return 版本号
     * @author liuduo
     * @date 2018-08-08 19:31:38
     */
    @Override
    public Long getVersionNoById(Long id) {
        return bizInstockplanDetailDao.getVersionNoById(id);
    }

    /**
     * 更新入库佳话中的实际入库数量
     * @param bizInstockplanDetail 入库计划
     * @author liuduo
     * @date 2018-08-08 20:17:42
     */
    @Override
    public int updateActualInstockNum(BizInstockplanDetail bizInstockplanDetail) {
        return bizInstockplanDetailDao.updateActualInstockNum(bizInstockplanDetail);
    }

    /**
     * 修改入库计划的完成状态
     * @param bizInstockplanDetailList 入库计划
     * @author liuduo
     * @date 2018-08-09 11:16:12
     */
    @Override
    public int updateCompleteStatus(List<BizInstockplanDetail> bizInstockplanDetailList) {
        return bizInstockplanDetailDao.updateCompleteStatus(bizInstockplanDetailList);
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
    public List<BizInstockplanDetail> queryInstockplan(String applyNo,  String inRepositoryNo, String productType) {
        List<BizInstockplanDetail> bizInstockplanDetails = bizInstockplanDetailDao.queryInstockplan(applyNo, inRepositoryNo, productType);
        bizInstockplanDetails.forEach(item -> {
            item.setShouldInstocknum(item.getPlanInstocknum() - item.getActualInstocknum());
        });
        return bizInstockplanDetails;
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

    /**
     * 根据申请单号查询入库仓库
     * @param applyNo 入库单号
     * @return 入库仓库
     * @author liuduo
     * @date 2018-08-13 15:20:27
     */
    @Override
    public List<String> getByApplyNo(String applyNo, String orgCode) {
        return bizInstockplanDetailDao.getByApplyNo(applyNo, orgCode);
    }

    /**
     * 根据申请单号和入库仓库查询入库计划
     * @param applyNo 申请单号
     * @param inRepositoryNo 入库仓库
     * @return 入库计划
     * @author liuduo
     * @date 2018-08-11 13:17:42
     */
    @Override
    public List<BizInstockplanDetail> queryListByApplyNoAndInReNo(String applyNo, String inRepositoryNo) {
        return bizInstockplanDetailDao.queryListByApplyNoAndInReNo(applyNo, inRepositoryNo);
    }

    /**
     * 根据入库计划id查询版本号
     * @param ids 入库计划id
     * @return 入库计划的版本号（乐观锁）
     * @author liuduo
     * @date 2018-08-28 15:03:22
     */
    @Override
    public List<UpdatePlanStatusDTO> getVersionNoByIds(List<Long> ids) {
        return bizInstockplanDetailDao.getVersionNoByIds(ids);
    }

    /**
     * 删除所有入库计划
     * @param applyNo 申请单号
     * @author liuduo
     * @date 2018-10-31 16:13:14
     */
    @Override
    public void deleteInStockPlan(String applyNo) {
        bizInstockplanDetailDao.deleteInStockPlan(applyNo);
    }

}
