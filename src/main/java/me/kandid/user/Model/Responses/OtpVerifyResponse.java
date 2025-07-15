package me.kandid.user.Model.Responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "OTP Verification Response", description = "Response model for OTP verification containing JWT, error message, and account existence status")
public class OtpVerifyResponse {
    @Schema(description = "JWT token for authenticated user", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String jwt;

    @Schema(description = "Error message if any error occurred during OTP verification", example = "Invalid OTP")
    String error;

    @Schema(description = "Indicates if the account exists", example = "true")
    boolean accountExists;

    public OtpVerifyResponse(String jwt, String error, Boolean accountExists) {
        this.jwt = jwt;
        this.error = error;
        this.accountExists = accountExists;
    }
}
