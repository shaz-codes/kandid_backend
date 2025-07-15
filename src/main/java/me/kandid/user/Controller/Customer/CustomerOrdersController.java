package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Create order, return a order, generate payment link

@RequestMapping("orders")
@RestController
@CrossOrigin
@Tag(name = "Customer Orders", description = "Endpoints for managing customer orders")
public class CustomerOrdersController {
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
    @Operation(summary = "Get All Orders", description = "Retrieves all customer orders using JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(arraySchema = @Schema(implementation = CustomerOrder.class)))),
            @ApiResponse(responseCode = "404", description = "No orders found for the customer", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<List<CustomerOrder>> getOrders(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CustomerOrder> orders = customerService.getAllCustomerOrders(phone);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Order by ID", description = "Retrieves a specific customer order by its ID using JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerOrder.class))),
            @ApiResponse(responseCode = "404", description = "Order not found for the customer", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<CustomerOrder> getAllOrders(@RequestHeader(name = "Authorization") String token,
            @PathVariable long id) {
        long phone = decodePhoneFromJWT(token);
        CustomerOrder order = customerService.getCustomerOrderById(id, phone);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
