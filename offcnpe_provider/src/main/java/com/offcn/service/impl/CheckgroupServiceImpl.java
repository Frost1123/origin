package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.mapper.CheckgroupCheckitemMapper;
import com.offcn.mapper.CheckgroupMapper;
import com.offcn.mapper.SetmealCheckgroupMapper;
import com.offcn.pojo.Checkgroup;
import com.offcn.pojo.CheckgroupCheckitem;
import com.offcn.pojo.SetmealCheckgroup;
import com.offcn.service.CheckgroupService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service(interfaceClass = CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {
    @Resource
    CheckgroupMapper checkgroupMapper;
    @Resource
    CheckgroupCheckitemMapper checkgroupCheckitemMapper;
    @Resource
    SetmealCheckgroupMapper setmealCheckgroupMapper;

    @Override
    public void addGroup(Checkgroup checkgroup, Integer[] checkitemIds) {
        checkgroupMapper.insert(checkgroup);
        CheckgroupCheckitem checkgroupCheckitem = new CheckgroupCheckitem();
        checkgroupCheckitem.setCheckgroupId(checkgroup.getId());
        for (Integer id : checkitemIds) {
            checkgroupCheckitem.setCheckitemId(id);
            checkgroupCheckitemMapper.insert(checkgroupCheckitem);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean pageBean) {
        Page<Checkgroup> page = new Page<>(pageBean.getCurrentPage(),pageBean.getPageSize());
        QueryWrapper<Checkgroup> queryWrapper = new QueryWrapper<>();
        if (pageBean.getQueryString() != null && pageBean.getQueryString().length() != 0) {
            queryWrapper.like("code",pageBean.getQueryString())
                     .or()
                        .like("name",pageBean.getQueryString())
                     .or()
                        .like("helpCode",pageBean.getQueryString());
        }
        Page<Checkgroup> checkgroupPage = checkgroupMapper.selectPage(page,queryWrapper);
        return new PageResult(checkgroupPage.getTotal(),checkgroupPage.getRecords());
    }

    @Override
    public Checkgroup findGroupInfoById(Integer id) {
        return checkgroupMapper.selectById(id);
    }

    @Override
    public void editGroup(Checkgroup checkgroup, Integer[] checkitemIds) {
        //更新检查组信息
        checkgroupMapper.updateById(checkgroup);
        //由于中间表改动，所以先删除中间表数据
        QueryWrapper<CheckgroupCheckitem> wrapper = new QueryWrapper<>();
        wrapper.eq("checkgroup_id",checkgroup.getId());
        checkgroupCheckitemMapper.delete(wrapper);
        //然后更新中间表
        CheckgroupCheckitem checkgroupCheckitem = new CheckgroupCheckitem();
        checkgroupCheckitem.setCheckgroupId(checkgroup.getId());
        for (Integer id : checkitemIds) {
            checkgroupCheckitem.setCheckitemId(id);
            checkgroupCheckitemMapper.insert(checkgroupCheckitem);
        }
    }

    @Override
    public void deleteInfoById(Integer id) {
        //先删除相应的中间表
        HashMap<String,Object> map = new HashMap<>();
        map.put("checkgroup_id",id);
        checkgroupCheckitemMapper.deleteByMap(map);
        setmealCheckgroupMapper.deleteByMap(map);
        //再删除检查组
        checkgroupMapper.deleteById(id);
    }

    @Override
    public List showAllGroupInfo() {
        return checkgroupMapper.selectList(null);
    }

    @Override
    public List<Integer> getCheckGroupIdBySetmealId(Integer groupId) {
        LambdaQueryWrapper<SetmealCheckgroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SetmealCheckgroup::getCheckgroupId);
        lambdaQueryWrapper.eq(SetmealCheckgroup::getSetmealId,groupId);
        return setmealCheckgroupMapper.selectObjs(lambdaQueryWrapper).stream().map(o -> (Integer)o).collect(Collectors.toList());
    }
}
