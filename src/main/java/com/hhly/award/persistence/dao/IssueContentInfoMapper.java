package com.hhly.award.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.award.bo.IssueContentInfoBO;

public interface IssueContentInfoMapper {
	/**
	 * 
	 * @Description: 查询推荐内容表
	 * @param lotteryCode 彩种ID
	 * @param lotteryChild 子彩种ID
	 * @param orderStatus 开奖状态
	 * @param cancelSystemCode 重置开奖的赛事编号
	 * @return List<IssueContentInfoBO>
	 * @author wuLong
	 * @date 2018年1月6日 下午4:01:00
	 */
	public List<IssueContentInfoBO> getIssueContentInfos(@Param("lotteryCode")Integer lotteryCode,@Param("lotteryChild")Integer lotteryChild,
			@Param("orderStatus")Integer orderStatus,@Param("cancelSystemCode")String cancelSystemCode);
	
	/**
	 * @Description: 更新推荐内容表
	 * @param id 内容表id
	 * @param orderStatus 开奖状态
	 * @param amount 开奖奖金
	 * @author wuLong
	 * @date 2018年1月6日 下午4:06:53
	 */
	public void updateIssueContentInfoBO(@Param("id")Integer id,@Param("orderStatus")Integer orderStatus,@Param("amount")BigDecimal amount);
	
	
	
}
