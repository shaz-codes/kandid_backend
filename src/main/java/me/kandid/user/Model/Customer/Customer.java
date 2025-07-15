package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;


/*
 * TODO: need a DTO for this without the FCM token as the token is for internal use only,
 *  remove tokens on logout and add a endpoint for token addition
 */

@Entity
@Data
@Schema(title = "Customer Object", description = "Details about the customer")
public class Customer {
    @Id
    @Schema(title = "Phone Number", example = "9161086557")
    private long phone;
    @Schema(title = "Name", example = "Samriddh Verma")
    private String name;
    @Schema(title = "Email", example = "samriddh@kandid.me")
    private String email;
    @Schema(title = "Profile Picture", description = "Unique identifier for the profile picture stored in aws bucket, if present", example = "https://aws.com/bucket/sdadmkasdma")
    private String profilePicture;
    @Schema(title = "Gender", example = "MALE")
    private String gender;
    @Schema(title = "Not Implemented")
    private String referralCode;
    @Schema(title = "Not Implemented")
    private String referredBy;
    @Schema(title = "Internal Tracker for login id")
    private String verificationId;
    @Schema(title = "Device Tokens", description = "FCM tokens for logged in devices", example = "hhjkasdmka.djasda.dasda")
    private List<String> devices;
}
