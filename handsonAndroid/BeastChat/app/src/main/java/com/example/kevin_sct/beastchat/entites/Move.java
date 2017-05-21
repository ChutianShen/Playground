package com.example.kevin_sct.beastchat.entites;

/**
 * Created by kevin_sct on 5/20/17.
 */

public class Move {
    private String moveId;
    private String moveText;
    private String moveSenderEmail;

    public Move() {
    }

    public Move(String moveId, String moveText, String moveSenderEmail) {
        this.moveId = moveId;
        this.moveText = moveText;
        this.moveSenderEmail = moveSenderEmail;
    }

    public String getMoveId() {
        return moveId;
    }

    public String getMoveText() {
        return moveText;
    }

    public String getMoveSenderEmail() {
        return moveSenderEmail;
    }
}
