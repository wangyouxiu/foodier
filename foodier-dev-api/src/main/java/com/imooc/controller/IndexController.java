package com.imooc.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


/**
 * @version 1.0
 * @ClassName IndexController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 15:43
 **/
@Api(value = "首页",tags = {"首页展示相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        String carouselStr = redisOperator.get("carousel");
        List<Carousel> carousels;
        if (StringUtils.isBlank(carouselStr)) {
            carousels = carouselService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JSON.toJSONString(carousels));
        }else {
            carousels = JSON.parseArray(carouselStr, Carousel.class);
        }
        return IMOOCJSONResult.ok(carousels);
    }

    @ApiOperation(value = "获取商品分类(一级分类)",notes = "获取商品分类(一级分类)",httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {
        String categoriesStr = redisOperator.get("cats");
        List<Category> categories;
        if (StringUtils.isBlank(categoriesStr)) {
            categories = categoryService.queryAllRootLecelCat();
            redisOperator.set("cats", JSON.toJSONString(categories));
        }else {
            categories = JSON.parseArray(categoriesStr, Category.class);
        }
        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId){
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list;
        String listStr = redisOperator.get("subCat:" + rootCatId);
        if (StringUtils.isBlank(listStr)) {
            list = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat:" + rootCatId, JSON.toJSONString(list));
        }else {
            list = JSON.parseArray(listStr, CategoryVO.class);
        }
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "获取根分类下最新的6个商品",notes = "获取根分类下最新的6个商品",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId){
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("缺少请求参数");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }
}
