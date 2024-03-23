package com.example.proindividual.models;

import java.io.Serializable;

public class Coach implements Serializable {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String role;
    private String profileImageUrl;

    public Coach(){

    }

    public Coach(String email, String password, String name, String surname, String role, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getName() {return name;}

    public String getSurname() {return surname;}

    public String getRole() {return role;}
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) { this.password = password;
    }
    public void setRole(String role) {this.role = role;}

    public void setName(String name) { this.name = name;
    }

    public void setSurname(String surname) {this.surname = surname;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
