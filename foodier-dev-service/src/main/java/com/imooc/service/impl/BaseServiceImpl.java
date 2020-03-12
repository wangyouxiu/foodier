package com.imooc.service.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public class BaseServiceImpl {

    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageInfo = new PageInfo(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setTotal(pageInfo.getPages());
        grid.setRecords(pageInfo.getTotal());
        grid.setRows(list);
        return grid;
    }
}
