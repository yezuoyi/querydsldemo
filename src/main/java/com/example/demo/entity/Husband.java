package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table(name = "Husband")
public class Husband implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hid;

    @NonNull@Column(nullable = false)
    private String name;


    @OneToOne(mappedBy="husband")
    private Wife wife;

    public Husband(){
    }

    public Husband(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Husband{" +
                "hid=" + hid +
                ", name='" + name + '\'' +
                ", wife=" + wife +
                '}';
    }
}