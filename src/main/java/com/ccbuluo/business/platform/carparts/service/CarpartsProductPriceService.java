package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.entity.RelProductPrice;
import com.ccbuluo.business.platform.allocateapply.dto.AllocateapplyDetailDTO;
import com.ccbuluo.business.vehiclelease.entity.BizOrderChannelprice;
import com.ccbuluo.core.entity.UploadFileInfo;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftBean;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.EditBasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.SaveBasicCarpartsProductDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 零配件价格service
 * @author zhangkangjian
 * @date 2018-09-06 16:17:38
 */
public interface CarpartsProductPriceService {
    /**
     * 查询零配件的信息和价格
     * @param queryCarpartsProductDTO 查询零配件条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>>
     * @author zhangkangjian
     * @date 2018-09-06 16:25:05
     */
    StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductPriceList(QueryCarpartsProductDTO queryCarpartsProductDTO);
    /**
     * 设置价格
     * @param relProductPrice
     * @author zhangkangjian
     * @date 2018-09-06 19:27:11
     */
    void saveProductPrice(List<RelProductPrice> relProductPrice);
    /**
     * 查询维修单的零配件列表
     * @param queryCarpartsProductDTO 查询条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 查询分页信息
     * @author zhangkangjian
     * @date 2018-09-28 10:31:47
     */
    StatusDto<Page<BasicCarpartsProductDTO>> queryServiceProductList(QueryCarpartsProductDTO queryCarpartsProductDTO);
    /**
     * 上传图片
     * @param base64 图片base64编码
     * @return StatusDto<UploadFileInfo>
     * @author zhangkangjian
     * @date 2018-10-31 10:45:26
     */
    StatusDto<UploadFileInfo> uploadImage(String base64) throws UnsupportedEncodingException;
    /**
     * 查询当前机构下所有的零配件（不限制数量，不限制是否设置价格）
     * @param queryCarpartsProductDTO 查询的条件
     * @return StatusDto<Page<BasicCarpartsProductDTO>> 分页的零配件列表
     * @author zhangkangjian
     * @date 2018-11-05 15:40:42
     */
    StatusDto<Page<BasicCarpartsProductDTO>> queryAllServiceProductList(QueryCarpartsProductDTO queryCarpartsProductDTO);

    /**
     * 查询零配件的详情
     * @param carpartsCode 零配件code
     * @return StatusDtoThriftBean<EditBasicCarpartsProductDTO>
     * @author zhangkangjian
     * @date 2018-11-05 16:56:30
     */
    StatusDto<SaveBasicCarpartsProductDTO> findCarpartsProductdetail(String carpartsCode);

    /**
     * 保存零配件
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-11-12 14:37:33
     */
    StatusDto<String> saveCarpartsProduct(SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO);

    /**
     * 编辑零部件
     * @param saveBasicCarpartsProductDTO 零配件实体
     * @return StatusDto<String>
     * @author weijb
     * @date 2018-07-02 18:52:40
     */
    StatusDto<String> editCarpartsProduct(SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO);
    /**
     * 零配件列表分页查询
     * @param keyword 零部件名称
     * @param offset 起始数
     * @param pageSize 每页数量
     * @author zhangkangjian
     * @date 2018-11-12 19:52:44
     */
    StatusDto<Page<BasicCarpartsProductDTO>> queryCarpartsProductList(String keyword, Integer offset, Integer pageSize);

    /**
     * 查询零配件的信息
     * @param keyword 查询条件
     * @return
     * @author zhangkangjian
     * @date 2018-11-13 11:04:47
     */
    StatusDto<List<BasicCarpartsProductDTO>> queryCarparts(String keyword, RelProductPrice.PriceLevelEnum priceLevelEnum);
    /**
     *  删除零配件
     * @param carpartsCode 零配件code
     * @author zhangkangjian
     * @date 2018-11-14 15:42:34
     */
    StatusDto<String> deleteCarpartsProduct(String carpartsCode);

    /**
     * 批量更新零配件价格结束时间
     * @param updateRelProductPriceList 零配件价格列表
     * @author zhangkangjian
     * @date 2018-11-21 13:45:40
     */
    void batchUpdateProductPrice(List<RelProductPrice> updateRelProductPriceList);
    /**
     * 批量插入零件价格列表
     * @param saveRelProductPriceList 零配件价格列表
     * @author zhangkangjian
     * @date 2018-11-21 14:30:32
     */
    void batchSaveProductPrice(List<RelProductPrice> saveRelProductPriceList);
}
