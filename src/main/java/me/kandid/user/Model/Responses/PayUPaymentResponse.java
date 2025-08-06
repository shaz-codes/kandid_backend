package me.kandid.user.Model.Responses;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Data
public class PayUPaymentResponse {
    private String mihpayid;
    private String mode;
    private String status;
    private String unmappedstatus;
    private String key;
    private String txnid;
    private String amount;
    private String discount;
    private String net_amount_debit;
    private LocalDateTime addedon;
    private String productinfo;
    private String firstname;
    private String lastname;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String email;
    private String phone;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String udf6;
    private String udf7;
    private String udf8;
    private String udf9;
    private String udf10;
    private String hash;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String payment_source;
    private String pa_name;
    private String PG_TYPE;
    private String bank_ref_num;
    private String bankcode;
    private String error;
    private String error_Message;

    public static PayUPaymentResponse fromHashMap(HashMap<String, String> map) {
        PayUPaymentResponse res = new PayUPaymentResponse();
        res.setMihpayid(map.get("mihpayid"));
        res.setMode(map.get("mode"));
        res.setStatus(map.get("status"));
        res.setUnmappedstatus(map.get("unmappedstatus"));
        res.setKey(map.get("key"));
        res.setTxnid(map.get("txnid"));
        res.setAmount(map.get("amount"));
        res.setDiscount(map.get("discount"));
        res.setNet_amount_debit(map.get("net_amount_debit"));

        // Parse addedon datetime
        String addedonStr = map.get("addedon");
        if (addedonStr != null && !addedonStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            res.setAddedon(LocalDateTime.parse(addedonStr, formatter));
        }

        res.setProductinfo(map.get("productinfo"));
        res.setFirstname(map.get("firstname"));
        res.setLastname(map.get("lastname"));
        res.setAddress1(map.get("address1"));
        res.setAddress2(map.get("address2"));
        res.setCity(map.get("city"));
        res.setState(map.get("state"));
        res.setCountry(map.get("country"));
        res.setZipcode(map.get("zipcode"));
        res.setEmail(map.get("email"));
        res.setPhone(map.get("phone"));

        res.setUdf1(map.get("udf1"));
        res.setUdf2(map.get("udf2"));
        res.setUdf3(map.get("udf3"));
        res.setUdf4(map.get("udf4"));
        res.setUdf5(map.get("udf5"));
        res.setUdf6(map.get("udf6"));
        res.setUdf7(map.get("udf7"));
        res.setUdf8(map.get("udf8"));
        res.setUdf9(map.get("udf9"));
        res.setUdf10(map.get("udf10"));

        res.setHash(map.get("hash"));

        res.setField1(map.get("field1"));
        res.setField2(map.get("field2"));
        res.setField3(map.get("field3"));
        res.setField4(map.get("field4"));
        res.setField5(map.get("field5"));
        res.setField6(map.get("field6"));
        res.setField7(map.get("field7"));
        res.setField8(map.get("field8"));
        res.setField9(map.get("field9"));

        res.setPayment_source(map.get("payment_source"));
        res.setPa_name(map.get("pa_name"));
        res.setPG_TYPE(map.get("PG_TYPE"));
        res.setBank_ref_num(map.get("bank_ref_num"));
        res.setBankcode(map.get("bankcode"));
        res.setError(map.get("error"));
        res.setError_Message(map.get("error_Message"));

        return res;
    }
}

