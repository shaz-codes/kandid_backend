package me.kandid.user.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Service.PayUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "payU Controller",
        description = "handles checkout, success/failure webhooks and refunds/returns/cancellation, will get moved to" +
                " somewhere else prolly payments.kandid.me"
)
@RestController
@RequestMapping("/")
@CrossOrigin
public class PayUController {

    @Autowired
    PayUService payUService;

    @Operation(
            summary = "Checkout",
            description = "Needs Customer Address, Items (orderItems)"
    )
    @PostMapping("/checkout")
    public String checkout(@RequestBody CustomerOrder order, @RequestHeader(name = "Authorization") String token,
                           @RequestParam String surl, @RequestParam String furl) throws Exception {
//        order.setCustomerPhone(Utils.decodePhoneFromJWT(token));
// TODO: create custom order class
        return payUService.checkOut(order, surl, furl);
    }

    @Operation(
            summary = "To be used by payU to send successful order webhook",
            description = "To be used by payU to send successful order webhook"
    )
    @PostMapping("/success")
    public String success(@RequestBody String s) throws Exception {
//        TODO: implement order status checking
        return "hi";
    }

    @PostMapping("/failure")
    public String failure(@RequestBody String s) throws Exception {
//        TODO: implement order status checking
        return "hi";
    }
}
