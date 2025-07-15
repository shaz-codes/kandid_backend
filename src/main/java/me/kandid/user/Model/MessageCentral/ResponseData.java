package me.kandid.user.Model.MessageCentral;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@lombok.Data
public class ResponseData {
    private long verificationId;
    private long mobileNumber;
    private String verificationStatus;
    private String transactionId;
    private int responseCode;
    private String errorMessage;
    private String timeout;
}
