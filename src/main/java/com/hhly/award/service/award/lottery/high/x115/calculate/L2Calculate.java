package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.stereotype.Service;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 乐选2
 * @author jiangwei
 * @date 2017年10月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class L2Calculate extends AbstractHappyCalculate {

	@Override
	protected int getNum() {
		return 2;
	}

	@Override
	protected WinInfo compute(boolean[] winCondition, String[] tempCodes, String[] drawCode) {
		int num = 0;
		for (boolean win : winCondition) {
			if (win) {
				num++;
			}
		}
		String prize = "乐二三等";
		int count = 0;
		if (num == 2) {
			count = 1;
			if (winCondition[0] && winCondition[1]) {
				if (tempCodes[0].equals(drawCode[0]) 
						&& tempCodes[1].equals(drawCode[1])) {
					prize = "乐二一等";
				}else{
					prize = "乐二二等";
				}
			}
		}
		return new WinInfo(count,prize);
	}

}
