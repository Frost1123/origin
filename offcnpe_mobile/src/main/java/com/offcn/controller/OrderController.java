package com.offcn.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.Result;
import com.offcn.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {
    @Reference
    OrderService orderService;

    @RequestMapping("submitOrder")
    public Result submitOrder(@RequestBody Map map ){
        //设置预约类型
        map.put("orderType","微信预约");
        return  orderService.submitOrder(map);
    }

    @RequestMapping("findOrderById")
    public Result findOrderById(@RequestParam("id") Integer orderId) {
        Map map = null;
        try {
            map = orderService.findOrderById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL,map);
        }
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}
