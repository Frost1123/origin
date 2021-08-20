package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.offcn.constants.DateUtils;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.Result;
import com.offcn.mapper.MemberMapper;
import com.offcn.mapper.OrderMapper;
import com.offcn.mapper.OrdersettingMapper;
import com.offcn.mapper.SetmealMapper;
import com.offcn.pojo.Member;
import com.offcn.pojo.Order;
import com.offcn.pojo.Ordersetting;
import com.offcn.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Resource
    OrdersettingMapper ordersettingMapper;
    @Resource
    MemberMapper memberMapper;
    @Resource
    OrderMapper orderMapper;
    @Resource
    SetmealMapper setmealMapper;

    @Override
    public Result submitOrder(Map map) {
        Order order;
        Member member;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            //1.判断所选日期是否已有预约设置
            QueryWrapper<Ordersetting> wrapper = new QueryWrapper<>();
            wrapper.eq("orderDate", DateUtils.parseString2Date((String)map.get("orderDate")));
            Ordersetting ordersetting = ordersettingMapper.selectOne(wrapper);
            if (ordersetting == null) {
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //2.检查当前日期的预约人数是否已满
            if (ordersetting.getNumber() <= ordersetting.getReservations()) {
                return new Result(false,MessageConstant.ORDER_FULL);
            }
            //3.检查此人是否重复预约,即所选日期、身份证号、通过member表查询到的memeber_id 这条数据在order表中已存在
            QueryWrapper<Member> wrapper1 = new QueryWrapper<>();
            //身份证是唯一的，此处用身份证来查身份
            wrapper1.eq("idCard",map.get("idCard"));
            member = memberMapper.selectOne(wrapper1);
            QueryWrapper<Order> wrapper2;
            if (member != null) {
                wrapper2 = new QueryWrapper<>();
                wrapper2.eq("member_id",member.getId());
                wrapper2.eq("orderDate",map.get("orderDate"));
                wrapper2.eq("setmeal_id",map.get("setmealId"));
                order = orderMapper.selectOne(wrapper2);
                if (order != null) {
                    //已注册会员，并重复预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }else {
                    //已注册会员，未重复预约，则先更新电话号码(便于联系)，再进行预约
                    member.setPhonenumber((String) map.get("telephone"));
                    memberMapper.update(member,wrapper1);
                    order = new Order();
                    order.setMemberId(member.getId());
                    LocalDate orderdate = LocalDate.parse((String)map.get("orderDate"),dateTimeFormatter);
                    order.setOrderdate(orderdate);
                    order.setOrdertype("微信预约");
                    order.setOrderstatus("未就诊状态");
                    order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
                    orderMapper.insert(order);
                    //预约成功后，预约设置的已预约人数需加1
                    Integer reservation = ordersetting.getReservations()+1;
                    ordersetting.setReservations(reservation);
                    ordersettingMapper.update(ordersetting,wrapper);
                    //返回数据
                    Integer id = orderMapper.selectOne(wrapper2).getId();
                    return new Result(true,MessageConstant.ORDER_SUCCESS,id);
                }
            }else {
                //未注册会员，则必定没有重复预约，需先注册，再预约
                //注册
                member = new Member();
                member.setName((String)map.get("name"));
                member.setSex((String) map.get("sex"));
                member.setIdcard((String) map.get("idCard"));
                member.setPhonenumber((String) map.get("telephone"));
                String date = DateUtils.parseDate2String(new Date(),"yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date,dateTimeFormatter);
                member.setRegtime(localDate);
                memberMapper.insert(member);
                //预约
                Order order1 = new Order();
                order1.setMemberId(member.getId());
                LocalDate orderdate = LocalDate.parse((String)map.get("orderDate"),dateTimeFormatter);
                order1.setOrderdate(orderdate);
                order1.setOrdertype("微信预约");
                order1.setOrderstatus("未就诊状态");
                order1.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
                orderMapper.insert(order1);
                //已预约人数加一
                Integer reservation = ordersetting.getReservations()+1;
                ordersetting.setReservations(reservation);
                ordersettingMapper.update(ordersetting,wrapper);
                //返回数据
                wrapper2 = new QueryWrapper<>();
                wrapper2.eq("member_id",member.getId());
                wrapper2.eq("orderDate",map.get("orderDate"));
                wrapper2.eq("setmeal_id",map.get("setmealId"));
                order = orderMapper.selectOne(wrapper2);
                Integer id = orderMapper.selectOne(wrapper2).getId();
                return new Result(true,MessageConstant.ORDER_SUCCESS,id);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIl);
        }
    }

    @Override
    public Map findOrderById(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("orderdate",order.getOrderdate());
        hashMap.put("ordertype",order.getOrdertype());
        String member = memberMapper.selectById(order.getMemberId()).getName();
        hashMap.put("member",member);
        String setmeal = setmealMapper.selectById(order.getSetmealId()).getName();
        hashMap.put("setmeal",setmeal);
        return hashMap;
    }

    @Override
    public List<Integer> getSetmealCount(LinkedList<Integer> setmealIds) {
        LinkedList<Integer> setmealCount = new LinkedList<>();
        for (Integer id : setmealIds) {
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("setmeal_id",id);
            setmealCount.add(orderMapper.selectCount(wrapper));
        }
        return setmealCount;
    }
}
