package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @ClassName SimpleItemVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/21 16:57
 **/
@Data
public class SimpleItemVO {
    private String itemId;
    private String itemName;
    private String itemUrl;
    private Date createdTime;
}
