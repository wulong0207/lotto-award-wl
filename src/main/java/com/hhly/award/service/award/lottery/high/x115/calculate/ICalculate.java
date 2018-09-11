package com.hhly.award.service.award.lottery.high.x115.calculate;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 各种玩法计算规则
 * 实现类必须是无状态的类
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public  interface ICalculate {
	/**
	 * 单式计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:51:29
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo  simple(String content,String [] drawCode);
	/**
	 * 复试计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:54:12
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo  complex(String content,String [] drawCode);
	/**
	 * 胆拖计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:54:12
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @return 中奖注数
	 */
	WinInfo  gallDrag(String content,String [] drawCode);
}
