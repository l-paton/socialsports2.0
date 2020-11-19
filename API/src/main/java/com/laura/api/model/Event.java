package com.laura.api.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@Size(max = 64)
	@Column(name="SPORT", nullable=false)
	private String sport;
	
	@Size(max = 32)
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="START_DATE")
	private Date startDate;

	@Column(name="TIME")
	private String time;
	
	@Column(name="CREATED_AT")
	private Date createdAt;
	
	@Column(name="MAX_PARTICIPANTS")
	private int maxParticipants;
	
	@Column(name="PRICE")
	private float price;
	
	@Size(max = 64)
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="FINISH")
	private boolean finish;
	
	@Embedded
	private Requirement requirement;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "participants_event",
		joinColumns = @JoinColumn(name = "event_id"), 
	  	inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> participants;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "applicants_event",
		joinColumns = @JoinColumn(name = "event_id"), 
	  	inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> applicants;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="event")
	private List<CommentEvent> userComments;
	

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	public Set<User> getApplicants() {
		return applicants;
	}

	public void setApplicants(Set<User> applicants) {
		this.applicants = applicants;
	}

	public List<CommentEvent> getUserComments() {
		return userComments;
	}

	public void setUserComments(List<CommentEvent> userComments) {
		this.userComments = userComments;
	}

	@Override
	public String toString() {
		return "Event [address=" + address + ", applicants=" + applicants + ", comments=" + comments + ", createdAt="
				+ createdAt + ", finish=" + finish + ", id=" + id + ", maxParticipants=" + maxParticipants
				+ ", organizer=" + organizer + ", participants=" + participants + ", price=" + price + ", requirement="
				+ requirement + ", sport=" + sport + ", startDate=" + startDate + ", time=" + time + ", userComments="
				+ userComments + "]";
	}

}
