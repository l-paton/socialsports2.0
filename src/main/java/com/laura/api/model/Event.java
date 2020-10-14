package com.laura.api.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SPORT_EVENTS")
public class Event {

	@Id
	@GeneratedValue
	@Column(name="ID")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="ORGANIZADOR_ID", referencedColumnName="ID")
	private User organizer;
	
	@Column(name="SPORT", nullable=false)
	private String sport;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="CREATED_AT")
	private Date createdAt;
	
	@Column(name="MAX_PARTICIPANTS")
	private int maxParticipants;
	
	@Column(name="PRICE")
	private float price;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="FINISH")
	private boolean finish;
	
	@ManyToMany(mappedBy = "eventsJoined")
	private Set<User> participants;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}
	
}
