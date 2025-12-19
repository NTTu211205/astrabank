package com.example.astrabank.models;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("amount")
    private String amount;

    public Notification(String title, String content, String amount) {
        this.title = title;
        this.content = content;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Notification() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
