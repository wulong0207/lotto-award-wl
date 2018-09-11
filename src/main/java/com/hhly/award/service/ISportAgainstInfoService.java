package com.hhly.award.service;

import java.util.List;

import com.hhly.award.bo.SportAgainstInfoBO;


public interface ISportAgainstInfoService {
	/**
	 * 
	 * @Description: 查询对阵信息
	 * @param systemCodes 对阵编号
	 * @param lotteryCode 彩种id
	 * @return List
	 * @author wuLong
	 * @date 2017年5月17日 上午9:44:53
	 */
	public List<SportAgainstInfoBO> findBySystemCodeSLotteryCode(List<String> systemCodes,Integer lotteryCode);
	/**
	 * 
	 * @Description: 查询对阵信息
	 * @param systemCodes 对阵编号
	 * @return List<SportAgainstInfoBO>
	 * @author wuLong
	 * @date 2017年5月25日 上午10:03:31
	 */
	public List<SportAgainstInfoBO> findBySystemCodes(List<String> systemCodes,Integer lotteryCode);
	/**
	 * @Description: 修改赛事状态为已开奖
	 * @param systemCodes
	 * @param lotteryCode
	 * @author wuLong
	 * @date 2017年7月3日 下午4:54:53
	 */
	public void updateMatchState(List<String> systemCodes,Integer lotteryCode);
}
