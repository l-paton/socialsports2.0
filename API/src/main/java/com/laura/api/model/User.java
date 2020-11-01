package com.laura.api.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="USERS", uniqueConstraints = {@UniqueConstraint(columnNames = "EMAIL")} )
public class User {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private long id;
	
	@JsonIgnore
	@NotBlank
	@Email
	@Size(max = 320)
	@Column(name="EMAIL", nullable=false)
	private String email;
	
	@JsonIgnore
	@NotBlank
	@Size(max = 64)
	@Column(name="PASSWORD", nullable=false)
	private String password;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="GENDER")
	private String gender;

	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="BIRTHDAY")
	private Date birthday;
	
	@JsonIgnore
	@Column(name="CREATED_AT")
	private Date createdAt;
	
	@Column(name="PICTURE")
	private String picture;

	@Column(name="DESCRIPTION")
	private String description; 

	/*@JsonIgnore
	@Column(name="ACTIVATED")
	private boolean activated;*/
	
	@JsonIgnore
	@ManyToMany(mappedBy = "participants")
	private Set<Event> eventsJoined = new HashSet<>();

	@JsonIgnore
	@ManyToMany(mappedBy = "applicants")
	private Set<Event> eventsApplied = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany
	private Set<User> friends = new HashSet<>();

	public User() {
		
	}
	
	public User(String email, String firstname, String lastname, String password, String gender, Date createAt) {
		this.email = email;
		this.firstName = firstname;
		this.lastName = lastname;
		this.password = password;
		this.gender = gender;
		this.createdAt =  createAt;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setcreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<Event> getEventsJoined() {
		return eventsJoined;
	}

	public void setEventsJoined(Set<Event> eventsJoined) {
		this.eventsJoined = eventsJoined;
	}

	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Event> getEventsApplied() {
		return eventsApplied;
	}

	public void setEventsApplied(Set<Event> eventsApplied) {
		this.eventsApplied = eventsApplied;
	}

	/*public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}*/	
}
