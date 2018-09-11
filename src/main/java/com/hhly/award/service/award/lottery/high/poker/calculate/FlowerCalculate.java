package com.hhly.award.service.award.lottery.high.poker.calculate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 同花
 * @author jiangwei
 * @date 2017年7月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class FlowerCalculate implements ICalculate{

	@Override
	public WinInfo simple(List<String> num, String pairs, String flower, boolean sequence, String content) {
		int n = 0;
		String prize = "同花单选";
		if(StringUtils.isNotEmpty(flower)){
			if("AT".equals(content)){
				n = 1;
				prize = "同花包选";
			}else if(content.indexOf(flower) != -1){
				n = 1;
			}
		}
		return new WinInfo(n, prize);
	}

	@Override
	public WinInfo complex(List<String> num, String pairs, String flower, boolean sequence, String content) {
		throw new ServiceRuntimeException("票格式错误");
	}

	@Override
	public WinInfo gallDrag(List<String> num, String pairs, String flower, boolean sequence, String content) {
		throw new ServiceRuntimeException("票格式错误");
	}
}
