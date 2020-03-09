package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @ClassName ItemCommentVO
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/2/26 21:58
 **/
@Data
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
