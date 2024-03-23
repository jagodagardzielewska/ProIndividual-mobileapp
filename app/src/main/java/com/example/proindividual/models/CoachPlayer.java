package com.example.proindividual.models;

import java.util.List;

public class CoachPlayer {
    private String coachUserId;
    private List<String> playerUserIds;


    public CoachPlayer() {
    }

    public CoachPlayer(String coachUserId, List<String> playerUserIds) {
        this.coachUserId = coachUserId;
        this.playerUserIds = playerUserIds;
    }


    public String getCoachUserId() {
        return coachUserId;
    }

    public List<String> getPlayerUserIds() {
        return playerUserIds;
    }


    public void setCoachUserId(String coachUserId) {
        this.coachUserId = coachUserId;
    }

    public void setPlayerUserIds(List<String> playerUserIds) {
        this.playerUserIds = playerUserIds;
    }
}
