package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.util.MatchUtil;
/**
 * @desc 任选计算
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractOptionalCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, List<String> drawCode) {
		String[] codes = content.split("[,;]");
		int count = 0;
		int temp = 0;
		int num = getNum();
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			if (drawCode.contains(code)) {
				temp++;
			}
			if (i % num == 0) {
				if (temp == num) {
					count++;
				}
				temp = 0;
			}
		}
		return new WinInfo(count, getPrize());
	}

	@Override
	public WinInfo complex(String content, List<String> drawCode) {
		String[] codes = content.split(",");
		int num = 0;
		for (String code : codes) {
			if(drawCode.contains(code)){
				num ++;
			}
		}
		int count = MatchUtil.pac(num, getNum());
		return new WinInfo(count, getPrize());
	}

	/**
	 * 选好个数
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月4日 下午5:53:25
	 * @return
	 */
	public abstract int getNum();

	/**
	 * 中奖类型
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月4日 下午5:53:32
	 * @return
	 */
	public abstract String getPrize();

}
