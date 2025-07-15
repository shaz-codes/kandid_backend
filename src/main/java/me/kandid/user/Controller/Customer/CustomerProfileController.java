package me.kandid.user.Controller.Customer;

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
    public ResponseEntity<Customer> profile(@RequestHeader(name = "Authorization") String token) {

        Customer customer = customerService.getCustomer(decodePhoneFromJWT(token));
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("save")
    public ResponseEntity<Customer> saveProfile(@RequestHeader(name = "Authorization") String token, @RequestBody Customer customer) {
        customer.setPhone(decodePhoneFromJWT(token));
        return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<Customer> updateProfile(@RequestHeader(name = "Authorization") String token, @RequestBody Customer customer) {
        customer.setPhone(decodePhoneFromJWT(token));
        return new ResponseEntity<>(customerService.updateCustomer(customer), HttpStatus.OK);
    }


}
