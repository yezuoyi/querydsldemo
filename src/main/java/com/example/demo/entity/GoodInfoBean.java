package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "good_infos")
@Data
public class GoodInfoBean
    implements Serializable
{
    //主键
    @Id
    @GeneratedValue
    @Column(name = "tg_id")
    private Long id;
    //商品标题
    @Column(name = "tg_title")
    private String title;
    //商品价格
    @Column(name = "tg_price")
    private double price;
    //商品单位
    @Column(name = "tg_unit")
    private String unit;
    //商品排序
    @Column(name = "tg_order")
    private int order;
    //类型外键
    @Column(name = "tg_type_id")
    private Long typeId;
}
