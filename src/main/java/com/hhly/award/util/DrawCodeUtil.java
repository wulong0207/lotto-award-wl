package com.hhly.award.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.hhly.award.base.common.LotteryEnum.Lottery;
import com.hhly.award.base.common.SymbolConstants;

/**
 * 
 * @author jiangwei
 * @Version 1.0
 * @CreatDate 2016-12-6 上午10:45:44
 * @Desc 彩种开奖号码验证
 */
public class DrawCodeUtil {
	/**
	 * 验证开奖号码是否正确
	 * @param code 开奖号码
	 * @param lotteryCode 彩种编号
	 * @return
	 */
	public static boolean isCorrect(String code,Integer lotteryCode){
		Lottery lot = Lottery.getLottery(lotteryCode);
		switch (lot) {
		case SSQ:
			return ssq(code);
		case D11X5:
		case HB11X5:
		case JS11X5:
		case LN11X5:
		case SD11X5:
		case JX11X5:
		case HL11X5:
		case TJ11X5:
		case SX11X5:
		case JL11X5:
		case CQ11X5:
		case SC11X5:	
		case AH11X5:
		case FJ11X5:
		case HLJ11X5:
		case SHX11X5:
		case GS11X5:
		case YN11X5:
		case ZJ11X5:
		case BJ11X5:
		case SH11X5:
		case GX11X5:
		case GZ11X5:
		case XJ11X5:
		case HEB11X5:
		case NX11X5:
		case QH11X5:
		case NMG11X5:
		case XZ11X5:
			return elevenFive(code);
		case QLC:
			return qlc(code);
		case DLT:
			return dlt(code);
		case PL5:
			return pl5(code);
		case PL3:
		case F3D:
			return pl3(code);
		case LHC:
			break;
		case QXC:
			return qxc(code);
		case CQSSC:
		case JXSSC:
		case XJSSC:
		case YNSSC:
		case TJSSC:
			return ssc(code);
		case PK10:
			break;
		case CQKL10:
		case DKL10:
		case GXKL10:
		case HLJKL10:
		case TJKL10:
		case SHXKL10:
		case YNKL10:
		case SZKL10:
		case SXKL10:
			return cqkl10(code);
		case HNKL10:
			break;
		case BJKL8:
			break;
		case GXK3:
		case AHK3:
		case JLK3:
		case JSK3:
		case JXK3:
		case QHK3:
		case HBK3:
		case SHK3:
		case BJK3:
		case FJK3:
		case HNK3:
		case GSK3:
		case HEBK3:
		case GZK3:
		case XZGK3:
			return k3(code);
		case SHSSL:
			break;
		case ZC6:
			return zc6(code);
		case JQ4:
			return jq4(code);
		case SFC:
		case ZC_NINE:
			return sfc(code);
		case FB:
		case BB:
			return true;
		case SDPOKER:
			return sdpoker(code);
		default:
			break;
		}
		return false;
	}
	/**
	 * 山东快乐扑克
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月7日 下午3:44:13
	 * @param code
	 * @return
	 */
	private  static boolean sdpoker(String code){
		String puke = "2,3,4,5,6,7,8,9,10,J,Q,K,A";
		String []  all = code.split("\\|");
		if(arrayLen(all,3)){
			if(Objects.equals(all[0],all[1])
					|| Objects.equals(all[0],all[2])
					|| Objects.equals(all[1],all[2])){
				return false;
			}
			for (String string : all) {
				String str[] = string.split("_");
				if(str.length != 2){
					return false;
				}
				int num = Integer.parseInt(str[0]);
				if(num < 1 || num > 4){
					return false;
				}
				if(puke.indexOf(str[1])==-1){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 重庆块乐10分
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月7日 上午11:09:57
	 * @param code
	 * @return
	 */
	public  static boolean cqkl10(String code){
		String [] all = code.split(SymbolConstants.COMMA); 
		   if(isNotRep(all)
				   &&rangeNum(all,1,20)
				   &&codeLen(all,2)
				   &&arrayLen(all,8)){
			   return true;
		   }
		   return false;
	}
	
	private static boolean k3(String code) {
		String [] all = code.split(SymbolConstants.COMMA); 
		   if(rangeNum(all,1,6)
				   &&codeLen(all,1)
				   &&arrayLen(all,3)){
			   return true;
		   }
		   return false;
	}
	/**
	 * 时时彩
	 * @param code
	 * @return
	 */
	private static boolean ssc(String code) {
		return pl5(code);
	}
	/**
	 * 排列5
	 * @param code
	 * @return
	 */
	public static boolean pl5(String code) {
		   String []  all = code.split("\\|");
		   if(rangeNum(all,0,9)
				   &&codeLen(all,1)
				   &&arrayLen(all,5)){
			   return true;
		   }
		   return false;
	}
	/**
	 *排列3 
	 * @param code
	 * @return
	 */
	public static boolean pl3(String code) {
		String []  all = code.split("\\|");
		   if(rangeNum(all,0,9)
				   &&codeLen(all,1)
				   &&arrayLen(all,3)){
			   return true;
		   }
		   return false;
    }
	/**
	 * 七星彩
	 * @param code
	 * @return
	 */
	public static boolean qxc(String code) {
		   String [] all = code.split("\\|");
		   if(rangeNum(all,0,9)
				   &&codeLen(all,1)
				   &&arrayLen(all,7)){
			   return true;
		   }
		   return false;
	}
	/**
	 * 七乐彩
	 * @param code
	 * @return
	 */
	public static boolean qlc(String code) {
		String [] all = code.split(SymbolConstants.COMMA);
		   if(isNotRep(all)
				   &&rangeNum(all,1,30)
				   &&codeLen(all,2)
				   &&arrayLen(all,8)){
			   return true;
		   }
		   return false;
	}
	/**
	 * 大乐透
	 * @param code
	 * @return
	 */
	public static boolean dlt(String code) {
		String[] all = code.split("\\|");
		String[] left  = all[0].split(SymbolConstants.COMMA);
		String[] right  = all[1].split(SymbolConstants.COMMA);
		   if(isNotRep(left)
				   &&rangeNum(left,1,35)
				   &&codeLen(left,2)
				   &&arrayLen(left,5)
				   
				   &&isNotRep(right)
				   &&rangeNum(right,1,12)
				   &&codeLen(right,2)
				   &&arrayLen(right,2)){
			   return true;
		   }else{
			   return false;
		   }
	}
	/**
	 * 6场
	 * @param code
	 * @return
	 */
	public static boolean zc6(String code) {
		String [] all = code.split("\\|");
		   if(include(all,new String[]{"0","1","3","*","_"})
				   &&arrayLen(all,12)){
			  return true;
		   }
		   return false;
	}
	/**
	 * 4场
	 * @param code
	 * @return
	 */
	public static boolean jq4(String code) {
		String [] all = code.split("\\|");
		   if(include(all,new String[]{"0", "1", "2", "3", "*", "_"})
				   &&arrayLen(all,8)){
			  return true;
		   }
		   return false;
	}
	/**
	 * 十四场
	 * @param code
	 * @return
	 */
	public static boolean sfc(String code) {
		  String [] all = code.split("\\|");
		  if(include(all,new String[]{"0","1","3","*","_"})
				   &&arrayLen(all,14)){
			  return true;
		  }
		  return false;
	}

	/**
	 * 双色球
	 * @param code
	 * @return
	 */
	public static boolean elevenFive(String code){
		   String [] all = code.split(SymbolConstants.COMMA); 
		   if(isNotRep(all)
				   &&rangeNum(all,1,11)
				   &&codeLen(all,2)
				   &&arrayLen(all,5)){
			   return true;
		   }
		   return false;
	}
	/**
	 * 验证双色球
	 * @param code
	 * @return
	 */
	public static boolean ssq(String code){
		   String [] all = code.split("\\|");
		   String [] left  = all[0].split(SymbolConstants.COMMA);
		   String [] right = {all[1]};
		   if(isNotRep(left)
				   &&rangeNum(left,1,33)
				   &&codeLen(left,2)
				   &&codeLen(right,2)
				   &&rangeNum(right,1,16)
				   &&arrayLen(left,6)){
			   return true;
		   }else{
			   return false;
		 }
	}
	/**
	 * 判断开奖号码长度
	 * @param code
	 * @param len
	 * @return
	 */
	public static boolean arrayLen(String code[],int len){
		if(code.length != len){
			return false;
		}
		return true;
	}
	/**
	 * 开奖号码是否包含在指定的字符串
	 * @param code
	 * @param incl
	 * @return
	 */
	public static boolean include(String [] code,String [] incl){
		for(int i=0;i<code.length;i++){
			boolean isIn = false;
			for(int j=0;j<incl.length;j++){
				if(code[i].equals(incl[j])){
					isIn = true;
					break;
				}
			}
			if(!isIn){
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断号码长度
	 * @param code
	 * @param len
	 * @return
	 */
	public static boolean codeLen(String [] code,int len){
		for(int i=0;i<code.length;i++){
			if(code[i].length() != len){
				return false;
			}
		}
		return true;
	}
    /**
     * 是否在制定范围
     * @param code
     * @param min
     * @param max
     * @return
     */
	public static boolean rangeNum(String [] code,int min,int max){
		for(int i=0;i<code.length;i++){
			if(!code[i].matches("[0-9]+")){
				return false;
			}
			int num = Integer.parseInt(code[i]);
			if(num > max || num < min){
				return false;
			}
		}
		return true;
	}
	/**
	 * 是否不重复
	 * @param code
	 * @return
	 */
	public static boolean isNotRep(String [] code){
		for(int i=0;i<code.length;i++){
			for(int j = i+1;j<code.length;j++){
				if(code[i].equals(code[j]) ){
					return false;
				}
			}
		}
		return true;
	}
	public static boolean isCorrectTest(String code,String lotteryCode){
		String method = lotteryCode.toLowerCase();
		//String spelStr = "T(com.hhly.persistence.util.DrawCodeUtil)."+method+"('"+code+"')";
		//Expression exp=expParser.parseExpression(spelStr);
		try {
			Object o =ClassUtils.getMethod(DrawCodeUtil.class, method,String.class).invoke(new DrawCodeUtil(), code);
			return (boolean) o;
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		//return exp.getValue(Boolean.class);
		return false;
	}
	/**
	 * 判断子是否包含在数字中
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月26日 下午5:28:49
	 * @param str 数组
	 * @param value 值
	 * @return 包含返回1 ，不包含返回0
	 */
	public static  int include(String value,String[] str){
		for (String select : str) {
			if (select.equals(value)) {
				return 1;
			}
		}
		return 0;
	}
	/**
	 * 获取一个数组在另外一个数组中存在个数
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月28日 上午9:45:33
	 * @param value 被检查的数组
	 * @param all 检查值
	 * @return 包含的个数
	 */
	public static  int contain(String[] values ,String[] all){
		int num = 0;
		for (String value : values) {
			num +=include(value,all);
		}
		return num;
	}
	/**
	 * 直选单式多注计算中奖注数
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 下午12:13:48
	 * @param codes 选择号码
	 * @param drawCode 开奖号码
	 * @param codeNum 单式号码个数
	 * @return
	 */
	public static int simple(String[] codes, String[] drawCode,int codeNum) {
		int num = 0;
		int inNum = 0;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			int remain = i % codeNum;
			remain = remain == 0 ? codeNum : remain;
			// 直选位置要一致
			if (Objects.equals(drawCode[remain - 1], code)) {
				inNum++;
			}
			if (remain == codeNum) {
				if (inNum == codeNum) {
					num++;
				}
				inNum = 0;
			}
		}
		return num;
	}
	/**
	 * 直选复试计算中奖注数
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年6月12日 下午12:22:48
	 * @param codes
	 * @param drawCode
	 * @return
	 */
	public static int directRepeated(String content,String first,String senond,String [] drawCode){
		String [] codes = StringUtils.tokenizeToStringArray(content, first);
		int length = codes.length - 1;
		for (int i = 0; i <= length; i++) {
			String[] strs = StringUtils.tokenizeToStringArray(codes[i],senond);
			int inNum = include(drawCode[i], strs);
			if (inNum == 0) {
				break;
			}
			if (i == length) {
				return 1;
			}
		}
		return 0;
	}
}
