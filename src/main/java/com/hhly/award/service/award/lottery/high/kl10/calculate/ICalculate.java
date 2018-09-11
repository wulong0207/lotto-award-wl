package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * 算奖
 * @desc
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICalculate {
	/**
	 * 单式计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月5日 上午8:51:29
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo simple(String content,List<String> drawCode);
	/**
	 * 复试计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月5日 上午8:54:12
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo complex(String content,List<String> drawCode);
}
