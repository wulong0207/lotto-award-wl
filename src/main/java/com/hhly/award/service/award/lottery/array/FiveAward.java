package com.hhly.award.service.award.lottery.array;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;
import com.hhly.award.util.DrawCodeUtil;
@Service
public class FiveAward extends AbstractNumberAward {
	/**
	 * 开奖号码
	 */
	protected String[] drawCodeAll;

	@Override
	protected void handleDrawCode(String code) {
		drawCodeAll = code.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 2);
		addDraw = splitDrawDetail(drawDetail, 0, 3);
	}

	@Override
	protected boolean haveDrawCode() {
		return drawCodeAll != null;
	}

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		int num = 0;
		switch (detail.getContentType()) {
		case 1:
			//单式
			String [] codes =StringUtils.tokenizeToStringArray(detail.getTicketContent(), "|;");
			num =  DrawCodeUtil.simple(codes, drawCodeAll,5);
			break;
		case 2:
			//复试
			num = DrawCodeUtil.directRepeated(detail.getTicketContent(), "|", ",", drawCodeAll);
			break;
		default:
			break;
		}
		if (num == 0) {
			return getNotWinMoney();
		}
		return getWinMoney("直选", num, detail.getMultipleNum());
	}

	@Override
	protected String getLevel(String prize) {
		return prize;
	}
}
