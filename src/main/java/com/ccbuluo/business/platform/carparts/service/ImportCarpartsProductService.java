package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.http.StatusDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangkangjian
 * @date 2018-11-21 15:13:14
 */
public interface ImportCarpartsProductService {

    /**
     * 导入零配件
     * @param multipartFile
     * @return StatusDto<String>
     * @author zhangkangjian
     * @date 2018-11-16 11:46:02
     */
    StatusDto<String> importCarparts(MultipartFile multipartFile) throws Exception;
}
