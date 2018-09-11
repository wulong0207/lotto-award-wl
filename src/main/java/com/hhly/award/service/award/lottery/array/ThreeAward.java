package com.hhly.award.service.award.lottery.array;

import org.springframework.stereotype.Service;

/**
 * @desc 排列3开奖
 * @author jiangwei
 * @date 2017年6月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ThreeAward extends AbstractArrayAward {

	@Override
	protected int getType(int childCode) {
		switch (childCode) {
		case 10401:
			return 1;
		case 10402:
			return 2;
		case 10403:
			return 3;
		default:
			break;
		}
		return 0;
	
	}

}
