package com.hhly.award.service.award.lottery.high.k3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.service.award.lottery.AbstractNumberAward;

/**
 * @desc 快3 开奖
 * @author jiangwei
 * @date 2017年6月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractK3Award extends AbstractNumberAward {
	/**
	 * 拆分后的开奖号码
	 */
	protected Set<String> drawCodeSet;
	/**
	 * 和值
	 */
	protected String sum;
	/**
	 * 2同号
	 */
	protected String twoSame;
	/**
	 * 连号
	 */
	protected Boolean consecutive = Boolean.FALSE;

	@Override
	protected void handleDrawCode(String code) {
		String[] codes = code.split(SymbolConstants.COMMA);
		int num = 0;
		drawCodeSet = new HashSet<>();
		int [] intNum = new int[3];
		for (int i = 0; i < codes.length; i++) {
			int size = drawCodeSet.size();
			drawCodeSet.add(codes[i]);
			//计算2同号
			if(size == drawCodeSet.size()){
				twoSame = codes[i];
			}
			int codeNum = Integer.parseInt(codes[i]);
			// 计算和值
			num += codeNum;
			intNum[i] = codeNum;
		}
		sum = String.valueOf(num);
		//计算是否连号
		Arrays.sort(intNum);
		if(intNum[1] -  intNum[0] ==1
				&&intNum[2] -  intNum[1] ==1){
			consecutive = Boolean.TRUE;
		}
	}

	@Override
	protected void handleDrawDetail(String drawDetail) {
		draw = splitDrawDetail(drawDetail, 0, 1);
		addDraw = splitDrawDetail(drawDetail, 0, 2);
	}

	@Override
	protected boolean haveDrawCode() {
		return CollectionUtils.isNotEmpty(drawCodeSet);
	}

	@Override
	protected String getLevel(String prize) {
		return prize;
	}

}
