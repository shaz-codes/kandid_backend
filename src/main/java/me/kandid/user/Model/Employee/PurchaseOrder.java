package me.kandid.user.Model.Employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PurchaseOrder {
    @Id
    private int id;
    private String status;
}
