package com.ccbuluo.business.platform.carparts.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.core.entity.OSSClientProperties;
import com.ccbuluo.http.StatusDto;
import jdk.nashorn.api.tree.ContinueTree;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * <dl>
 * <dd>Description: Excel导出工具类,数据集单页</dd>
 * <dd>@date：2017/11/24 下午11:39</dd>
 * <dd>@author：hqbzl</dd>
 * </dl>
 */
@Service
public class ExportSingleUtils<T> {
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;

	@Autowired
    private BusinessPropertyHolder businessPropertyHolder;


//	public ExportSingleUtils( HSSFWorkbook workbook, HSSFSheet sheet) {
//		this.workbook = workbook;
//		this.sheet = sheet;
//	}

	public int darwRow(int rowIndex, String[] values, HSSFClientAnchor anchor, String image) throws MalformedURLException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        HSSFRow row = sheet.createRow(rowIndex);
        row.setHeight((short) 1000);
		if (StringUtils.isBlank(image)) {
            for (int i = 0; i < values.length; i++) {
                sheet.setColumnWidth((short) i, (short) 4500);
                HSSFCell cell = row.createCell(i);
                HSSFCellStyle style = workbook.createCellStyle();//设置列样式
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
                style.setWrapText(true);
                cell.setCellStyle(style);
                cell.setCellValue(values[i]);
            }
        } else {
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            String s = businessPropertyHolder.getPathPrefix(BusinessPropertyHolder.FILE_PATH) + image;
            URL url = new URL(s);
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            try {
                BufferedImage bufferImg = ImageIO.read(url);
                String file = url.getFile();
                if (file.toUpperCase().endsWith("PNG")) {
                    ImageIO.write(bufferImg, "png", byteArrayOut);
                } else if (file.toUpperCase().endsWith("JPEG")) {
                    ImageIO.write(bufferImg, "jpeg", byteArrayOut);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < values.length; i++) {
                sheet.setColumnWidth((short) i, (short) 4500);
                HSSFCell cell = row.createCell(i);
                HSSFCellStyle style = workbook.createCellStyle();//设置列样式
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
                style.setWrapText(true);
                cell.setCellStyle(style);
                if (i == 5 && anchor != null) {
                    patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
                cell.setCellValue(values[i]);
            }
        }
		return rowIndex++;
	}

	public void build(HttpServletResponse resp) {
		try {
			ServletOutputStream out = null;
			resp.setContentType("application/x-msdownload");
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode("零配件信息.xls", "UTF-8") + "\"");
			out = resp.getOutputStream();
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
