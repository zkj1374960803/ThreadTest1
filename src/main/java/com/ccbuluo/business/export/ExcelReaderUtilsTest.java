package com.ccbuluo.business.export;

import com.ccbuluo.business.entity.BasicCarpartsProduct;
import com.ccbuluo.excel.imports.ExcelReaderUtils;
import com.ccbuluo.excel.imports.ExcelRowReaderBean;
import com.ccbuluo.excel.readpic.ExcelPictruePos;
import com.ccbuluo.excel.readpic.ExcelPictruesUtils;
import com.ccbuluo.excel.readpic.ExcelShapeSaveLocal;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

//import org.apache.tomcat.jdbc.pool.DataSource;
//import com.ccbuluo.DBHelper;

public class ExcelReaderUtilsTest {

	@Test
	public void testBean() throws Exception {
		// 列映射，实体字段，对应的excel列索引
		ExcelRowReaderBean<BasicCarpartsProduct> readerBean = new ExcelRowReaderBean<BasicCarpartsProduct>(BasicCarpartsProduct.class) {
			@Override
			public boolean checkRow(int sheetIndex, int curRow, List<String> rowlist) {
				if (curRow == 0) { // 标题行放过
					return false;
				}
				String join = StringUtils.join(rowlist, ',');
				System.out.println(join + " rowLength:\t " + rowlist.size());
				return true;
			}

		};
		// 以下条件与checkRow效果相同，均可实现跳过某行类型的数据
		// 设置开始读取的行，之前的行被跳过
		readerBean.setStartRow(1);
		// 调协列的宽度，小于宽度的列被跳过
		readerBean.setTargetColLength(4);
		// 读取excel
		ExcelReaderUtils.readExcel(readerBean, "C:\\Users\\m1881\\Desktop\\3.xlsx");
		// 获取读取的结果结果
		List<BasicCarpartsProduct> data = readerBean.getData();
		System.out.println("data.size():\t" + data.size());
		// 后续处理
		data.forEach(System.out::println);


		Map<String, Collection<ExcelPictruePos>> allDate = ExcelPictruesUtils.getAllDate("C:\\Users\\m1881\\Desktop\\3.xlsx", 1, new ExcelShapeSaveLocal("D:\\a"));
		for (Map.Entry<String, Collection<ExcelPictruePos>> entry : allDate.entrySet()) {
			System.out.println(entry.getValue());
		}
	}

	@Test
	public void testColumnMapper() throws Exception {
//		Map<String, Integer> columnIndexMapperBeanField = Maps.newHashMap();
//		// 列映射，实体字段，对应的excel列索引
//		columnIndexMapperBeanField.put("id", 0);
//		columnIndexMapperBeanField.put("no", 1);
//		columnIndexMapperBeanField.put("name", 2);
//		ExcelRowReaderBean<User> readerBean = new ExcelRowReaderBean<User>(User.class, columnIndexMapperBeanField) {
//			@Override
//			public boolean checkRow(int sheetIndex, int curRow, List<String> rowlist) {
//				// 标题行放过, 如有其它业务逻辑，可按此种方法处理，本方法返回false当前行被跳过
//				if (curRow == 0) {
//					return false;
//				}
//				String join = StringUtils.join(rowlist, ',');
//				System.out.println(join + " rowLength:\t " + rowlist.size());
//				return true;
//			}
//		};
//		// 以上条件与checkRow效果相同，均可实现跳过某行类型的数据
//		// 设置开始读取的行，之前的行被跳过
//		readerBean.setStartRow(2);
//		// 调协列的宽度，小于宽度的列被跳过
//		readerBean.setTargetColLength(4);
//		// 读取excel
//		ExcelReaderUtils.readExcel(readerBean, "D:\\aa\\xxx.xlsx");
//		// 获取读取的结果结果
//		List<User> data = readerBean.getData();
//		// 后续处理
//		data.forEach(System.out::println);
	}

//	@Test
//	public void testDB() throws Exception {
//		// 数据库列名字
//		String[] columns = new String[] { "a", "b", "create_date" };
//		// 数据库表名
//		String tableName = "test_001";
//		DataSource dataSource = DBHelper.getInstance();
//		// 数据库jdbcTemplate
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//		ExcelRowReaderDB readerDBx = new ExcelRowReaderDB(jdbcTemplate, tableName, columns, 20) {
//			@Override
//			public boolean checkRow(int sheetIndex, int curRow, List<String> rowlist) {
//				if (curRow == 0) { // 标题行放过
//					return false;
//				}
//				String join = StringUtils.join(rowlist, ',');
//				rowlist.add("new Date()");
//				System.out.println(join + " rowLength:\t " + rowlist.size());
//				return true;
//			}
//		};
//		ExcelReaderUtils.readExcel(readerDBx, "D:\\aa\\xxx.xlsx");
//	}
}
