package com.example.proindividual.models;

public class Training {
    private String trainingId;
    private String coachUserId;
    private String playerUserId;
    private String title;
    private String category;
    private String date;
    private String details;

    public Training() {
    }


    public Training(String trainingId, String coachUserId, String playerUserId, String title, String category, String date, String details) {
        this.trainingId = trainingId;
        this.coachUserId = coachUserId;
        this.playerUserId = playerUserId;
        this.title = title;
        this.category = category;
        this.date = date;
        this.details = details;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public String getCoachUserId() {
        return coachUserId;
    }

    public String getPlayerUserId() {
        return playerUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    // Settery
    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public void setCoachUserId(String coachUserId) {
        this.coachUserId = coachUserId;
    }

    public void setPlayerUserId(String playerUserId) {
        this.playerUserId = playerUserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
