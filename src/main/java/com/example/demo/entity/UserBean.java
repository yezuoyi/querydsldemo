package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserBean
{
    @Id
    @GeneratedValue
    @Column(name = "u_id")
    private Long id;
    @Column(name = "u_username")
    private String name;
    @Column(name = "u_age")
    private int age;
    @Column(name = "u_score")
    private double socre;
}

