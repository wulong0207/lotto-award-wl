package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 选一数投
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class OneCountTenCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, List<String> drawCode) {
		String[] codes  =  content.split("[,;]");
		String first  = drawCode.get(0);
		int count = 0;
		for (String code : codes) {
			if(code.equals(first)){
				count++;
			}
		}
		return new WinInfo(count, "选一数投");
	}

	@Override
	public WinInfo complex(String content, List<String> drawCode) {
		return simple(content, drawCode);
	}

}
