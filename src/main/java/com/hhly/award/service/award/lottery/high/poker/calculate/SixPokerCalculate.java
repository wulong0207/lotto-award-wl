package com.hhly.award.service.award.lottery.high.poker.calculate;

import org.springframework.stereotype.Service;
/**
 * @desc 任选六
 * @author jiangwei
 * @date 2017年7月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class SixPokerCalculate extends AbstractCalculate {

	@Override
	public String getPrize() {
		return "任选六";
	}

	@Override
	public int getCount() {
		return 6;
	}

}
