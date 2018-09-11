package com.hhly.award.service.award.lottery.high.kl10;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.kl10.calculate.FiveOptionalTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.FourOptionalTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.ICalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.OneCountTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.OneRedTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.ThreeDirectTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.ThreeGroupTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.ThreeOptionalTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.TwoDirectTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.TwoGroupTenCalculate;
import com.hhly.award.service.award.lottery.high.kl10.calculate.TwoOptionalTenCalculate;
/**
 * @desc 广东快乐10分
 * @author jiangwei
 * @date 2017年8月7日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class CqKl10Award extends AbstractKl10Award {
	
	@Autowired
	private OneCountTenCalculate oneCountTenCalculate;
	@Autowired
	private OneRedTenCalculate oneRedTenCalculate;
	@Autowired
	private TwoOptionalTenCalculate twoOptionalTenCalculate;
	@Autowired
	private TwoGroupTenCalculate twoGroupTenCalculate;
	@Autowired
	private TwoDirectTenCalculate twoDirectTenCalculate;
	@Autowired
	private ThreeOptionalTenCalculate  threeOptionalTenCalculate;
	@Autowired
	private ThreeGroupTenCalculate threeGroupTenCalculate;
	@Autowired
	private ThreeDirectTenCalculate threeDirectTenCalculate;
	@Autowired
	private FourOptionalTenCalculate fourOptionalTenCalculate;
	@Autowired
	private FiveOptionalTenCalculate fiveOptionalTenCalculate;
	
	@Override
	public ICalculate getCalculate(int lotteryChildCode) {
		switch (lotteryChildCode) {
		case 22201://前一数投
			return oneCountTenCalculate;
		case 22202://前一红投
			return oneRedTenCalculate;
		case 22203://任选二
			return twoOptionalTenCalculate;
		case 22204://选二连组
			return twoGroupTenCalculate;
		case 22205://选二连直
			return twoDirectTenCalculate;
		case 22206://任选三
			return threeOptionalTenCalculate;
		case 22207://选三前组
			return threeGroupTenCalculate;
		case 22208://选三前直
			return threeDirectTenCalculate;
		case 22209://任选四
			return fourOptionalTenCalculate;
		case 22210://任选五
			return fiveOptionalTenCalculate;
		default:
			throw new ServiceRuntimeException("不存在该子玩法");
		}
	}

}
