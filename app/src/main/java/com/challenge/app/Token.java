package com.challenge.app;

import java.io.Serializable;

public class Token implements Serializable{
    private String content;
    private Long expiryTime;

    public Token() {
        content = null;
        expiryTime = null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String newContent) {
        content = newContent;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

}
