package com.diary.torp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	@Id
	@GeneratedValue
	private long id;
	
	@Size(min = 3, max = 15)
	@Column(unique = true, nullable = false, length = 15)
	private String userId;

	@Size(min = 4, max = 20)
	@Column(nullable = false, length = 15)
	@JsonIgnore
	private String password;

	@Size(min = 3, max = 20)
	@Column(nullable = false, length = 20)
	private String name;
	
	private boolean deleted = false;
	
	public boolean matchPassword(String password) {
		return this.password.equals(password);
	}
	
	public User() {
		
	}
	
	public User(String userId, String password, String name) {
		this.userId = userId;
		this.password = password;
		this.name = name;
	}
	
	public void update(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	public boolean isGuestUser() {
		return false;
	}

	//getter(), setter() methods	
	public long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	//toString() method
	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", password=" + password + ", name=" + name + ", deleted="
				+ deleted + "]";
	}
}
