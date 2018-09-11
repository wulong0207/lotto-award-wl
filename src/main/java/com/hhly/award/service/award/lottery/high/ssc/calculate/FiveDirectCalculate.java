package com.hhly.award.service.award.lottery.high.ssc.calculate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 五星直选
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class FiveDirectCalculate implements ICalculate {
	private String prize = "五星直选";
	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] codes = StringUtils.tokenizeToStringArray(content, "|;");
		int count = 0;
		int num = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			int y = i % 5;
			y = y == 0 ? 5 : y;
			if (code.equals(drawCode[y - 1])) {
				num++;
			}
			if (y == 5) {
				if (num == 5) {
					count++;
				}
				num = 0;
			}
		}
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		String[] codes = StringUtils.tokenizeToStringArray(content, "|");
		int count = 0;
		for (int i = 0; i < codes.length; i++) {
			if(codes[i].indexOf(drawCode[i]) == -1){
				break;
			}
			if(i == 4){
				 count = 1;
			}
		}
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo sum(String content, String twoSum, String threeSum,String [] drawCode) {
		throw new ServiceRuntimeException("票玩法类型错误，不能计算开奖");
	}

}
