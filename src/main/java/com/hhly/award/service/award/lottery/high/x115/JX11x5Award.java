package com.hhly.award.service.award.lottery.high.x115;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.lottery.high.x115.calculate.G2Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.G3Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.ICalculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.Q1Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.Q2Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.Q3Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R2Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R3Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R4Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R5Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R6Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R7Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.R8Calculate;
/**
 * 江西11选5
 * @desc
 * @author jiangwei
 * @date 2017年11月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class JX11x5Award extends Abstract11X5Award {
	
	@Autowired
	private R2Calculate r2Calculate;
	@Autowired
	private R3Calculate r3Calculate;
	@Autowired
	private R4Calculate r4Calculate;
	@Autowired
	private R5Calculate r5Calculate;
	@Autowired
	private R6Calculate r6Calculate;
	@Autowired
	private R7Calculate r7Calculate;
	@Autowired
	private R8Calculate r8Calculate;
	@Autowired
	private Q1Calculate q1Calculate;
	@Autowired
	private Q2Calculate q2Calculate;
	@Autowired
	private Q3Calculate q3Calculate;
	@Autowired
	private G2Calculate g2Calculate;
	@Autowired
	private G3Calculate g3Calculate;
	
	@Override
	protected ICalculate getCalculate(TicketInfoBO detail) {

		ICalculate calculate = null;
		switch (detail.getLotteryChildCode()) {
		case 21302:
			calculate = r2Calculate;
			break;
		case 21303:
			calculate = r3Calculate;
			break;
		case 21304:
			calculate = r4Calculate;
			break;
		case 21305:
			calculate = r5Calculate;
			break;
		case 21306:
			calculate = r6Calculate;
			break;
		case 21307:
			calculate = r7Calculate;
			break;
		case 21308:
			calculate = r8Calculate;
			break;
		case 21309:
			calculate = q1Calculate;
			break;
		case 21311:
			calculate = q2Calculate;
			break;
		case 21313:
			calculate = q3Calculate;
			break;
		case 21310:
			calculate = g2Calculate;
			break;
		case 21312:
			calculate = g3Calculate;
			break;
		default:
			throw new ServiceRuntimeException("不存在子玩法");
		}
		return calculate;
	
	}

}
