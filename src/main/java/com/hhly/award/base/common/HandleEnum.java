
package com.hhly.award.base.common;
/**
 * @desc    
 * @author  cheng chen
 * @date    2017年6月19日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class HandleEnum {

	public enum Type{
		
		OPEN_AWARD((short)1, "开奖"), 
		
		PAYOUT_AWARD((short)2, "派奖");
		
		private Type(short value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

		private short value;
		
		private String desc;
	}
}
