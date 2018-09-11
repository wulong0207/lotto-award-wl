package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.stereotype.Service;
/**
 * @desc 前三组选
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class G3Calculate extends AbstractGroupCalculate {

	@Override
	public String getPrize() {
		return "前三组选";
	}

	@Override
	public int getNum() {
		return 3;
	}

}
