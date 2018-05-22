package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "blog_users")
public class BlogUser {
	@Id
	@GeneratedValue
	private Long id;

	private String login;

	private Boolean disabled;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
	private Set<BlogPost> blogPosts = new HashSet<>(0);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Set<BlogPost> getBlogPosts() {
		return blogPosts;
	}

	public void setBlogPosts(Set<BlogPost> blogPosts) {
		this.blogPosts = blogPosts;
	}
	
	
}
