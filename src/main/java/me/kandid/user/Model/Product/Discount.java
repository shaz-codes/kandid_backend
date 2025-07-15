package me.kandid.user.Model.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class Discount {
    @Id
    private long id;
    private String productCode;
    private double discountedPrice;
    private Instant discountedFrom;
    private Instant discountedTo;
}
