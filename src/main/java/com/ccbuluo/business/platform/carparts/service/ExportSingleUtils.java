package com.ccbuluo.business.platform.carparts.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <dl>
 * <dd>Description: Excel导出工具类,数据集单页</dd>
 * <dd>@date：2017/11/24 下午11:39</dd>
 * <dd>@author：hqbzl</dd>
 * </dl>
 */
public class ExportSingleUtils<T> {
	private String fileFullPath; // 目标文件全路径
	private String characterEncoding = "utf-8"; // 字符集
	private List<T> list = null; // 数据项
	private int headerIndex = 0; // 标题开始位置 占一行
	private int contextIndex = 1; // 内容起始位置
	private String dateFmt = "yyyy-MM-dd HH:mm:ss"; // 日期格式化
	private String sheetName = "mySheet"; // 默认工作表名称
	private LinkedHashMap<String, String> headerMapper = null; // 表头字段映射
	private String emptyFillStr = ""; // 空数据填充符
	private boolean isWriteRowNumer = false; // 是否加入行号
	private String rowNumberTitle = "序号";
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;

	public ExportSingleUtils(String fileFullPath, LinkedHashMap<String, String> headerMapper, List<T> list) {
		this.fileFullPath = fileFullPath;
		this.headerMapper = headerMapper;
		this.list = list;
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet(this.sheetName);
	}

	public int darwRow(int rowIndex, String[] values) {
		HSSFRow row = sheet.createRow(rowIndex); // 标题行坐标
		for (int i = 0; i < values.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(values[i]);
		}
		return rowIndex++;
	}

	public void build(boolean result) {
		try {
			FileOutputStream outputStream = new FileOutputStream(this.fileFullPath);
			workbook.write(outputStream);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void build() {
		// 产生Excel表头
		HSSFRow row = sheet.createRow(this.headerIndex); // 标题行坐标
		Collection<String> title = headerMapper.values();
		int hIndex = 0;
		if (isWriteRowNumer) {
			row.createCell(0).setCellValue(this.rowNumberTitle);
			hIndex++;
		}
		for (String h : title) {
			row.createCell(hIndex).setCellValue(h);
			hIndex++;
		}
		List<T> listT = this.list;
		int rowIdx = this.headerIndex + 1;
		Collection<String> files = headerMapper.keySet();
		for (T obj : listT) {
			row = sheet.createRow(rowIdx);
			int colIdx = 0;
			if (isWriteRowNumer) {
				row.createCell(0).setCellValue(rowIdx);
				colIdx++;
			}
			for (String fieldName : files) {
				Cell cell = row.createCell(colIdx);
				Object propValue = null;
				try {
					if (obj instanceof Map) { // Map类型的集合
						Map<String, Object> map = (Map<String, Object>) obj;
						propValue = map.get(fieldName);
					} else {
						propValue = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(obj, fieldName);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (null == propValue || "".equals(propValue)) {
					propValue = this.emptyFillStr;
				}
				if (propValue instanceof Date) {
					SimpleDateFormat df = new SimpleDateFormat(this.dateFmt);
					String dateStr = df.format(propValue);
					cell.setCellValue(dateStr);
				} else if (propValue instanceof Double) {
					cell.setCellValue((Double) propValue);
				} else if (propValue instanceof Integer) {
					cell.setCellValue((Integer) propValue);
				} else if (propValue instanceof Boolean) {
					cell.setCellValue((Boolean) propValue);
				} else {
					cell.setCellValue(propValue.toString());
				}
				colIdx++; // 列偏移
			}
			rowIdx++; // 行号偏移
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(this.fileFullPath);
			workbook.write(outputStream);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getHeaderIndex() {
		return headerIndex;
	}

	public void setHeaderIndex(int headerIndex) {
		this.headerIndex = headerIndex;
	}

	public int getContextIndex() {
		return contextIndex;
	}

	public void setContextIndex(int contextIndex) {
		this.contextIndex = contextIndex;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(String dateFmt) {
		this.dateFmt = dateFmt;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public LinkedHashMap<String, String> getHeaderMapper() {
		return headerMapper;
	}

	public void setHeaderMapper(LinkedHashMap<String, String> headerMapper) {
		this.headerMapper = headerMapper;
	}

	public String getEmptyFillStr() {
		return emptyFillStr;
	}

	public void setEmptyFillStr(String emptyFillStr) {
		this.emptyFillStr = emptyFillStr;
	}

	public boolean isWriteRowNumer() {
		return isWriteRowNumer;
	}

	public void setWriteRowNumer(boolean isWriteRowNumer) {
		this.isWriteRowNumer = isWriteRowNumer;
	}

	public String getRowNumberTitle() {
		return rowNumberTitle;
	}

	public void setRowNumberTitle(String rowNumberTitle) {
		this.rowNumberTitle = rowNumberTitle;
	}

	public static void main(String[] args) {

		String fileFullPath = "d:/test.xls";
		LinkedHashMap<String, String> headerMapper = Maps.newLinkedHashMap();
		List<Map<String, Object>> list = Lists.newArrayList();
		ExportSingleUtils<Map<String, Object>> ex = new ExportSingleUtils<Map<String, Object>>(fileFullPath,
				headerMapper, list);
		int darwRow = ex.darwRow(0, new String[] { "1", "2", "3", "4" });
		ex.build(true);
	}

}
