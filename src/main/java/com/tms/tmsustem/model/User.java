package com.tms.tmsustem.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String username;
    private String password;
    private String role;
    
    @OneToMany(mappedBy = "user") // mappedBy refers to the "user" field in the Task class
    private List<Task> tasks; // Collection of tasks associated with this user
    
    public User() {
		super();
	}
    
	public User(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = "USER";
	}
	public Long getId() {
		return user_id;
	}
	public void setId(Long id) {
		this.user_id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

    // getters and setters
    
}
