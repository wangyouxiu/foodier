package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0
 * @ClassName UserBO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/16 16:29
 **/
@ApiModel(value = "用户对象BO",description = "从客户端，由用户传入的数据，封装在此entity中")
@Data
public class UserBO {
    @ApiModelProperty(value = "用户名",name = "username",example = "imooc",required = true)
    private String username;
    @ApiModelProperty(value = "密码",name = "password",example = "123456",required = true)
    private String password;
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "123456",required = false)
    private String confirmPassword;
}
