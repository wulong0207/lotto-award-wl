package com.hhly.award.service.award.lottery.high.ssc.calculate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 三星直选
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ThreeDirectCalculate implements ICalculate {
	
	private String prize = "三星直选";
	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] codes = StringUtils.tokenizeToStringArray(content, "-|;");
		int count = 0;
		int num = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			int y  = i % 3;
			if (y == 1) {
				if (code.equals(drawCode[2])) {
					num++;
				}
			}else if (y == 2) {
				if (code.equals(drawCode[3])) {
					num++;
				}
			} else {
				if (code.equals(drawCode[4])) {
					num++;
				}
				if (num == 3) {
					count++;
				}
				num = 0;
			}
		}
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		String[] codes = StringUtils.tokenizeToStringArray(content, "-|");
		int count = 0;
		for (int i = 0; i < codes.length; i++) {
			if (i == 0) {
				if (codes[i].indexOf(drawCode[2]) == -1) {
					break;
				}
			}if (i == 1) {
				if (codes[i].indexOf(drawCode[3]) == -1) {
					break;
				}
			} else if (i == 2) {
				if (codes[i].indexOf(drawCode[4]) != -1) {
					count = 1;
				}
				break;
			}
		}
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo sum(String content, String twoSum, String threeSum,String [] drawCode) {
		int count = 0;
		if (threeSum.equals(content)) {
			count = 1;
		}
		return new WinInfo(count, prize);
	}

}
