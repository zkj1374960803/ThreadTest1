package com.ccbuluo.business.export;

import com.ccbuluo.excel.export.RowCellType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <dl>
 * <dd>Description: Excel导出工具类,数据集单页</dd>
 * <dd>@date：2017/11/24 下午11:39</dd>
 * <dd>@author：hqbzl</dd>
 * </dl>
 */
public class ExportSingleSheetUtils<T> {
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
	private String colSpanTarget = "$"; // 行单元格合并标记

	/**
	 * 结果集填充单sheet
	 *
	 * @param fileFullPath
	 * @param headerMapper
	 * @param list
	 */
	public ExportSingleSheetUtils(String fileFullPath, LinkedHashMap<String, String> headerMapper, List<T> list) {
		this(fileFullPath);
		this.fileFullPath = fileFullPath;
		this.headerMapper = headerMapper;
		this.list = list;
	}

	/**
	 * 自己填充数据
	 *
	 * @param fileFullPath
	 */
	public ExportSingleSheetUtils(String fileFullPath) {
		this.fileFullPath = fileFullPath;
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

	/**
	 * 单行填充excel最后调用,形参无实际意义
	 *
	 * @param result
	 */
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

	// firstRow 区域中第一个单元格的行号
	// lastRow 区域中最后一个单元格的行号
	// firstCol 区域中第一个单元格的列号
	// lastCol 区域中最后一个单元格的列号
	public String getColSpanTarget() {
		return colSpanTarget;
	}

	public void setColSpanTarget(String colSpanTarget) {
		this.colSpanTarget = colSpanTarget;
	}

	public int darwRowColSpan(int rowIndex, String[] values) {
		this.darwRowColSpan(rowIndex, values, getCellStyle(true), true);
		return rowIndex++;
	}

	public int darwRowColSpanNoBorder(int rowIndex, String[] values) {
		this.darwRowColSpan(rowIndex, values, getCellStyle(false), false);
		return rowIndex++;
	}

	public int darwRowColSpan(int rowIndex, String[] values, HSSFCellStyle cellStyle, boolean isBorder) {
		List<Integer> nIndex = new ArrayList<Integer>();
		Map<Integer, Integer> dataIndex = Maps.newHashMap();
		List<CellRangeAddress> listCras = Lists.newArrayList();
		for (int i = 0; i < values.length; i++) {
			if (values[i].equalsIgnoreCase(this.getColSpanTarget())) {
				nIndex.add(i);
			} else {
				// 第一个单元格不合并,目标集合没有值
				int startIndex = (nIndex.size() == 0 ? i : nIndex.get(0));
				dataIndex.put(startIndex, i); // 把单元格的索引和对应目标值存起来
				CellRangeAddress range = new CellRangeAddress(rowIndex, rowIndex, startIndex, i);
				if (isBorder) {
					listCras.add(range);
				}
				this.sheet.addMergedRegion(range);
				nIndex.clear();
			}
		}
		HSSFRow row = sheet.createRow(rowIndex); // 标题行坐标
		for (int i = 0; i < values.length; i++) {
			Set<Integer> startIndexs = dataIndex.keySet();
			if (!startIndexs.contains(i)) {
				continue;
			}
			HSSFCell cell = row.createCell(i);
			Integer valIndex = dataIndex.get(i);
			cell.setCellValue(values[valIndex]);
			if (null != cellStyle) {
				cell.setCellStyle(cellStyle);
			}
		}
		setBorders(listCras);
		return rowIndex++;
	}

	public int darwRowColSpanRowCellType(int rowIndex, RowCellType cols, List<T> t, HSSFCellStyle cellStyle,
                                         boolean isBorder) {
		String[] generCollection = cols.generCollection(t);
		return darwRowColSpan(rowIndex, generCollection, cellStyle, isBorder);
	}

	public int darwRowRowSpan(int rowIndex, String[] values, HSSFCellStyle cellStyle, boolean isBorder) {
		List<Integer> nIndex = new ArrayList<Integer>();
		Map<Integer, Integer> dataIndex = Maps.newHashMap();
		List<CellRangeAddress> listCras = Lists.newArrayList();
		for (int i = 0; i < values.length; i++) {
			if (values[i].equalsIgnoreCase(this.getColSpanTarget())) {
				nIndex.add(i);
			} else {
				// 第一个单元格不合并,目标集合没有值
				int startIndex = (nIndex.size() == 0 ? i : nIndex.get(0));
				dataIndex.put(startIndex, i); // 把单元格的索引和对应目标值存起来
				CellRangeAddress range = new CellRangeAddress(rowIndex, rowIndex, startIndex, i);
				if (isBorder) {
					listCras.add(range);
				}
				this.sheet.addMergedRegion(range);
				nIndex.clear();
			}
		}
		HSSFRow row = sheet.createRow(rowIndex); // 标题行坐标
		for (int i = 0; i < values.length; i++) {
			Set<Integer> startIndexs = dataIndex.keySet();
			if (!startIndexs.contains(i)) {
				continue;
			}
			HSSFCell cell = row.createCell(i);
			Integer valIndex = dataIndex.get(i);
			cell.setCellValue(values[valIndex]);
			if (null != cellStyle) {
				cell.setCellStyle(cellStyle);
			}
		}
		if (rowIndex > 0) {
			// 起始行号，终止行号， 起始列号，终止列号
			String beforeCellValue = getCellValue(rowIndex - 1, 0);
			String currentCellValue = getCellValue(rowIndex, 0);
			CellRangeAddress range = new CellRangeAddress(rowIndex - 1, rowIndex, 0, 0);
			this.sheet.addMergedRegion(range);
			System.out.println("beforeCellValue:\t" + beforeCellValue + "\tcurrentCellValue:\t" + currentCellValue);
			this.sheet.getRow(0).createCell(0).setCellValue("xx");
			HSSFCell cell = this.sheet.getRow(rowIndex - 1).getCell(0);
			CellRangeAddress arrayFormulaRange = cell.getArrayFormulaRange();
			int firstRow = arrayFormulaRange.getFirstRow();
			System.out.println("firstRow:\t" + firstRow);
		}
		setBorders(listCras);
		return rowIndex++;
	}

	public String getCellValue(int rowInde, int colIdex) {
		HSSFRow row = this.sheet.getRow(rowInde);
		String stringCellValue = row.getCell(colIdex).getStringCellValue();
		return stringCellValue;
	}

	public void setBorders(List<CellRangeAddress> ranges) {
		for (CellRangeAddress range : ranges) {
			RegionUtil.setBorderLeft(1, range, this.sheet, this.workbook);
			RegionUtil.setBorderBottom(1, range, this.sheet, this.workbook);
			RegionUtil.setBorderRight(1, range, this.sheet, this.workbook);
			RegionUtil.setBorderTop(1, range, this.sheet, this.workbook);
		}
	}

	/**
	 * 常用边框居中
	 *
	 * @return
	 */
	public HSSFCellStyle getCellStyle(boolean isBorder) {
		HSSFCellStyle cellStyle = this.workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 设置水平对齐方式
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 设置垂直对齐方式
		if (isBorder) {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		}
		return cellStyle;
	}

	public static void main(String[] args) {
		String fileFullPath = "d:/test1.xls";
		ExportSingleSheetUtils<Map<String, Object>> ex = new ExportSingleSheetUtils<Map<String, Object>>(
				fileFullPath);
		ex.setColSpanTarget("$");
		ex.darwRowColSpan(1, new String[] { "$", "$", "$", "1", "$", "$", "$", "3", "$", "4" });
		ex.darwRowColSpan(2, new String[] { "1", "1", "$", "1", "$", "$", "$", "3", "2", "4" });
		ex.darwRowColSpan(3, new String[] { "1", "$", "$", "1", "$", "$", "$", "3", "$", "4" });
		ex.darwRowColSpan(4, new String[] { "1", "1", "$", "$", "$", "$", "$", "3", "1", "4" });
		ex.darwRowColSpan(5, new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" });
		ex.darwRowColSpan(6, new String[] { "$", "$", "$", "$", "$", "$", "$", "$", "$", "10" });
		ex.darwRowColSpan(8, new String[] { "$", "$", "$", "$", "$", "$", "$", "$", "$", "10" }, null, false);
		ex.darwRowColSpanNoBorder(10, new String[] { "$", "$", "$", "$", "$", "$", "$", "$", "$", "10" });
		ex.darwRowColSpan(12, new String[] { "$", "$", "$", "$", "$", "$", "$", "$", "$", "10" });
//		ex.darwRowRowSpan(0, new String[] { "1111", "2", "3", "4", "5", "6", "7", "8", "9", "10" }, null, true);
//		ex.darwRowRowSpan(1, new String[] { "1111", "2", "3", "4", "5", "6", "7", "8", "9", "10" }, null, true);
//		ex.darwRowRowSpan(2, new String[] { "1111", "2", "3", "4", "5", "6", "7", "8", "9", "10" }, null, true);
//		ex.darwRowRowSpan(3, new String[] { "1111", "2", "3", "4", "5", "6", "7", "8", "9", "10" }, null, true);
//		ex.darwRowRowSpan(4, new String[] { "1111", "2", "3", "4", "5", "6", "7", "8", "9", "10" }, null, true);
		ex.build(true);
//		List<Map<String, Object>> listMap = Lists.newArrayList();
//		ex.setDateFmt(DateUtils.DF_YMD_ZH);
//		ex.darwRowColSpanRowCellType(0, new RowCellType() {
//			@Override
//			public String[] generCollection(List t) {
//				return new String[] {};
//			}
//
//		}, listMap, null, false);
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

}
