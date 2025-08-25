package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@Schema(title = "Address of customer")
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "for Internal Use")
    private long id;

    @Schema(
            title = "Name",
            example = "Samriddh"
    )
    private String customerName;

    @Schema(
            title = "Phone Number",
            description = "Phone Number of customer",
            example = "9161086557"
    )
    private long customerPhone;
    @Schema(
            title = "Address Line 1",
            example = "84D-137"
    )

    @Column(length = 511)
    private String line1;
    @Schema(
            title = "City",
            example = "Lucknow"
    )
    private String city;
    @Schema(
            title = "State",
            example = "Uttar Pradesh"
    )
    private String state;
    @Schema(
            title = "Country",
            example = "India"
    )
    private String country;
    @Schema(
            title = "Postal Code",
            example = "226001"
    )
    private String postalCode;
    @Schema(
            title = "Nickname",
            description = "Nickname for the address, used for quick identification",
            example = "Home"
    )
    private String nickname;
    @Schema(
            title = "Phone Number for address",
            description = "Phone number of the customer for this address, CAN BE DIFFERENT THAN THE CUSTOMER PHONE",
            example = "9161086557"
    )
    private String phone;
    @Schema(
            title = "Latitude",
            description = "Latitude for the address, used for location services",
            example = "26.8467"
    )
    private double latitude;
    @Schema(
            title = "Longitude",
            description = "Longitude for the address, used for location services",
            example = "80.9462"
    )
    private double longitude;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(
            title = "Creation Timestamp",
            description = "Timestamp when the address was created, can be used for sorting",
            example = "2023-10-01T12:00:00Z"
    )
    private Instant created;
    @UpdateTimestamp
    @Schema(
            title = "Modification Timestamp",
            description = "Timestamp when the address was last modified, can be used for sorting",
            example = "2023-10-01T12:00:00Z"
    )
    private Instant modified;
}
