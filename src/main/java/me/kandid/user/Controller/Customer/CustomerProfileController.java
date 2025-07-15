package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Customer.Customer;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Add endpoint to receive FCM token and add to list of devices

@RestController
@RequestMapping("profile")
@CrossOrigin
@Tag(name = "Customer Profile", description = "Endpoints for managing customer profile operations")
public class CustomerProfileController {
    @Autowired
    private CustomerService customerService;

    long decodePhoneFromJWT(String token) {
        DecodedJWT jwt = JWT
                .require(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")))
                .withIssuer("Kandid User")
                .build().verify(token);

        return Long.parseLong(jwt.getSubject());
    }

    @GetMapping()
    @Operation(summary = "Get Customer Profile", description = "Retrieves the customer's profile using JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer profile not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<Customer> profile(@RequestHeader(name = "Authorization") String token) {

        Customer customer = customerService.getCustomer(decodePhoneFromJWT(token));
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("save")
    @Operation(summary = "Save Customer Profile", description = "Saves the customer's profile using JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile saved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid customer data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<Customer> saveProfile(@RequestHeader(name = "Authorization") String token,
            @RequestBody Customer customer) {
        customer.setPhone(decodePhoneFromJWT(token));
        return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.OK);
    }

    @PutMapping("update")
    @Operation(summary = "Update Customer Profile", description = "Updates the customer's profile using JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid customer data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<Customer> updateProfile(@RequestHeader(name = "Authorization") String token,
            @RequestBody Customer customer) {
        customer.setPhone(decodePhoneFromJWT(token));
        return new ResponseEntity<>(customerService.updateCustomer(customer), HttpStatus.OK);
    }

}
