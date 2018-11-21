package com.ccbuluo.business.platform.carparts.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 导出零配件service
 * @author liuduo
 * @version v1.0.0
 * @date 2018-11-21 15:58:40
 */
public interface ExportCarpartsService {
    /**
     * 导出零配件
     * @param resp
     * @author liuduo
     * @date 2018-11-21 16:41:15
     */
    void exportCarparts(HttpServletResponse resp)  throws IOException;
}
