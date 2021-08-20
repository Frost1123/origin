package com.offcn.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.Result;
import com.offcn.pojo.Setmeal;
import com.offcn.service.OrderService;
import com.offcn.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("setmealcli")
public class SetmealCliController {
    @Reference
    SetmealService setmealService;
    @Reference
    OrderService orderService;

    @RequestMapping("getAllSetmeal")
    public Result getAllSetmeal() {
        List<Setmeal> list = null;
        try {
            list = setmealService.getAllSetmeal();
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL,list);
        }
        return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    @RequestMapping("findInfoById")
    public Result findInfoById(Integer id) {
        Setmeal setmeal = null;
        try {
            setmeal = setmealService.findSetmealById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL,setmeal);
        }
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
