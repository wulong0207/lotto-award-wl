package com.hhly.award.service.award.entity;
/**
 * @desc 中奖信息
 * @author jiangwei
 * @date 2017年5月26日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class WinInfoBO {
	//中奖金额
	private int money;
	//中奖信息
	private String detail;
	
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	
}
