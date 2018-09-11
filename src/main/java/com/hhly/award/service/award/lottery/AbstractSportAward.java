package com.hhly.award.service.award.lottery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.hhly.award.persistence.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.award.base.common.LotteryEnum;
import com.hhly.award.base.common.MatchStatusEnum;
import com.hhly.award.base.common.SymbolConstants;
import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.bo.OrderInfoBO;
import com.hhly.award.bo.SportAgainstInfoBO;
import com.hhly.award.bo.SportDrawBBBO;
import com.hhly.award.bo.SportDrawBJBO;
import com.hhly.award.bo.SportDrawFBBO;
import com.hhly.award.bo.SportDrawOldInfoBO;
import com.hhly.award.bo.SportDrawWFBO;
import com.hhly.award.service.award.AbstractAward;
import com.hhly.award.util.ObjectUtil;
import com.hhly.award.util.RedisUtil;

public abstract class AbstractSportAward extends AbstractAward {
	protected Logger logger = LoggerFactory.getLogger(AbstractSportAward.class);
	/**赛事集合**/
	protected  Vector<SportAgainstInfoBO> againstInfoBOs =  new Vector<>();

	protected Map<Integer, List<String>> systemCodeMap = new ConcurrentHashMap<>();
	
	protected  Map<String, SportAgainstInfoBO> sportAginstInfoMap  =new ConcurrentHashMap<>();
	
	/** 老足彩中奖等级对应的中间金额 **/
	protected  Map<String, String> draw = new ConcurrentHashMap<>();
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	SportDrawBBDaoMapper drawBBDaoMapper;
	@Autowired
	SportDrawBJDaoMapper drawBJDaoMapper;
	@Autowired
	SportDrawFBDaoMapper drawFBDaoMapper;
	@Autowired
	SportDrawOldInfoDaoMapper drawOldInfoDaoMapper;
	@Autowired
	SportDrawWFDaoMapper drawWFDaoMapper;

	@Autowired
	private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;
	/** 足球让球胜平负 或 篮球让分胜负 **/
	protected static final String R = "R";
	/** 足球胜平负或篮球胜负 **/
	protected static final String S = "S";
	/** 足球全场比分 **/
	protected static final String Q = "Q";
	/** 足球总进球数 **/
	protected static final String Z = "Z";
	/** 足球半全场胜平负 **/
	protected static final String B = "B";
	/** 篮球胜分差 **/
	protected static final String C = "C";
	/** 篮球大小分 **/
	protected static final String D = "D";
	/** 出票sp 分隔符号 A **/
	protected static final String A = "A";
	/** 表示赛事取消的 符号 * **/
	protected static final String XH = "*";

	/**
	 * 冠军
	 **/
	protected static final String G = "G";

	/**
	 * 冠亚军
	 **/
	protected static final String GY = "GY";

	/**   竞彩足球混投*/
	protected static final int ID_FHT = 30001;//混投
	/**   竞彩足球胜平负 */
	protected static final int ID_JCZQ = 30002;// 胜平负
	/**   竞彩足球让球胜平负*/
	protected static final int ID_RQS = 30003;//让球胜平负
	/**   竞彩足球比分 */
	protected static final int ID_FBF = 30004 ;//比分
	/**   竞彩足球总进球 */
	protected static final int ID_FZJQ = 30005;//总进球
	/**   竞彩足球半全场 */
	protected static final int ID_FBCQ = 30006;//半全场
	
