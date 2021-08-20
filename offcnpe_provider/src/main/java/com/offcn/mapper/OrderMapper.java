package com.offcn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.offcn.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzw
 * @since 2021-08-13
 */
public interface OrderMapper extends BaseMapper<Order> {
    Integer findOrderCountByDate(String today);

    Integer findOrderCountThisWeek(@RequestParam("thisWeekMonday") String thisWeekMonday,@Param("thisWeekSunday") String thisWeekSunday);

    Integer findOrderCountThisMonth(@RequestParam("firstDay4ThisMonth") String firstDay4ThisMonth,@Param("lastDay4ThisMonth") String thisWeekSunday);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal(@Param("thisWeekMonday") String thisWeekMonday, @Param("thisWeekSunday") String thisWeekSunday);


}
