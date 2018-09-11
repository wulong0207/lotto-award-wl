package com.hhly.award.service.award.lottery.high.ssc;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;
import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.service.award.lottery.high.ssc.calculate.ICalculate;

/**
 * @desc 时时彩开奖
 * @author jiangwei
 * @date 2017年7月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractSscAward extends AbstractNumberAward {
	/**
	 * 开奖号码数组
	 */
	protected String[] drawCodeAll;
	/**
	 * 后2位和值
	 */
	protected String twoSum;
	/**
	 * 后3位和值
	 */
	protected String threeSum;

	@Override
	protected void handleDrawCode(String code) {
		drawCodeAll = code.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		int num = 0;
		for (int i = 1; i <= 3; i++) {
			String str = drawCodeAll[drawCodeAll.length-i];
			num += Integer.parseInt(str);
			if(i==2){
				twoSum = "" + num;
			}else if(i==3){
				threeSum = ""+ num;
			}
		}
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 1);
		draw.put("五星通选前三", 220);
		draw.put("五星通选后三", 220);
		draw.put("五星通选前二", 20);
		draw.put("五星通选后二", 20);
		addDraw = splitDrawDetail(drawDetail, 0, 2);
	}

	@Override
	protected boolean haveDrawCode() {
		return drawCodeAll != null;
	}

	@Override
	protected String getLevel(String prize) {
		return prize;
	}

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		// 玩法计算规则
		ICalculate calculate = getCalculate(detail.getLotteryChildCode());
		WinInfo info = null;
		// 单式，复试，和值计算中奖号码数
		if (detail.getContentType() == 1) {
			info = calculate.simple(content, drawCodeAll);
		} else if (detail.getContentType() == 2) {
			info = calculate.complex(content, drawCodeAll);
		} else if (detail.getContentType() == 6) {
			info = calculate.sum(content, twoSum, threeSum,drawCodeAll);
		} else {
			throw new ServiceRuntimeException("开奖票选好类型错误" + detail);
		}
		// 计算中奖金额
		return computeMoney(info.getPrize(), detail.getMultipleNum(), true);
	}
	/**
	 * 获取计算开奖类型
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月12日 上午9:50:12
	 * @param lotteryChildCode
	 * @return
	 */
	protected  abstract ICalculate getCalculate(int lotteryChildCode);
}
