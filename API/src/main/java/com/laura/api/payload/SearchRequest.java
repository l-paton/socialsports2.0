package com.laura.api.payload;

import java.util.Date;

public class SearchRequest {
    
    private String sport;
    private String address;
    private Date startDate;
    private String time;

    public SearchRequest(String sport, String address, Date startDate, String time) {
        this.sport = sport;
        this.address = address;
        this.startDate = startDate;
        this.time = time;
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
    
}
