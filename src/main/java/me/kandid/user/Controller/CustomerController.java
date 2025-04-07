package me.kandid.user.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Customer.*;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RequestMapping("/")
@RestController
@CrossOrigin
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    long decodePhoneFromJWT(String token) {
        DecodedJWT jwt = JWT
                .require(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")))
                .withIssuer("Kandid User")
                .build().verify(token);

        return Long.parseLong(jwt.getSubject());
    }

    @GetMapping("otp/send")
    public ResponseEntity<String> otpSend(@RequestParam long phone) {
        String id;
        try {
            id = customerService.sendOTP(phone);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(id,HttpStatus.OK);
    }

    @GetMapping("otp/verify")
    public ResponseEntity<String> otpVerify(@RequestParam String id,@RequestParam String code) {
        String phone;
        try {
            phone = customerService.verifyOTP(id, code);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String jwt = JWT.create()
                .withIssuer("Kandid User")
                .withSubject(phone)
                .withExpiresAt(Instant.now().plusSeconds(2678400))
                .sign(Algorithm.HMAC256(System.getenv("ENCRYPT_KEY_KANDID")));

        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }

    @GetMapping("profile")
    public ResponseEntity<Customer> profile(@RequestHeader(name = "Authorization") String token) {

        Customer customer = customerService.getCustomer(decodePhoneFromJWT(token));
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }

    @PostMapping("profile/save")
    public ResponseEntity<Customer> saveProfile(@RequestHeader(name = "Authorization") String token, @RequestBody Customer customer) {
        customer.setPhone(decodePhoneFromJWT(token));
        return new ResponseEntity<>(customerService.saveCustomer(customer),HttpStatus.OK);
    }

    @PutMapping("profile/update")
    public ResponseEntity<Customer> updateProfile(@RequestHeader(name = "Authorization") String token, @RequestBody Customer customer) {
        customer.setPhone(decodePhoneFromJWT(token));
        return new ResponseEntity<>(customerService.updateCustomer(customer),HttpStatus.OK);
    }

    @GetMapping("address/all")
    public ResponseEntity<List<CustomerAddress>> getAllAddress(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CustomerAddress> list = customerService.getAllCustomerAddress(phone);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("address/{id}")
    public ResponseEntity<CustomerAddress> getAddress(@PathVariable("id") long id,@RequestHeader(name = "Authorization") String token ) {
        CustomerAddress add = customerService.getCustomerAddress(id);
        if(add == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (decodePhoneFromJWT(token) == add.getCustomerPhone()) {
            return new ResponseEntity<>(add,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("address/save")
    public ResponseEntity<CustomerAddress> saveAddress(@RequestHeader(name = "Authorization") String token, @RequestBody CustomerAddress customerAddress) {
        long phone = decodePhoneFromJWT(token);
        customerAddress.setCustomerPhone(phone);
        return new ResponseEntity<>(customerService.addCustomerAddress(customerAddress),HttpStatus.OK);
    }

    @PutMapping("address/update")
    public ResponseEntity<CustomerAddress> updateAddress(@RequestHeader(name = "Authorization") String token, @RequestBody CustomerAddress customerAddress) {
        long phone = decodePhoneFromJWT(token);
        customerAddress.setCustomerPhone(phone);
        return new ResponseEntity<>(customerService.updateCustomerAddress(customerAddress),HttpStatus.OK);
    }

    @DeleteMapping("address/delete")
    public ResponseEntity<String> deleteAddress(@RequestHeader(name = "Authorization") String token, @RequestBody CustomerAddress customerAddress) {
        long phone = decodePhoneFromJWT(token);
        customerAddress.setCustomerPhone(phone);
        customerService.deleteCustomerAddress(customerAddress);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @GetMapping("wishlist")
    public ResponseEntity<CustomerWishlist> getWishlist(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        CustomerWishlist wish = customerService.getCustomerWishlist(phone);
        if (wish == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wish,HttpStatus.OK);
    }

    @PostMapping("wishlist/add")
    public ResponseEntity<CustomerWishlist> addToWishlist(@RequestHeader(name = "Authorization") String token, @RequestBody String productCode) {
        long phone = decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addToCustomerWishlist(phone,productCode),HttpStatus.OK);
    }

    @DeleteMapping("wishlist/delete")
    public ResponseEntity<CustomerWishlist> removeFromWishlist(@RequestHeader(name = "Authorization") String token, @RequestBody String productCode) {
        long phone = decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.removeFromCustomerWishlist(phone,productCode),HttpStatus.OK);
    }

    @GetMapping("orders")
    public ResponseEntity<List<CustomerOrder>> getOrders(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CustomerOrder> orders = customerService.getAllCustomerOrders(phone);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PostMapping("orders")
    public ResponseEntity<CustomerOrder> getAllOrders(@RequestHeader(name = "Authorization") String token, @RequestBody long orderId ) {
        long phone = decodePhoneFromJWT(token);
        CustomerOrder order = customerService.getCustomerOrderById(orderId,phone);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("cart")
    public ResponseEntity<List<CartItems>> getCart(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CartItems> cart = customerService.getCustomerCart(phone);
        if (cart.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @PostMapping("cart/add")
    public ResponseEntity<List<CartItems>> addToCart(@RequestHeader(name = "Authorization") String token, @RequestBody CartItems cartItems) {
        long phone = decodePhoneFromJWT(token);
        return new ResponseEntity<>(customerService.addToCustomerCart(phone,cartItems),HttpStatus.OK);
    }

    @DeleteMapping("cart/remove")
    public ResponseEntity<List<CartItems>> removeFromCart(@RequestHeader(name = "Authorization") String token, @RequestBody CartItems cartItems) {
        long phone = decodePhoneFromJWT(token);
        customerService.removeFromCustomerCart(phone,cartItems);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
