package com.hhly.award.service.award.entity;

import java.util.List;

public class AwardOrderBO {
	// 开奖失败订单
	private List<FailOrderBO> fail;
	// 中奖订单
	private int winCount;

	public List<FailOrderBO> getFail() {
		return fail;
	}

	public void setFail(List<FailOrderBO> fail) {
		this.fail = fail;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

}
