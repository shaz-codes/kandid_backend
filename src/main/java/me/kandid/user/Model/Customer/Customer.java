package me.kandid.user.Model.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    private long phone;
    private String name;
    private String email;
    private String profilePicture;
    private String gender;
    private String referralCode;
    private String referredBy;
}
