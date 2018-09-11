package com.hhly.award.persistence.dao;


import com.hhly.award.persistence.po.OrderGroupContentPO;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderGroupContentDaoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderGroupContentPO record);

    int insertSelective(OrderGroupContentPO record);

    List<OrderGroupContentPO> selectByOrderCode(@Param("orderCode") String orderCode);

    int updateByPrimaryKeySelective(OrderGroupContentPO record);

    int updatePer(List<OrderGroupContentPO> record);

    double selectBuyAmount(@Param("orderCode") String orderCode);

    int updateAddBonus(List<Map> list);
}