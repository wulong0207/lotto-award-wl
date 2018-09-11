package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 选红数投
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class OneRedTenCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, List<String> drawCode) {
		String first  = drawCode.get(0);
		int count = 0;
		if("19".equals(first) || "20".equals(first)){
			count = content.split(";").length;
		}
		return new WinInfo(count, "选一红投");
	}

	@Override
	public WinInfo complex(String content, List<String> drawCode) {
		throw new ServiceRuntimeException("票投注类型错误，不存在复试开奖");
	}

}
