package com.hhly.award.service.award.lottery.array;

/**
 * @desc 排列开奖计算
 * @author jiangwei
 * @date 2017年6月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICalculate {
	/**
	 * 直选的计算方式
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 上午11:16:55
	 * @param contentType 投注类型
	 * @param content 投注类容
	 * @param drawCode 开奖号码
	 * @param sum 和值
	 * @return
	 */
	int direct(int contentType, String content,String[] drawCode,String sum);
	/**
	 * 组3 玩法
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 上午11:35:52
	 * @param contentType 
	 * @param content
	 * @param drawCode
	 * @param sum
	 * @param countNum
	 * @param sameCode 相同号码
	 * @return
	 */
	int groupThree(int contentType, String content,String[] drawCode,String sum,int countNum,String sameCode);
	
	/**
	 * 组6 玩法
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 上午11:35:52
	 * @param contentType
	 * @param content
	 * @param drawCode
	 * @param sum
	 * @param countNum
	 * @return
	 */
	int groupSix(int contentType, String content,String[] drawCode,String sum,int countNum);
	
}
