package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.constants.Assert;
import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.business.platform.supplier.dto.EditSupplierDTO;
import com.ccbuluo.business.platform.supplier.dto.QuerySupplierListDTO;
import com.ccbuluo.business.platform.supplier.dto.ResultFindSupplierDetailDTO;
import com.ccbuluo.business.platform.supplier.dto.ResultSupplierListDTO;

import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.exception.ThriftRpcException;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weakref.jmx.internal.guava.base.Preconditions;

import javax.annotation.Resource;
import java.util.List;

/**
 * 供应商实现类
 * @author zhangkangjian
 * @date 2018-07-03 13:57:26
 */
@Service
public class SupplierServiceImpl implements SupplierService{
    @Resource(name = "bizServiceSupplierDao")
    private BizServiceSupplierDao bizServiceSupplierDao;
    @Autowired
    private UserHolder userHolder;
    @Resource(name = "generateProjectCodeService")
    private GenerateProjectCodeService generateProjectCodeService;


    /**
     * 添加供应商
     * @param bizServiceSupplier 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    @Override
    public StatusDto<String> createSupplier(BizServiceSupplier bizServiceSupplier) throws TException {
        // 参数校验
        Assert.notNull(bizServiceSupplier);
        Assert.notEmpty(bizServiceSupplier.getSupplierName());
        Assert.notEmpty(bizServiceSupplier.getLinkman());
        Assert.notEmpty(bizServiceSupplier.getSupplierPhone());
        Assert.notEmpty(bizServiceSupplier.getProvinceName());
        Assert.notEmpty(bizServiceSupplier.getProvinceCode());
        Assert.notEmpty(bizServiceSupplier.getCityName());
        Assert.notEmpty(bizServiceSupplier.getCityCode());
        Assert.notEmpty(bizServiceSupplier.getAreaName());
        Assert.notEmpty(bizServiceSupplier.getAreaCode());
        Assert.notEmpty(bizServiceSupplier.getSupplierAddress());
        Assert.notEmpty(bizServiceSupplier.getMajorProduct());

        String loggedUserId = userHolder.getLoggedUserId();
        bizServiceSupplier.setOperator(loggedUserId);
        bizServiceSupplier.setCreator(loggedUserId);
        // 生成供应商编号
        StatusDto<String> stringStatusDto = generateProjectCodeService.grantCode(CodePrefixEnum.FG);
        if(!stringStatusDto.isSuccess()){
            return StatusDto.buildFailure("生成供应商编号失败！");
        }
        bizServiceSupplier.setSupplierCode(stringStatusDto.getData());
        // 信息校验
        checkSupplierInfo(bizServiceSupplier.getId(), bizServiceSupplier.getSupplierPhone(), bizServiceSupplier.getSupplierName());
        bizServiceSupplierDao.saveEntity(bizServiceSupplier);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 供应商参数校验
     * @param bizServiceSupplier 供应商信息
     * @author zhangkangjian
     * @date 2018-07-12 17:34:49
     */
    private void supplierParameterChecking(BizServiceSupplier bizServiceSupplier) {
        Assert.notNull(bizServiceSupplier);
        Assert.notEmpty(bizServiceSupplier.getSupplierName());
        Assert.notEmpty(bizServiceSupplier.getLinkman());
        Assert.notEmpty(bizServiceSupplier.getSupplierPhone());
        Assert.notEmpty(bizServiceSupplier.getProvinceName());
        Assert.notEmpty(bizServiceSupplier.getProvinceCode());
        Assert.notEmpty(bizServiceSupplier.getCityName());
        Assert.notEmpty(bizServiceSupplier.getCityCode());
        Assert.notEmpty(bizServiceSupplier.getAreaName());
        Assert.notEmpty(bizServiceSupplier.getAreaCode());
        Assert.notEmpty(bizServiceSupplier.getSupplierAddress());
        Assert.notEmpty(bizServiceSupplier.getMajorProduct());
    }

    /**
     * 供应商信息校验
     * @param id
     * @param phone 供应商手机号
     * @param name 供应商姓名
     * @author zhangkangjian
     * @date 2018-07-03 16:39:32
     */
    private void checkSupplierInfo(Long id, String phone, String name) {
        // 手机号校验
        String str = id == null ? null : id.toString();
        compareRepeat(str , phone, "supplier_phone", "biz_service_supplier", "手机号重复！");
        // 名字验重
        compareRepeat(str , name, "supplier_name", "biz_service_supplier", "供应商名称重复！");
    }

