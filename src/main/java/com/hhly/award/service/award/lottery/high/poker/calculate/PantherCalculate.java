package com.hhly.award.service.award.lottery.high.poker.calculate;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 豹子
 * @author jiangwei
 * @date 2017年7月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class PantherCalculate implements ICalculate {

	@Override
	public WinInfo simple(List<String> num, String pairs, String flower, boolean sequence, String content) {
		int n = 0;
		String prize = "豹子单选";
		if(num.size() == 1){
			if("Y,Y,Y".equals(content)){
				n= 1;
				prize = "豹子包选";
			}else if(content.indexOf(num.get(0)) != -1){
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
