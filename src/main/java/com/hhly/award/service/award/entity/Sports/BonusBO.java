package com.hhly.award.service.award.entity.Sports;

import java.math.BigDecimal;

import com.hhly.award.base.common.BaseBO;

@SuppressWarnings("serial")
public class BonusBO extends BaseBO{
	/**
	 * 税前奖金
	 */
	private BigDecimal preWinBonus ;
	/**
	 * 税后奖金
	 */
	private BigDecimal afterWinBonus ;
	public BigDecimal getPreWinBonus() {
		return preWinBonus;
	}
	public void setPreWinBonus(BigDecimal preWinBonus) {
		this.preWinBonus = preWinBonus;
	}
	public BigDecimal getAfterWinBonus() {
		return afterWinBonus;
	}
	public void setAfterWinBonus(BigDecimal afterWinBonus) {
		this.afterWinBonus = afterWinBonus;
	}
}
