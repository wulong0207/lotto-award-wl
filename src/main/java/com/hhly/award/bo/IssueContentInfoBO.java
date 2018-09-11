package com.hhly.award.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.hhly.award.base.common.BaseBO;
/**
 * 
 * @ClassName: IssueContentInfoBO 
 * @Description: 推荐内容表，关联推荐表，代替部分订单表功能
 * @author wuLong
 * @date 2018年1月5日 下午2:43:16 
 *
 */
@SuppressWarnings("serial")
public class IssueContentInfoBO extends BaseBO {
	public int id;
	/**
	 * 原始内容表主键ID
	 */
	public int issueContentOriginalId;
	/**
	 * 彩期
	 */
	public String issueCode;
	/**
	 * 标准投注内容
	 */
	public String planContent;
	/**
	 * 1：未开奖2：未中奖3：已中奖
	 */
	public int orderStatus;
	/**
	 * 彩种编号
	 */
	public int lotteryCode;
	/**
	 * 子玩法编号
	 */
	public int lotteryChildCode;
	/**
	 * 推荐查看金额
	 */
	public BigDecimal amount;
	
	public Date createTime;
	/**
	 * 竞技彩购买的场次编号
	 */
	public String buyScreen;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIssueContentOriginalId() {
		return issueContentOriginalId;
	}

	public void setIssueContentOriginalId(int issueContentOriginalId) {
		this.issueContentOriginalId = issueContentOriginalId;
	}

	public String getIssueCode() {
		return issueCode;
	}

	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}

	public String getPlanContent() {
		return planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}


	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(int lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public int getLotteryChildCode() {
		return lotteryChildCode;
	}

	public void setLotteryChildCode(int lotteryChildCode) {
		this.lotteryChildCode = lotteryChildCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBuyScreen() {
		return buyScreen;
	}

	public void setBuyScreen(String buyScreen) {
		this.buyScreen = buyScreen;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
