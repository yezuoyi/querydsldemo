package com.example.demo.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;


@Entity
@Table(name = "Wife")
public class Wife implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wid;
    
    @NonNull@Column(nullable = false)
    private String name;
    
    
    @OneToOne
    @JoinColumn(name="hus_id")
    private Husband husband;
    
    
    

    public Husband getHusband() {
		return husband;
	}

	public void setHusband(Husband husband) {
		this.husband = husband;
	}

	public Integer getWid() {
		return wid;
	}

	public void setWid(Integer wid) {
		this.wid = wid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


    public Wife(){
    }

    public Wife(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Wife{" +
                "wid=" + wid +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode(){
        return wid;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Wife))
            return  false;
        Wife wife = (Wife) obj;
        if(wife.getWid() == this.wid)
            return true;
        else
            return false;
    }
}