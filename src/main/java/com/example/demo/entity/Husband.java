package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Husband")
public class Husband implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Integer hid;

    @NonNull@Column(nullable = false)
    private String name;


    @OneToOne(cascade = CascadeType.PERSIST,mappedBy="husband")
    @JsonManagedReference
    private Wife wife;

    
    public Integer getHid() {
		return hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Wife getWife() {
		return wife;
	}

	public void setWife(Wife wife) {
		this.wife = wife;
	}

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