package me.kandid.user.Model.Customer;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
public class CustomerAddress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long customerPhone;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String nickname;
    private long phone;
    private double latitude;
    private double longitude;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;
    @UpdateTimestamp
    private Instant modified;
}
