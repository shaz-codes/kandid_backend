package me.kandid.user.Model.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class PayUVerifyResponse {
    private int status;
    private String msg;

    @JsonProperty("transaction_details")
    private Map<String, PayUTransactionDetailsResponse> transactionDetails;

}
