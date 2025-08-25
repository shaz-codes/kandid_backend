package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Model.Requests.OrderRequest;
import me.kandid.user.Service.CustomerService;
import me.kandid.user.Service.OrderService;
import me.kandid.user.Service.ProductService;
import me.kandid.user.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

// TODO: Create order, return a order, generate payment link

@RequestMapping("/")
@RestController
@CrossOrigin
@Tag(
        name = "Customer Orders",
        description = "Endpoints for managing customer orders"
)
public class CustomerOrdersController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("orders")
    @Operation(
            summary = "Get All Orders",
            description = "Retrieves all customer orders using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Orders retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CustomerOrder.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No orders found for the customer",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<List<CustomerOrder>> getOrders(@RequestHeader(name = "Authorization") String token) {
        long phone = Utils.decodePhoneFromJWT(token);
        List<CustomerOrder> orders = customerService.getAllCustomerOrders(phone).reversed();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("orders/{id}")
    @Operation(
            summary = "Get Order by ID",
            description = "Retrieves a specific customer order by its ID using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerOrder.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found for the customer",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<CustomerOrder> getorder(@RequestHeader(name = "Authorization") String token,
                                                  @PathVariable String id) {
        long phone = Utils.decodePhoneFromJWT(token);
        CustomerOrder order = customerService.getCustomerOrderById(Long.parseLong(id.replace("ORD", "")), phone);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Operation(
            summary = "Cancel for orders less than 2 minutes old",
            description = "Needs orderid",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "sends a location to redirect to",
                            headers = @Header(name = "Location")
                    )
            }
    )
    @GetMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@RequestHeader(name = "Authorization") String token,
                                         @PathVariable String id) throws
            IOException {
        long phone = Utils.decodePhoneFromJWT(token);
        CustomerOrder order = orderService.cancelOrder(phone,
                Long.parseLong(id.replace("ORD", "")));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Operation(
            summary = "Checkout for already confirmed orders",
            description = "Needs orderid",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "sends a location to redirect to",
                            headers = @Header(name = "Location")
                    )
            }
    )
    @GetMapping("/orders/{id}/pay")
    public ResponseEntity<?> checkout_confirmed(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable String id) throws
            IOException {
        long phone = Utils.decodePhoneFromJWT(token);
        URL url = orderService.checkout_confirmed(phone, Long.parseLong(id.replace("ORD", "")));
        return new ResponseEntity<>(Map.of("location", url.toString()), HttpStatus.FOUND);
    }

    @Operation(
            summary = "Checkout prepaid",
            description = "Needs Customer Address, Items (orderItems)",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "sends a location to redirect to",
                            headers = @Header(name = "Location")
                    )
            }
    )
    @PostMapping("/checkout/prepaid")
    public ResponseEntity<?> checkout_prepaid(@RequestBody OrderRequest order,
                                              @RequestHeader(name = "Authorization") String token)
            throws Exception {
        long phone = Utils.decodePhoneFromJWT(token);
        URL url = orderService.checkout_prepaid(phone, order);
        return new ResponseEntity<>(Map.of("location", url.toString()), HttpStatus.FOUND);
    }

    @Operation(
            summary = "Checkout cod",
            description = "Needs Customer Address, Items (orderItems)",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "sends a location to redirect to",
                            headers = @Header(name = "Location")
                    )
            }
    )
    @PostMapping("/checkout/cod")
    public ResponseEntity<?> checkout_cod(@RequestBody OrderRequest order,
                                          @RequestHeader(name = "Authorization") String token)
            throws Exception {
        long phone = Utils.decodePhoneFromJWT(token);
        URL url = orderService.checkout_cod(phone, order);
        return new ResponseEntity<>(Map.of("location", url.toString()), HttpStatus.FOUND);
    }


}
