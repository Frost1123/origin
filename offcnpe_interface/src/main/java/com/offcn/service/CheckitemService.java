package com.offcn.service;

import com.offcn.constants.PageResult;
import com.offcn.constants.QueryPageBean;
import com.offcn.pojo.Checkitem;

public interface CheckitemService {
    void save(Checkitem checkitem);
    PageResult findPage(QueryPageBean queryPageBean);
    void deleteInfoById(Integer id);
    Checkitem findInfoById(Integer id);
    void updateInfoById(Checkitem checkitem);
}
