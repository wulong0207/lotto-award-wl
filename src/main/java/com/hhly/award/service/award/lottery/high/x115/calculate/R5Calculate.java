package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.stereotype.Service;
/**
 * @desc 任五
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class R5Calculate extends AbstractRCalculate {

	@Override
	public String getPrize() {
		return "任五";
	}

	@Override
	public int getNum() {
		return 5;
	}

}
