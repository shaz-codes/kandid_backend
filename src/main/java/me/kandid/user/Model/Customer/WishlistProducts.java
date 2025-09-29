package me.kandid.user.Model.Customer;

import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Types.Product;

import java.time.Instant;

@Entity
@Data
public class WishlistProducts {
    @EmbeddedId
    private WishlistProductsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("phone")
    @JoinColumn(name = "phone")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("product_code")
    @JoinColumn(name = "product_code")
    private Product product;

    private Instant addedAt;
}

