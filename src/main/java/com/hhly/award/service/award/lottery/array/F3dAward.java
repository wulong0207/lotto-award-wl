package com.hhly.award.service.award.lottery.array;

import org.springframework.stereotype.Service;

/**
 * @desc 福彩3D
 * @author jiangwei
 * @date 2017年6月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class F3dAward extends AbstractArrayAward {

	@Override
	protected int getType(int childCode) {
		switch (childCode) {
		case 10501:
			return 1;
		case 10502:
			return 2;
		case 10503:
			return 3;
		default:
			break;
		}
		return 0;
	}

}
