package com.hhly.award.service.award.lottery.high.ssc.calculate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 大小单双
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class SizeCalculate implements ICalculate {
	
	private String prize = "大小单双";
	@Override
	public WinInfo simple(String content, String[] drawCode) {
		// 大2 小1 单3 双4
		int s = Integer.parseInt(drawCode[3]);
		int g = Integer.parseInt(drawCode[4]);
		String s1, s2, g1, g2 = "";
		s1 = s >= 5 ? "2" : "1";// 十位大小
		g1 = g >= 5 ? "2" : "1";// 个位大小
		s2 = s % 2 == 1 ? "3" : "4";// 十位单双
		g2 = g % 2 == 1 ? "3" : "4";// 个位单双
		String[] codes = StringUtils.tokenizeToStringArray(content, "|;");
		int count = 0;
		int num = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			if (i % 2 == 1) {
				if (code.equals(s1) || code.equals(s2)) {
					num++;
				}
			} else {
				if (code.equals(g1) || code.equals(g2)) {
					num++;
				}
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
		throw new ServiceRuntimeException("票玩法类型错误，不能计算开奖");
	}

	@Override
	public WinInfo sum(String content, String twoSum, String threeSum,String [] drawCode) {
		throw new ServiceRuntimeException("票玩法类型错误，不能计算开奖");
	}

}
