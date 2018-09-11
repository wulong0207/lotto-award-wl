package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 选三前直
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ThreeDirectTenCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, List<String> drawCode) {
		String draw = drawCode.get(0)+"|"+drawCode.get(1)+"|"+drawCode.get(2);
		String codes[] = content.split(";");
		int count = 0;
		for (String code : codes) {
			if(draw.equals(code)){
				count ++;
			}
		}
		return new WinInfo(count, "选三前直");
	}

	@Override
	public WinInfo complex(String content, List<String> drawCode) {
		throw new ServiceRuntimeException("票选好方式错误");
	}
}
