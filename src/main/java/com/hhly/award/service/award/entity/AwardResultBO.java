package com.hhly.award.service.award.entity;

import java.util.ArrayList;
import java.util.List;

import com.hhly.award.base.common.BaseBO;

/**
 * @desc 开奖结果
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AwardResultBO extends BaseBO {
	// 成功数
	private int success;
	// 失败数
	private int fail;
	// 开奖失败订单
	private List<String> failOrder = new ArrayList<>();
	// 已开奖时间
	private long awardSecond;
	// 预计剩余开奖时间
	private long planSecond;
	// 总订单数
	private int total;
	//开奖号码
	private String drawCode;
	//开奖错误信息
	private List<String> failMessage  = new ArrayList<>();
	//中奖订单
	private int winCount;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public List<String> getFailOrder() {
		return failOrder;
	}

	public void setFailOrder(List<String> failOrder) {
		this.failOrder = failOrder;
	}

	public long getAwardSecond() {
		return awardSecond;
	}

	public void setAwardSecond(long awardSecond) {
		this.awardSecond = awardSecond;
	}

	public long getPlanSecond() {
		return planSecond;
	}

	public void setPlanSecond(long planSecond) {
		this.planSecond = planSecond;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the drawCode
	 */
	public String getDrawCode() {
		return drawCode;
	}

	/**
	 * @param drawCode the drawCode to set
	 */
	public void setDrawCode(String drawCode) {
		this.drawCode = drawCode;
	}

	@Override
	public String toString() {
		return "AwardResultBO [success=" + success + ", fail=" + fail + ", failOrder="
				+ failOrder + ", awardSecond=" + awardSecond + ", planSecond=" + planSecond + ", total=" + total
				+ ", drawCode=" + drawCode + "]";
	}

	/**
	 * @return the failMessage
	 */
	public List<String> getFailMessage() {
		return failMessage;
	}

	/**
	 * @param failMessage the failMessage to set
	 */
	public void setFailMessage(List<String> failMessage) {
		this.failMessage = failMessage;
	}

	/**
	 * @return the winCount
	 */
	public int getWinCount() {
		return winCount;
	}

	/**
	 * @param winCount the winCount to set
	 */
	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}
	
}
