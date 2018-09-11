package com.hhly.award.service.award.lottery.high.ssc.calculate;

import com.hhly.award.service.award.lottery.high.WinInfo;

public interface ICalculate {

	/**
	 * 单式计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:51:29
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo simple(String content,String [] drawCode);
	/**
	 * 复试计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:54:12
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo complex(String content,String [] drawCode);
	/**
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月11日 下午3:25:29
	 * @param content
	 * @param sum
	 * @return
	 */
	WinInfo sum(String content, String twoSum, String threeSum,String [] drawCode);
}
