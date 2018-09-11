package com.hhly.award.service.award.lottery.high.ssc.calculate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.base.exception.ServiceRuntimeException;
import com.hhly.award.service.award.lottery.high.WinInfo;

/**
 * @desc 三星组三
 * @author jiangwei
 * @date 2017年7月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class ThreeGroupThreeCalculate implements ICalculate {
	
	private String prize = "三星组三";
	@Override
	public WinInfo simple(String content, String[] drawCode) {
		String codeEqual = "";
		String simple = "";
		String drawCode3 = drawCode[2];
		String drawCode4 = drawCode[3];
		String drawCode5 = drawCode[4];
		if(drawCode3.equals(drawCode4) && drawCode4.equals(drawCode5)){
			return new WinInfo(0, prize);
		}
		if(drawCode3.equals(drawCode4)){
			codeEqual = drawCode3;
			simple = drawCode5;
		}else if(drawCode3.equals(drawCode5)){
			codeEqual = drawCode3;
			simple = drawCode4;
		}else if(drawCode4.equals(drawCode5)){
			codeEqual = drawCode4;
			simple = drawCode3;
		}else{
			return new WinInfo(0, prize);
		}
		String[] codes = StringUtils.tokenizeToStringArray(content, ",;");
		int count = 0;
		for (int i = 0; i < codes.length; i+=3) {
			String code1 = codes[i];
			String code2 = codes[i+1];
			String code3 = codes[i+2];
			if(code1.equals(code2)){
				if(code1.equals(codeEqual) && code3.equals(simple)){
					count++;
				}
			}else if(code1.equals(code3)){
				if(code1.equals(codeEqual) && code2.equals(simple)){
					count++;
				}
			}else if(code2.equals(code3)){
				if(code2.equals(codeEqual) && code1.equals(simple)){
					count++;
				}
			}	
		}
		return new WinInfo(count, prize);
	}

	@Override
	public WinInfo complex(String content, String[] drawCode) {
		if(!check(drawCode)){
			return new WinInfo(0, prize);
		}
		String[] codes = content.split(",");
		int num = 0;
		for (int i = 2; i < drawCode.length; i++) {
			for (int j = 0; j < codes.length; j++) {
				if (codes[j].equals(drawCode[i])) {
					num++;
					break;
				}
			}
		}
		int count = num == 3 ? 1 : 0;
		return new WinInfo(count, prize);
	}
	/**
	 * 检查是否是组三
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月12日 下午3:24:53
	 * @param drawCode
	 * @return
	 */
	private  boolean check(String[] drawCode){
		String drawCode3 = drawCode[2];
		String drawCode4 = drawCode[3];
		String drawCode5 = drawCode[4];
		if(drawCode3.equals(drawCode4) && drawCode4.equals(drawCode5)){
			return  false;
		}
		if(drawCode3.equals(drawCode4) || drawCode3.equals(drawCode5) || drawCode4.equals(drawCode5)){
		   return true;	
		}
		return false;
	}
	@Override
	public WinInfo sum(String content, String twoSum, String threeSum,String [] drawCode) {
		throw new ServiceRuntimeException("票玩法类型错误，不能计算开奖");
	}

}
