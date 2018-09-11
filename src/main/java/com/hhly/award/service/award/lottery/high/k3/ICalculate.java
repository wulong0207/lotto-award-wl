package com.hhly.award.service.award.lottery.high.k3;

import java.util.Set;

import com.hhly.award.service.award.lottery.high.WinInfo;
/**
 * @desc 快3 开奖 
 * @author jiangwei
 * @date 2017年6月9日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ICalculate {
	 /**
	  * 和值
	  * @author jiangwei
	  * @Version 1.0
	  * @CreatDate 2017年6月9日 下午12:04:09
	  * @param sum 和值
	  * @param content 投注类容
	  * @return 注数
	  */
	WinInfo sum(String sum,String content);
     /**
      * 3同号通选
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:05:56
      * @param drawCodeSet
      * @return
      */
	WinInfo threeSame(Set<String> drawCodeSet);
     
     /**
      * 3同号单选
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:05:56
      * @param drawCodeSet
      * @return
      */
	WinInfo threeSameSimple(Set<String> drawCodeSet,String content);
     
     /**
      * 三不同号
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:05:56
      * @param drawCodeSet
      * @return
      */
	WinInfo threeDifferenceSimple(Set<String> drawCodeSet,String content);
     /**
      * 三连号通选
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:09:38
      * @param consecutive
      * @return
      */
	WinInfo consecutive(boolean consecutive);
     
     /**
      * 2同号复选
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:05:56
      * @param drawCodeSet
      * @return
      */
	WinInfo twoSame(String twoSame,String content);
     /**
      * 2同号单选
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:05:56
      * @param drawCodeSet
      * @return
      */
	WinInfo twoSameSimple(Set<String> drawCodeSet,String twoSame,String content);
     
     /**
      * 二不同号
      * @author jiangwei
      * @Version 1.0
      * @CreatDate 2017年6月9日 下午12:05:56
      * @param drawCodeSet
      * @return
      */
	WinInfo twoDifferenceSimple(Set<String> drawCodeSet,String content);
}
