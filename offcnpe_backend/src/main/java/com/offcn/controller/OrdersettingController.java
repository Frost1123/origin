package com.offcn.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.POIUtils;
import com.offcn.constants.Result;
import com.offcn.service.OrdersettingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzw
 * @since 2021-08-13
 */
@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {
    @Reference
    OrdersettingService ordersettingService;

    //上传excel表格
    @RequestMapping("uploadTempleate")
    public Result uploadTempleate(@RequestParam("excelFile")MultipartFile file) {
        try {
            List<String[]> strings = POIUtils.readExcel(file);
            ordersettingService.uploadTemplate(strings);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    //查询预约数据
    @RequestMapping("getOrdersettingByDate")
    public Result getOrdersettingByDate(String date) {
        List<Map> list = null;
        try {
            list = ordersettingService.getOrdersettingByDate(date);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL,list);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
    }

    //设置预约信息
    @RequestMapping("editNumberByDate")
    public Result editNumberByDate(@RequestParam("number") Integer number,@RequestParam("orderDate") String orderDate) {
        try {
            ordersettingService.editNumberByDate(number,orderDate);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}

