package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@Schema(name = "Address of customer")
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "for Internal Use")
    private long id;
    @Schema(name = "Phone Number", description = "Phone Number of customer", example = "9161086557")
    private long customerPhone;
    @Schema(name = "Address Line 1", example = "84D-137")
    private String line1;
    @Schema(name = "Address Line 2", example = "Chatameel")
    private String line2;
    @Schema(name = "City", example = "Lucknow")
    private String city;
    @Schema(name = "State", example = "Uttar Pradesh")
    private String state;
    @Schema(name = "Country", example = "India")
    private String country;
    @Schema(name = "Postal Code", example = "226001")
    private String postalCode;
    @Schema(name = "Nickname", description = "Nickname for the address, used for quick identification", example = "Home")
    private String nickname;
    @Schema(name = "Phone Number for address", description = "Phone number of the customer for this address, CAN BE DIFFERENT THAN THE CUSTOMER PHONE", example = "9161086557")
    private String phone;
    @Schema(name = "Latitude", description = "Latitude for the address, used for location services", example = "26.8467")
    private double latitude;
    @Schema(name = "Longitude", description = "Longitude for the address, used for location services", example = "80.9462")
    private double longitude;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(name = "Creation Timestamp", description = "Timestamp when the address was created, can be used for sorting", example = "2023-10-01T12:00:00Z")
    private Instant created;
    @UpdateTimestamp
    @Schema(name = "Modification Timestamp", description = "Timestamp when the address was last modified, can be used for sorting", example = "2023-10-01T12:00:00Z")
    private Instant modified;
}
