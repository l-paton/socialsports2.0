package com.laura.api.payload;

import java.util.Date;

public class SearchRequest {
    
    private String sport;
    private String address;
    private Date startDate;
    private String time;
    private float score;

    public SearchRequest(String sport, String address, Date startDate, String time, float score) {
        this.sport = sport;
        this.address = address;
        this.startDate = startDate;
        this.time = time;
        this.score = score;
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

    public Date getstartDate() {
        return startDate;
    }

    public void setstartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SearchRequest [address=" + address + ", score=" + score + ", sport=" + sport + ", startDate="
                + startDate + ", time=" + time + "]";
    }
    
}
