package me.kandid.user.Model.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayUVerifyResponse {
    private int status;
    private String msg;

    @JsonProperty("transaction_details")
    private PayUTransactionDetailsResponse transactionDetails;

}
