package me.kandid.user.Model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ProductVariants {
    @Id
    private String sku;
    @Column(nullable = false)
    private String size;
    private boolean available;
//    available will get final updated when
//    the last order's return period gets over

    private int stock;
}
