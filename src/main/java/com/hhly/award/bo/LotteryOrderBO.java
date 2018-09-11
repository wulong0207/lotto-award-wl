package com.hhly.award.bo;

import java.util.ArrayList;
import java.util.List;

public class LotteryOrderBO {
	
    private Integer lotteryCode;
     
    private String lotteryIssue;
     
    private List<String> orders;

	public LotteryOrderBO(Integer lotteryCode, String lotteryIssue) {
		this.lotteryCode = lotteryCode;
		this.lotteryIssue = lotteryIssue;
		orders = new ArrayList<>();
	}

	public Integer getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(Integer lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public String getLotteryIssue() {
		return lotteryIssue;
	}

	public void setLotteryIssue(String lotteryIssue) {
		this.lotteryIssue = lotteryIssue;
	}

	public List<String> getOrders() {
		return orders;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}
     
     
}
