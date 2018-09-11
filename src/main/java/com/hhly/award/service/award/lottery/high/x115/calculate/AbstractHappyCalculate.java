package com.hhly.award.service.award.lottery.high.x115.calculate;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 乐选
 * @author jiangwei
 * @date 2017年10月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractHappyCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] codes = content.split("[|,;]");
		boolean[] winCondition = new boolean[drawCode.length];
		String[] tempCodes = new String[getNum()];
		WinInfo info = new WinInfo();
		for (int i = 0; i < codes.length; i++) {
			String code = codes[i];
			// 记录中奖位置
			for (int j = 0; j < drawCode.length; j++) {
				if (code.equals(drawCode[j])) {
					winCondition[j] = true;
					break;
				}
			}
			tempCodes[i % getNum()] = code;
			// 判断是否比较完一注
			if ((i + 1) % getNum() == 0) {
				WinInfo temp = compute(winCondition,tempCodes,drawCode);
				info.addPrize(temp);
				for (int j = 0; j < winCondition.length; j++) {
					winCondition[j] = false;
				}
			}
		}
		return info;
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		throw new ServiceRuntimeException("玩法错误只支持单式");
	}

	@Override
	public WinInfo gallDrag(String content, String[] drawCode) {
		throw new ServiceRuntimeException("玩法错误只支持单式");
	}

	/**
	 * 乐选几
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年10月11日 下午3:17:29
	 * @return
	 */
	protected abstract int getNum();

	/**
	 * 计算中奖情况
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年10月11日 下午3:25:13
	 * @param winCondition
	 * @return
	 */
	protected abstract WinInfo compute(boolean[] winCondition,String[] tempCodes,String[] drawCode);
}
