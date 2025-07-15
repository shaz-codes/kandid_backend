package me.kandid.user.Controller.Customer;

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
    public ResponseEntity<List<CustomerOrder>> getOrders(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CustomerOrder> orders = customerService.getAllCustomerOrders(phone);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerOrder> getAllOrders(@RequestHeader(name = "Authorization") String token, @PathVariable long id) {
        long phone = decodePhoneFromJWT(token);
        CustomerOrder order = customerService.getCustomerOrderById(id, phone);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}

