package com.hhly.award.service.award.lottery.high.k3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 江苏快3
 * @author jiangwei
 * @date 2017年6月9日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class Jsk3Award extends AbstractK3Award {

	@Autowired
	private DefaultCalculate calculate;

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		WinInfo winfo = null;
		switch (detail.getLotteryChildCode()) {
		case 23301:
			winfo = calculate.sum(sum, content);
			break;
		case 23307:
			winfo = calculate.threeSame(drawCodeSet);
			break;
		case 23306:
			winfo = calculate.threeSameSimple(drawCodeSet, content);
			break;
		case 23305:
			winfo = calculate.threeDifferenceSimple(drawCodeSet, content);
			break;
		case 23308:
			winfo = calculate.consecutive(consecutive);
			break;
		case 23303:
			winfo = calculate.twoSame(twoSame, content);
			break;
		case 23302:
			winfo = calculate.twoSameSimple(drawCodeSet, twoSame, content);
			break;
		case 23304:
			winfo = calculate.twoDifferenceSimple(drawCodeSet, content);
			break;
		default:
			throw new ServiceRuntimeException("不存在子玩法");
		}
		return computeMoney(winfo.getPrize(), detail.getMultipleNum(), true);
	}
}
