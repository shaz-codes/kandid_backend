package me.kandid.user.Controller.Customer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Customer Authentication",
        description = "Endpoints for customer authentication using OTP (One Time Password)"
)
public class CustomerAuthController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("send")
    @Operation(
            summary = "Send OTP",
            description = "Sends a One Time Password (OTP) to the customer's phone number for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OTP sent successfully",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error, contact me about it",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
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
    @Operation(
            summary = "Verify OTP",
            description = "Verifies the One Time Password (OTP) sent to the customer's phone number and returns a JWT" +
                    " token, based on the value of the accountExists we take the details of the user"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OTP verified successfully, JWT token returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OtpVerifyResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error, contact me about it",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OtpVerifyResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> otpVerify(@RequestParam String id, @RequestParam String code) {
        long phone;
        try {
            phone = customerService.verifyOTP(id, code);
        } catch (Exception e) {
            return new ResponseEntity<>(new OtpVerifyResponse(null, e.getMessage(), false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jwt = JWT.create()
                        .withIssuer("Kandid User")
                        .withSubject(String.valueOf(phone))
                        .withExpiresAt(Instant.now().plusSeconds(2678400))
//                        .sign(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")));
                        .sign(Algorithm.HMAC256("i0vriteFm08yJZxXrmuWiY7hsDDZhIcW"));

        return new ResponseEntity<>(new OtpVerifyResponse(jwt, null, customerService.customerExist(phone)),
                HttpStatus.OK);
    }
}
