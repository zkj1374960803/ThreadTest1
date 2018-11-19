package com.ccbuluo.business.export;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExportRowSpanUtils {
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private Map<Long, List<String>> colMap;
	private Map<Long, Long> colSpanStartIndex;
	private Long countRow = 0l;// sheet总行数
	private List<Long> colSpanIndex; // 合并列索引
	public Long startIndex = 0l; // sheet开始行
	private File outFile = null; // 输出文件
	private Long referenceColIndex = null;// 合并参考列,其它列合并不会超过参考列
	private List<CellRangeAddress> listCras = Lists.newArrayList();

	public ExportRowSpanUtils(HSSFWorkbook wb, HSSFSheet sheet, File outFile, List<Long> colSpanIndex) {
		this.wb = wb;
		this.outFile = outFile;
	}

	public ExportRowSpanUtils(HSSFSheet sheet, File outFile, List<Long> colSpanIndex) {

	}

	public ExportRowSpanUtils(File outFile, List<Long> colSpanIndex) {
		this.wb = new HSSFWorkbook();
		this.sheet = wb.createSheet();
		this.colMap = Maps.newHashMap();
		this.colSpanIndex = colSpanIndex;
		this.colSpanStartIndex = Maps.newHashMap();
		if (null != colSpanIndex && colSpanIndex.size() > 0) {
			for (int i = 0; i < colSpanIndex.size(); i++) {
				Long key = colSpanIndex.get(i);
				this.colMap.put(key, new ArrayList<String>());
				this.colSpanStartIndex.put(key, this.startIndex);
			}
		}
	}

	public int drawRow(int rowIndex, String[] str) {
		this.countRow++;
		if (colSpanIndex.size() > str.length) {
			throw new IllegalArgumentException("rowspan length is  long");
		}
		HSSFRow row = sheet.createRow(rowIndex);
		int cellIndex = 0;
		boolean referenceColIndexTag = false;

		for (int i = 0; i < str.length; i++) {
			String currColValue = str[i];
			Long colIndexLong = Long.valueOf(cellIndex);
			if (colSpanIndex.contains(colIndexLong)) {// 目标列是要合并列
				List<String> list = this.colMap.get(colIndexLong);

				list.add(currColValue);
				if (list.size() > 1) {
					String beforeVal = list.get(list.size() - 2);
					// 内容相同，并且参考列没有合并过，
					if (beforeVal.equals(currColValue) && !referenceColIndexTag) {
						cellIndex++;
						continue;
					} else {
						// 该列上一行该合并了
						Long colSpanStart = colSpanStartIndex.get(colIndexLong);
						CellRangeAddress mr = new CellRangeAddress(colSpanStart.intValue(), rowIndex - 1, cellIndex,
								cellIndex);
						this.sheet.addMergedRegion(mr);
						if (referenceColIndex != null && referenceColIndex.intValue() == cellIndex) {
							referenceColIndexTag = true;
						}
						this.listCras.add(mr);
						colSpanStartIndex.put(colIndexLong, Long.valueOf(rowIndex));
					}
				}
			}
			HSSFCell cell = row.createCell(cellIndex);
			cell.setCellValue(currColValue);
			cell.setCellStyle(getCellStyle(true));
			cellIndex++;
		}
		return rowIndex++;
	}

	public int drawRow_(int rowIndex, String[] str) {
		this.countRow++;
		if (colSpanIndex.size() > str.length) {
			throw new IllegalArgumentException("rowspan length is  long");
		}
		HSSFRow row = sheet.createRow(rowIndex);
		int cellIndex = 0;
		boolean referenceColIndexTag = false;

		for (int i = 0; i < str.length; i++) {
			String currColValue = str[i];
			Long colIndexLong = Long.valueOf(cellIndex);
			if (colSpanIndex.contains(colIndexLong)) {// 目标列是要合并列
				List<String> list = this.colMap.get(colIndexLong);

				list.add(currColValue);
				if (list.size() > 1) {
					String beforeVal = list.get(list.size() - 2);
					// 内容相同，并且参考列没有合并过，
					if (beforeVal.equals(currColValue) && !referenceColIndexTag) {
						cellIndex++;
						continue;
					} else {
						// 该列上一行该合并了
						Long colSpanStart = colSpanStartIndex.get(colIndexLong);
						CellRangeAddress mr = new CellRangeAddress(colSpanStart.intValue(), rowIndex - 1, cellIndex,
								cellIndex);
						this.sheet.addMergedRegion(mr);
						if (referenceColIndex != null && referenceColIndex.intValue() == cellIndex) {
							referenceColIndexTag = true;
						}
						this.listCras.add(mr);
						colSpanStartIndex.put(colIndexLong, Long.valueOf(rowIndex));
					}
				}
			}
			HSSFCell cell = row.createCell(cellIndex);
			cell.setCellValue(currColValue);
			cell.setCellStyle(getCellStyle(true));
			cellIndex++;
		}
		return rowIndex++;
	}

	public boolean getReferenceColIndexisRowSpan() {
		boolean result = false;
		return result;
	}

	public String getCellValue(int rowInde, int colIdex) {
		HSSFRow row = this.sheet.getRow(rowInde);
		String stringCellValue = row.getCell(colIdex).getStringCellValue();
		return stringCellValue;
	}

	public void build() {
		// 针对最后一次做合并
		Set<Long> keySet = colSpanStartIndex.keySet();
		Iterator<Long> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Long rowIndex = iterator.next();
			Long lastStartColIndex = colSpanStartIndex.get(rowIndex);
			CellRangeAddress mr = new CellRangeAddress(lastStartColIndex.intValue(), this.countRow.intValue() - 1,
					rowIndex.intValue(), rowIndex.intValue());
			this.listCras.add(mr);
			this.sheet.addMergedRegion(mr);
		}
		this.setBorders(this.listCras);
		try {
			FileOutputStream outputStream = new FileOutputStream("d:/excel.xls");
			wb.write(outputStream);
			outputStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HSSFCellStyle getCellStyle(boolean isBorder) {
		HSSFCellStyle cellStyle = this.wb.createCellStyle();
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

	public void setBorders(List<CellRangeAddress> ranges) {
		for (CellRangeAddress range : ranges) {
			RegionUtil.setBorderLeft(1, range, this.sheet, this.wb);
			RegionUtil.setBorderBottom(1, range, this.sheet, this.wb);
			RegionUtil.setBorderRight(1, range, this.sheet, this.wb);
			RegionUtil.setBorderTop(1, range, this.sheet, this.wb);
		}
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public Long getCountRow() {
		return countRow;
	}

	public void setCountRow(Long countRow) {
		this.countRow = countRow;
	}

	public File getOutFile() {
		return outFile;
	}

	public void setOutFile(File outFile) {
		this.outFile = outFile;
	}

	public Long getReferenceColIndex() {
		return referenceColIndex;
	}

	public void setReferenceColIndex(Long referenceColIndex) {
		this.referenceColIndex = referenceColIndex;
	}

	public static void main(String[] args) throws IOException {
		File file = new File("d:/excel.xls");
		ExportRowSpanUtils st = new ExportRowSpanUtils(file, Arrays.asList(0l, 1l, 2l, 3l));
		//st.setReferenceColIndex(1l);
		String[] str = new String[] { "1", "2", "3", "4" };
		String[] str1 = new String[] { "1", "2", "3", "4" };
		String[] str2 = new String[] { "t", "2", "3", "4" };
		String[] str3 = new String[] { "x", "3", "3", "4" };
		String[] str4 = new String[] { "x", "3", "3", "4" };

		String[] str5 = new String[] { "a", "2", "3", "4" };
		String[] str6 = new String[] { "a", "2", "3", "4" };
		String[] str7 = new String[] { "b", "2", "3", "4" };

		String[] str8 = new String[] { "c", "u", "x", "4" };
		String[] str9 = new String[] { "c", "2", "x", "4" };
		String[] str10 = new String[] { "c", "2", "x", "4" };
		String[] str11 = new String[] { "c", "11", "2", "4" };
		String[] str12 = new String[] { "c", "11", "2", "4" };
		String[] str13 = new String[] { "c", "22", "2", "4" };
		String[] str14 = new String[] { "c", "22", "2", "4" };
		st.drawRow(0, str);
		st.drawRow(1, str1);
		st.drawRow(2, str2);
		st.drawRow(3, str3);
		st.drawRow(4, str4);
		st.drawRow(5, str5);
		st.drawRow(6, str6);
		st.drawRow(7, str7);
		st.drawRow(8, str8);
		st.drawRow(9, str9);
		st.drawRow(10, str10);
		st.drawRow(11, str11);
		st.drawRow(12, str12);
		st.drawRow(13, str13);
		st.drawRow(14, str14);
		st.build();
		// sheet.addMergedRegion(new CellRangeAddress(0, 9, 0, 0));
		// sheet.addMergedRegion(new CellRangeAddress(0, 5, 0, 0));
	}
}
