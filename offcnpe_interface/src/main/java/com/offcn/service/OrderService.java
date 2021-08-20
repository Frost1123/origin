package com.offcn.service;

import com.offcn.constants.Result;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Result submitOrder(Map map);

    Map findOrderById(Integer orderId);

    List<Integer> getSetmealCount(LinkedList<Integer> setmealIds);
}
