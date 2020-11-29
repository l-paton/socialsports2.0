package tfg.dam.socialsports.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable {
    private Long id;
    private User organizer;
    private String sport;
    private String address;
    private Date startDate;
    private String time;
    private Date createdAt;
    private int maxParticipants;
    private float price;
    private String comments;
    private Requirement requirement;
    private boolean finish;
    private ArrayList<User> participants;
    private ArrayList<User> applicants;

    public Event(String sport, String address, Date startDate, String time, int maxParticipants, float price, String comments, Requirement requirement) {
        this.sport = sport;
        this.address = address;
        this.startDate = startDate;
        this.time = time;
        this.maxParticipants = maxParticipants;
        this.price = price;
        this.comments = comments;
        this.requirement = requirement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public ArrayList<User> getApplicants() {
        return applicants;
    }

    public void setApplicants(ArrayList<User> applicants) {
        this.applicants = applicants;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", organizer=" + organizer +
                ", sport='" + sport + '\'' +
                ", address='" + address + '\'' +
                ", startDate=" + startDate +
                ", time='" + time + '\'' +
                ", createdAt=" + createdAt +
                ", maxParticipants=" + maxParticipants +
                ", price=" + price +
                ", comments='" + comments + '\'' +
                ", requisitos=" + requirement +
                ", finish=" + finish +
                ", participants=" + participants +
                ", applicants=" + applicants +
                '}';
    }
}
