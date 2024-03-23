package com.example.proindividual.models;

import java.io.Serializable;

public class Player implements Serializable {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String weight;
    private String height;
    private String birth;
    private String role;
    private String profileImageUrl;

    public Player() {
    }

    public Player(String email, String password, String name, String surname,
                  String weight, String height, String birth, String role, String getProfileImageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.weight = weight;
        this.height = height;
        this.birth = birth;
        this.role = role;
        this.profileImageUrl = getProfileImageUrl;
    }


    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPassword() {
        return password;
    }
    public String getName() {return name;}

    public String getSurname() {return surname;}

    public String getWeight() {return weight;}
    public String getHeight() {return height;}

    public String getBirth() {return birth;}

    public String getRole() {return role;}


    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setName(String name) {this.name = name;}


    public void setSurname(String surname) {this.surname = surname;}

    public void setWeight(String weight) {this.weight = weight;}

    public void setHeight(String height) {this.height = height;}

    public void setBirth(String birth) {this.birth = birth;}

    public void setRole(String role) {this.role = role;}
}
