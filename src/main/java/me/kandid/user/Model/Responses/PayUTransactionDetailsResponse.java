package me.kandid.user.Model.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayUTransactionDetailsResponse {
    private String mihpayid;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("bank_ref_num")
    private String bankRefNum;

    private String amt;

    @JsonProperty("transaction_amount")
    private String transactionAmount;

    private String txnid;

    @JsonProperty("additional_charges")
    private String additionalCharges;

    private String productinfo;
    private String firstname;
    private String bankcode;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;

    private String field2;
    private String field9;

    @JsonProperty("error_code")
    private String errorCode;

    private String addedon;

    @JsonProperty("payment_source")
    private String paymentSource;

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("error_Message")
    private String errorMessage;

    @JsonProperty("net_amount_debit")
    private double netAmountDebit;

    private String disc;
    private String mode;

    @JsonProperty("PG_TYPE")
    private String pgType;

    @JsonProperty("card_no")
    private String cardNo;

    private String status;
    private String unmappedstatus;

    @JsonProperty("Merchant_UTR")
    private String merchantUtr;

    @JsonProperty("Settled_At")
    private String settledAt;

    @JsonProperty("card_token")
    private String cardToken;

    @JsonProperty("payment_aggregator")
    private String paymentAggregator;

    private String offerAvailed;
}
