package com.offcn.service;

import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.pojo.Checkgroup;

import java.util.List;

public interface CheckgroupService {
    void addGroup(Checkgroup checkgroup,Integer[] checkitemIds);
    PageResult findPage(QueryPageBean pageBean);
    Checkgroup findGroupInfoById(Integer id);
    void editGroup(Checkgroup checkgroup,Integer[] checkitemIds);
    void deleteInfoById(Integer id);
    List showAllGroupInfo();
}
