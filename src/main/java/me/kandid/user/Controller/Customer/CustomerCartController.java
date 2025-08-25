package me.kandid.user.Controller.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Product.Types.CartProduct;
import me.kandid.user.Service.CustomerService;
import me.kandid.user.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://kandid.me")
@RestController
@RequestMapping("cart")
@Tag(
        name = "Customer Cart",
        description = "Endpoints for managing customer cart operations"
)
public class CustomerCartController {
    @Autowired
    private CustomerService customerService;

    @GetMapping()
    @Operation(
            summary = "Get Cart",
            description = "Retrieves the customer's cart items using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart items retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartProduct.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cart not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
            }
    )
    public ResponseEntity<List<CartProduct>> getCart(@RequestHeader(name = "Authorization") String token) {
        long phone = Utils.decodePhoneFromJWT(token);
        List<CartProduct> cart = customerService.getCustomerCart(phone);
        if (cart.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("add")
    @Operation(
            summary = "Add to Cart",
            description = "Adds items to the customer's cart using JWT token for authentication."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Items added to cart successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(arraySchema = @Schema(implementation = CartProduct.class))
                            )
                    ),
            }
    )
    public ResponseEntity<List<CartProduct>> addToCart(@RequestHeader(name = "Authorization") String token,
                                                       @RequestBody CartProduct cartProduct) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addToCustomerCart(phone, cartProduct), HttpStatus.OK);
    }

    @PutMapping("/edit")
    @Operation(
            summary = "Edit Cart Item",
            description = "Edit items from customer's cart"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated cart successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(arraySchema = @Schema(implementation = CartProduct.class))
                            )
                    ),
            }
    )
    public ResponseEntity<List<CartProduct>> editCart(@RequestHeader(name = "Authorization") String token,
                                                      @RequestBody CartProduct cartProduct) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.editCustomerCart(phone, cartProduct), HttpStatus.OK);
    }

    @DeleteMapping("remove")
    @Operation(
            summary = "Remove from Cart",
            description = "Removes items from the customer's cart using JWT token for authentication, returns updated cart."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Items removed from cart successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(arraySchema = @Schema(implementation = CartProduct.class))
                            )
                    ),
            }
    )
    public ResponseEntity<List<CartProduct>> removeFromCart(@RequestHeader(name = "Authorization") String token,
                                                            @RequestBody CartProduct cartProduct) {
        long phone = Utils.decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.removeFromCustomerCart(phone, cartProduct), HttpStatus.OK);
    }
}
