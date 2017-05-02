package com.example.kevin_sct.beastchat.entites;

/**
 * Created by kevin_sct on 4/30/17.
 */

public class Message {
    private String messageId;
    private String messageText;
    private String messageSenderEmail;
    private String messageSenderPicture;

    public Message() {
    }

    public Message(String messageId, String messageText, String messageSenderEmail, String messageSenderPicture) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.messageSenderEmail = messageSenderEmail;
        this.messageSenderPicture = messageSenderPicture;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageSenderEmail() {
        return messageSenderEmail;
    }

    public String getMessageSenderPicture() {
        return messageSenderPicture;
    }
}
