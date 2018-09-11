package com.hhly.award.service.award.entity;
/**
 * @desc 失败信息
 * @author jiangwei
 * @date 2017年7月20日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class FailOrderBO {
	//开奖订单号
	private String orderCode;
	//开奖错误信息
	private String message;

	
	public FailOrderBO(String orderCode, String message) {
		this.orderCode = orderCode;
		this.message = message;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
