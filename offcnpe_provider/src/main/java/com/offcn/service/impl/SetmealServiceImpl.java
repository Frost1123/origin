package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.mapper.SetmealCheckgroupMapper;
import com.offcn.mapper.SetmealMapper;
import com.offcn.pojo.Setmeal;
import com.offcn.pojo.SetmealCheckgroup;
import com.offcn.service.SetmealService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Resource
    SetmealMapper setmealMapper;
    @Resource
    SetmealCheckgroupMapper setmealCheckgroupMapper;

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
}
