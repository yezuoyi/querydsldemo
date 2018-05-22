package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "blog_post")
public class BlogPost {
	@Id
	@GeneratedValue
	private Long id;

	private String title;

	private String body;

	@ManyToOne
	@JsonBackReference
	private BlogUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public BlogUser getUser() {
		return user;
	}

	public void setUser(BlogUser user) {
		this.user = user;
	}

}
