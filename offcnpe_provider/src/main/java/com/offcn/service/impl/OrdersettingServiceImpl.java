package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.offcn.constants.DateUtils;
import com.offcn.mapper.OrdersettingMapper;
import com.offcn.pojo.Ordersetting;
import com.offcn.service.OrdersettingService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {
    @Resource
    OrdersettingMapper ordersettingMapper;
    @Override
    public void uploadTemplate(List<String[]> strings) {
        if (strings != null && strings.size() > 0) {
            Ordersetting ordersetting = new Ordersetting();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            for (String[] info : strings) {
                LocalDate date = LocalDate.parse(info[0],dateTimeFormatter);
                ordersetting.setOrderdate(date);
                ordersetting.setNumber(Integer.parseInt(info[1]));
                LambdaQueryWrapper<Ordersetting> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Ordersetting::getOrderdate,ordersetting.getOrderdate());
                //根据日期做查询
                Ordersetting ordersetting1 = ordersettingMapper.selectOne(lambdaQueryWrapper);
                if (ordersetting1 != null) {
                    //该日期设置过
                    ordersetting1.setNumber(Integer.parseInt(info[1]));
                    ordersettingMapper.updateById(ordersetting1);
                } else {
                    //该日期未设置过
                    ordersetting.setReservations(0);
                    ordersettingMapper.insert(ordersetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrdersettingByDate(String date) {
        String startDate=date+"-1";
        Date date1;
        String endDate = null;
        try {
             date1 = DateUtils.parseString2Date(startDate, "yyyy-MM-dd");
             endDate = DateUtils.parseDate2String(DateUtils.getLastDayOfMonth(date1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryWrapper<Ordersetting> queryWrapper=new QueryWrapper<>();
        queryWrapper.between("orderDate",startDate,endDate);
        List<Ordersetting> list = ordersettingMapper.selectList(queryWrapper);
        List<Map>data= new ArrayList<>();
        list.forEach(i->{
            Map map=new HashMap();
            map.put("date",i.getOrderdate().getDayOfMonth());
            map.put("number",i.getNumber());
            map.put("reservations",i.getReservations());
            data.add(map);
        });
        return data;
    }

    @Override
    public void editNumberByDate(Integer number,String orderDate) {
        QueryWrapper<Ordersetting> wrapper = new QueryWrapper<>();
        wrapper.eq("orderDate",orderDate);
        Ordersetting ordersetting = ordersettingMapper.selectOne(wrapper);
        if(ordersetting != null) {
            //更新
            ordersetting.setNumber(number);
            ordersettingMapper.update(ordersetting, wrapper);
        }else {
            //添加
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(orderDate, dateTimeFormatter);
            Ordersetting ordersetting1 = new Ordersetting();
            ordersetting1.setOrderdate(localDate);
            ordersetting1.setNumber(number);
            ordersetting1.setReservations(0);
            ordersettingMapper.insert(ordersetting1);
        }
    }
}
