package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.mapper.*;
import com.offcn.pojo.*;
import com.offcn.service.SetmealService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Resource
    SetmealMapper setmealMapper;
    @Resource
    SetmealCheckgroupMapper setmealCheckgroupMapper;
    @Resource
    CheckgroupCheckitemMapper checkgroupCheckitemMapper;
    @Resource
    CheckitemMapper checkitemMapper;
    @Resource
    CheckgroupMapper checkgroupMapper;


    @Override
    public void addMeal(Setmeal setmeal, Integer[] groupInfo) {
        setmealMapper.insert(setmeal);
        //然后增加中间表相关数据
        SetmealCheckgroup setmealCheckgroup = new SetmealCheckgroup();
        setmealCheckgroup.setSetmealId(setmeal.getId());
        for (Integer groupId : groupInfo) {
            setmealCheckgroup.setCheckgroupId(groupId);
            setmealCheckgroupMapper.insert(setmealCheckgroup);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean pageBean) {
        Page<Setmeal> page = new Page<>(pageBean.getCurrentPage(),pageBean.getPageSize());
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        if (pageBean.getQueryString() != null && pageBean.getQueryString().length() != 0) {
            wrapper.like("name",pageBean.getQueryString())
                    .or()
                    .like("code",pageBean.getQueryString())
                    .or()
                    .like("helpCode",pageBean.getQueryString());
        }
        Page<Setmeal> page1 = setmealMapper.selectPage(page, wrapper);
        return new PageResult(page1.getTotal(),page.getRecords());
    }

    @Override
    public Setmeal findSetmealInfoById(Integer id) {
        return setmealMapper.selectById(id);
    }

    @Override
    public String editSetmeal(Setmeal setmeal, Integer[] checkgroupIds) {
        //获取之前的图片的名称,返回给controller用于删除，节省服务器空间
        String path = setmealMapper.selectById(setmeal.getId()).getImg();
        //更新套餐相关数据
        setmealMapper.updateById(setmeal);
        //删除相关的中间表
        HashMap<String,Object> map = new HashMap<>();
        map.put("setmeal_id",setmeal.getId());
        System.out.println(setmeal.getId());
        setmealCheckgroupMapper.deleteByMap(map);
        //再添加新的中间表
        SetmealCheckgroup setmealCheckgroup = new SetmealCheckgroup();
        setmealCheckgroup.setSetmealId(setmeal.getId());
        for (Integer id : checkgroupIds) {
            setmealCheckgroup.setCheckgroupId(id);
            setmealCheckgroupMapper.insert(setmealCheckgroup);
        }
        return path;
    }

    @Override
    public void deleteInfoById(Integer id) {
        //先删除相应的中间表
        HashMap<String,Object> map = new HashMap<>();
        map.put("setmeal_id",id);
        setmealCheckgroupMapper.deleteByMap(map);
        //再删除检套餐
        setmealMapper.deleteById(id);
    }

    @Override
    public List<Setmeal> getAllSetmeal() {
        return setmealMapper.selectList(null);
    }

    @Override
    public Setmeal findSetmealById(Integer id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        LambdaQueryWrapper<SetmealCheckgroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealCheckgroup::getSetmealId,id);
        lambdaQueryWrapper.select(SetmealCheckgroup::getCheckgroupId);
        //利用中间表获取group的id列表
        List<Integer> groupIdList = setmealCheckgroupMapper.selectObjs(lambdaQueryWrapper).stream().map(
                o -> (Integer) o
        ).collect(Collectors.toList());
        //利用中间表获取group的集合
        if (groupIdList.size() >0) {
        List<Checkgroup> checkgroupList = checkgroupMapper.selectBatchIds(groupIdList);
            for (Checkgroup checkgroup : checkgroupList) {
                LambdaQueryWrapper<CheckgroupCheckitem> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(CheckgroupCheckitem::getCheckgroupId, checkgroup.getId());
                lambdaQueryWrapper1.select(CheckgroupCheckitem::getCheckitemId);
                List<Integer> itemIdList = checkgroupCheckitemMapper.selectObjs(lambdaQueryWrapper1).stream().map(
                        o -> (Integer) o
                ).collect(Collectors.toList());
                //获取检查项的集合
                if (itemIdList.size() > 0) {
                    List<Checkitem> itemList = checkitemMapper.selectBatchIds(itemIdList);
                    checkgroup.setCheckitems(itemList);
                }
            }
            //将checkgroupList封装到setmeal中
            setmeal.setCheckGroups(checkgroupList);
            return setmeal;
        }
        return setmeal;
    }
}
