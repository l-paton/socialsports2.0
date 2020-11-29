package tfg.dam.socialsports.model;

import java.io.Serializable;

public class Requirement implements Serializable {

    private int minAge;
    private int maxAge;
    private String gender;  
    private float reputation;

    public Requirement(int minAge, int maxAge, String gender, float reputation) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.reputation = reputation;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getReputation() {
        return reputation;
    }

    public void setReputation(float reputation) {
        this.reputation = reputation;
    }

    @Override
    public String toString() {
        return "Requisitos{" +
                "edadMinima=" + minAge +
                ", edadMaxima=" + maxAge +
                ", requisitoDeGenero='" + gender + '\'' +
                ", reputacionNecesaria=" + reputation +
                '}';
    }

    public void inicializarValoresNulos() {
        if (gender == null)
            gender = "";
    }
}
