package com.hhly.award.service.award.entity;

import java.util.Map;

public class WinMoneyBO {
	// 税前
	private double preBonus;
	// 税后
	private double aftBonus;
	// 加奖
	private double addedBonus;
	// 总金额
	private double totalMoney;
	// 税后金额
	private double totalAfterMoney;
	//中奖详情
	private Map<String, Integer> winningDetail;
	

	public WinMoneyBO(double preBonus, double aftBonus, double addedBonus, Map<String, Integer> winningDetail) {
		this.preBonus = preBonus;
		this.aftBonus = aftBonus;
		this.addedBonus = addedBonus;
		this.winningDetail = winningDetail;
	}

	public WinMoneyBO(double preBonus, double aftBonus, double addedBonus, double totalMoney, double totalAfterMoney, Map<String, Integer> winningDetail) {
		this.preBonus = preBonus;
		this.aftBonus = aftBonus;
		this.addedBonus = addedBonus;
		this.totalMoney = totalMoney;
		this.totalAfterMoney = totalAfterMoney;
		this.winningDetail = winningDetail;
	}

	public double getPreBonus() {
		return preBonus;
	}

	public void setPreBonus(double preBonus) {
		this.preBonus = preBonus;
	}

	public double getAftBonus() {
		return aftBonus;
	}

	public void setAftBonus(double aftBonus) {
		this.aftBonus = aftBonus;
	}

	public double getAddedBonus() {
		return addedBonus;
	}

	public void setAddedBonus(double addedBonus) {
		this.addedBonus = addedBonus;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public double getTotalAfterMoney() {
		return totalAfterMoney;
	}

	public void setTotalAfterMoney(double totalAfterMoney) {
		this.totalAfterMoney = totalAfterMoney;
	}

	public Map<String, Integer> getWinningDetail() {
		return winningDetail;
	}

	public void setWinningDetail(Map<String, Integer> winningDetail) {
		this.winningDetail = winningDetail;
	}

	@Override
	public String toString() {
		return "WinMoneyBO [preBonus=" + preBonus + ", aftBonus=" + aftBonus + ", addedBonus=" + addedBonus
				+ ", totalMoney=" + totalMoney + ", totalAfterMoney=" + totalAfterMoney + ", winningDetail="
				+ winningDetail + "]";
	}

}
