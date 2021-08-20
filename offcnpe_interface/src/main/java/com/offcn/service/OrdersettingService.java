package com.offcn.service;


import java.util.List;
import java.util.Map;

public interface OrdersettingService {

    void uploadTemplate(List<String[]> strings);

    List<Map> getOrdersettingByDate(String date);

    void editNumberByDate(Integer number,String orderDate);
}
