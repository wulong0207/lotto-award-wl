package com.hhly.award.service.award.lottery.ba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.util.DrawCodeUtil;
import com.hhly.award.util.MatchUtil;

/**
 * @desc 双色球开奖
 * @author jiangwei
 * @date 2017年5月25日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class SsqAward extends AbstractBeforeAfterAward {

	public SsqAward() {
		super(6, 1);
	}

	// 前驱号码
	private String[] before;
	// 后区号码
	private String after;
	// 中奖规则对应 中奖等级
	private static Map<String, String> GRADE;
	/**
	 * 中奖规则对应 中奖等级
	 */
	static {
		GRADE = new HashMap<String, String>() {
			private static final long serialVersionUID = 3121120417212006755L;
			{
				put("6-1", "一等奖");
				put("6-0", "二等奖");
				put("5-1", "三等奖");
				put("5-0", "四等奖");
				put("4-1", "四等奖");
				put("4-0", "五等奖");
				put("3-1", "五等奖");
				put("2-1", "六等奖");
				put("1-1", "六等奖");
				put("0-1", "六等奖");
			}
		};
	}

	@Override
	protected boolean haveDrawCode() {
		return before != null && after != null;
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 2);
		addDraw = splitDrawDetail(drawDetail, 0, 3);
	}

	@Override
	protected String getLevel(String key) {
		return GRADE.get(key);
	}

	@Override
	protected WinMoneyBO simple(TicketInfoBO detail) {
		Map<String, Integer> winningDetail = new HashMap<>();
		double totalMoney = 0, preBonus = 0, aftBonus = 0, totalAfterMoney = 0, addedBonus = 0;
		int dragIn = 0;// 前区托码
		int afterIn = 0;// 后区
		// 单
		String[] simple = StringUtils.tokenizeToStringArray(detail.getTicketContent(), ",+;");
		for (int i = 1; i <= simple.length; i++) {
			String no = simple[i - 1];
			if (i % 7 == 0) {
				if (after.equals(no)) {
					afterIn = 1;
				}
				WinMoneyBO win = winCompute(detail.getMultipleNum(), true, 0, 0, 6, dragIn, 0, 0, 1, afterIn);
				if (!CollectionUtils.isEmpty(win.getWinningDetail())) {
					totalMoney = MatchUtil.sum(totalMoney, win.getPreBonus());
					totalAfterMoney = MatchUtil.sum(totalAfterMoney, win.getAftBonus());
					preBonus = MatchUtil.sum(preBonus, win.getPreBonus());
					aftBonus = MatchUtil.sum(aftBonus, win.getAftBonus());
					addedBonus = MatchUtil.sum(addedBonus, win.getAddedBonus());
					for (Map.Entry<String, Integer> entry : win.getWinningDetail().entrySet()) {
						Integer num = winningDetail.get(entry.getKey());
						if (num == null) {
							num = 0;
						}
						winningDetail.put(entry.getKey(), num + entry.getValue());
					}
				}
				dragIn = 0;
				afterIn = 0;
			} else {
				dragIn += DrawCodeUtil.include(no, before);
			}
		}
		return new WinMoneyBO(preBonus, aftBonus, addedBonus, totalMoney, totalAfterMoney, winningDetail);
	}

	@Override
	protected WinMoneyBO reexamine(TicketInfoBO detail) {
		int dragIn = 0;
		int afterIn = 0;
		// 01,02,03,04,05,06,07,08+01,02,03
		String[] all = detail.getTicketContent().split(SymbolConstants.ADD_DOUBLE_SLASH);
		String[] beforeAll = all[0].split(SymbolConstants.COMMA);
		String[] afterAll = all[1].split(SymbolConstants.COMMA);
		dragIn = DrawCodeUtil.contain(beforeAll, before);
		afterIn = DrawCodeUtil.include(after, afterAll);
		WinMoneyBO win = winCompute(detail.getMultipleNum(), true, 0, 0, beforeAll.length, dragIn, 0, 0,
				afterAll.length, afterIn);
		return win;
	}

	@Override
	protected WinMoneyBO gallDrag(TicketInfoBO detail) {
		int gallIn = 0;
		int dragIn = 0;
		int afterIn = 0;
		// 01,02,03,04#05,06,07+01,02
		String[] all = detail.getTicketContent().split("[#+]");
		String[] gallAll = all[0].split(SymbolConstants.COMMA);
		String[] dragAll = all[1].split(SymbolConstants.COMMA);
		String[] afterAll = all[2].split(SymbolConstants.COMMA);
		gallIn = DrawCodeUtil.contain(gallAll, before);
		dragIn = DrawCodeUtil.contain(dragAll, before);
		afterIn = DrawCodeUtil.include(after, afterAll);
		WinMoneyBO win = winCompute(detail.getMultipleNum(), true, gallAll.length, gallIn, dragAll.length, dragIn, 0, 0,
				afterAll.length, afterIn);
		return win;
	}

	@Override
	protected void handleDrawCode(String code) {
		// 02,03,04,05,09,10|15
		String[] str = code.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		before = str[0].split(SymbolConstants.COMMA);
		after = str[1];
	}

	public static void main(String[] args) {
		SsqAward award = new SsqAward();
		award.handleDrawCode("02,03,04,05,09,10|15");
		award.handleDrawDetail(
				"一等奖|26|1000000,二等奖|222|200000,三等奖|3608|3000,四等奖|132859|200,五等奖|1854184|10,六等奖|13842428|5");
		OrderInfoBO orderInfoBO = new OrderInfoBO();
		List<TicketInfoBO> list = new ArrayList<>();
		orderInfoBO.setTicketInfoBOs(list);
		for (int i = 0; i < 1; i++) {
			TicketInfoBO bo = new TicketInfoBO();
			bo.setContentType(3);
			bo.setMultipleNum(1);
			bo.setTicketContent("02,03,04,05#12,14,09,10+15");
			list.add(bo);
		}
		award.compute(orderInfoBO);
		System.out.println("总");
		System.out.println(orderInfoBO.getWinningDetail());
		System.out.println(orderInfoBO.getPreBonus().toString());
		System.out.println(orderInfoBO.getAftBonus().toString());
		System.out.println(orderInfoBO.getAddedBonus().toString());
		System.out.println(orderInfoBO.getWinningStatus());
		System.out.println("--------------");
		for (TicketInfoBO ticketInfoBO : list) {
			System.out.println(ticketInfoBO.getWinningDetail());
			System.out.println(ticketInfoBO.getPreBonus().toString());
			System.out.println(ticketInfoBO.getAftBonus().toString());
			System.out.println(ticketInfoBO.getAddedBonus().toString());
			System.out.println(ticketInfoBO.getWinningStatus());
		}
	}

}
