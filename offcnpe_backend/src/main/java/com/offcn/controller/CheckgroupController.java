package com.offcn.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.constants.MessageConstant;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.constants.Result;
import com.offcn.pojo.Checkgroup;
import com.offcn.service.CheckgroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzw
 * @since 2021-08-13
 */
@RestController
@RequestMapping("checkgroup")
public class CheckgroupController {
    @Reference
    private CheckgroupService checkgroupService;

    //添加检查组
    @RequestMapping("addGroup")
    public Result addGroup(@RequestBody Checkgroup checkgroup, Integer[] checkitemIds) {
        try {
            checkgroupService.addGroup(checkgroup,checkitemIds);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询检查组
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkgroupService.findPage(queryPageBean);
        return pageResult;
    }

    //回显检查组信息
    @RequestMapping("findGroupInfoById")
    public Result findGroupInfoById(Integer id) {
        Checkgroup checkgroup = null;
        try {
            checkgroup = checkgroupService.findGroupInfoById(id);
        }catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL,checkgroup);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroup);
    }

    //修改数据
    @RequestMapping("editGroup")
    public Result editGroup(@RequestBody Checkgroup checkgroup,Integer[] checkitemIds) {
        try{
            checkgroupService.editGroup(checkgroup,checkitemIds);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //删除检查组
    @RequestMapping("deleteInfoById")
    public Result deleteInfoById(Integer id) {
        try {
            checkgroupService.deleteInfoById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有检查组信息，回显操作
    @RequestMapping("showAllGroupInfo")
    public Result showAllGroupInfo() {
        List<Checkgroup> list = null;
        try {
            list = checkgroupService.showAllGroupInfo();
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL,list);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS,list);
    }
}

