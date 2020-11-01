package tfg.dam.socialsports.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {

    private long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private Date birthday;
    private Date createdAt;
    private float userScore;
    private float organizerScore;
    private String picture;
    private ArrayList<User> listaAmigos;
    private ArrayList<User> listaBloqueados;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public float getUserScore() {
        return userScore;
    }

    public void setUserScore(float userScore) {
        this.userScore = userScore;
    }

    public float getOrganizerScore() {
        return organizerScore;
    }

    public void setOrganizerScore(float organizerScore) {
        this.organizerScore = organizerScore;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    public ArrayList<User> getListaAmigos() {
        return listaAmigos;
    }

    public void setListaAmigos(ArrayList<User> listaAmigos) {
        this.listaAmigos = listaAmigos;
    }

    public ArrayList<User> getListaBloqueados() {
        return listaBloqueados;
    }

    public void setListaBloqueados(ArrayList<User> listaBloqueados) {
        this.listaBloqueados = listaBloqueados;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "emailUsuario='" + email + '\'' +
                ", nombreUsuario='" + firstName + '\'' +
                ", apellidosUsuario='" + lastName + '\'' +
                ", generoUsuario='" + gender + '\'' +
                ", reputacionParticipanteUsuario=" + userScore +
                '}';
    }

    public void inicializarValoresNulos() {
        if (email == null)
            email = "";
        if (password == null)
            password = "";
        if (firstName == null)
            firstName = "";
        if (lastName == null)
            lastName = "";
        if (gender == null)
            gender = "";
        if (address == null)
            address = "";
        if (createdAt == null)
            createdAt = new Date();
        if (listaAmigos == null)
            listaAmigos = new ArrayList<>();
        if(picture == null){
            picture = "";
        }
        if (listaBloqueados == null)
            listaBloqueados = new ArrayList<>();
    }
}
