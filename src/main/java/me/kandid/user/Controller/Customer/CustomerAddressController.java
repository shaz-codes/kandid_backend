package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Customer.CustomerAddress;
import me.kandid.user.Service.CustomerService;
import me.kandid.user.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("address")
@RestController
@CrossOrigin
@Tag(
        name = "Customer Addresses",
        description = "Operations for managing customer addresses"
)
public class CustomerAddressController {
    @Autowired
    private CustomerService customerService;

    @GetMapping()
    @Operation(
            summary = "Get All Addresses",
            description = "Retrieve all of the customer's addresses"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address found",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CustomerAddress.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Addresses found",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<List<CustomerAddress>> getAllAddress(@RequestHeader(name = "Authorization") String token) {
        long phone = Utils.decodePhoneFromJWT(token);
        List<CustomerAddress> list = customerService.getAllCustomerAddress(phone);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get Address by specific id",
            description = "Get specific address of a customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found address",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerAddress.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No address found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Something happened",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<CustomerAddress> getAddress(@PathVariable long id,
                                                      @RequestHeader(name = "Authorization") String token) {
        CustomerAddress add = customerService.getCustomerAddress(id);
        if (add == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (Utils.decodePhoneFromJWT(token) == add.getCustomer().getPhone()) {
            return new ResponseEntity<>(add, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("save")
    @Operation(
            summary = "Save a new address",
            description = "Saves new address to customer's address list",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address saved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerAddress.class)
                            )
                    )
            }
    )
    public ResponseEntity<CustomerAddress> saveAddress(@RequestHeader(name = "Authorization") String token,
                                                       @RequestBody CustomerAddress customerAddress) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addCustomerAddress(customerAddress, phone), HttpStatus.OK);
    }

    @PutMapping("update")
    @Operation(
            summary = "Update customer address",
            description = "Updates customer address, send whole object and make sure to include id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerAddress.class)
                            )
                    )
            }
    )
    public ResponseEntity<CustomerAddress> updateAddress(@RequestHeader(name = "Authorization") String token,
                                                         @RequestBody CustomerAddress customerAddress) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.updateCustomerAddress(customerAddress, phone), HttpStatus.OK);
    }

    @DeleteMapping("delete")
    @Operation(
            summary = "delete a customer address",
            description = "deletes a customer's address, only the id is needed in the body",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<String> deleteAddress(@RequestHeader(name = "Authorization") String token,
                                                @RequestBody CustomerAddress customerAddress) {
        long phone = Utils.decodePhoneFromJWT(token);
        customerService.deleteCustomerAddress(customerAddress, phone);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
