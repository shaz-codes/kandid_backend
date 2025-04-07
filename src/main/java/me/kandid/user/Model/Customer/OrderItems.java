package me.kandid.user.Model.Customer;

import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Product;

@Data
@Entity
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;
    private String productSku;
    @Transient
    private Product product;
    private int quantity;
}
