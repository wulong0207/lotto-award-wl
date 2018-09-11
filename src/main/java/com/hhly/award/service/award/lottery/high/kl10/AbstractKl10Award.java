package com.hhly.award.service.award.lottery.high.kl10;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;
import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.service.award.lottery.high.kl10.calculate.ICalculate;
/**
 * @desc 快乐10分
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractKl10Award extends AbstractNumberAward{
	
	private List<String> drawCodeList = null;
	
	@Override
	protected void handleDrawCode(String code) {
		drawCodeList =Arrays.asList(code.split(","));
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 1);
		addDraw = splitDrawDetail(drawDetail, 0, 2);
	}
	
	@Override
	protected String getLevel(String prize) {
		return prize;
	}
	
	@Override
	protected boolean haveDrawCode() {
		return CollectionUtils.isNotEmpty(drawCodeList);
	}

	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		String content = detail.getTicketContent();
		//玩法计算规则
		ICalculate calculate = getCalculate(detail.getLotteryChildCode());
		WinInfo info = null;
		//单式，复试 计算中奖号码数
		if(detail.getContentType() == 1){
			info = calculate.simple(content, drawCodeList);
		}else if(detail.getContentType() == 2){
			info = calculate.complex(content, drawCodeList);
		}else{
			throw new ServiceRuntimeException("不存在选好类型");
		}
		//计算中奖金额
		return computeMoney(info.getPrize(), detail.getMultipleNum(), true);
	}
	/**
	 * 获取开奖耍法
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月4日 下午5:36:07
	 * @param lotteryChildCode
	 * @return
	 */
	public abstract ICalculate getCalculate(int lotteryChildCode);

}
