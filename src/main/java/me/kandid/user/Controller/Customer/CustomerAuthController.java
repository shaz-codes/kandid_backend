package me.kandid.user.Controller.Customer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Responses.OtpVerifyResponse;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

// TODO: add endpoint to logout, AND REMOVE FCM TOKEN

@RestController
@RequestMapping("otp")
@CrossOrigin
public class CustomerAuthController {
    @Autowired
    private CustomerService customerService;

    long decodePhoneFromJWT(String token) {
        DecodedJWT jwt = JWT
                .require(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")))
                .withIssuer("Kandid User")
                .build().verify(token);

        return Long.parseLong(jwt.getSubject());
    }

    @GetMapping("send")
    public ResponseEntity<String> otpSend(@RequestParam String phone) {
        String id;
        try {
            id = customerService.sendOTP(phone);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("verify")
    public ResponseEntity<OtpVerifyResponse> otpVerify(@RequestParam String id, @RequestParam String code) {
        long phone;
        try {
            phone = customerService.verifyOTP(id, code);
        } catch (Exception e) {
            return new ResponseEntity<>(new OtpVerifyResponse(null, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jwt = JWT.create()
                .withIssuer("Kandid User")
                .withSubject(String.valueOf(phone))
                .withExpiresAt(Instant.now().plusSeconds(2678400))
                .sign(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")));

        return new ResponseEntity<>(new OtpVerifyResponse(jwt, null, customerService.customerExist(phone)), HttpStatus.OK);
    }
}
