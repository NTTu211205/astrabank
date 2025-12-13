package com.example.astrabank.api.request;

public class LoginPhoneRequest {
    private String idToken;

    public LoginPhoneRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
