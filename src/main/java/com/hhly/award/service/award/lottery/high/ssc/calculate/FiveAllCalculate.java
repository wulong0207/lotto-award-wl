package com.hhly.award.service.award.lottery.high.ssc.calculate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 五星通选
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class FiveAllCalculate implements ICalculate {

	private String prize = "五星通选";

	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] codes = StringUtils.tokenizeToStringArray(content, "|;");
		WinInfo info = new WinInfo(0, prize);
		for (int i = 0; i < codes.length; i += 5) {
			boolean e1 = false, e2 = false, e3 = false, e4 = false, e5 = false;
			if (drawCode[0].equals(codes[i])) {
				e1 = true;
			}
			if (drawCode[1].equals(codes[i + 1])) {
				e2 = true;
			}
			if (drawCode[2].equals(codes[i + 2])) {
				e3 = true;
			}
			if (drawCode[3].equals(codes[i + 3])) {
				e4 = true;
			}
			if (drawCode[4].equals(codes[i + 4])) {
				e5 = true;
			}
			if (e1 && e2 && e3 && e4 && e5) {
				info.addPrize(1, prize);
			} else if (e1 && e2 && e3) {
				info.addPrize(1, "五星通选前三");
			} else if (e3 && e4 && e5) {
				info.addPrize(1, "五星通选后三");
			} else {
				if (e1 && e2) {
					info.addPrize(1, "五星通选前二");
				}
				if (e4 && e5) {
					info.addPrize(1, "五星通选后二");
				}
			}
		}
		return info;
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
