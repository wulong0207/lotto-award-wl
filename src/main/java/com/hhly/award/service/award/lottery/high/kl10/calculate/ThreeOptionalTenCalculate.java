package com.hhly.award.service.award.lottery.high.kl10.calculate;

import org.springframework.stereotype.Service;
/**
 * @desc 任选三
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ThreeOptionalTenCalculate extends AbstractOptionalCalculate {

	@Override
	public int getNum() {
		return 3;
	}

	@Override
	public String getPrize() {
		return "任选三";
	}

}
