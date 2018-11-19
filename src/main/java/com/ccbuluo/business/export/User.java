package com.ccbuluo.business.export;

import com.ccbuluo.excel.imports.ExcelField;

public class User {
	@ExcelField(colIndex = 0)
	private Integer id;
	@ExcelField(colIndex = 2)
	private String name;
	@ExcelField(colIndex = 1)
	private String no;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", no=" + no + "]";
	}

}
