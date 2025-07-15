package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "otp-vid-temp")
@Data
@Schema(title = "OtpLogin", description = "Temporary model for OTP login verification")
public class OtpLogin {
    @Id
    @Schema(title = "Verification ID", description = "Unique identifier for the OTP verification process", example = "123456")
    private long verificationId;
    @Schema(title = "Phone Number", description = "Phone number of the customer attempting to log in", example = "9161086557")
    private long phone;
}
