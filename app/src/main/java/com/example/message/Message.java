package com.example.message;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 14:51 2019/11/19
 */
public class Message {
    private String sender;
    private String reciver;

    private String type;
    private Boolean isRight;

    private String content;

    public Message(Boolean isRight, String content) {
        this.isRight = isRight;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }
}
