package com.hhly.award.service.award.lottery.high.ssc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.ssc.calculate.FiveAllCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.FiveDirectCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.ICalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.OneCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.SizeCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.ThreeDirectCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.ThreeGroupSixCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.ThreeGroupThreeCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.TwoDirectCalculate;
import com.hhly.award.service.award.lottery.high.ssc.calculate.TwoGroupCalculate;

/**
 * @desc 重庆时时彩开奖
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class CqSscAward extends AbstractSscAward {
	
	@Autowired
    private OneCalculate oneCalculate;
	@Autowired
	private TwoDirectCalculate twoDirectCalculate;
	@Autowired
	private ThreeDirectCalculate threeDirectCalculate;
	@Autowired
	private FiveDirectCalculate fiveDirectCalculate;
	@Autowired
	private SizeCalculate sizeCalculate;
	@Autowired
	private TwoGroupCalculate twoGroupCalculate;
	@Autowired
	private ThreeGroupSixCalculate threeGroupSixCalculate;
	@Autowired
	private ThreeGroupThreeCalculate threeGroupThreeCalculate;
	@Autowired
	private FiveAllCalculate fiveAllCalculate;
	
	@Override
	protected ICalculate getCalculate(int lotteryChildCode) {
		switch (lotteryChildCode) {
		case 20101:// 重庆时时彩五星直选
			return fiveDirectCalculate;
		case 20102:// 重庆时时彩五星通选
			return fiveAllCalculate;
		case 20103:// 重庆时时彩三星直选
			return threeDirectCalculate;
		case 20104:// 重庆时时彩三星组三
			return threeGroupThreeCalculate;
		case 20105:// 重庆时时彩三星组六
			return threeGroupSixCalculate;
		case 20106:// 重庆时时彩二星直选
			return twoDirectCalculate;
		case 20107:// 重庆时时彩二星组选
			return twoGroupCalculate;
		case 20108:// 重庆时时彩一星
			return oneCalculate;
		case 20109:// 重庆时时彩大小单双
			return sizeCalculate;
		default:
			throw new ServiceRuntimeException("重庆时时彩开奖计算方式不存在");
		}
	}

}