    /**
     * 编辑供应商
     * @param editSupplierDTO 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    @Override
    public void editsupplier(EditSupplierDTO editSupplierDTO) {
        // 参数校验
        Assert.notNull(editSupplierDTO);
        Assert.notEmpty(editSupplierDTO.getSupplierName());
        Assert.notEmpty(editSupplierDTO.getLinkman());
        Assert.notEmpty(editSupplierDTO.getSupplierPhone());
        Assert.notEmpty(editSupplierDTO.getProvinceName());
        Assert.notEmpty(editSupplierDTO.getProvinceCode());
        Assert.notEmpty(editSupplierDTO.getCityName());
        Assert.notEmpty(editSupplierDTO.getCityCode());
        Assert.notEmpty(editSupplierDTO.getAreaName());
        Assert.notEmpty(editSupplierDTO.getAreaCode());
        Assert.notEmpty(editSupplierDTO.getSupplierAddress());
        Assert.notEmpty(editSupplierDTO.getMajorProduct());

        String loggedUserId = userHolder.getLoggedUserId();
        editSupplierDTO.setOperator(loggedUserId);
        checkSupplierInfo(editSupplierDTO.getId(), editSupplierDTO.getSupplierPhone(), editSupplierDTO.getSupplierName());
        bizServiceSupplierDao.update(editSupplierDTO);
    }

    /**
     * 供应商启用/停用接口
     * @param id 供应商id
     * @param supplierStatus 停用启用状态 1：启用 0：停用
     * @author zhangkangjian
     * @date 2018-07-03 17:09:11
     */
    @Override
    public void updateSupplierStatus(Long id, Integer supplierStatus) {
        bizServiceSupplierDao.updateSupplierStatus(id, supplierStatus);
    }

    /**
     * 查询供应商列表
     * @param querySupplierListDTO 查询条件
     * @return List<ResultSupplierListDTO> 供应商列表
     * @author zhangkangjian
     * @date 2018-07-04 09:57:21
     */
    @Override
    public Page<ResultSupplierListDTO> querySupplierList(QuerySupplierListDTO querySupplierListDTO) {
        return bizServiceSupplierDao.querySupplierList(querySupplierListDTO);
    }

    /**
     * 查询供应商详情
     * @param id 供应商id
     * @return ResultSupplierListDTO 供应商信息
     * @author zhangkangjian
     * @date 2018-07-04 10:41:23
     */
    @Override
    public ResultFindSupplierDetailDTO findSupplierDetail(Long id) {
        return bizServiceSupplierDao.getById(id);
    }

    /**
     * 比较两个uuid是否有重复的
     * @param id 用户的id
     * @param ids 用户ids
     * @param tip 提示语
     * @author zhangkangjian
     * @date 2018-05-23 16:05:19
     * @version v1.0.0
     */
    private void compareRepeat(String id, List<String> ids, String tip){
        if(id != null){
            if(ids.size() == 1){
                if(!id.equals(ids.get(0))){
                    throw new CommonException(Constants.ERROR_CODE, tip);
                }
            }else if(ids.size() > 1){
                throw new CommonException(Constants.ERROR_CODE, "此数据已产生重复数据！ " + tip);
            }
        }else {
            if(ids.size() == 1){
                throw new CommonException(Constants.ERROR_CODE, tip);
            }else if(ids.size() > 1){
                throw new CommonException(Constants.ERROR_CODE, "此数据已产生重复数据！ " + tip);
            }
        }
    }

    /**
     * 重复校验根据id判断
     * 例子：compareRepeat("2" , "18761326500", "supplier_phone", "biz_service_supplier", "手机号重复！");
     * @param id
     * @param value 验重的值
     * @param fields 验重的字段
     * @param tableName 表名
     * @param tip 失败提示语 成功不提示语
     * @author zhangkangjian
     * @date 2018-07-03 16:06:48
     */
    public void compareRepeat(String id, String value, String fields, String tableName, String tip){
        List<String> ids = bizServiceSupplierDao.queryIds(value, fields, tableName);
        compareRepeat(id, ids ,tip);
    }


}
