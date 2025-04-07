package me.kandid.user.Model.Customer;

import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Product;

@Entity
@Data
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long customerPhone;
    private String productSku;
    @Transient
    private Product product;
    private int quantity;
}
