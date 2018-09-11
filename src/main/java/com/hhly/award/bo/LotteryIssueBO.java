package com.hhly.award.bo;

import com.hhly.award.base.common.BaseBO;

/**
 * 返回彩种期号信息
 * 
 * @author HouXB
 *
 */
@SuppressWarnings("serial")
public class LotteryIssueBO extends BaseBO {
	/**
	 * 开奖号码
	 */
	private String drawCode;
	/**
	 * 格式例如： 一等奖,2,10000000 | 二等奖,5,200000 |用 | 线隔开；代表 奖项，注数，每注中奖金额
	 */
	private String drawDetail;
	
	public String getDrawCode() {
		return drawCode;
	}
	
	public void setDrawCode(String drawCode) {
		this.drawCode = drawCode;
	}
	
	public String getDrawDetail() {
		return drawDetail;
	}
	public void setDrawDetail(String drawDetail) {
		this.drawDetail = drawDetail;
	}
	
	
}
