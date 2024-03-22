package com.example.proindividual.models;

import java.io.Serializable;

public class Coach implements Serializable {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String role;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getName() {return name;}

    public String getSurname() {return surname;}

    public String getRole() {return role;}

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
}
