package com.ccbuluo.business.platform.supplier.service;

import com.ccbuluo.business.constants.Assert;
import com.ccbuluo.business.constants.Constants;
import com.ccbuluo.business.entity.BizServiceProjectcode;
import com.ccbuluo.business.entity.BizServiceSupplier;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import com.ccbuluo.business.platform.supplier.dao.BizServiceSupplierDao;
import com.ccbuluo.business.platform.supplier.dto.*;

import com.ccbuluo.core.common.UserHolder;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftList;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.json.JsonUtils;
import com.ccbuluo.merchandiseintf.carparts.category.dto.RelSupplierProductDTO;
import com.ccbuluo.merchandiseintf.carparts.category.service.CarpartsCategoryService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsCategoryService carpartsCategoryService;


    /**
     * 添加供应商
     * @param bizServiceSupplier 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    @Override
    public StatusDto<String> createSupplier(BizServiceSupplier bizServiceSupplier) throws IOException {
        // 校验参数和参数合法性
        checkParamAndSupplierInfo(bizServiceSupplier);
        // 保存供应商信息
        saveSupplier(bizServiceSupplier);
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 保存供应商信息
     * @param bizServiceSupplier 供应商信息
     * @author zhangkangjian
     * @date 2018-07-25 10:18:44
     */
    private void saveSupplier(BizServiceSupplier bizServiceSupplier) {
        String loggedUserId = userHolder.getLoggedUserId();
        bizServiceSupplier.setOperator(loggedUserId);
        bizServiceSupplier.setCreator(loggedUserId);
        // 生成供应商编号
        StatusDto<String> supplierCode = generateProjectCodeService.grantCode(BizServiceProjectcode.CodePrefixEnum.FG);
        if(!supplierCode.isSuccess()){
            throw new CommonException(supplierCode.getCode(), "生成供应商编号失败！");
        }
        bizServiceSupplier.setSupplierCode(supplierCode.getData());
        bizServiceSupplierDao.saveEntity(bizServiceSupplier);
    }

    /**
     *  校验参数和参数合法性
     * @param object 供应商信息
     * @author zhangkangjian
     * @date 2018-07-25 10:20:24
     */
    private void checkParamAndSupplierInfo(Object object) throws IOException {
        // 类型转换
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = JsonUtils.writeValue(object);
        BizServiceSupplier bizServiceSupplier = mapper.readValue(json, BizServiceSupplier.class);
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
        // 手机号校验
        checkPhone(bizServiceSupplier.getSupplierPhone());
        // 信息校验
        checkSupplierInfo(bizServiceSupplier.getId(), bizServiceSupplier.getSupplierPhone(), bizServiceSupplier.getSupplierName());
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
        compareRepeat(id , phone, "supplier_phone", "biz_service_supplier", "手机号重复！");
        // 名字验重
        compareRepeat(id , name, "supplier_name", "biz_service_supplier", "供应商名称重复！");
    }

    /**
     * 编辑供应商
     * @param editSupplierDTO 供应商信息
     * @author zhangkangjian
     * @date 2018-07-03 14:32:57
     */
    @Override
    public void editsupplier(EditSupplierDTO editSupplierDTO) throws IOException {
        // 校验参数和参数合法性
        checkParamAndSupplierInfo(editSupplierDTO);
        // 更新供应商信息
        editSupplierDTO.setOperator(userHolder.getLoggedUserId());
        bizServiceSupplierDao.update(editSupplierDTO);
    }

    /**
     *  手机号格式校验
     * @param supplierPhone 供应商手机号
     * @author zhangkangjian
     * @date 2018-07-25 10:01:13
     */
    private void checkPhone(String supplierPhone) {
        String regex = "^1[34578][0-9]{9}$";
        boolean matches = Pattern.matches(regex, supplierPhone);
        Assert.isTrue(matches, "手机号校验失败！");
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
     * 添加关联商品
     *
     * @param saveRelSupplierProductDTO 关联商品DTO
     * @return StatusDto
     * @author zhangkangjian
     * @date 2018-08-01 10:06:19
     */
    @Override
    public StatusDto<String> createRelSupplierProduct(SaveRelSupplierProductDTO saveRelSupplierProductDTO) {
        String loggedUserId = userHolder.getLoggedUserId();
        // 过滤垃圾数据和填充数据
        List<RelSupplierProduct> supplierProductList = null;
        if(saveRelSupplierProductDTO != null){
            supplierProductList = saveRelSupplierProductDTO.getSupplierProductList();
            List<RelSupplierProduct> collect = supplierProductList.stream().filter(a -> StringUtils.isNoneBlank(a.getProductCode(), a.getSupplierCode(), a.getProductType())).collect(Collectors.toList());
            collect.stream().forEach(a -> {
                a.setCreator(loggedUserId);
                a.setOperator(loggedUserId);
            });
            // 批量插入
            bizServiceSupplierDao.batchSave(collect);
        }
        return StatusDto.buildSuccessStatusDto();
    }

    /**
     * 查询供应商的商品（零配件，物料）
     *
     * @param queryRelSupplierProductDTO 查询条件
     * @return StatusDto<Page < RelSupplierProduct>> 分页信息
     * @author zhangkangjian
     * @date 2018-08-01 11:46:53
     */
    @Override
    public Page<QueryRelSupplierProductDTO> findSupplierProduct(QueryRelSupplierProductDTO queryRelSupplierProductDTO) {
        Page<QueryRelSupplierProductDTO> queryRelSupplierProductPage = null;
        if(Constants.PRODUCT_TYPE_FITTINGS.equals(queryRelSupplierProductDTO.getProductType())){
            queryRelSupplierProductPage = queryFittingsProduct(queryRelSupplierProductDTO);
        }else{
            queryRelSupplierProductPage = queryEquipmentProduct(queryRelSupplierProductDTO);
        }
        return queryRelSupplierProductPage;
    }

    /**
     * 删除供应商关系
     * @param id
     * @author zhangkangjian
     * @date 2018-08-01 20:12:26
     */
    @Override
    public void deleteSupplierProduct(Long id) {
        RelSupplierProduct sp = bizServiceSupplierDao.findRelSupplierProduct(id);
        if(sp != null){
            bizServiceSupplierDao.deleteSupplierProduct(sp);
        }
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
    private void compareRepeat(Long id, List<Long> ids, String tip){
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
     * @param fields 数据库字段，验重的字段
     * @param tableName 数据库表名
     * @param tip 失败提示语 成功不提示语
     * @author zhangkangjian
     * @date 2018-07-03 16:06:48
     */
    public void compareRepeat(Long id, String value, String fields, String tableName, String tip){
        List<Long> ids = bizServiceSupplierDao.queryIds(value, fields, tableName);
        compareRepeat(id, ids ,tip);
    }

    /**
     * 查询供商零配件商品
     * @param queryRelSupplierProductDTO 查询条件
     * @return Page<RelSupplierProduct> 分页的商品信息
     * @author zhangkangjian
     * @date 2018-08-01 14:40:39
     */
    @Override
    public Page<QueryRelSupplierProductDTO> queryFittingsProduct(QueryRelSupplierProductDTO queryRelSupplierProductDTO) {
        Page<QueryRelSupplierProductDTO> queryRelSupplierProductPage = bizServiceSupplierDao.querySupplierProduct(queryRelSupplierProductDTO);
        List<QueryRelSupplierProductDTO> products = queryRelSupplierProductPage.getRows();
        List<String> productCodes = products.stream().map(QueryRelSupplierProductDTO::getProductCode).collect(Collectors.toList());
        StatusDtoThriftList<RelSupplierProductDTO> productDto = carpartsCategoryService.queryCarpartsByProductCode(productCodes);
        StatusDto<List<RelSupplierProductDTO>> resolve = StatusDtoThriftUtils.resolve(productDto, RelSupplierProductDTO.class);
        List<RelSupplierProductDTO> data = resolve.getData();
        Map<String, RelSupplierProductDTO> dataMap = data.stream().collect(Collectors.toMap(RelSupplierProductDTO::getProductCode, a -> a,(k1, k2)->k1));
        products.stream().forEach(a ->{
            RelSupplierProductDTO relSupplierProductDTO = dataMap.get(a.getProductCode());
            a.setCategoryName(relSupplierProductDTO.getCategoryName());
            a.setProductName(relSupplierProductDTO.getProductName());
        });
        return queryRelSupplierProductPage;
    }

    /**
     *  查询供商物料商品
     * @param queryRelSupplierProductDTO 查询条件
     * @return Page<RelSupplierProduct> 分页的商品信息
     * @author zhangkangjian
     * @date 2018-08-01 20:01:27
     */
    public Page<QueryRelSupplierProductDTO> queryEquipmentProduct(QueryRelSupplierProductDTO queryRelSupplierProductDTO) {
        return bizServiceSupplierDao.queryEquipmentProduct(queryRelSupplierProductDTO);
    }

    /**
     * 根据商品的code查询供应商的信息（下拉框）
     * @param code 商品的code
     * @param type 商品类型（注：FITTINGS零配件，EQUIPMENT物料）
     * @return StatusDto<List < RelSupplierProduct>>
     * @author zhangkangjian
     * @date 2018-08-07 15:32:17
     */
    @Override
    public List<QuerySupplierInfoDTO> querySupplierInfo(String code, String type) {
        List<QuerySupplierInfoDTO> list = bizServiceSupplierDao.querySupplierInfo(code, type);
        return list;
    }


}
