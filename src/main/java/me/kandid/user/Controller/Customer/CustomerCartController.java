package me.kandid.user.Controller.Customer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Customer.CartItems;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("cart")
public class CustomerCartController {
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
    public ResponseEntity<List<CartItems>> getCart(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CartItems> cart = customerService.getCustomerCart(phone);
        if (cart.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<List<CartItems>> addToCart(@RequestHeader(name = "Authorization") String token, @RequestBody CartItems cartItems) {
        long phone = decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addToCustomerCart(phone, cartItems), HttpStatus.OK);
    }

    @DeleteMapping("remove")
    public ResponseEntity<List<CartItems>> removeFromCart(@RequestHeader(name = "Authorization") String token, @RequestBody CartItems cartItems) {
        long phone = decodePhoneFromJWT(token);
        customerService.removeFromCustomerCart(phone, cartItems);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
