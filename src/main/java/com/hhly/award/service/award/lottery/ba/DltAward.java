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
 * @desc 大乐透开奖
 * @author jiangwei
 * @date 2017年5月25日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class DltAward extends AbstractBeforeAfterAward {
	// 前驱号码
	private String[] before;
	// 后区号码
	private String[] after;
	// 中奖规则对应 中奖等级
	private static Map<String, String> GRADE;
	static {
		GRADE = new HashMap<String, String>() {
			private static final long serialVersionUID = 1312410620651458077L;
			{
				put("5-2", "一等奖");
				put("5-1", "二等奖");
				put("5-0", "三等奖");
				put("4-2", "三等奖");
				put("4-1", "四等奖");
				put("3-2", "四等奖");
				put("4-0", "五等奖");
				put("3-1", "五等奖");
				put("2-2", "五等奖");
				put("3-0", "六等奖");
				put("1-2", "六等奖");
				put("2-1", "六等奖");
				put("0-2", "六等奖");
			}
		};
	}

	public DltAward() {
		super(5, 2);
	}

	@Override
	protected WinMoneyBO gallDrag(TicketInfoBO detail) {
		// 01,02,03#04,05,06,07+02#03,05
		int gall, gallIn, drag, dragIn, afterGall, afterGallIn, afterDrag, afterDragIn;
		String[] all = detail.getTicketContent().split(SymbolConstants.ADD_DOUBLE_SLASH);
		String[] beforeAll = all[0].split(SymbolConstants.NUMBER_SIGN);
		String[] gallNum = null;
		String[] dragNum = null;
		if (beforeAll.length == 1) {
			gallNum = new String[0];
			dragNum = beforeAll[0].split(SymbolConstants.COMMA);
		} else {
			gallNum = beforeAll[0].split(SymbolConstants.COMMA);
			dragNum = beforeAll[1].split(SymbolConstants.COMMA);
		}
		gall = gallNum.length;
		gallIn = DrawCodeUtil.contain(gallNum, before);
		drag = dragNum.length;
		dragIn = DrawCodeUtil.contain(dragNum, before);

		String[] afterAll = all[1].split(SymbolConstants.NUMBER_SIGN);
		if (afterAll.length == 1) {
			gallNum = new String[0];
			dragNum = afterAll[0].split(SymbolConstants.COMMA);
		} else {
			gallNum = afterAll[0].split(SymbolConstants.COMMA);
			dragNum = afterAll[1].split(SymbolConstants.COMMA);
		}
		afterGall = gallNum.length;
		afterGallIn = DrawCodeUtil.contain(gallNum, after);
		afterDrag = dragNum.length;
		afterDragIn = DrawCodeUtil.contain(dragNum, after);

		boolean isAddMoney = isAdd(detail.getLottoAdd());
		WinMoneyBO win = winCompute(detail.getMultipleNum(), isAddMoney, gall, gallIn, drag, dragIn, afterGall,
				afterGallIn, afterDrag, afterDragIn);
		return win;
	}

	@Override
	protected WinMoneyBO reexamine(TicketInfoBO detail) {
		// 01,02,03,04,05,06,07,08+01,02,03
		String[] all = detail.getTicketContent().split(SymbolConstants.ADD_DOUBLE_SLASH);
		String[] beforeAll = all[0].split(SymbolConstants.COMMA);
		String[] afterAll = all[1].split(SymbolConstants.COMMA);
		int dragIn = DrawCodeUtil.contain(beforeAll, before);
		int afterIn = DrawCodeUtil.contain(afterAll, after);
		boolean isAddMoney = isAdd(detail.getLottoAdd());
		WinMoneyBO win = winCompute(detail.getMultipleNum(), isAddMoney, 0, 0, beforeAll.length, dragIn, 0, 0,
				afterAll.length, afterIn);
		return win;
	}

	@Override
	protected WinMoneyBO simple(TicketInfoBO detail) {
		boolean isAddMoney = isAdd(detail.getLottoAdd());
		Map<String, Integer> winningDetail = new HashMap<>();
		double totalMoney = 0, preBonus = 0, aftBonus = 0, totalAfterMoney = 0, addedBonus = 0;
		int dragIn = 0;// 前区托码
		int afterIn = 0;// 后区
		// 单
		String[] simple = StringUtils.tokenizeToStringArray(detail.getTicketContent(), ",+;");
		for (int i = 1; i <= simple.length; i++) {
			String no = simple[i - 1];
			if (i % 7 == 0) {
				afterIn += DrawCodeUtil.include(no, after);
				WinMoneyBO win = winCompute(detail.getMultipleNum(), isAddMoney, 0, 0, 5, dragIn, 0, 0, 2, afterIn);
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
			} else if (i % 7 == 6) {
				afterIn = DrawCodeUtil.include(no, after);
			} else {
				dragIn += DrawCodeUtil.include(no, before);
			}
		}
		return new WinMoneyBO(preBonus, aftBonus, addedBonus, totalMoney, totalAfterMoney, winningDetail);

	}

	@Override
	protected String getLevel(String prize) {
		return GRADE.get(prize);
	}

	@Override
	protected void handleDrawCode(String code) {
		// 01,04,24,27,32|03,10
		String[] str = code.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		before = str[0].split(SymbolConstants.COMMA);
		after = str[1].split(SymbolConstants.COMMA);
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 2);
		addDraw = splitDrawDetail(drawDetail, 0, 4);
	}

	@Override
	protected boolean haveDrawCode() {
		return before != null && after != null;
	}

	/**
	 * 是否大乐透追加
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月30日 下午6:44:11
	 * @param add
	 * @return
	 */
	private boolean isAdd(Short add) {
		return add != null && add == 1;
	}

	public static void main(String[] args) {
		DltAward award = new DltAward();
		award.handleDrawCode("02,03,04,05,09|01,02");
		award.handleDrawDetail(
				"一等奖|26|1000000|1|100000,二等奖|222|100000|1|10000,三等奖|3608|3000|1|1500,四等奖|132859|200|1|100,五等奖|1854184|10|1|5,六等奖|13842428|5|0|0");
		OrderInfoBO orderInfoBO = new OrderInfoBO();
		List<TicketInfoBO> list = new ArrayList<>();
		orderInfoBO.setTicketInfoBOs(list);
		for (int i = 0; i < 1; i++) {
			TicketInfoBO bo = new TicketInfoBO();
			bo.setContentType(3);
			bo.setMultipleNum(1);
			bo.setTicketContent("02,05#03,19,21,22,24+01#02,06");
			bo.setLottoAdd((short) 1);
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
