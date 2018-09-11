package com.hhly.award.base.common;

import java.util.Objects;

/**
 * @author huangb
 *
 * @Date 2016年12月1日
 *
 * @Desc 彩种子玩法类型
 */
public class LotteryChildEnum {

	public enum LotteryChild {
		SSQ_PT(10001, "普通投注"), SSQ_DT(10002, "胆拖投注"),
		DLT_PT(10201, "普通投注"), DLT_DT(10202, "胆拖投注"),
		/**
		 * 山东十一选五子玩法
		 */
		// 山东十一选五任选二
		SD11X5_R2(21502,"山东十一选五任选二"),
		// 山东十一选五任选三
		SD11X5_R3(21503,"山东十一选五任选三"),
		// 山东十一选五任选四
		SD11X5_R4(21504,"山东十一选五任选四"),
		// 山东十一选五任选五
		SD11X5_R5(21505,"山东十一选五任选五"),
		// 山东十一选五任选六
		SD11X5_R6(21506,"山东十一选五任选六"),
		// 山东十一选五任选七
		SD11X5_R7(21507,"山东十一选五任选七"),
		// 山东十一选五任选八
		SD11X5_R8(21508,"山东十一选五任选八"),
		// 山东十一选五前一
		SD11X5_Q1(21509,"山东十一选五前一"),
		// 山东十一选五前二组选
		SD11X5_Q2_GROUP(21510,"山东十一选五前二组选"),
		// 山东十一选五前二直选
		SD11X5_Q2_DIRECT(21511,"山东十一选五前二直选"),
		// 山东十一选五前三组选
		SD11X5_Q3_GROUP(21512,"山东十一选五前三组选"),
		// 山东十一选五前三直选
		SD11X5_Q3_DIRECT(21513,"山东十一选五前三直选"),
		
		/**
		 * 江苏快3
		 */
		/**和值*/
		JSK3_S(23301),
		/**三同号通选*/
		JSK3_TT3(23302),
		/**三同号单选*/
		JSK3_TD3(23303),
		/**三不同号*/
		JSK3_BT3(23304),
		/**三连号通选*/
		JSK3_L3(23305),
		/**二同号复选*/
		JSK3_TF2(23306),
		/**二同号单选*/
		JSK3_TD2(23307),
		/**二不同号*/
		JSK3_BT2(23308),
		/**
		 * 一码包中
		 * 一码包中时，传到后台都是拆分好的单式
		 * */
		//JSK3_BZ1(23309),
		
		
		
		/**
		 * 竞彩足球
		 */
		/**混投*/
		ID_FHT(30001, "竞彩足球混投"),
		/**胜平负*/
		ID_JCZQ(30002, "竞彩足球胜平负"),
		/**让胜平负*/
		ID_RQS(30003, "竞彩足球让球胜平负"),
		/**比分*/
		ID_FBF(30004, "竞彩足球比分"),
		/**总进球*/
		ID_FZJQ(30005, "竞彩足球总进球"),
		/**半全场*/
		ID_FBCQ(30006, "竞彩足球半全场"),
		
        /**
         * 竞彩篮球
         */
		/*竞彩篮球胜负*/
		ID_JCLQ_SF(30101, "竞彩篮球胜负"),
		/*竞彩篮球让分*/
		ID_JCLQ_RF(30102, "竞彩篮球让分胜负"),
		/*竞彩篮球大小分*/
		ID_JCLQ_DXF(30103, "竞彩篮球大小分"),
		/*竞彩篮球胜分差*/
		ID_JCLQ_SFC(30104, "竞彩篮球胜分差"),
		/**混投*/
		ID_JCLQ_HHGG(30105, "竞彩篮球混投"),

		/***老足彩9场胜平负普通投注*/
		ID_NINE_BET(30501, "9场胜平负普通投注"),
		/***老足彩9场胜平负胆拖投注*/
		ID_NINE_BANKERS_BET(30502, "9场胜平负胆拖投注"),

		/**
		 * 北单
		 */
		/**   让球胜平负*/
		ID_BD_RQS(30601, "北单让球胜平负"),
		/** 上下单双 */
		ID_BD_SXDX(30602, "北单上下单双"),
		/**   总进球 */
		ID_BD_FZJQ(30603, "北单总进球"),
		/**   比分 */
		ID_BD_FBF(30604, "北单比分"),
		/**   半全场 */
		ID_BD_FBCQ(30605, "北单半全场"),
		
		/** 胜负过关 */
		ID_SFGG_PT(30701, "胜负过关普通投注"),
		
		/**十四场*/
		ID_FOURTEEN(30401, "十四场普通投注");
		
		/**
		 * 类型值
		 */
		private int value;

		/**
		 * 描述
		 */
		private String desc;

		LotteryChild(int value) {
			this.value = value;
		}

		LotteryChild(int value, String desc) {
			this(value);
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		/**
		 * @param value
		 *            类型值
		 * @return true/false
		 * @Desc 是否包含指定类型
		 */
		public static boolean contain(Integer value) {
			if (value == null) {
				return false;
			}
			for (LotteryChild temp : LotteryChild.values()) {
				if (Objects.equals(temp.getValue(), value)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * @param value
		 *            类型值
		 * @return true/false
		 * @Desc 是否是双色球玩法
		 */
		public static boolean isSsq(Integer value) {
			if (value == null) {
				return false;
			}
			// ssq-两种子玩法
            return LotteryChild.SSQ_PT.getValue() == value.intValue()
                    || LotteryChild.SSQ_DT.getValue() == value.intValue();
        }

		public static LotteryChild valueOf(Integer value) {
			if (null == value) {
				return null;
			}
			for (LotteryChild child : values()) {
				if (Objects.equals(value, child.getValue())) {
					return child;
				}
			}
			return null;
		}
		
		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	public enum SaleStatus {

		STOP("暂停销售", (short) 0), NORMAL("正常销售", (short) 1),
		END("截止销售",(short) 4);

		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private short value;

		SaleStatus(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		/**
		 * @param value
		 * @return
		 * @Desc 是否包含指定状态
		 */
		public static boolean contain(short value) {
			for (SaleStatus temp : SaleStatus.values()) {
				if (temp.getValue() == value) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 是否正常销售
		 * @param value
		 * @return
		 */
		public static boolean isSalable(Short value) {
			return Objects.equals(NORMAL.getValue(), value);
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
	}

}
