package com.hhly.award.service.award.lottery.high.ssc.calculate;

import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 一星
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class OneCalculate implements ICalculate {
	private String prize = "一星";
	@Override
	public WinInfo simple(String content, String[] drawCode) {
		int num = 0;
		char code = drawCode[4].charAt(0);
		for (byte b : content.getBytes()) {
			if(b == code){
				num++;
			}
		}
		return new WinInfo(num, prize);
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
