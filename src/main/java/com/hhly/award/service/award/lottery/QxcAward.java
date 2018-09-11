package com.hhly.award.service.award.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;

/**
 * @desc 七星彩开奖计算
 * @author jiangwei
 * @date 2017年7月25日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class QxcAward extends AbstractNumberAward {
	// 开奖号码
	private String[] drawCodeAll;

	@Override
	protected void handleDrawCode(String code) {
		drawCodeAll = code.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
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
		Map<String, Integer> prize = null;
		// 单式，复试
		if (detail.getContentType() == 1) {
			prize = simple(content);
		} else if (detail.getContentType() == 2) {
			prize = reexamine(content);
		} else {
			throw new ServiceRuntimeException("玩法类型错误ContentType：" + detail.getContentType());
		}
		// 计算中奖金额
		return computeMoney(prize, detail.getMultipleNum(), true);
	}

	/**
	 * 复试计算
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月25日 下午4:50:23
	 * @param content
	 * @return
	 */
	private Map<String, Integer> reexamine(String content) {
		Map<String, Integer> result = new HashMap<>();
		String[] codes = StringUtils.tokenizeToStringArray(content, "|");
		//所有选择方式中奖情况
		List<boolean[]> listIn = new ArrayList<>();
		listIn.add(new boolean[7]);
		for (int i = 0; i < codes.length; i++) {
			String[] w = codes[i].split(",");
			String dc = drawCodeAll[i];
			List<boolean[]> temp = new ArrayList<>();
			//复选号码比对
			for (String str : w) {
				boolean in = false;
				if (dc.equals(str)) {
					in = true;
				}
				//中奖情况添加
				for (boolean[] li : listIn) {
					boolean[] ti = li.clone();
					ti[i] = in;
					temp.add(ti);
				}
			}
			listIn = temp;
		}
		for (boolean[] in : listIn) {
			String prize = prize(in);
			if (prize != null) {
				Integer num = result.get(prize);
				num = num == null ? 1 : num +1;
				result.put(prize, num);
			}
		}
		return result;
	}

	/**
	 * 单式计算
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月25日 下午4:48:36
	 * @param content
	 * @return
	 */
	private Map<String, Integer> simple(String content) {
		Map<String, Integer> result = new HashMap<>();
		String[] codes = StringUtils.tokenizeToStringArray(content, "|;");
		boolean[] in = new boolean[7];
		for (int i = 0; i < codes.length; i += 7) {
			for (int j = 0; j < drawCodeAll.length; j++) {
				if (Objects.equals(codes[i + j], drawCodeAll[j])) {
					in[j] = true;
				} else {
					in[j] = false;
				}
			}
			String prize = prize(in);
			if (prize != null) {
				Integer num = result.get(prize);
				num = num == null ? 1 : num +1;
				result.put(prize, num);
			}
		}
		return result;
	}

	/**
	 * 判断中几等奖
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月25日 下午4:59:20
	 * @param in
	 * @return
	 */
	private String prize(boolean[] in) {
		int temp = 0;
		int num = 0;
		for (boolean b : in) {
			if (b) {
				num ++;
				temp = temp < num ? num : temp;
			} else {
				num = 0;
			}
		}
		switch (temp) {
		case 2:
			return "六等奖";
		case 3:
			return "五等奖";
		case 4:
			return "四等奖";
		case 5:
			return "三等奖";
		case 6:
			return "二等奖";
		case 7:
			return "一等奖";
		default:
			break;
		}
		return null;
	}
}
