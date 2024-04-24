package com.example.proindividual.models;

public class ReadyTraining {
    private String readyTrainingId;
    private String title;
    private String category;
    private String details;

    public ReadyTraining() {
    }

    public ReadyTraining(String readyTrainingId, String title, String category, String details) {
        this.readyTrainingId = readyTrainingId;
        this.title = title;
        this.category = category;
        this.details = details;
    }

    public String getReadyTrainingId() {
        return readyTrainingId;
    }

    public void setReadyTrainingId(String readyTrainingId) {
        this.readyTrainingId = readyTrainingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

