package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.stereotype.Service;
/**
 * @desc 任八
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class R8Calculate extends AbstractRCalculate {

	@Override
	public String getPrize() {
		return "任八";
	}

	@Override
	public int getNum() {
		return 8;
	}

}
