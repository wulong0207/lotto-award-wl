package com.hhly.award.persistence.dao;

import com.hhly.award.persistence.po.TicketAlarmConfigPO;
import com.hhly.award.persistence.po.TicketAlarmInfoPO;

public interface TicketAlarmInfoDaoMapper {
    /**
     * 添加报警信息
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月9日 下午3:41:37
     * @param po
     * @return
     */
    int addAlarmInfo(TicketAlarmInfoPO po);
    /**
     * 获取报警配置
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月9日 下午3:41:48
     * @param alarmConfigId
     * @return
     */
	TicketAlarmConfigPO getAlarmConfig(int alarmConfigId);
}