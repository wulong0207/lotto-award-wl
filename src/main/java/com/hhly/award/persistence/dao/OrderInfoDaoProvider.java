package com.hhly.award.persistence.dao;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import com.hhly.award.base.common.SymbolConstants;

public class OrderInfoDaoProvider {
	
	public String updatePreBonusByOrderDetailForMap(Map<String,String> orderDetailPreBonusMap){
		StringBuffer sb = new StringBuffer();
		for(String key : orderDetailPreBonusMap.keySet()){
			String ky[] = key.split(SymbolConstants.COMMA);
			int detailKey = Integer.valueOf(ky[0]);
			String issport = ky[1];
			String a[] = orderDetailPreBonusMap.get(key).split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
			BigDecimal preBonus = BigDecimal.valueOf(Double.valueOf(a[0]));
			BigDecimal aftBonus = BigDecimal.valueOf(Double.valueOf(a[1]));
			String windetail = a[2].replaceAll("注", "");
			Map<String, String> ticketWinDetail = new LinkedHashMap<>();
			String wds[] = windetail.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AT);
			for(String wa : wds){
				String wdx[] = wa.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
				int mutliNum = Integer.valueOf(wdx[1]);
				String wd[] = wdx[0].split(SymbolConstants.COMMA);
				for(String w : wd){
					String d[] = w.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UNDERLINE);
					String k = d[0];int v = Integer.valueOf(d[1]);
					if(ticketWinDetail.containsKey(k)){
						String tk[] = ticketWinDetail.get(k).split(SymbolConstants.COMMA);
						if(issport.equals("0")){
							String dx = (v + Integer.valueOf(tk[0]))+SymbolConstants.COMMA+(mutliNum);
							ticketWinDetail.put(k, dx);
						}else{
							String dx = (v*mutliNum + Integer.valueOf(tk[0]))+SymbolConstants.COMMA+(mutliNum+Integer.valueOf(tk[1]));
							ticketWinDetail.put(k, dx);
						}
					}else{
						if(issport.equals("0")){
							String dx = v+SymbolConstants.COMMA+mutliNum;
							ticketWinDetail.put(k, dx);
						}else{
							String dx = v*mutliNum+SymbolConstants.COMMA+mutliNum;
							ticketWinDetail.put(k, dx);
						}
						
					}
				}
			}
			StringBuilder wdided = new StringBuilder();
			for(String k : ticketWinDetail.keySet()){
				String tk[] = ticketWinDetail.get(k).split(SymbolConstants.COMMA);
				if(issport.equals("0")){
					wdided.append(k).append(SymbolConstants.UNDERLINE).append(Integer.valueOf(tk[0])).append(SymbolConstants.COMMA);
				}else{
					wdided.append(k).append(SymbolConstants.UNDERLINE).append(Integer.valueOf(tk[0])/Integer.valueOf(tk[1])).append(SymbolConstants.COMMA);
				}
			}
			sb.append("UPDATE ORDER_DETAIL SET PRE_BONUS = ").append(preBonus).append(",AFT_BONUS =").append(aftBonus).append(",WINNING_DETAIL ='").append(wdided.deleteCharAt(wdided.length()-1).toString()).append("'").append(" WHERE ID = ").append(detailKey).append(";");
		}
		String sql = sb.toString();
		return sql;
	}
}
