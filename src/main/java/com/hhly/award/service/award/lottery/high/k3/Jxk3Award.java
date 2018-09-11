package com.hhly.award.service.award.lottery.high.k3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 江西快3
 * @author jiangwei
 * @date 2017年11月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class Jxk3Award extends AbstractK3Award {

	@Autowired
	private DefaultCalculate calculate;

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		WinInfo winfo = null;
		switch (detail.getLotteryChildCode()) {
		case 23401:
			winfo = calculate.sum(sum, content);
			break;
		case 23407:
			winfo = calculate.threeSame(drawCodeSet);
			break;
		case 23406:
			winfo = calculate.threeSameSimple(drawCodeSet, content);
			break;
		case 23405:
			winfo = calculate.threeDifferenceSimple(drawCodeSet, content);
			break;
		case 23408:
			winfo = calculate.consecutive(consecutive);
			break;
		case 23403:
			winfo = calculate.twoSame(twoSame, content);
			break;
		case 23402:
			winfo = calculate.twoSameSimple(drawCodeSet, twoSame, content);
			break;
		case 23404:
			winfo = calculate.twoDifferenceSimple(drawCodeSet, content);
			break;
		default:
			throw new ServiceRuntimeException("不存在子玩法");
		}
		return computeMoney(winfo.getPrize(), detail.getMultipleNum(), true);
	}
}
