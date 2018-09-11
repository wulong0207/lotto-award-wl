package com.hhly.award.service.award.lottery.high.x115;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;
import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.service.award.lottery.high.x115.calculate.ICalculate;
/**
 * @desc 11选5开奖抽象类
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class Abstract11X5Award extends AbstractNumberAward {
	/**
	 * 拆分后的开奖号码
	 */
	protected String[] drawCodeAll;

	@Override
	protected void handleDrawCode(String code) {
		drawCodeAll = code.split(SymbolConstants.COMMA);
	}
	
	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 1);
		addDraw = splitDrawDetail(drawDetail, 0, 2);
	}

	@Override
	protected boolean haveDrawCode() {
		return drawCode != null;
	}

	@Override
	protected String getLevel(String prize) {
		return prize;
	}

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		//玩法计算规则
		ICalculate calculate = getCalculate(detail);
		WinInfo info= null;
		//单式，复试，胆拖计算中奖号码数
		if(detail.getContentType() == 1){
			info = calculate.simple(content, drawCodeAll);
		}else if(detail.getContentType() == 2){
			info = calculate.complex(content, drawCodeAll);
		}else{
			info = calculate.gallDrag(content, drawCodeAll);
		}
		//计算中奖金额
		return computeMoney(info.getPrize(), detail.getMultipleNum(), true);
	}
	/**
	 * 获取开奖计算类
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月18日 下午5:36:52
	 * @param detail
	 * @return
	 */
	protected abstract ICalculate getCalculate(TicketInfoBO detail);
}
