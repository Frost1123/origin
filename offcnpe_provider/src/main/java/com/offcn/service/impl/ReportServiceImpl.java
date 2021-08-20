package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.offcn.constants.DateUtils;
import com.offcn.mapper.MemberMapper;
import com.offcn.mapper.OrderMapper;
import com.offcn.pojo.Member;
import com.offcn.service.ReportService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Component
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<Integer> getMemberReport(List<String> monthLists) {
        LinkedList<Integer> memberList = new LinkedList<>();
        for (String month : monthLists) {
            try {
                String startDate = month + "-1";
                Date date = DateUtils.getLastDayOfMonth(DateUtils.parseString2Date(startDate));
                String endDate = DateUtils.parseDate2String(date,"yyyy-MM-dd");
                QueryWrapper<Member> wrapper = new QueryWrapper<>();
                wrapper.lt("regTime",endDate);
                Integer integer = memberMapper.selectCount(wrapper);
                memberList.add(integer);
            } catch (Exception e) {
                e.printStackTrace();
                return memberList;
            }
        }
        return memberList;
    }

    @Override
    public Map<String, Object> getBusinessReportData() {

        try {

            //获得当前日期
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            //获得本周一的日期
            String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //获得本周日的日期
            String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //获得本月第一天的日期
            String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //获取本月最后一天的日期
            String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
            //本日新增会员数
            Integer todayNewMember = memberMapper.findMemberCountByDate(today);

            //总会员数
            Integer totalMember = memberMapper.findMemberTotalCount();
            //本周新增会员数
            Integer thisWeekNewMember = memberMapper.findMemberCountAfterDate(thisWeekMonday);
            //本月新增会员数
            Integer thisMonthNewMember = memberMapper.findMemberCountAfterDate(firstDay4ThisMonth);

            //今日预约数
            Integer todayOrderNumber = orderMapper.findOrderCountByDate(today);
            //本周预约数
            Integer thisWeekOrderNumber = orderMapper.findOrderCountThisWeek(thisWeekMonday,thisWeekSunday);
            //本月预约数
            Integer thisMonthOrderNumber = orderMapper.findOrderCountThisMonth(firstDay4ThisMonth,lastDay4ThisMonth);
            //今日到诊数
            Integer todayVisitsNumber = orderMapper.findVisitsCountByDate(today);
            //本周到诊数
            Integer thisWeekVisitsNumber = orderMapper.findVisitsCountAfterDate(thisWeekMonday);
            //本月到诊数
            Integer thisMonthVisitsNumber = orderMapper.findVisitsCountAfterDate(firstDay4ThisMonth);

            //本周热门套餐查询
            List<Map> hotSetmeal = orderMapper.findHotSetmeal(thisWeekMonday,thisWeekSunday);

            Map<String,Object> result = new HashMap<>();
            result.put("reportDate",today);
            result.put("todayNewMember",todayNewMember);
            result.put("totalMember",totalMember);
            result.put("thisWeekNewMember",thisWeekNewMember);
            result.put("thisMonthNewMember",thisMonthNewMember);
            result.put("todayOrderNumber",todayOrderNumber);
            result.put("thisWeekOrderNumber",thisWeekOrderNumber);
            result.put("thisMonthOrderNumber",thisMonthOrderNumber);
            result.put("todayVisitsNumber",todayVisitsNumber);
            result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            result.put("hotSetmeal",hotSetmeal);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

