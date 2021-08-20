package com.offcn.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.constants.*;
import com.offcn.pojo.Setmeal;
import com.offcn.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("uploadpic")
    public Result uploadpic(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String fileName = new Date().getTime()+originalFilename.substring(index-1);
        try {
            File file = new File("D:/files/"+fileName);
            imgFile.transferTo(file);
            //上传成功，将文件名称保存在redis中
            redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_UPLOAD,fileName);
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
            //图片需要长久存储到后台，需要保存到redis中的DB字段的Set集合中
            redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB,setmeal.getImg());
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

    @RequestMapping("findSetmealInfoById")
    public Result findSetmealInfoById(Integer id) {
        Setmeal setmeal = null;
        try {
            setmeal = setmealService.findSetmealInfoById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL,setmeal);
        }
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    //编辑套餐
    @RequestMapping("editSetmeal")
    public Result editSetmeal(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
            String path = setmealService.editSetmeal(setmeal,checkgroupIds);
            //删除原来的图片(包括服务器的图片以及redis保存的图片名称)，节省空间
            new File("D:\\files\\"+path).delete();
            redisTemplate.opsForSet().remove(RedisConstant.SETMEAL_PIC_DB,path);
            redisTemplate.opsForSet().remove(RedisConstant.SETMEAL_PIC_UPLOAD,path);
            //图片需要长久存储到后台，需要保存到redis中的DB字段的Set集合中
            redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB,setmeal.getImg());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //删除套餐
    @RequestMapping("deleteInfoById")
    public Result deleteInfoById(Integer id) {
        try {
            setmealService.deleteInfoById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.DELETET_SETMEAL_SUCCESS);
    }
}

