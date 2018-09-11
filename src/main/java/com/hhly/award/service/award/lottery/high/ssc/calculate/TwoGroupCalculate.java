package com.hhly.award.service.award.lottery.high.ssc.calculate;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 二星组选
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class TwoGroupCalculate implements ICalculate {

	private String prize = "二星组选";

	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] codes = StringUtils.tokenizeToStringArray(content, ",;");
		int num = 0, count = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			if (code.equals(drawCode[3]) || code.equals(drawCode[4])) {
				num++;
			}
			if (i % 2 == 0) {
				if (num == 2) {
					count++;
				}
				num = 0;
			}
		}
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		String[] codes = content.split(",");
		int num = 0;
		for (int j = 0; j < codes.length; j++) {
			for (int i = 3; i < drawCode.length; i++) {
				if (codes[j].equals(drawCode[i])) {
					num++;
					break;
				}
			}
		}
		int count = num == 2 ? 1 : 0;
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo sum(String content, String twoSum, String threeSum, String[] drawCode) {
		int count = 0;
		if (twoSum.equals(content)) {
			count = 1;
			if (Objects.equals(drawCode[3], drawCode[4])) {
				count = 2;
			}
		}
		return new WinInfo(count, prize);
	}

}
