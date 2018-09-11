package com.hhly.award.service.award.lottery.array;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;

/**
 * @desc 排列（3,5,3D）开奖
 * @author jiangwei
 * @date 2017年6月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractArrayAward extends AbstractNumberAward {
	/**
	 * 开奖号码
	 */
	protected String[] drawCodeAll;
	/**
	 * 和值
	 */
	protected String sum;
	/**
	 * 开奖号码数
	 */
	protected int countNum;
	
	protected String sameCode;

	@Autowired
	private ArrayThreeCalculate calculate;

	@Override
	protected void handleDrawCode(String code) {
		drawCodeAll = code.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		Set<String> set = new HashSet<>();
		int num = 0;
		for (String string : drawCodeAll) {
			if(set.contains(string)){
				sameCode = string;
			}else{
				set.add(string);	
			}
			num += Integer.parseInt(string);
		}
		sum = String.valueOf(num);
		countNum = set.size();
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
	protected String getLevel(String prize) {
		return prize;
	}

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		String prize = "";
		int num = 0;
		int type = getType(detail.getLotteryChildCode());
		switch (type) {
		case 1:
			prize = "直选";
			num = calculate.direct(detail.getContentType(), content, drawCodeAll, sum);
			break;
		case 2:
			prize = "组三";
			num = calculate.groupThree(detail.getContentType(), content, drawCodeAll, sum, countNum,sameCode);
			break;
		case 3:
			prize = "组六";
			num = calculate.groupSix(detail.getContentType(), content, drawCodeAll, sum, countNum);
			break;
		default:
			break;
		}
		if (num == 0) {
			return getNotWinMoney();
		}
		return getWinMoney(prize, num, detail.getMultipleNum());
	}

	/**
	 * 获取玩法类型
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 上午11:52:48
	 * @param childCode
	 *            子玩法ID
	 * @return 1：直选，2：组3,3：组6
	 */
	protected abstract int getType(int childCode);
}
