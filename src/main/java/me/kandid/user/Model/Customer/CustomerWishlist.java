package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Types.Product;

import java.util.Set;

@Data
@Entity
@Schema(
        title = "Customer Wishlist",
        description = "Wishlist of products for a customer"
)
public class CustomerWishlist {
    @Id
    @Schema(
            title = "Phone",
            description = "Phone number of the customer",
            example = "9161086557"
    )
    private long customerPhone;
    @ManyToMany
    @JoinTable(
            name = "wishlist_product",
            joinColumns = @JoinColumn(
                    name = "phone",
                    referencedColumnName = "customerPhone"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_code",
                    referencedColumnName = "code"
            )
    )
    @Schema(
            title = "Products",
            description = "List of products in the customer's wishlist"
    )
    private Set<Product> products;
}
