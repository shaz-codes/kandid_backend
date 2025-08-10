package me.kandid.user.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Service.PayUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(
        name = "payU Controller",
        description = "handles success/failure webhooks and refunds/returns/cancellation, will get moved to" +
                " somewhere else prolly payments.kandid.me"
)
@RestController
@RequestMapping("/")
@CrossOrigin
public class PayUController {

    @Autowired
    PayUService payUService;


    @Operation(
            summary = "To be used by payU to send successful order webhook",
            description = "To be used by payU to send successful order webhook",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "sends a location to redirect to",
                            headers = @Header(name = "Location")
                    )
            }
    )
    @PostMapping("/success")
    public ResponseEntity<?> success(@RequestBody String s) throws IOException {
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", payUService.success(s).toString()).build();
    }

    @Operation(
            summary = "To be used by payU to send failure order webhook",
            description = "To be used by payU to send failure order webhook",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "sends a location to redirect to",
                            headers = @Header(name = "Location")
                    )
            }
    )
    @PostMapping("/failure")
    public ResponseEntity<?> failure(@RequestBody String s) throws IOException {
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", payUService.failure(s).toString()).build();
    }
}
