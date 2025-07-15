package me.kandid.user.Controller.Customer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Customer.CustomerWishlist;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("wishlist")
public class CustomerWishlistController {
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
    public ResponseEntity<CustomerWishlist> getWishlist(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        CustomerWishlist wish = customerService.getCustomerWishlist(phone);
        if (wish == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wish, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<CustomerWishlist> addToWishlist(@RequestHeader(name = "Authorization") String token, @RequestBody String productCode) {
        long phone = decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addToCustomerWishlist(phone, productCode), HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<CustomerWishlist> removeFromWishlist(@RequestHeader(name = "Authorization") String token, @RequestBody String productCode) {
        long phone = decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.removeFromCustomerWishlist(phone, productCode), HttpStatus.OK);
    }
}
