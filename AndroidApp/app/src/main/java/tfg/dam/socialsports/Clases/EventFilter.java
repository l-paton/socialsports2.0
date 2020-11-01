package tfg.dam.socialsports.Clases;

import java.util.Date;

public class EventFilter {

    private String sport;
    private String address;
    private Date startAt;
    private String time;
    private float reputation;

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

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getReputation() {
        return reputation;
    }

    public void setReputation(float reputation) {
        this.reputation = reputation;
    }

    @Override
    public String toString() {
        return "FiltroDeEvento{" +
                "sport='" + sport + '\'' +
                ", address='" + address + '\'' +
                ", startAt=" + startAt +
                ", time='" + time + '\'' +
                ", reputation=" + reputation +
                '}';
    }
}
