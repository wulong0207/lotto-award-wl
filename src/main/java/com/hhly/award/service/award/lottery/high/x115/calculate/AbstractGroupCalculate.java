package com.hhly.award.service.award.lottery.high.x115.calculate;

import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * 组选计算规则
 * @desc
 * @author jiangwei
 * @date 2017年6月3日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractGroupCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String[] all = content.split("[,;]");
		//组选方式
		int type = getNum();
		int drawNum = 0;
		int draw = 0;
		for (int i = 1; i <= all.length; i++) {
			String str = all[i - 1];
			//比对选择号码与前3个号码是否相同
			for (int j = 0; j < type; j++) {
				if (str.equals(drawCode[j])) {
					draw++;
					break;
				}
			}
			int num = i % type;
			if (num == 0) {
				if (draw == type) {
					drawNum++;
				}
				draw = 0;
			}
		}
		return new WinInfo(drawNum, getPrize());
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		String[] all = content.split(",");
		int num = 0;
		int type = getNum();
		int count = 0;
		win:for (String str : all) {
			//判断选择号码是否包含前三个
			for (int i = 0; i < type; i++) {
				if (str.equals(drawCode[i])) {
					num++;
					if (num == type) {
						count = 1;
						break win;
					}
					break;
				}
			}
		}
		return new WinInfo(count, getPrize());
	}

	@Override
	public WinInfo gallDrag(String content, String[] drawCode) {
		String[] all = content.split("#");
		String[] before = all[0].split(",");
		String[] after = all[1].split(",");
		int type = getNum();
		int num = 0;
		//胆码区中码个数
		for (String str : before) {
			for (int i = 0; i < type; i++) {
				if (str.equals(drawCode[i])) {
					num++;
					break;
				}
			}
		}
		int count =0;
		//胆码区必须全中
		if (num == before.length) {
			//托区是否中码
			win:for (String str : after) {
				for (int i = 0; i < type; i++) {
					if (str.equals(drawCode[i])) {
						num ++;
						break;
					}
				}
				if(num == type){
					count = 1;
					break win;
				}
			}
		}
		return new WinInfo(count, getPrize());
	}
	/**
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:54:41
	 * @return 中奖奖项
	 */
	protected abstract String getPrize();
	/**
	 * 选号方式
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月3日 上午8:55:01
	 * @return
	 */
	protected abstract int getNum();
}
