package com.hhly.award.service.award.lottery.high.poker.calculate;

import java.util.List;

import org.springframework.util.StringUtils;

import com.hhly.award.service.award.lottery.high.WinInfo;
import com.hhly.award.util.calcutils.Combine;

/**
 * @desc 默认实现
 * @author jiangwei
 * @date 2017年7月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractCalculate implements ICalculate {
	
	private static Combine combine = new Combine();
 
	@Override
	public WinInfo simple(List<String> num, String pairs, String flower, boolean sequence, String content) {
		String[] codes = StringUtils.tokenizeToStringArray(content, ",;");
		int w = 0, n = 0;
		int count = getCount();
		boolean isPairs = false ,isPanther = false;
		for (int i = 1; i <= codes.length; i++) {
			String code = codes[i - 1];
			if (num.contains(code)) {
				n++;
				if(code.equals(pairs)){
					isPairs = true;
				}else if (num.size() == 1){
					isPanther = true;
				}
			}
			if (i % count == 0) {
				if(count < 3){
					if(count == n ){
						w ++;
					}
				}else{
					//任选3 以上，对子豹子需要特殊处理
					if(isPairs){
						if(n == 2){
							w++;
						}
					}else if(isPanther){
						if(n == 1){
							w++;
						}
					}else{
						if(n == 3){
							w++;
						}
					}
				}
				n = 0;
				isPairs = isPanther = false;
			}
		}
		return new WinInfo(w, getPrize());
	}

	@Override
	public WinInfo complex(List<String> num, String pairs, String flower, boolean sequence, String content) {
		String[] codes = content.split(",");
		List<String[]> list = combine.mn4Arr(codes, getCount());
		String sb = joinSimple("", list);
		return simple(num, pairs, flower, sequence, sb);
	}

	@Override
	public WinInfo gallDrag(List<String> num, String pairs, String flower, boolean sequence, String content) {
		String[] str = content.split("#");
		String[] gall = str[0].split(",");
		String[] drag = str[1].split(",");
		int dragCount = getCount() - gall.length;
		List<String[]> list = combine.mn4Arr(drag, dragCount);
		String sb = joinSimple(str[0], list);
		return simple(num, pairs, flower, sequence, sb);
	}
    /**
     * 转换为单式计算
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年8月30日 下午2:37:32
     * @param head
     * @param list
     * @return
     */
	private String joinSimple(String head, List<String[]> list) {
		StringBuffer sb = new StringBuffer();
		for (String[] strings : list) {
			if(sb.length() > 0){
				sb.append(";");
			}
			sb.append(head);
			for (String string : strings) {
				sb.append(",");
				sb.append(string);
			}
		}
		return sb.toString();
	}
	/**
	 * 中奖类型
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年7月14日 下午2:40:32
	 * @return
	 */
	public  abstract String getPrize();
	/**
	 * 选号方式
	 */
	public abstract int getCount();
}
