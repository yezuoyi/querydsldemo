package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "good_types")
@Data
public class GoodTypeBean
    implements Serializable
{
    //主键
    @Id
    @GeneratedValue
    @Column(name = "tgt_id")
    private Long id;
    //类型名称
    @Column(name = "tgt_name")
    private String name;
    //是否显示
    @Column(name = "tgt_is_show")
    private int isShow;
    //排序
    @Column(name = "tgt_order")
    private int order;
}
