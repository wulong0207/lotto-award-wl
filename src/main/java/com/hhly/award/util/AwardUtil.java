package com.hhly.award.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.SportAgainstInfoBO;

/**
 * 
 * @ClassName: SportsSplitTicket 
 * @Description: 足彩拆票工具类 
 * @author wuLong
 * @date 2017年4月5日 下午5:25:51 
 *
 */
@Component
public class AwardUtil {
	/**
	 * 
	 * @Description: list拆分
	 * @param mb 多少条  为一份
	 * @param list 总记录
	 * @return
	 * @throws NumberFormatException
	 * @author wuLong
	 * @param <T>
	 * @date 2017年5月24日 下午2:51:59
	 */
	public static <T> List<List<T>> subList(Integer mb , List<T> list) throws NumberFormatException{
		int a = list.size();
		int b = a/mb;
		int modular = a%mb;
		if(modular > 0){
			b = b + 1;
		}
		List<List<T>> lists = new ArrayList<>();
		for(int i = 0; i<b ;i++){
			if(i==b-1 && modular>0){
				lists.add(list.subList(i*mb, i*mb+modular));
			}else{
				lists.add(list.subList(i*mb, (i+1)*mb));
			}
		}
		return lists;
	}
	
	
	public static String getBuyScreen(String content)throws ServiceRuntimeException {
		if(ObjectUtil.isBlank(content)){
			return null;
		}
		String[] ct = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		StringBuffer sb = new StringBuffer();
		for (String a : ct){
			if(a.contains(SymbolConstants.UNDERLINE)){
				sb.append(a.substring(0,a.indexOf(SymbolConstants.UNDERLINE)));
			}else{
				if(a.contains(SymbolConstants.MIDDLE_PARENTHESES_LEFT)){
					sb.append(a.substring(0,a.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)));
				}else{
					sb.append(a.substring(0,a.indexOf(SymbolConstants.PARENTHESES_LEFT)));
				}
			}
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	/**
	 * 
	 * @Description: 得到所选的赛事最早开赛的时间
	 * @param buyScreen 所选的赛事  逗号分隔
	 * @param againstInfoBOs 对阵集合
	 * @return  Date
	 * @author wuLong
	 * @date 2017年5月17日 上午10:18:22
	 */
	public static Date getSportsTicketFirstPlayTime(String buyScreen,List<SportAgainstInfoBO> againstInfoBOs)throws ServiceRuntimeException {
		Date firstPlayTime = null;
		List<String> bs = Arrays.asList(buyScreen.split(SymbolConstants.COMMA));
		Collections.sort(bs);
		String systemCode = bs.get(0);
		for(SportAgainstInfoBO sportAgainstInfoBO : againstInfoBOs){
			if(systemCode.equals(sportAgainstInfoBO.getSystemCode())){
				firstPlayTime = sportAgainstInfoBO.getStartTime();
				break;
			}
		}
		return firstPlayTime;
	}
	/**
	 * 判断是否需要按照比赛场地开奖
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月14日 上午10:37:59
	 * @param lotteryCode
	 * @return
	 */
	public static  boolean isSport(int lotteryCode){
		return lotteryCode == Lottery.FB.getName() || lotteryCode == Lottery.BB.getName()
				|| lotteryCode == Lottery.BJDC.getName() || lotteryCode == Lottery.SFGG.getName() || lotteryCode == Lottery.GYJJC.getName() || lotteryCode == Lottery.GJJC.getName();
	}
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		for(int i = 1;i<102;i++){
			list.add(i+"");
		}
		List<List<String>> lists = subList(10, list);
		for(List<String> list2 : lists){
			System.out.println(list2);
		}
	}
}
