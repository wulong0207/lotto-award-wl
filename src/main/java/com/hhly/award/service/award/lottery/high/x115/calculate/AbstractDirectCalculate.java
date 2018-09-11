package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.util.StringUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.util.DrawCodeUtil;

/**
 * @desc 直选计算规则
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractDirectCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] all = StringUtils.tokenizeToStringArray(content, "|;");
		int num = directlyElectedSimple(drawCode, all, getNum());
		return new WinInfo(num, getPrize());
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		int num = directComplex(content, drawCode, getNum());
		return new WinInfo(num, getPrize());
	}

	@Override
	public WinInfo gallDrag(String content, String[] drawCode) {
		throw new ServiceRuntimeException("直选不存在胆拖");
	}
	/**
	 * 单式直选，选择号码与开奖号码位数一样
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午9:19:18
	 * @param drawCode 开奖号码
	 * @param all 选择号码
	 * @param type 选择类型
	 * @return
	 */
	protected int directlyElectedSimple(String[] drawCode, String[] all, int type) {
		int drawNum = 0;
		int draw = 0;
		for (int i = 1; i <= all.length; i++) {
			String str = all[i - 1];
			int num = i % type;
			if (num == 0) {
				if (str.equals(drawCode[type - 1])) {
					draw++;
				}
				if (draw == type) {
					drawNum++;
				}
				draw = 0;
			} else if (num == 1) {
				if (str.equals(drawCode[0])) {
					draw++;
				}
			} else {
				if (str.equals(drawCode[1])) {
					draw++;
				}
			}
		}
		return drawNum;
	}
	/**
	 * 复试计算规则
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午9:23:56
	 * @param content
	 * @param drawCode
	 * @param num
	 * @return
	 */
	protected int directComplex(String content, String[] drawCode, int num) {
		String[] all = StringUtils.tokenizeToStringArray(content, "|");
		for (int i = 0; i < num; i++) {
			String[] code = all[i].split(",");
			int in = DrawCodeUtil.include(drawCode[i], code);
			if (in == 0) {
				return 0;
			}
		}
		return 1;
	}
	/**
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:54:41
	 * @return 中奖奖项
	 */
	protected abstract String getPrize();
	/**
	 * 选号方式
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:55:01
	 * @return
	 */
	protected abstract int getNum();
}
