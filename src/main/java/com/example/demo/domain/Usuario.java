package com.example.demo.domain;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIOS")
public class Usuario {
	
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "usr_seq_gen")
	@SequenceGenerator(
			name = "usr_seq_gen",
			sequenceName = "SEQ_USUARIOS",
			allocationSize = 1
	)
	private Long id;
	
	@Column(name = "USERNAME", nullable = false, unique = true, length = 100)
	private String username; 
	
	@Column(name = "PASSWORD_HASH", nullable = false )
	private String passwordHash; 
	
	@Column(name = "ROLE", nullable = false, length = 50)
	private String role = "USER"; 
	
	@Column(name = "CREATED_AT", insertable = false, updatable = false)
	private Date createdAt; 
	
	
	@Column(name="LAST_ACCESS")
	private Timestamp lastAccess; 
	
	
	@Column(name = "IP_CREATED", length = 45)
	private String ipCreated; 
	
	@Column(name = "IP_LAST_ACCESS", length = 45)
	private String ipLastAccess;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPasswordHash() {
		return passwordHash;
	}


	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Timestamp getLastAccess() {
		return lastAccess;
	}


	public void setLastAccess(Timestamp lastAccess) {
		this.lastAccess = lastAccess;
	}


	public String getIpCreated() {
		return ipCreated;
	}


	public void setIpCreated(String ipCreated) {
		this.ipCreated = ipCreated;
	}


	public String getIpLastAccess() {
		return ipLastAccess;
	}


	public void setIpLastAccess(String ipLastAccess) {
		this.ipLastAccess = ipLastAccess;
	} 
	
	
	
	
	
	
	
	
	
}
