package com.hhly.award.service.award.lottery.array;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.util.DrawCodeUtil;

/**
 * @desc 排列3 开奖
 * @author jiangwei
 * @date 2017年6月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ArrayThreeCalculate implements ICalculate {

	@Override
	public int direct(int contentType, String content, String[] drawCode, String sum) {
		int num = 0;
		if (contentType == 1) {
			String[] codes = StringUtils.tokenizeToStringArray(content, "|;,");
			num =  DrawCodeUtil.simple(codes, drawCode, 3);
		} else if (contentType == 2) {
			// 直选复选 选择号码与开奖位置一致且相同
			num = DrawCodeUtil.directRepeated(content, "|", ",", drawCode);
		} else if (contentType == 6) {
			if (Objects.equals(sum, content)) {
				num = 1;
			}
		}
		return num;

	}

	@Override
	public int groupThree(int contentType, String content, String[] drawCode, String sum, int countNum,String sameCode) {
		//判断是否是组3形态（开奖2个不同号码）
		if (countNum != 2) {
			return 0;
		}
		return group(contentType, content, drawCode, sum, sameCode);
	}
	
	@Override
	public int groupSix(int contentType, String content, String[] drawCode, String sum, int countNum) {
		//判断是否是组6形态 (开奖3个不同号码)
		if (countNum != 3) {
			return 0;
		}
		return group(contentType, content, drawCode, sum, null);
	}
	/**
	 * 组选计算
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 下午4:00:39
	 * @param contentType 选好类型
	 * @param content 类容
	 * @param drawCode 开奖号码
	 * @param sum 和值
	 * @param sameCode 相同号码
	 * @return
	 */
	private int group(int contentType, String content, String[] drawCode, String sum, String sameCode) {
		int num = 0;
		if (contentType == 1) {
			num = groupSimple(content, drawCode, sameCode);
		} else if (contentType == 2) {
			// 选择号码完全包含开奖号码
			num = repeated(content, drawCode);
		} else if (contentType == 6) {
			if (Objects.equals(sum, content)) {
				num = 1;
			}
		}
		return num;
	}
	/**
	 * 组选单式（投注号码与开奖号码一致，顺序不限制）
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 下午4:02:58
	 * @param content 类容
	 * @param drawCode 开奖号码
	 * @param sameCode 相同号码
	 * @return
	 */
	private int groupSimple(String content, String[] drawCode, String sameCode) {
		String[] codes = content.split("[,;]");
		int num =0;
		int inNum = 0;
		int sameNum = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			int remain = i % 3;
			remain = remain == 0 ? 3 : remain;
			// 直选位置要一致
			inNum += DrawCodeUtil.include(code, drawCode);
			if(Objects.equals(sameCode, code)){
				sameNum++;
			}
			if (remain == 3) {
				if (inNum == 3
						&&(sameCode == null || sameNum == 2)) {
					num++;
				}
				sameNum = 0;
				inNum = 0;
			}
		}
		return num;
	}
	/**
	 * 组选复试(所选号码包含开奖号码)
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 下午4:01:50
	 * @param content
	 * @param drawCode
	 * @return
	 */
	private int repeated(String content, String[] drawCode) {
		int num = 0;
		String[] all = StringUtils.tokenizeToStringArray(content, ",");
		int inNum = 0;
		for (String string : drawCode) {
			inNum += DrawCodeUtil.include(string, all);
		}
		if (inNum == 3) {
			num = 1;
		}
		return num;
	}

}
