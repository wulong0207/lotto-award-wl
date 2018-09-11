package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.stereotype.Service;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 乐选3
 * @author jiangwei
 * @date 2017年10月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class L3Calculate extends AbstractHappyCalculate {

	@Override
	protected int getNum() {
		return 3;
	}

	@Override
	protected WinInfo compute(boolean[] winCondition, String[] tempCodes, String[] drawCode) {
		int num = 0;
		for (boolean win : winCondition) {
			if (win) {
				num++;
			}
		}
		String prize = "乐三三等";
		int count = 0;
		if (num == 3) {
			count = 1;
			if (winCondition[0] && winCondition[1] && winCondition[2]) {
				if (tempCodes[0].equals(drawCode[0]) 
						&& tempCodes[1].equals(drawCode[1])
						&& tempCodes[2].equals(drawCode[2])) {
					prize = "乐三一等";
				}else{
					prize = "乐三二等";
				}
			}
		}
		return new WinInfo(count,prize);
	}

}
