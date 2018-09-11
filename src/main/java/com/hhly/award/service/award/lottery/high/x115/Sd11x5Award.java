package com.hhly.award.service.award.lottery.high.x115;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.lottery.high.x115.calculate.G2Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.G3Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.ICalculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.L2Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.L3Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.L4Calculate;
import com.hhly.award.service.award.lottery.high.x115.calculate.L5Calculate;
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
 * @desc 山东11选5开奖实现类
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class Sd11x5Award extends Abstract11X5Award {
	
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
	@Autowired
	private L2Calculate l2Calculate;
	@Autowired
	private L3Calculate l3Calculate;
	@Autowired
	private L4Calculate l4Calculate;
	@Autowired
	private L5Calculate l5Calculate;
	
	protected ICalculate getCalculate(TicketInfoBO detail) {
		ICalculate calculate = null;
		switch (detail.getLotteryChildCode()) {
		case 21502:
			calculate = r2Calculate;
			break;
		case 21503:
			calculate = r3Calculate;
			break;
		case 21504:
			calculate = r4Calculate;
			break;
		case 21505:
			calculate = r5Calculate;
			break;
		case 21506:
			calculate = r6Calculate;
			break;
		case 21507:
			calculate = r7Calculate;
			break;
		case 21508:
			calculate = r8Calculate;
			break;
		case 21509:
			calculate = q1Calculate;
			break;
		case 21511:
			calculate = q2Calculate;
			break;
		case 21513:
			calculate = q3Calculate;
			break;
		case 21510:
			calculate = g2Calculate;
			break;
		case 21512:
			calculate = g3Calculate;
			break;
		case 21514:
			calculate = l2Calculate;
			break;
		case 21515:
			calculate = l3Calculate;
			break;
		case 21516:
			calculate = l4Calculate;
			break;
		case 21517:
			calculate = l5Calculate;
			break;
		default:
			throw new ServiceRuntimeException("不存在子玩法");
		}
		return calculate;
	}
}
