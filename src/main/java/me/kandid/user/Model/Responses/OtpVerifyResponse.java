package me.kandid.user.Model.Responses;

public class OtpVerifyResponse {
    String jwt;
    String error;
    boolean accountExists;

    public OtpVerifyResponse(String jwt, String error, Boolean accountExists) {
        this.jwt = jwt;
        this.error = error;
        this.accountExists = accountExists;
    }
}
