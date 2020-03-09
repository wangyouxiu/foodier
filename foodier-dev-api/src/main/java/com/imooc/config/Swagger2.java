package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @version 1.0
 * @ClassName Swagger2
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 22:46
 **/
@Configuration
@EnableSwagger2
public class Swagger2 {

    //原路径
    //https://localhost:8088/swagger-ui.html
    //https://localhost:8088/doc.html

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)      //指定api类型为swagger2
                .apiInfo(apiInfo())                         //用于指定api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))     //指定controller包
                .paths(PathSelectors.any())                 //所有controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口api")                //文档页标题
                .contact(new Contact("imooc",
                        "https://www.imooc.com",
                        "abc@imooc.com"))                //联系人信息
                .description("专为天天吃货提供的api文档")          //详细信息
                .version("1.0.0")
                .termsOfServiceUrl("https://www.imooc.com")     //网站地址
                .build();
    }
}
