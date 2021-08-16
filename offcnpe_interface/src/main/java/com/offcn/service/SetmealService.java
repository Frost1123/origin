package com.offcn.service;

import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.pojo.Setmeal;

public interface SetmealService {
    void addMeal(Setmeal setmeal,Integer[] groupInfo);
    PageResult findPage(QueryPageBean pageBean);
}
