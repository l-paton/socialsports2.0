package tfg.dam.socialsports.Clases;

import java.io.Serializable;

public class EventScore implements Serializable {

    private Long idUser;
    private Long idEvent;
    private float score;

    public EventScore() {
    }

    public EventScore(Long idUser, Long idEvent, float score) {
        this.idUser = idUser;
        this.idEvent = idEvent;
        this.score = score;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
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
