package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import me.kandid.user.Model.Product.Types.CartProduct;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;


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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<CustomerAddress> addresses;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<CustomerOrder> orders;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Schema(
            title = "Products",
            description = "List of products in the customer's wishlist"
    )
    private Set<WishlistProducts> wishlist;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<CartProduct> cartProducts;
    private String gender;
    @Schema(title = "Not Implemented")
    private String referralCode;
    @Schema(title = "Not Implemented")
    private String referredBy;
    @Schema(title = "Internal Tracker for login id")
    private String verificationId;
    @Schema(title = "Device Tokens", description = "FCM tokens for logged in devices", example = "hhjkasdmka.djasda.dasda")
    private List<String> devices;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}