	 /**竞彩篮球胜负**/
	protected static final int ID_JCLQ_SF = 30101;
    /**竞彩篮球让分**/
	protected static final int ID_JCLQ_RF = 30102;
    /**竞彩篮球大小分**/
	protected static final int ID_JCLQ_DXF = 30103;
    /**竞彩篮球胜分差**/
	protected static final int ID_JCLQ_SFC = 30104;
    /**竞彩篮球混合过关**/
	protected static final int ID_JCLQ_HHGG = 30105;
	/**竞彩足球彩种ID集**/
	private static final int[] SPORT_FOOTBAL_LOTTERYS = {ID_FHT, ID_RQS, ID_JCZQ, ID_FBF, ID_FZJQ, ID_FBCQ};
	/**竞彩篮球彩种ID集**/
	private static final int[] SPORT_BASKETBAL_LOTTERYS = {ID_JCLQ_SF, ID_JCLQ_RF, ID_JCLQ_DXF, ID_JCLQ_SFC, ID_JCLQ_HHGG};
	/**扣税税率20%税 **/
	protected static final double TAX_RATE = 0.2;
	/** 胜 **/
	public static final int WON = 3; 
	/** 平 **/
	public static final int FLAT = 1;
	/** 负 **/
	public static final int LOSS = 0;
	/** 大分 **/
	public static final int BIG_SCORE = 99;
	/** 小分 **/
	public static final int SMALL_SCORE = 00;
	
	/**
	 * 单注单倍中奖金额超过1W  扣20%税 
	 */
	protected static final BigDecimal TAX_UPPER_LIMIT_AMOUNT = BigDecimal.valueOf(10000);

	
	@Override
	protected void depthClone(AbstractAward award) {
		AbstractSportAward sportAward =  (AbstractSportAward) award;
		sportAward.againstInfoBOs = new Vector<>();
		sportAward.systemCodeMap = new ConcurrentHashMap<>();
		sportAward.sportAginstInfoMap  = new ConcurrentHashMap<>();
		sportAward.draw = new ConcurrentHashMap<>();
	}
	/**
	 * 
	 * @Description: 修改状态已审核的赛事为已开奖
	 * @param systemCodes
	 * @param lotteryCode
	 * @author wuLong
	 * @date 2017年7月3日 下午4:52:07
	 */
	protected void updateMatchState(String[] a,Integer lotteryCode) {
		List<String> sysCodes = new ArrayList<>();
		for(String b : a){
			SportAgainstInfoBO againstInfoBO = sportAginstInfoMap.get(b+","+lotteryCode);
			if(!ObjectUtil.isBlank(againstInfoBO)&&againstInfoBO.getMatchStatus()==MatchStatusEnum.SportMatchStatusEnum.REVIEW.getValue()){
				sysCodes.add(b);
			}
		}
		if(!sysCodes.isEmpty()){
			sportAgainstInfoService.updateMatchState(sysCodes, lotteryCode);
		}
	}


	/**
	 * 判断彩种ID是否为竞彩足球
	 * @param Id
	 * @return
	 * true:是
	 * false:否
	 *
	 */
	protected static boolean checkFBLotteryId(int lotteryId) {
		for(int tempI : SPORT_FOOTBAL_LOTTERYS){
			if(tempI == lotteryId)
				return true;
		}
		return false;
	}
	
