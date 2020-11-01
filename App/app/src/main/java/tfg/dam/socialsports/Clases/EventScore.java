package tfg.dam.socialsports.Clases;

import java.io.Serializable;

public class EventScore implements Serializable {

    private String idUser;
    private String idEvent;
    private float score;

    public EventScore() {
    }

    public EventScore(String idUser, String idEvent, float score) {
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.score = score;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PuntuacionEvento{" + idUser + " - " + idEvent + " - " + score + "}\n";
    }
}
