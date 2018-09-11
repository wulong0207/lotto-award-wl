package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.stereotype.Service;

import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 乐选四
 * @author jiangwei
 * @date 2017年10月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class L4Calculate extends AbstractHappyCalculate {

	@Override
	protected int getNum() {
		return 4;
	}

	@Override
	protected WinInfo compute(boolean[] winCondition,String[] tempCodes,String[] drawCode) {
		int num = 0;
		for (boolean win : winCondition) {
			if (win) {
				num++;
			}
		}
		String prize = "乐四一等";
		int count = 0;
		if(num == 4){
			count = 1;
			prize = "乐四一等";
		}else if(num == 3){
			count = 1;
			prize = "乐四二等";
		}
		return new WinInfo(count, prize);
	}

}
