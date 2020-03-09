package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

/**
 * @version 1.0
 * @ClassName CarouselService
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/19 15:40
 **/
public interface CarouselService {

    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    List<Carousel> queryAll(Integer isShow);
}