	/**
	 * 判断彩种ID是否为竞彩篮球
	 * @param Id
	 * @return
	 * true:是
	 * false:否
	 *
	 */
	public static boolean checkBBLotteryId(int lotteryId) {
		for(int tempI : SPORT_BASKETBAL_LOTTERYS){
			if(tempI == lotteryId)
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Description: 批量查询赛事信息
	 * @param scs 赛事systemcode集合 
	 * @return List<SportAgainstInfoBO>
	 * @author wuLong
	 * @date 2017年5月25日 上午11:32:20
	 */
	public List<SportAgainstInfoBO> findBySportAgainstInfoSystemCodes(List<OrderInfoBO> orderInfoBOs) {
		if(ObjectUtil.isBlank(orderInfoBOs)){
			return null;
		}
		Map<Integer, List<String>> bMap = new HashMap<>();
		for(OrderInfoBO orderInfoBO : orderInfoBOs){
			int lotteryCode = orderInfoBO.getLotteryCode();
			List<String> systemCodes = new ArrayList<>();
			List<String> b = new ArrayList<>();
			if(systemCodeMap.containsKey(lotteryCode)){
				systemCodes = systemCodeMap.get(lotteryCode);
			}
			if(bMap.containsKey(lotteryCode)){
				b = bMap.get(lotteryCode);
			}
			String buyScreen = orderInfoBO.getBuyScreen();
			logger.info("订单编号:"+orderInfoBO.getOrderCode()+",购买的场次数:"+buyScreen);
			if(ObjectUtil.isBlank(buyScreen)){
				throw new ServiceRuntimeException("订单购买赛事场次为空");
			}
			String[] bs = buyScreen.split(SymbolConstants.COMMA);
			for(String a : bs){
				if(!systemCodes.contains(a)){
					systemCodes.add(a);
				}
				if(!b.contains(a)){
					b.add(a);
				}
			}
			if(!ObjectUtil.isBlank(systemCodes)){
				systemCodeMap.put(lotteryCode, systemCodes);
			}
			if(!ObjectUtil.isBlank(b)){
				bMap.put(lotteryCode, b);
			}
		}
		if(!ObjectUtil.isBlank(againstInfoBOs)){
			for(Integer lotteryCode : systemCodeMap.keySet()){
				List<SportAgainstInfoBO> ais = sportAgainstInfoService.findBySystemCodes(systemCodeMap.get(lotteryCode),lotteryCode);
				againstInfoBOs.addAll(ais);
			}
		}else{
			if(!ObjectUtil.isBlank(bMap)){
				for(Integer lotteryCode : bMap.keySet()){
					List<SportAgainstInfoBO> ais = sportAgainstInfoService.findBySystemCodes(bMap.get(lotteryCode),lotteryCode);
					againstInfoBOs.addAll(ais);
				}
			}
		}
		return againstInfoBOs;
	}
	
	/**
	 * 
	 * @Description: 得到彩种的赛事集合的赛事赛过集合
	 * @param lotteryCode 大彩种id
	 * @return Map<String, Object>
	 * @author wuLong
	 * @date 2017年5月26日 下午4:25:29
	 */
	public Map<String, Object> parseSportAgainstInfo(Integer lotteryCode){
		if(ObjectUtil.isBlank(againstInfoBOs)){
			return null;
		}
		List<Long> sportAgainInfoIds = new ArrayList<>();
		Map<Long, String> mapSystemCodeId = new HashMap<>();
		for(SportAgainstInfoBO againstInfoBO : againstInfoBOs){
			long againstInfoId = againstInfoBO.getId();
			String systemCode = againstInfoBO.getSystemCode();
			sportAgainInfoIds.add(againstInfoId);
			mapSystemCodeId.put(againstInfoId, systemCode);
			sportAginstInfoMap.put(systemCode+","+lotteryCode, againstInfoBO);
		}
		LotteryEnum.Lottery lottery = LotteryEnum.Lottery.getLottery(lotteryCode);
		Map<String, Object> map = new HashMap<>(); 
		switch (lottery) {
			case BB:
				List<SportDrawBBBO> drawBBBOs = drawBBDaoMapper.findBySportAgainstInfoIdS(sportAgainInfoIds);
				if(!ObjectUtil.isBlank(drawBBBOs)){
					for(SportDrawBBBO sportDrawBBBO : drawBBBOs){
						map.put(mapSystemCodeId.get(sportDrawBBBO.getSportAgainstInfoId()), sportDrawBBBO);
					}
				}
				break;
			case FB:
				List<SportDrawFBBO> drawFBBOs = drawFBDaoMapper.findBySportAgainstInfoIdS(sportAgainInfoIds);
				if(!ObjectUtil.isBlank(drawFBBOs)){
					for(SportDrawFBBO sportDrawFBBO : drawFBBOs){
						map.put(mapSystemCodeId.get(sportDrawFBBO.getSportAgainstInfoId()), sportDrawFBBO);
					}
				}
				break;
			case BJDC:
				List<SportDrawBJBO> drawBJBOs = drawBJDaoMapper.findBySportAgainstInfoIdS(sportAgainInfoIds);
				if(!ObjectUtil.isBlank(drawBJBOs)){
					for(SportDrawBJBO sportDrawBJBO : drawBJBOs){
						map.put(mapSystemCodeId.get(sportDrawBJBO.getSportAgainstInfoId()), sportDrawBJBO);
					}
				}
				break;
			case SFGG:
				List<SportDrawWFBO> drawWFBOs = drawWFDaoMapper.findBySportAgainstInfoIdS(sportAgainInfoIds);
				if(!ObjectUtil.isBlank(drawWFBOs)){
					for(SportDrawWFBO sportDrawBJBO : drawWFBOs){
						map.put(mapSystemCodeId.get(sportDrawBJBO.getSportAgainstInfoId()), sportDrawBJBO);
					}
				}
				break;
			case ZC6:
			case JQ4:
			case SFC:
			case ZC_NINE:
				List<SportDrawOldInfoBO> drawOldInfoBOs = drawOldInfoDaoMapper.findBySportAgainstInfoIdS(sportAgainInfoIds);
				if(!ObjectUtil.isBlank(drawOldInfoBOs)){
					for(SportDrawOldInfoBO drawOldInfoBO : drawOldInfoBOs){
						map.put(mapSystemCodeId.get(drawOldInfoBO.getSportAgainstInfoId()), drawOldInfoBO);
					}
				}
				break;
			case GYJJC:
			case GJJC:
				List<SportAgainstInfoBO> sportAgainstInfoBOS = sportAgainstInfoDaoMapper.findSportAgainstInfoByIds(sportAgainInfoIds, lotteryCode);
				if (!ObjectUtil.isBlank(sportAgainstInfoBOS)) {
					for (SportAgainstInfoBO sportAgainstInfoBO : sportAgainstInfoBOS) {
						map.put(mapSystemCodeId.get(sportAgainstInfoBO.getId()), sportAgainstInfoBO);
					}
				}
				break;
			default:
				map = null;
				break;
		}
		return map;
	}
	
	/**
	 * 
	 * @Description: 根据足球全场比分 和 让球数得到胜平负或让球胜平负的彩果
	 * @param fullScore 全场比分
	 * @param rqNum 让球数 可以为  0 ， 为 -1 视为 主让客 ，为 1视为客让主
	 * @return integer 3|1|0， 分别表示 胜|平|负
	 * @author wuLong
	 * @date 2017年5月27日 上午10:19:13
	 */
	public Integer getSpfRqResult(String fullScore,Double rqNum){
		if(ObjectUtil.isBlank(fullScore)||rqNum==null){
			return null;
		}
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		double homeTeamGoals = Double.valueOf(fs[0]);
		double visitTeamGoals = Double.valueOf(fs[1]);
		Integer letSpf = null;
		homeTeamGoals = homeTeamGoals+rqNum;
		if(homeTeamGoals > visitTeamGoals ){
			letSpf = WON;
		}else if(Objects.equals(homeTeamGoals,visitTeamGoals)){
			letSpf = FLAT;
		}else{
			letSpf = LOSS;
		} 
		return letSpf;
	}
	/**
	 * 
	 * @Description: 根据全场比分得到  上单(1) 上双(2) 下单(3) 下双(4) 的彩果
	 * @param fullScore
	 * @return Integer
	 * @author wuLong
	 * @date 2017年8月17日 下午2:25:56
	 */
	public Integer getSdxs(String fullScore){
		//上单(1) 上双(2) 下单(3) 下双(4)
		/*上+单，上盘指主队与客队总进球数结果大于或等于3，单数指主队与客队总进球数为奇数。
		  上+双，上盘指主队与客队总进球数结果大于或等于3，双数指主队与客队总进球数为偶数。
		  下+单，下盘指主队与客队总进球数结果小于3，单数指主队与客队总进球数为奇数。
		  下+双，下盘指主队与客队总进球数结果小于3，双数指主队与客队总进球数为偶数。*/
		if(ObjectUtil.isBlank(fullScore)){
			return null;
		}
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		Integer homeTeamGoals = Integer.valueOf(fs[0]);
		Integer visitTeamGoals = Integer.valueOf(fs[1]);
		Integer sumGoals = homeTeamGoals+visitTeamGoals;
		Integer result = null;
		if(sumGoals<3){
			if(sumGoals%2==0){
				result = 4;
			}else{
				result = 3;
			}
		}else{
			if(sumGoals%2==0){
				result = 2;
			}else{
				result = 1;
			}
		}
		return result;
	}
	
	public Integer getZjqResult(String fullScore){
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		Integer homeTeamGoals = Integer.valueOf(fs[0]);
		Integer visitTeamGoals = Integer.valueOf(fs[1]);
		int sumGoals = homeTeamGoals+visitTeamGoals;
		if(sumGoals >=7){
			return 7;
		}else{
			return sumGoals;
		}
	}
	
	public String getBfResult(String fullScore){
		String[] sqt = {"10","20","21","30","31","32","40","41","42","50","51","52"};
		String[] pqt = {"00","11","22","33"};
		String[] fqt = {"01","02","12","03","13","23","04","14","24","05","15","25"};
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		String key = fs[0]+fs[1];
		Integer homeTeamGoals = Integer.valueOf(fs[0]);
		Integer visitTeamGoals = Integer.valueOf(fs[1]);
		if(Objects.equals(homeTeamGoals, visitTeamGoals)){
			List<String> list = Arrays.asList(pqt);
			if(list.contains(key)){
				return key;
			}else{
				return "99";
			}
		}else if(homeTeamGoals>visitTeamGoals){
			List<String> list = Arrays.asList(sqt);
			if(list.contains(key)){
				return key;
			}else{
				return "90";
			}
		}else{
			List<String> list = Arrays.asList(fqt);
			if(list.contains(key)){
				return key;
			}else{
				return "09";
			}
		}
	}
	
	public String getBjdcBfResult(String fullScore){
		String[] sqt = {"10","20","21","30","31","32","40","41","42"};
		String[] pqt = {"00","11","22","33"};
		String[] fqt = {"01","02","12","03","13","23","04","14","24"};
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		String key = fs[0]+fs[1];
		Integer homeTeamGoals = Integer.valueOf(fs[0]);
		Integer visitTeamGoals = Integer.valueOf(fs[1]);
		if(Objects.equals(homeTeamGoals, visitTeamGoals)){
			List<String> list = Arrays.asList(pqt);
			if(list.contains(key)){
				return key;
			}else{
				return "99";
			}
		}else if(homeTeamGoals>visitTeamGoals){
			List<String> list = Arrays.asList(sqt);
			if(list.contains(key)){
				return key;
			}else{
				return "90";
			}
		}else{
			List<String> list = Arrays.asList(fqt);
			if(list.contains(key)){
				return key;
			}else{
				return "09";
			}
		}
	}
	
	/**
	 * 
	 * @Description: 根据篮球全场比分 和 让分数得到胜平负或让分胜平负的彩果
	 * @param fullScore 全场比分
	 * @param rf 让分数 可以为  0 ， 为 -1 视为 主让客 ，为 1视为客让主
	 * @return
	 * @author wuLong
	 * @date 2017年5月27日 下午2:26:26
	 */
	public Integer getRfsf(String fullScore,double rf){
		if(ObjectUtil.isBlank(fullScore)||ObjectUtil.isBlank(rf)){
			return null;
		}
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		double visitTeamGoals = Double.valueOf(fs[0]);
		double homeTeamGoals = Double.valueOf(fs[1]);
		Integer letSpf = null;
		if(rf<0){
			visitTeamGoals = visitTeamGoals+Math.abs(rf);
		}else{
			visitTeamGoals = visitTeamGoals-Math.abs(rf);
		}
		if(visitTeamGoals > homeTeamGoals ){
			letSpf = LOSS;
		}else if(visitTeamGoals < homeTeamGoals ){
			letSpf = WON;
		}else{
			letSpf = FLAT;
		} 
		return letSpf;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.abs(11.85));
	}
	/**
	 * 
	 * @Description: 根据篮球全场比分 得到大小分的结果
	 * @param fullScore 全场比分
	 * @param dxf 大小分数
	 * @return Integer 大(99) 小(00)
	 * @author wuLong
	 * @date 2017年5月27日 下午2:46:10
	 */
	public Integer getDxfResult(String fullScore,double dxf){
		if(ObjectUtil.isBlank(fullScore)||ObjectUtil.isBlank(dxf)){
			return null;
		}
		String[] fs =  fullScore.split(SymbolConstants.COLON);
		double sumScore = Double.valueOf(fs[0])+Double.valueOf(fs[1]);
		Integer letSpf = null;
		if(sumScore > dxf ){
			letSpf = BIG_SCORE;
		}else if(sumScore < dxf){
			letSpf = SMALL_SCORE;
		} 
		return letSpf;
	}
	/**
	 * 返回开奖结果
	 * @Description: //{1705232003[0:0,0:0]={B=_11, S=_1}, 1705232001[1:0,5:0]={S=_3, R@-1=_3}, 1705232002[1:0,4:0]={R@-4=_1}}
	 * @param drawCodeMap
	 * @return
	 * @author wuLong
	 * @date 2017年5月28日 下午3:45:15
	 */
	public String generateDrawCode(Map<String, Map<String,String>> drawCodeMap){
		if(ObjectUtil.isBlank(drawCodeMap)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String key : drawCodeMap.keySet()){
			sb.append(key);
			Map<String,String> map = drawCodeMap.get(key);
			sb.append(SymbolConstants.PARENTHESES_LEFT);
			for(String k : map.keySet()){
				sb.append(k).append(map.get(k)).append(SymbolConstants.COMMA);
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(SymbolConstants.PARENTHESES_RIGHT);
			sb.append(SymbolConstants.VERTICAL_BAR);
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/** 
	 * @Description: 根据赛事编号，封装成map 
	 * @param content
	 * @param receiptContent
	 * @author wuLong
	 * @date 2017年5月26日 上午9:43:44
	 */
	public Map<String, String> pakegeContenMap(String content) {
		Map<String, String> contentMap = new HashMap<>();
		String[] cts = content.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
		for(int i =0,len = cts.length;i<len;i++){
			String d = cts[i];
			String matchNo = null;
			if(d.contains(SymbolConstants.UNDERLINE)){
				matchNo = d.substring(0, d.indexOf(SymbolConstants.UNDERLINE));
				contentMap.put(matchNo, d.substring(d.indexOf(SymbolConstants.UNDERLINE)+1));
			}else if(d.contains(SymbolConstants.MIDDLE_PARENTHESES_LEFT)){
				matchNo = d.substring(0, d.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT));
				contentMap.put(matchNo, d.substring(d.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)));
			}else{
				matchNo = d.substring(0, d.indexOf(SymbolConstants.PARENTHESES_LEFT));
				contentMap.put(matchNo, d.substring(d.indexOf(SymbolConstants.PARENTHESES_LEFT)));
			}
		}
		return contentMap;
	}
	/**
	 * 
	 * @Description: 拼装中奖描述  : 2串1_3注,3串1_4注
	 * @param winPasswayZsMap
	 * @return String
	 * @author wuLong
	 * @date 2017年5月29日 上午11:06:44
	 */
	public String getWinningDetail(Map<String, Integer> winPasswayZsMap){
		StringBuffer sb = new StringBuffer();
		for(String key : winPasswayZsMap.keySet()){
			sb.append(key).append(SymbolConstants.UNDERLINE).append(winPasswayZsMap.get(key)).append("注").append(SymbolConstants.COMMA);
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	/**
	 * 
	 * @Description: 模糊清除对应的key的缓存
	 * @param cacheKeys  格式如：key1,key2,key3
	 * @author wuLong
	 * @date 2017年9月19日 上午9:03:23
	 */
	protected void clearCacheAll(String cacheKeys){
		if(ObjectUtil.isBlank(cacheKeys)){
			return;
		}
		String keys[] = cacheKeys.split(SymbolConstants.COMMA);
		for(String k : keys){
			redisUtil.delAllString(k);
		}
	}
	/**
	 * 
	 * @Description: 清除对应的key的缓存
	 * @param cacheKeys  格式如：key1,key2,key3
	 * @author wuLong
	 * @date 2017年9月19日 上午9:03:23
	 */
	protected void clearCacheObj(String cacheKeys) {
		if(ObjectUtil.isBlank(cacheKeys)){
			return;
		}
		String keys[] = cacheKeys.split(SymbolConstants.COMMA);
		for(String k : keys){
			redisUtil.delString(k);
		}
	}
}
