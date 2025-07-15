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
@Schema(name = "Customer Object", description = "Details about the customer")
public class Customer {
    @Id
    @Schema(name = "Phone Number", example = "9161086557")
    private long phone;
    @Schema(name = "Name", example = "Samriddh Verma")
    private String name;
    @Schema(name = "Email", example = "samriddh@kandid.me")
    private String email;
    @Schema(name = "Profile Picture", description = "Unique identifier for the profile picture stored in aws bucket, if present", example = "https://aws.com/bucket/sdadmkasdma")
    private String profilePicture;
    @Schema(name = "Gender", example = "MALE")
    private String gender;
    @Schema(name = "Not Implemented")
    private String referralCode;
    @Schema(name = "Not Implemented")
    private String referredBy;
    @Schema(name = "Internal Tracker for login id")
    private String verificationId;
    @Schema(name = "Device Tokens", description = "FCM tokens for logged in devices", example = "hhjkasdmka.djasda.dasda")
    private List<String> devices;
}
