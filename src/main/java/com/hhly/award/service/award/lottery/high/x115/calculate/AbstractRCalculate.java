package com.hhly.award.service.award.lottery.high.x115.calculate;

import org.springframework.util.StringUtils;

import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.util.DrawCodeUtil;
import com.hhly.award.util.MatchUtil;

/**
 * @desc 任选计算规则
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractRCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] before = StringUtils.tokenizeToStringArray(content, ",;");
		int count = getPrizeSimple(drawCode, before, getNum());
		return new WinInfo(count, getPrize());
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		String[] before = StringUtils.tokenizeToStringArray(content, ",;");
		int count = getPrizeMany(drawCode, before, getNum());
		return new WinInfo(count, getPrize());
	}

	@Override
	public WinInfo gallDrag(String content, String[] drawCode) {
		String[] all = StringUtils.tokenizeToStringArray(content, "#");
		String[] before = StringUtils.tokenizeToStringArray(all[0], ",;");
		String[] after = StringUtils.tokenizeToStringArray(all[1], ",");
		int count = getPrizeGallDrag(drawCode, before, after, getNum());
		return new WinInfo(count, getPrize());
	}

	/**
	 * 胆拖计算
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:58:41
	 * @param drawCode
	 *            开奖号码
	 * @param before
	 *            胆码
	 * @param after
	 *            托码
	 * @param num
	 *            选号方式
	 * @return
	 */
	private int getPrizeGallDrag(String[] drawCode, String[] before, String[] after, int num) {
		int drawBefore = DrawCodeUtil.contain(before, drawCode);
		int drawAfter = DrawCodeUtil.contain(after, drawCode);
		// 选号码小于5，胆码需要全中，托马区根据中码个数进行Cnm计算
		if (num <= 5) {
			if (drawBefore != before.length) {
				return 0;
			}
			return MatchUtil.pac(drawAfter, num - before.length);
		}
		// 选好方式大于5，中码个数必须等于5
		if (drawBefore + drawAfter < 5) {
			return 0;
		}
		// 后区选择号码数
		int other = num - before.length - drawAfter;
		if (other < 0) {
			return 0;
		} else if (other == 0) {
			return 1;
		}
		int drag = after.length - drawAfter;
		// 选好大于5 对后区不中号码进行Cnm计算
		return MatchUtil.pac(drag, other);
	}

	/**
	 * 复试开奖号码计算
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午9:04:20
	 * @param drawCode
	 *            开奖号码
	 * @param before
	 *            选号码
	 * @param num
	 *            选好方式
	 * @return
	 */
	private int getPrizeMany(String[] drawCode, String[] before, int num) {
		// 中奖个数
		int draw = DrawCodeUtil.contain(before, drawCode);
		if (draw < num && draw != 5) {
			return 0;
		}
		if (num <= 5) {
			// 中奖号码Cnm
			return MatchUtil.pac(draw, num);
		} else {
			// 未中号码Cnm
			return MatchUtil.pac(before.length - 5, num - 5);
		}
	}

	/**
	 * 单式计算，单式一张票有多注
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午9:08:12
	 * @param drawCode
	 *            开奖号码
	 * @param before
	 *            选号
	 * @param num
	 *            选号方式
	 * @return
	 */
	private int getPrizeSimple(String[] drawCode, String[] before, int num) {
		// 中奖个数
		int prize = 0;
		int draw = 0;
		// 需要中码个数
		int prizeNum = num < 5 ? num : 5;
		for (int i = 1; i <= before.length; i++) {
			String str = before[i - 1];
			draw += DrawCodeUtil.include(str, drawCode);
			if (i % num == 0) {// 单注结束计算
				if (draw == prizeNum) {
					prize++;
				}
				draw = 0;
			}
		}
		return prize;
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
