package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Service.CustomerService;
import me.kandid.user.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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

    @GetMapping
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
                                    array = @ArraySchema(schema = @Schema(implementation = Product.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Wishlist not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<Set<Product>> getWishlist(@RequestHeader(name = "Authorization") String token) {
        long phone = Utils.decodePhoneFromJWT(token);
        Set<Product> wish = customerService.getCustomerWishlist(phone);
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
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<?> addToWishlist(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam String code) {
        long phone = Utils.decodePhoneFromJWT(token);
        customerService.addToCustomerWishlist(phone, code);
        return new ResponseEntity<>(HttpStatus.OK);
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
                            content = @Content()
                    )
            }
    )
    public ResponseEntity<?> removeFromWishlist(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam String code) {
        long phone = Utils.decodePhoneFromJWT(token);
        customerService.removeFromCustomerWishlist(phone, code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
