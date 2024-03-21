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

    public Player() {
    }

    public Player(String email, String password, String name, String surname,
                  String weight, String height, String birth, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.weight = weight;
        this.height = height;
        this.birth = birth;
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setName(String name) {this.name = name;}


    public void setSurname(String surname) {this.surname = surname;}

    public void setWeight(String weight) {this.weight = weight;}

    public void setHeight(String height) {this.height = height;}

    public void setBirth(String birth) {this.birth = birth;}
}
