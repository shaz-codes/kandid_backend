package me.kandid.user.Model.MessageCentral;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@lombok.Data
public class Response {
    private String message;
    private int responseCode;
    private ResponseData data;
}
