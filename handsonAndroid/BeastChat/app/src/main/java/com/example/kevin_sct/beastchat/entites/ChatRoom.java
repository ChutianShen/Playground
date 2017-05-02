package com.example.kevin_sct.beastchat.entites;

/**
 * Created by kevin_sct on 5/1/17.
 */

public class ChatRoom {
    private String friendPicture;
    private String friendName;
    private String friendEmail;
    private String lastMessage;
    private String lastMessageSenderEmail;
    private boolean lastMessageRead;
    private boolean sentLastMessage;


    public ChatRoom() {
    }


    public ChatRoom(String friendPicture, String friendName, String friendEmail, String lastMessage, String lastMessageSenderEmail, boolean lastMessageRead, boolean sentLastMessage) {
        this.friendPicture = friendPicture;
        this.friendName = friendName;
        this.friendEmail = friendEmail;
        this.lastMessage = lastMessage;
        this.lastMessageSenderEmail = lastMessageSenderEmail;
        this.lastMessageRead = lastMessageRead;
        this.sentLastMessage = sentLastMessage;
    }

    public String getLastMessageSenderEmail() {
        return lastMessageSenderEmail;
    }

    public String getFriendPicture() {
        return friendPicture;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isLastMessageRead() {
        return lastMessageRead;
    }

    public boolean isSentLastMessage() {
        return sentLastMessage;
    }

}
