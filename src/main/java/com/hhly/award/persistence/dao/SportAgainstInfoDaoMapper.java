package com.hhly.award.persistence.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.award.bo.SportAgainstInfoBO;


public interface SportAgainstInfoDaoMapper {

    /**
     * 
     * @Description: 根据list对阵Id,和彩种编号（大彩种）查询对阵信息
     * @param systemCodes
     * @param lotteryCode
     * @return
     * @author wuLong
     * @date 2017年3月24日 上午11:09:17
     */
    List<SportAgainstInfoBO> findSportAgainstInfoBySystemCodeS (@Param("systemCodes")List<String> systemCodes,@Param("lotteryCode")Integer lotteryCode);
    /**
	 * 
	 * @Description: 查询对阵信息
	 * @param systemCodes 对阵编号
	 * @return List<SportAgainstInfoBO>
	 * @author wuLong
	 * @date 2017年5月25日 上午10:03:31
	 */
    public List<SportAgainstInfoBO> findBySystemCodes(@Param("systemCodes")List<String> systemCodes,@Param("lotteryCode")Integer lotteryCode);
    /**
     * 修改赛事状态为已开奖
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param systemCodes
     * @param lotteryCode
     * @author wuLong
     * @date 2017年7月3日 下午4:55:46
     */
    public void updateMatchState(@Param("systemCodes")List<String> systemCodes, @Param("lotteryCode")Integer lotteryCode);

	/**
	 * 根据id 彩种获取对阵
	 *
	 * @param ids
	 * @param lotteryCode
	 * @return
	 */
	List<SportAgainstInfoBO> findSportAgainstInfoByIds(@Param("list") List<Long> ids, @Param("lotteryCode") Integer lotteryCode);
}