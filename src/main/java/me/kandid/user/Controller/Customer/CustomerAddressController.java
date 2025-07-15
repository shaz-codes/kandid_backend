package me.kandid.user.Controller.Customer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kandid.user.Model.Customer.CustomerAddress;
import me.kandid.user.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("address")
@RestController
@CrossOrigin
public class CustomerAddressController {
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
    public ResponseEntity<List<CustomerAddress>> getAllAddress(@RequestHeader(name = "Authorization") String token) {
        long phone = decodePhoneFromJWT(token);
        List<CustomerAddress> list = customerService.getAllCustomerAddress(phone);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerAddress> getAddress(@PathVariable long id,
            @RequestHeader(name = "Authorization") String token) {
        CustomerAddress add = customerService.getCustomerAddress(id);
        if (add == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (decodePhoneFromJWT(token) == add.getCustomerPhone()) {
            return new ResponseEntity<>(add, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("save")
    public ResponseEntity<CustomerAddress> saveAddress(@RequestHeader(name = "Authorization") String token,
            @RequestBody CustomerAddress customerAddress) {
        long phone = decodePhoneFromJWT(token);
        customerAddress.setCustomerPhone(phone);
        return new ResponseEntity<>(customerService.addCustomerAddress(customerAddress), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<CustomerAddress> updateAddress(@RequestHeader(name = "Authorization") String token,
            @RequestBody CustomerAddress customerAddress) {
        long phone = decodePhoneFromJWT(token);
        customerAddress.setCustomerPhone(phone);
        return new ResponseEntity<>(customerService.updateCustomerAddress(customerAddress), HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteAddress(@RequestHeader(name = "Authorization") String token,
            @RequestBody CustomerAddress customerAddress) {
        long phone = decodePhoneFromJWT(token);
        customerAddress.setCustomerPhone(phone);
        customerService.deleteCustomerAddress(customerAddress);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
