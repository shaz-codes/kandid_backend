package me.kandid.user.Model.Product;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
public class Discount {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private Long discountedPrice;
    private Instant discountedFrom;
    private Instant discountedTo;
}
