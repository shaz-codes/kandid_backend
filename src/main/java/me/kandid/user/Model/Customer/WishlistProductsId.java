package me.kandid.user.Model.Customer;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistProductsId implements Serializable {
    private long phone;
    private String product_code;
}
