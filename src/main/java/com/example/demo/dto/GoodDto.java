package com.example.demo.dto;

import java.io.Serializable;

public class GoodDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 主键
	private Long id;
	// 标题
	private String title;
	// 单位
	private String unit;
	// 价格
	private double price;
	// 类型名称
	private String typeName;
	// 类型编号
	private Long typeId;
	
	private Long extraId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getExtraId() {
		return extraId;
	}

	public void setExtraId(Long extraId) {
		this.extraId = extraId;
	}

}
