package com.hhly.award.service.award;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hhly.award.service.award.entity.FailOrderBO;

/**
 * @desc 保存开奖信息
 * @author jiangwei
 * @date 2017年5月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class OrderAwardResult {
	/**
	 * 彩种
	 */
	private int lotteryCode;
	/**
	 * 彩期
	 */
	private String lotteryIssue;
	/**
	 * 开始开奖时间
	 */
	private Date startTime;
	/**
	 * 截止时间
	 */
	private Date endTime;
	/**
	 * 总共订单数
	 */
	private int total;
	/**
	 * 成功数
	 */
	private int success;
	/**
	 * 开奖失败订单
	 */
	private List<String> failOrder;
	/**
	 * 开奖失败订单
	 */
	private List<String> failMessage;
	/**
	 * 开奖号码
	 */
	private String drawCode;

	private int winCount;

	public OrderAwardResult(int total, int lotteryCode, String lotteryIssue) {
		startTime = new Date();
		failOrder = new ArrayList<>();
		failMessage = new ArrayList<>();
		this.total = total;
		this.lotteryCode = lotteryCode;
		this.lotteryIssue = lotteryIssue;
	}

	/**
	 * 添加处理结果
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午3:39:26
	 * @param success
	 *            成功
	 * @param fail
	 *            失败
	 */
	public void add(int success, List<FailOrderBO> fail) {
		add(0, success, fail);
	}

	/**
	 * 添加处理结果
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午3:39:26
	 * @param winCount
	 *            中奖个数
	 * @param success
	 *            成功
	 * @param fail
	 *            失败
	 */
	public void add(int winCount, int success, List<FailOrderBO> fail) {
		synchronized (this) {
			this.winCount += winCount;
			this.success += success;
			if (fail != null) {
				for (FailOrderBO failOrderBO : fail) {
					failOrder.add(failOrderBO.getOrderCode());
					failMessage.add(failOrderBO.getOrderCode() + ":" + failOrderBO.getMessage());
				}
			}
		}
	}

	/**
	 * 服务器异常，添加失败订单
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月20日 下午4:13:44
	 * @param fail
	 */
	public void addFailOrder(List<String> fail) {
		synchronized (this) {
			if (fail != null) {
				for (String order : fail) {
					failOrder.add(order);
					failMessage.add(order + ":服务器异常");
				}
			}
		}
	}

	/**
	 * 判断是否开奖完成
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月27日 上午11:53:22
	 * @return
	 */
	public boolean isAccomplish() {
		return total <= (success + failOrder.size());
	}

	/**
	 * 是否全部开奖成功
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月20日 下午6:26:43
	 * @return
	 */
	public boolean isAllSuccess() {
		return total == success;
	}

	/**
	 * 是否正在开奖
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月24日 下午6:02:51
	 * @return
	 */
	public boolean isAward() {
		return total > (success + failOrder.size());
	}

	public int getLotteryCode() {
		return lotteryCode;
	}

	public String getLotteryIssue() {
		return lotteryIssue;
	}

	public int getTotal() {
		return total;
	}

	public int getSuccess() {
		return success;
	}

	public List<String> getFailOrder() {
		return failOrder;
	}

	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the drawCode
	 */
	public String getDrawCode() {
		return drawCode;
	}

	/**
	 * @param drawCode
	 *            the drawCode to set
	 */
	public void setDrawCode(String drawCode) {
		this.drawCode = drawCode;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the failMessage
	 */
	public List<String> getFailMessage() {
		return failMessage;
	}

	/**
	 * @param failMessage
	 *            the failMessage to set
	 */
	public void setFailMessage(List<String> failMessage) {
		this.failMessage = failMessage;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

}
