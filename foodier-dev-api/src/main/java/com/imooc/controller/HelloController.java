package com.imooc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @version 1.0
 * @ClassName HelloController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/15 16:41
 **/
@ApiIgnore
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object Hello() {
        return "Hello Word";
    }
}
