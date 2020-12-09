package tfg.dam.socialsports.model;

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
    private float reputationParticipant;
    private float reputationOrganizer;
    private String picture;
    private ArrayList<User> friends;
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

    public float getReputationParticipant() {
        return reputationParticipant;
    }

    public void setReputationParticipant(float reputationParticipant) {
        this.reputationParticipant = reputationParticipant;
    }

    public float getReputationOrganizer() {
        return reputationOrganizer;
    }

    public void setReputationOrganizer(float reputationOrganizer) {
        this.reputationOrganizer = reputationOrganizer;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
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
                ", reputacionParticipanteUsuario=" + reputationParticipant +
                '}';
    }

    public void inicializarValoresNulos() {
        if(email == null)
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
        if (friends == null)
            friends = new ArrayList<>();
        if(picture == null){
            picture = "";
        }
        if (listaBloqueados == null)
            listaBloqueados = new ArrayList<>();
    }
}
