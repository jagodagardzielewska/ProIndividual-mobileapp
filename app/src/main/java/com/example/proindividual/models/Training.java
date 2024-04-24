package com.example.proindividual.models;

public class Training {
    private String trainingId;
    private String coachUserId;
    private String playerUserId;
    private String title;
    private String category;
    private String date;
    private String details;
    private Ratings ratings;
    private boolean completed;
    private long duration;

    public Training() {
    }
    public Ratings getRatings() {
        return ratings;
    }

    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    public static class Ratings {
        private double satisfaction;
        private double effort;

        public Ratings() {
        }

        public Ratings(int satisfaction, int effort) {
            this.satisfaction = satisfaction;
            this.effort = effort;
        }

        public double getSatisfaction() {
            return satisfaction;
        }

        public void setSatisfaction(int satisfaction) {
            this.satisfaction = satisfaction;
        }

        public double getEffort() {
            return effort;
        }

        public void setEffort(int effort) {
            this.effort = effort;
        }
    }


        public Training(String trainingId, String coachUserId, String playerUserId, String title, String category, String date, String details, boolean completed) {
        this.trainingId = trainingId;
        this.coachUserId = coachUserId;
        this.playerUserId = playerUserId;
        this.title = title;
        this.category = category;
        this.date = date;
        this.details = details;
        this.completed = completed = false;
        this.duration = duration;

    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
