package com.hhly.award.service.award.lottery.high;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc 中奖信息
 * @author jiangwei
 * @date 2017年7月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class WinInfo {

	Map<String, Integer> prize = new HashMap<String, Integer>();

	public WinInfo() {

	}

	public WinInfo(int num, String prize) {
		this.prize.put(prize, num);
	}

	public void addPrize(int num, String prize) {
		Integer oldNum = this.prize.get(prize);
		num = oldNum == null ? num : num + oldNum;
		this.prize.put(prize, num);
	}

	public void addPrize(WinInfo info) {
		for (Map.Entry<String, Integer> p : info.getPrize().entrySet()) {
			if (prize.containsKey(p.getKey())) {
				prize.put(p.getKey(), prize.get(p.getKey()) + p.getValue());
			} else {
				prize.put(p.getKey(), p.getValue());
			}
		}
	}

	public int getAllCount() {
		int total = 0;
		for (Integer num : prize.values()) {
			total += num;
		}
		return total;
	}

	public Map<String, Integer> getPrize() {
		return prize;
	}
}
