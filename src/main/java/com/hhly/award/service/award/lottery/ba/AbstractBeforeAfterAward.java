package com.hhly.award.service.award.lottery.ba;

import java.util.HashMap;
import java.util.Map;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.service.award.lottery.AbstractNumberAward;
import com.hhly.award.util.MatchUtil;

/**
 * @desc 前后区彩种中奖解析如 6-1（双色球）
 * @author jiangwei
 * @date 2017年5月26日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractBeforeAfterAward extends AbstractNumberAward {
	// 前驱号码个数
	protected int beforeWay;
	// 后驱号码个数
	protected int afterWay;

	public AbstractBeforeAfterAward(int beforeWay, int afterWay) {
		this.beforeWay = beforeWay;
		this.afterWay = afterWay;
	}
    
	@Override
	protected WinMoneyBO computeWinMoney(TicketInfoBO detail) {
		WinMoneyBO win = null;
		switch (detail.getContentType()) {
		case 1:
			//单
			win = simple(detail);
			break;
		case 2:
			// 复
			win = reexamine(detail);
			break;
		case 3:
			// 胆,拖
			win = gallDrag(detail);
			break;
		default:
			throw new ServiceRuntimeException("票投注方式错误");
		}
		return win;
	}
	/**
	 * 胆拖
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月28日 上午11:34:06
	 * @param totalLevel
	 * @param detail
	 * @param content
	 * @param winningDetail
	 * @return
	 */
	protected abstract WinMoneyBO gallDrag( TicketInfoBO detail);

	/**
	 * 复试
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月28日 上午11:32:41
	 * @param totalLevel
	 * @param detail
	 * @param content
	 * @param winningDetail
	 * @return
	 */
	protected abstract WinMoneyBO reexamine(TicketInfoBO detail);

	/**
	 * 单式
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月28日 上午11:30:25
	 * @param totalLevel
	 * @param detail
	 * @param content
	 * @param winningDetail
	 * @return
	 */
	protected abstract WinMoneyBO simple(TicketInfoBO detail);
	/**
	 * 计算中奖金额，注释，中级等级
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月26日 下午5:59:34
	 * @param winningDetail
	 *            中奖详情
	 * @param totalLevel
	 *            中奖等级
	 * @param multipleNum
	 *            倍数
	 * @param gall
	 *            胆码
	 * @param gallIn
	 *            中胆码
	 * @param drag
	 *            拖码
	 * @param dragIn
	 *            中拖码
	 * @param afterGall
	 *            后区胆码
	 * @param afterGallIn
	 *            中后区胆码
	 * @param afterDrag
	 *            后区托码
	 * @param afterDragIn
	 *            中后区托码
	 * @return
	 */
	protected WinMoneyBO winCompute(int multipleNum,boolean isAddMoney,
			int gall, int gallIn, int drag, int dragIn,  
			int afterGall,int afterGallIn, int afterDrag, int afterDragIn) {
		Map<String, Integer> result = computePrize(gall, gallIn, drag, dragIn, afterGall, afterGallIn, afterDrag, afterDragIn, beforeWay,
				afterWay);
		return computeMoney(result, multipleNum, isAddMoney);
	}
	/**
	 * 数字彩中奖等级计算（彩种：双色球，大乐透） 为了保证计算的效率，注意参数的正确性 x + y = 前驱总数， a + b = 后驱总数 中奖方式
	 * "y-b" (注数)=
	 * C(托（未中总数）,x（未中）)C(托（中总数）,y（中）)*C(后驱（未中总数）,a（未中）)C(后驱（中总数）,b（中）)
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月26日 上午9:19:11
	 * @param gall
	 *            胆码投注个数
	 * @param gallIn
	 *            胆码中奖个数
	 * @param drag
	 *            托码投注个数
	 * @param dragIn
	 *            托码中奖个数
	 * @param afterGall
	 *            后驱胆码投注个数
	 * @param afterGallIn
	 *            后驱胆码中奖个数
	 * @param afterDrag
	 *            后驱托马投注个数
	 * @param afterDragIn
	 *            后驱托马中奖个数
	 * @param beforeWay
	 *            前驱总个数
	 * @param afterWay
	 *            后驱总个数
	 * @return key 中奖方式 value 注数
	 */
	protected Map<String, Integer> computePrize(int gall, int gallIn, int drag, int dragIn, int afterGall,
			int afterGallIn, int afterDrag, int afterDragIn, int beforeWay, int afterWay) {
		if (gallIn == 0 && dragIn == 0 && afterGallIn == 0 && afterDragIn == 0) {
			// 未中奖
			return new HashMap<>();
		}
		// 验证参数
		/*
		 * if (gallIn > gall || dragIn > drag || afterIn > after || gall + drag
		 * < beforeWay || after < afterWay || gall >= beforeWay || after <
		 * afterWay) { throw new ServiceRuntimeException("参数错误"); }
		 */
		Map<String, Integer> result = new HashMap<>();
		// 前区 未中托码总数
		int dragN = drag - dragIn;
		// 前区 托码区最多中码个数
		int maxDragIn = beforeWay - gall;
		maxDragIn = dragIn > maxDragIn ? maxDragIn : dragIn;
		// 前区 托码区最少中码个数
		int minDragIn = beforeWay - gall - (drag - dragIn);
		minDragIn = minDragIn < 0 ? 0 : minDragIn;

		// 后区 未中托码总数
		int afterDragN = afterDrag - afterDragIn;
		// 后区 托码区最多中码个数
		int maxAfterDragIn = afterWay - afterGall;
		maxAfterDragIn = afterDragIn > maxAfterDragIn ? maxAfterDragIn : afterDragIn;
		// 后区 托码区最少中码个数
		int minAfterDragIn = afterWay - afterGall - (afterDrag - afterDragIn);
		minAfterDragIn = minAfterDragIn < 0 ? 0 : minAfterDragIn;

		for (int i = maxDragIn; i >= minDragIn; i--) {
			int dragM = beforeWay - gall - i;
			// 前驱注数（托码未中 排列组合*托码中 排列组合）
			int beforeNum = cc(dragN, dragM, dragIn, i);
			// 前驱中奖个数
			int beforeIn = gallIn + i;
			// 后区
			for (int j = maxAfterDragIn; j >= minAfterDragIn; j--) {
				int afterDragM = afterWay - afterGall - j;
				int afterNum = cc(afterDragN, afterDragM, afterDragIn, j);
				int afterIn = afterGallIn + j;
				if (beforeIn > 0 || afterIn > 0) {
					result.put(beforeIn + "-" + afterIn, beforeNum * afterNum);
				}
			}
		}
		return result;
	}

	/**
	 * 2个排列组合计算（计算胆拖注数）
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月28日 上午10:11:03
	 * @param n1
	 *            排列组合1 n
	 * @param m1
	 *            排列组合1 m
	 * @param n2
	 *            排列组合2 n
	 * @param m2
	 *            排列组合2 n
	 * @return 2个排列组合的积
	 */
	private int cc(int n1, int m1, int n2, int m2) {
		int num = 1;
		if (m1 > 0) {
			num *= MatchUtil.pac(n1, m1);
		}
		if (m2 > 0) {
			num *= MatchUtil.pac(n2, m2);
		}
		return num;
	}

	public static void main(String[] args) {
		AbstractBeforeAfterAward numberAward = new SsqAward();
		long start = System.currentTimeMillis();
		Map<String, Integer> result = numberAward.computePrize(2, 2, 6, 3, 0, 0,0, 0, 5, 0);
		System.out.println(System.currentTimeMillis() - start);
		if (result == null) {
			return;
		}
		for (String string : result.keySet()) {
			System.out.println(string + "|" + result.get(string));
		}
		System.out.println(MatchUtil.pac(6, 2) * MatchUtil.pac(4, 1));
	}
}
