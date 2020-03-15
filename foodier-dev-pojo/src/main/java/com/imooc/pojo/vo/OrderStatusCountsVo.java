package com.imooc.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatusCountsVo implements Serializable {
    private int waitPayCounts;
    private int waitDeliverCounts;
    private int waitReceiveCounts;
    private int waitCommentCounts;

    public OrderStatusCountsVo(int waitPayCount, int waitDeliver, int waitReceive, int waitComment) {
        this.waitPayCounts = waitPayCount;
        this.waitDeliverCounts = waitDeliver;
        this.waitReceiveCounts = waitReceive;
        this.waitCommentCounts = waitComment;
    }
}
