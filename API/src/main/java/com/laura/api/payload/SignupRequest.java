package com.laura.api.payload;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
    @NotBlank
    @Size(max = 320)
    @Email
    private String email;
    
    @Size(max = 32)
    private String firstname;
    
    @Size(max = 32)
    private String lastname;

    private Date birthday;
    
    @NotBlank
    @Size(min = 6, max = 1024)
    private String password;

    @Size(max = 6)
    private String gender;
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public Date getBirthday() {
      return birthday;
    }

    public void setBirthday(Date birthday) {
      this.birthday = birthday;
    }

    @Override
    public String toString() {
      return "SignupRequest [birthday=" + birthday + ", email=" + email + ", firstname=" + firstname + ", gender="
          + gender + ", lastname=" + lastname + ", password=" + password + "]";
    }

}
