package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version 1.0
 * @ClassName Application
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/15 16:39
 **/
//开启定时任务
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mapper")
//扫描所有包以及相关组件
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
