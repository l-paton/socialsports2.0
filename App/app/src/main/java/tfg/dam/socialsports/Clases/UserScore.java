package tfg.dam.socialsports.Clases;

import java.io.Serializable;

public class UserScore implements Serializable {

    private String idUser;
    private String idRatedUser;
    private Long idEvent;
    private float score;

    public UserScore() {
    }

    public UserScore(String idUser, String idRatedUser, Long idEvent, float score) {
        this.idUser = idUser;
        this.idRatedUser = idRatedUser;
        this.idEvent = idEvent;
        this.score = score;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdRatedUser() {
        return idRatedUser;
    }

    public void setIdRatedUser(String idRatedUser) {
        this.idRatedUser = idRatedUser;
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
        return "Puntuacion{" + idUser + "-" + idRatedUser + "-" + idEvent + "-" + score + "}\n";
    }
}
