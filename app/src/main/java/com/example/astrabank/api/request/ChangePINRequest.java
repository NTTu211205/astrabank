package com.example.astrabank.api.request;

public class ChangePINRequest {
    private String userId;
    private String oldPin;
    private String newPin;
    private String confirmNewPin;

    public ChangePINRequest(String userId, String oldPin, String newPin, String confirmNewPin) {
        this.userId = userId;
        this.oldPin = oldPin;
        this.newPin = newPin;
        this.confirmNewPin = confirmNewPin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getConfirmNewPin() {
        return confirmNewPin;
    }

    public void setConfirmNewPin(String confirmNewPin) {
        this.confirmNewPin = confirmNewPin;
    }
}
