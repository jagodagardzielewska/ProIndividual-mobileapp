package com.example.proindividual.models;

public class InviteCode {
    private String code;
    private String userId;
    private long expirationTime;


    public InviteCode() {
    }

    public InviteCode(String code, String userId, long expirationTime) {
        this.code = code;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }


    public String getCode() {
        return code;
    }

    public String getUserId() {
        return userId;
    }

    public long getExpirationTime() {
        return expirationTime;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
