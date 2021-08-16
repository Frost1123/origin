package com.offcn.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.constants.Result;
import com.offcn.pojo.Setmeal;
import com.offcn.service.SetmealService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzw
 * @since 2021-08-13
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;

    @RequestMapping("uploadpic")
    public Result uploadpic(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String fileName = new Date().getTime()+originalFilename.substring(index-1);
        try {
            File file = new File("D:/files/"+fileName);
            imgFile.transferTo(file);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL,fileName);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @RequestMapping("addMeal")
    public Result addMeal(@RequestBody Setmeal setmeal,Integer[] groupInfo) {
        try {
            setmealService.addMeal(setmeal,groupInfo);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean pageBean) {
        return setmealService.findPage(pageBean);
    }
}

