package com.kenzahn.zahn.model;

public class LoginModel {
    private LoginRes response;

    private String responseStatus;

    private String responseMessage;

    private String responseCode;

    public LoginRes getResponse() {
        return response;
    }

    public void setResponse(LoginRes response) {
        this.response = response;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", responseStatus = " + responseStatus + ", responseMessage = " + responseMessage + ", responseCode = " + responseCode + "]";
    }
}
