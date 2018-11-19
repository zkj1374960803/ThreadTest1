package com.ccbuluo.business.export;

import com.ccbuluo.excel.readpic.ExcelPictruePos;
import com.ccbuluo.excel.readpic.ExcelPictruesUtils;
import com.ccbuluo.excel.readpic.ExcelShapeSaveLocal;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.weakref.jmx.internal.guava.collect.Sets;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ExcelPictruesUtilsTest {
	@Test
	public void read() throws InvalidFormatException, IOException {
		String excelPath = "D:\\aa\\xxx.xlsx";
		// 以第一列为参考列（应该是不重复的唯一列）
		// 墨认的策略是保存了本地，可以实现自己的图片处理策略
		Map<String, Collection<ExcelPictruePos>> collect = ExcelPictruesUtils.getAllDate(excelPath, 1,
				new ExcelShapeSaveLocal("d:\\aa"));
		Collection<ExcelPictruePos> pics = collect.get("3603900-H314");
		// 后续逻辑
		pics.forEach(System.out::println);
	}

	@Test
	public void write() {
		String excelPath = "D:\\aa\\xxx.xlsx";
		Map<String, Collection<ExcelPictruePos>> allDate = ExcelPictruesUtils.getAllDate(excelPath, 1,
				new ExcelShapeSaveLocal("d:\\aa"));
		Set<ExcelPictruePos> sets = Sets.newHashSet();
		Collection<Collection<ExcelPictruePos>> values = allDate.values();
		for (Collection<ExcelPictruePos> collection : values) {
			sets.addAll(collection);
		}
		// 将xxx.xlsx中的图片，添加到writetext.xls中，最终输出 writetext111.xls即目标文件
		ExcelPictruesUtils.writePic("d:/writetext.xls", "d:/writetext111.xls", sets);
	}
}
