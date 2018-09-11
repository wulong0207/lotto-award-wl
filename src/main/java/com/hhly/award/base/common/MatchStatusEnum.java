package com.hhly.award.base.common;
/**
 * 赛事状态枚举
 * @ClassName: MatchStatusEnum 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author wuLong
 * @date 2017年5月29日 下午12:30:47 
 *
 */
public class MatchStatusEnum {
	//老足彩赛事状态，1：等待比赛；2：比赛中；3：已完场；4：延期；5：取消
	//其它竞技彩赛事状态：6：暂定赛程；7：未开售；8：预售中；9：销售中；10：暂停销售；11：销售截止；12：比赛进行中；13：延期；14：取消；15：已开奖；16：已派奖；17：已审核；数据从官方抓取，暂停销售为我们自己系统的状态，若为暂停销售，则数据抓取获得的状态不能改变此赛事状态。
	
	public enum OldMatchStatusEnum{
		WAITING_PAY("等待比赛", (short) 1), 
		PAYING("比赛中", (short) 2),
		OVER_PAY("已完场", (short) 3),
		DELAY_PAY("延期", (short) 4), 
		CANCLE_PAY("取消", (short) 5),
		PROVISIONAL("暂定赛程", (short) 6);
		
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private short value;

		private OldMatchStatusEnum(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public void setValue(short value) {
			this.value = value;
		}
		
		public static OldMatchStatusEnum parsePayStatus(short value){
			for(OldMatchStatusEnum payStatus : OldMatchStatusEnum.values()){
				if(payStatus.value == value){
					return payStatus;
				}
			}
			return null;
		}
	}
	
	
	/**
	 * 竞技彩赛事状态
	 * @ClassName: SportMatchStatusEnum 
	 * @Description: TODO(这里用一句话描述这个类的作用) 
	 * @author wuLong
	 * @date 2017年5月30日 下午2:38:18 
	 *
	 */
	public enum SportMatchStatusEnum{
		
		NOT_ON_SALE("未开售", (short) 7),
		PRE_SALE("预售中", (short) 8),
		IN_SALE("销售中", (short) 9),
		SUSPENDED_SALE("暂停销售", (short) 10),
		SALE_DEAD("销售截止", (short) 11),
		PAYING("比赛进行中", (short) 12),
		DELAY_PAY("延期", (short) 13),
		CANCLE_PAY("取消", (short) 14),
		OPEN_AWARD("已开奖", (short) 15),
		SEND_AWARD("已派奖", (short) 16),
        REVIEW("已审核", (short) 17),
        OVER("已淘汰", (short) 18);

        /**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private short value;

		private SportMatchStatusEnum(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public void setValue(short value) {
			this.value = value;
		}
		
		public static SportMatchStatusEnum parsePayStatus(short value){
			for(SportMatchStatusEnum payStatus : SportMatchStatusEnum.values()){
				if(payStatus.value == value){
					return payStatus;
				}
			}
			return null;
		}
	}
	
}
