package com.hhly.award.service.award.lottery.high.kl10.calculate;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhly.award.service.award.lottery.high.WinInfo;
/** 
 * @desc 任二连组
 * @author jiangwei
 * @date 2017年8月4日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class TwoGroupTenCalculate implements ICalculate {

	@Override
	public WinInfo simple(String content, List<String> drawCode) {
		String draw = StringUtils.collectionToDelimitedString(drawCode, ",");
		String[] codes = content.split("[,;]");
		int num = 0;
		for (int i = 0; i < codes.length; i+=2) {
			String code1 = codes[i];
			String code2 = codes[i+1];
			String first = code1 + "," + code2;
			String second = code2 + "," + code1;
			if(draw.indexOf(first) != -1 || draw.indexOf(second) != -1 ){
				num ++;
			}
		}
		return new  WinInfo(num, "选二连组");
	}

	@Override
	public WinInfo complex(String content, List<String> drawCode) {
		String[] codes = content.split(",");
		boolean [] in = new boolean[drawCode.size()];
		for (String code : codes) {
		    for (int i = 0; i < drawCode.size(); i++) {
				if(code.equals(drawCode.get(i))){
					in[i] = true;
					break;
				}
			}	
		}
		int num = 0;
		int count = 0;
		for (boolean b : in) {
			if(b){
				count++;
				if(count == 2){
					num += 1; 
					count = 1;
				}
			}else{
				count = 0;
			}
		}
		return new WinInfo(num, "选二连组");
	}
}

