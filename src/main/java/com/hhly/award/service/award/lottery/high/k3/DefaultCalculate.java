package com.hhly.award.service.award.lottery.high.k3;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 快3 默认实现开奖
 * @author jiangwei
 * @date 2017年6月9日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class DefaultCalculate implements ICalculate {

	@Override
	public WinInfo sum(String sum, String content) {
		int num = 0;
		if (sum.equals(content)) {
			num =  1;
		}
		return new WinInfo(num, getSumPrize(sum));
	}

	@Override
	public WinInfo threeSame(Set<String> drawCodeSet) {
		int num = 0;
		if (drawCodeSet.size() == 1) {
			num =  1;
		}
		return new WinInfo(num, "三同号通选");
	}

	@Override
	public WinInfo threeSameSimple(Set<String> drawCodeSet, String content) {
		int totalNum = 0;
		if (drawCodeSet.size() == 1) {
			int num = 0;
			String[] cotents = content.split(SymbolConstants.SEMICOLON);
			for (String string : cotents) {
				if (drawCodeSet.contains(string.substring(0, 1))) {
					num++;
				}
			}
			totalNum =  num;
		}
		return new WinInfo(totalNum, "三同号单选");
	}

	@Override
	public WinInfo threeDifferenceSimple(Set<String> drawCodeSet, String content) {
		int num = 0;
		if (drawCodeSet.size() >= 3) {
			num = difference(drawCodeSet, content, 3);
		}
		return new WinInfo(num, "三不同号");
	}

	@Override
	public WinInfo consecutive(boolean consecutive) {
		int num = 0;
		if (consecutive) {
			num =  1;
		}
		return new WinInfo(num, "三连号通选");
	}

	@Override
	public WinInfo twoSame(String twoSame, String content) {
		int num = 0;
		if (StringUtils.isNotBlank(twoSame) && content.indexOf(twoSame) != -1) {
			String str[] = content.split(SymbolConstants.SEMICOLON);
			String code = twoSame + twoSame + "*";
			for (String string : str) {
				if (code.equals(string)) {
					num++;
				}
			}
		}
		return new WinInfo(num, "二同号复选");
	}

	@Override
	public WinInfo twoSameSimple(Set<String> drawCodeSet, String twoSame, String content) {
		int num = 0;
		if (StringUtils.isNotBlank(twoSame)) {
			String[] cons = content.split("[,;]");
			int temp = 0;
			int same = 0;
			for (int i = 1; i <= cons.length; i++) {
				String str = cons[i - 1];
				if (drawCodeSet.contains(str)) {
					temp++;
				}
				if (str.equals(twoSame)) {
					same++;
				}
				if (i % 3 == 0) {
					if (temp == 3 && same == 2) {
						num++;
					}
					temp = 0;
					same = 0;
				}
			}
		}
		return new WinInfo(num, "二同号单选");
	}

	@Override
	public WinInfo twoDifferenceSimple(Set<String> drawCodeSet, String content) {
		int num = 0;
		if (drawCodeSet.size() > 1) {
			num =  difference(drawCodeSet, content, 2);
		}
		return new WinInfo(num, "二不同号");
	}

	/**
	 * 比对号码个数
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月9日 下午3:04:36
	 * @param drawCodeSet
	 * @param content
	 * @param type
	 * @return
	 */
	private int difference(Set<String> drawCodeSet, String content, int type) {
		String[] cons = content.split("[,;]");
		int num = 0;
		int temp = 0;
		for (int i = 1; i <= cons.length; i++) {
			String str = cons[i - 1];
			if (drawCodeSet.contains(str)) {
				temp++;
			}
			if (i % type == 0) {
				if (temp == type) {
					num++;
				}
				temp = 0;
			}
		}
		return num;
	}
	/**
	 * 获取和值奖项
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月8日 下午5:31:14
	 * @return
	 */
	private String getSumPrize(String sum) {
		return "和值" + sum;
	}
}
