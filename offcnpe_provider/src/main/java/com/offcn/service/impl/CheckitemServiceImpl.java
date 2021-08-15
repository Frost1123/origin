package com.offcn.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.mapper.CheckgroupCheckitemMapper;
import com.offcn.mapper.CheckitemMapper;
import com.offcn.pojo.Checkitem;
import com.offcn.service.CheckitemService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component//IOC
@Service(interfaceClass = CheckitemService.class)
@Transactional
public class CheckitemServiceImpl implements CheckitemService {

    @Resource
    private CheckitemMapper checkitemMapper;
    @Resource
    private CheckgroupCheckitemMapper checkgroupCheckitemMapper;

    @Override
    public void save(Checkitem checkitem) {
        checkitemMapper.insert(checkitem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //构建分页page对象
        Page<Checkitem> page1 = new Page<>(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //构建查询条件对象
        QueryWrapper<Checkitem> wrapper = new QueryWrapper<>();
        if(queryPageBean.getQueryString()!=null &&queryPageBean.getQueryString().length()>0){
            wrapper.like("name",queryPageBean.getQueryString());
            wrapper.or();
            wrapper.like("code",queryPageBean.getQueryString());
        }
        //分页查询数据
        Page<Checkitem> checkitemPage = checkitemMapper.selectPage(page1, wrapper);
        return new PageResult(checkitemPage.getTotal(),checkitemPage.getRecords());
    }

    @Override
    public void deleteInfoById(Integer id) {
        //如果有中间表，则先删除中间表那一条数据
        Wrapper wrapper = new QueryWrapper();
        ((QueryWrapper) wrapper).eq("checkitem_id",id);
        checkgroupCheckitemMapper.delete(wrapper);
        //然后删除检查项表的数据
        checkitemMapper.deleteById(id);
    }

    @Override
    public Checkitem findInfoById(Integer id) {
        return checkitemMapper.selectById(id);
    }

    @Override
    public void updateInfoById(Checkitem checkitem) {
        checkitemMapper.updateById(checkitem);
    }
}
