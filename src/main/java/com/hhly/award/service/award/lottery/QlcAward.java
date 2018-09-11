package com.hhly.award.service.award.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.TicketInfoBO;
import com.hhly.award.service.award.entity.WinMoneyBO;
import com.hhly.award.util.MatchUtil;

/**
 * @desc 七乐彩开奖
 * @author jiangwei
 * @date 2017年7月31日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class QlcAward extends AbstractNumberAward {
	// 普通开奖号码
	private List<String> drawCodeList;
	// 特别开奖号码
	private String specialCode;
	// 中奖规则对应 中奖等级
	private static Map<String, String> GRADE;

	static {
		GRADE = new HashMap<String, String>() {
			private static final long serialVersionUID = 1312410620651458077L;
			{
				put("7-0", "一等奖");
				put("6-1", "二等奖");
				put("6-0", "三等奖");
				put("5-1", "四等奖");
				put("5-0", "五等奖");
				put("4-1", "六等奖");
				put("4-0", "七等奖");
			}
		};
	}

	@Override
	protected void handleDrawCode(String code) {
		String[] str = code.split(",");
		drawCodeList = new ArrayList<>();
		for (int i = 0; i < str.length; i++) {
			if (i == 7) {
				specialCode = str[i];
			} else {
				drawCodeList.add(str[i]);
			}
		}
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 2);
		addDraw = splitDrawDetail(drawDetail, 0, 3);
	}

	@Override
	protected boolean haveDrawCode() {
		return CollectionUtils.isNotEmpty(drawCodeList);
	}

	@Override
	protected String getLevel(String prize) {
		return GRADE.get(prize);
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
		} else if (detail.getContentType() == 3) {
			prize = gallDrag(content);
		} else {
			throw new ServiceRuntimeException("玩法类型错误ContentType：" + detail.getContentType());
		}
		// 计算中奖金额
		return computeMoney(prize, detail.getMultipleNum(), true);
	}

	/**
	 * 胆拖 01,02#03,04,05,06,08,09
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月1日 上午11:53:45
	 * @param content
	 * @return
	 */
	private Map<String, Integer> gallDrag(String content) {
		// 对号码拆分，计算胆拖的中奖号码和未中奖号码个数，以及特殊号码位置
		String[] gd = content.split("#");
		int gall = 0, gallIn = 0, drag = 0, dragIn = 0, special = 0;
		for (int i = 0; i < gd.length; i++) {
			String[] codes = gd[i].split(",");
			int in = 0;
			for (String code : codes) {
				if (drawCodeList.contains(code)) {
					in++;
				}
				if (code.equals(specialCode)) {
					special = i == 0 ? 1 : 2;
				}
			}
			if (i == 0) {
				gall = codes.length - in;
				gallIn = in;
			} else {
				drag = codes.length - in;
				dragIn = in;
			}
		}
		return calculate(gall, gallIn, drag, dragIn, special);
	}

	/**
	 * 复试
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月31日 下午3:40:26
	 * @param content
	 * @return
	 */
	private Map<String, Integer> reexamine(String content) {
		String[] codes = content.split(",");
		int before = 0;
		int special = 0;
		for (String code : codes) {
			if (drawCodeList.contains(code)) {
				before++;
			}
			if (code.equals(specialCode)) {
				special = 2;
			}
		}
		return calculate(0, 0, codes.length - before, before, special);
	}

	/**
	 * 单式 七乐彩通过摇奖器确定开奖号码。摇奖时先摇出7个号码作为基本号码，再摇出1个号码作为特别号码。
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月31日 下午3:15:54
	 * @param content
	 * @return
	 */
	private Map<String, Integer> simple(String content) {
		Map<String, Integer> result = new HashMap<>();
		String[] codes = content.split("[,;]");
		int before = 0;
		int special = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			if (drawCodeList.contains(code)) {
				before++;
			}
			if (code.equals(specialCode)) {
				special = 1;
			}
			if (i % 7 == 0) {
				String key = before + "-" + special;
				Integer num = result.get(key);
				num = num == null ? 1 : num + 1;
				result.put(key, num);
				before = 0;
				special = 0;
			}
		}
		return result;
	}

	/**
	 * 七乐彩通过摇奖器确定开奖号码。摇奖时先摇出7个号码作为基本号码，再摇出1个号码作为特别号码。
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月31日 下午5:21:19
	 * @param gall
	 *            胆码未中
	 * @param gallIn
	 *            胆码中
	 * @param drag
	 *            托马未中
	 * @param dragIn
	 *            托马中
	 * @param special
	 *            特殊号码 中奖位置，1.胆码区，2.托马区
	 * @return
	 */
	private Map<String, Integer> calculate(int gall, int gallIn, int drag, int dragIn, int special) {
		Map<String, Integer> result = new HashMap<>();
		if (gallIn + dragIn < 4) {
			return result;
		}
		for (int i = 4 - gallIn; i <= dragIn; i++) {
			int dragN = 7 - gall - gallIn - i;
			int numNotIn = MatchUtil.pac(drag, dragN);
			int numIn = MatchUtil.pac(dragIn, i);
			if (numNotIn == 0 && dragN != 0) {
				continue;
			}
			int in = i + gallIn;
			if (special == 2) {
				int n = 7 - gall - gallIn - i - 1;
				int after = 0;
				if (n > 0) {
					after = MatchUtil.pac(drag - 1, n);
				} else if (n == 0) {
					after = 1;
				} else {
					after = -1;
				}
				int count = 0;
				if ((count = numIn * (numNotIn - after)) > 0) {
					result.put(in + "-0", count);
				}
				if ((count = numIn * after) > 0) {
					result.put(in + "-1", count);
				}
			} else {
				numNotIn = numNotIn == 0 ? 1 : numNotIn;
				if (special == 1) {
					result.put(in + "-1", numIn * numNotIn);
				} else {
					result.put(in + "-0", numIn * numNotIn);
				}
			}

		}
		return result;
	}

	public static void main(String[] args) {
		QlcAward qlcAward = new QlcAward();
		qlcAward.handleDrawCode("01,02,03,04,05,06,07,08");
		Map<String, Integer> result = qlcAward.reexamine("01,02,03,04,05,06,07,18,19");
		for (Map.Entry<String, Integer> entry : result.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println(MatchUtil.pac(7, 5));
		System.out.println(MatchUtil.pac(7, 6) *2);
	}
}
