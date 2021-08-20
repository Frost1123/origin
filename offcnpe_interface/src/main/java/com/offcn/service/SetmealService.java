package com.offcn.service;

import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void addMeal(Setmeal setmeal,Integer[] groupInfo);
    PageResult findPage(QueryPageBean pageBean);
    Setmeal findSetmealInfoById(Integer id);
    String editSetmeal(Setmeal setmeal,Integer[] checkgroupIds);
    void deleteInfoById(Integer id);
    List<Setmeal> getAllSetmeal();

    Setmeal findSetmealById(Integer id);
}
