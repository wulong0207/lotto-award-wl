package com.hhly.award.service.award.lottery.high.poker.calculate;

import java.util.List;

import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 快乐扑克3开奖计算
 * @author jiangwei
 * @date 2017年7月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICalculate {
	/**
	 * 单式
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月14日 上午11:49:01
	 * @param num 号码
	 * @param pairs 对子
	 * @param flower 同花
	 * @param sequence 顺子
	 * @return
	 */
	WinInfo simple(List<String> num, String pairs, String flower, boolean sequence,String content);
	/**
	 * 复式
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月14日 上午11:49:01
	 * @param num
	 * @param pairs
	 * @param flower
	 * @param sequence
	 * @return
	 */
	WinInfo complex(List<String> num, String pairs, String flower, boolean sequence,String content);
	/**
	 * 胆拖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月14日 上午11:49:01
	 * @param num
	 * @param pairs
	 * @param flower
	 * @param sequence
	 * @return
	 */
	WinInfo gallDrag(List<String> num, String pairs, String flower, boolean sequence,String content);
}
