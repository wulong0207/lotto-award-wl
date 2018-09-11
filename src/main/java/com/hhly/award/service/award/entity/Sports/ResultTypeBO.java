package com.hhly.award.service.award.entity.Sports;

import com.hhly.award.base.common.BaseBO;

@SuppressWarnings("serial")
public class ResultTypeBO extends BaseBO{
	/**
	 * 彩果
	 */
	private Object cg;
	/**
	 * 中的格式
	 */
	private String type;
	public Object getCg() {
		return cg;
	}
	public void setCg(Object cg) {
		this.cg = cg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
