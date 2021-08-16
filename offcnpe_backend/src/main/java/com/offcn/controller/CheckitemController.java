package com.offcn.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.constants.Result;
import com.offcn.pojo.Checkitem;
import com.offcn.service.CheckitemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("checkitem")
public class CheckitemController {

    @Reference
    private CheckitemService checkitemService;

    //添加检查项
    @RequestMapping("save")
    public Result save( @RequestBody Checkitem checkitem){

        try {
            checkitemService.save(checkitem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    //分页查询数据
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkitemService.findPage(queryPageBean);
        return pageResult;
    }

    //展示所有数据
    @RequestMapping("showAllItem")
    public Result showAllItem() {
        List<Checkitem> checkitemList = null;
        try {
            checkitemList = checkitemService.showAllItem();
        }catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL,checkitemList);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemList);
    }

    //删除检查项
    @RequestMapping("deleteInfoById")
    public Result deleteInfoById(Integer id) {
        try {
            checkitemService.deleteInfoById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //编辑数据时，回显数据
    @RequestMapping("findInfoById")
    public Result findInfoById(Integer id) {
        Checkitem checkitem = null;
        try {
            checkitem = checkitemService.findInfoById(id);
        }catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL,checkitem);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitem);
    }

    //更新检查项数据
    @RequestMapping("/updateInfoById")
    public Result updateInfoById(@RequestBody Checkitem checkitem){
        try{
            checkitemService.updateInfoById(checkitem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //通过检查组id获得相应的检查项
    @RequestMapping("getCheckItemIdByGroupId")
    public List<Integer> getCheckItemIdByGroupId(Integer id) {
        return checkitemService.getCheckItemIdByGroupId(id);
    }
}
