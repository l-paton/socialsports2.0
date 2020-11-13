package tfg.dam.socialsports.Clases;

import java.io.Serializable;

public class UserScore implements Serializable {

    private Long idUser;
    private Long idRatedUser;
    private Long idEvent;
    private float score;

    public UserScore() {
    }

    public UserScore(Long idUser, Long idRatedUser, Long idEvent, float score) {
        this.idUser = idUser;
        this.idRatedUser = idRatedUser;
        this.idEvent = idEvent;
        this.score = score;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdRatedUser() {
        return idRatedUser;
    }

    public void setIdRatedUser(Long idRatedUser) {
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
