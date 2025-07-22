package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Customer.CustomerWishlist;
import me.kandid.user.Service.CustomerService;
import me.kandid.user.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("wishlist")
@Tag(
        name = "Customer Wishlist",
        description = "Endpoints for managing customer wishlist operations"
)
public class CustomerWishlistController {
    @Autowired
    private CustomerService customerService;

    @GetMapping()
    @Operation(
            summary = "Get Wishlist",
            description = "Retrieves the customer's wishlist using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Wishlist retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerWishlist.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Wishlist not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<CustomerWishlist> getWishlist(@RequestHeader(name = "Authorization") String token) {
        long phone = Utils.decodePhoneFromJWT(token);
        CustomerWishlist wish = customerService.getCustomerWishlist(phone);
        if (wish == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wish, HttpStatus.OK);
    }

    @PostMapping("add")
    @Operation(
            summary = "Add to Wishlist",
            description = "Adds a product to the customer's wishlist using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product added to wishlist successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerWishlist.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request, invalid product code",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<CustomerWishlist> addToWishlist(@RequestHeader(name = "Authorization") String token,
                                                          @RequestBody String productCode) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addToCustomerWishlist(phone, productCode), HttpStatus.OK);
    }

    @DeleteMapping("delete")
    @Operation(
            summary = "Remove from Wishlist",
            description = "Removes a product from the customer's wishlist using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product removed from wishlist successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerWishlist.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found in wishlist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<CustomerWishlist> removeFromWishlist(@RequestHeader(name = "Authorization") String token,
                                                               @RequestBody String productCode) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.removeFromCustomerWishlist(phone, productCode), HttpStatus.OK);
    }
}
