package me.kandid.user.Model.Employee;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DeliveryPartner {
    @Id
    private long phone;
    private String username;
    private String name;
    private String address;
}
