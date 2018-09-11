package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 选三前组
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ThreeGroupTenCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, List<String> drawCode) {
		String[] codes = content.split("[,;]");
		int num = 0;
		int count = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			for (int j = 0; j < 3; j++) {
				if (code.equals(drawCode.get(j))) {
					count++;
				}
			}
			if (i % 3 == 0) {
				if (count == 3) {
					num++;
				}
				count = 0;
			}
		}
		return new WinInfo(num, "选三前组");
	}

	@Override
	public WinInfo complex(String content, List<String> drawCode) {
		String[] codes = content.split(",");
		int count = 0;
		for (String code : codes) {
			for (int i = 0; i < 3; i++) {
				if (code.equals(drawCode.get(i))) {
					count++;
					break;
				}
			}
		}
		int num = count == 3 ? 1 : 0;
		return new WinInfo(num, "选三前组");
	}

}
