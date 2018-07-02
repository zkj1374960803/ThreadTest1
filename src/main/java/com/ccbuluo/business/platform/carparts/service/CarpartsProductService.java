package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.platform.carparts.dto.SaveBasicCarpartsProductDTO;
import com.ccbuluo.business.platform.parameter.dto.SaveCarpartsParameterDTO;

/**
 * ${file_name}
 *
 * @author wuyibo
 * @date 2018-06-29 14:38:41
 */
public interface CarpartsProductService {

    /**
     * 添加零配件
     *
     * @return
     * @exception
     * @author weijb
     * @date 2018-06-29 15:03:07
     */
    int saveParameter(SaveBasicCarpartsProductDTO saveBasicCarpartsProductDTO);
}
