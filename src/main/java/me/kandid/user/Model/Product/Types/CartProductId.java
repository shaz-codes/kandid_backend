package me.kandid.user.Model.Product.Types;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CartProductId implements Serializable {
    private long customerPhone;
    private String productId;
}