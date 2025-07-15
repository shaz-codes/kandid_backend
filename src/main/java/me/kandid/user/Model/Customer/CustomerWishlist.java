package me.kandid.user.Model.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import me.kandid.user.Model.Product.Product;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Entity
@Schema(name = "Customer Wishlist", description = "Wishlist of products for a customer")
public class CustomerWishlist {
    @Id
    @Schema(name = "Phone", description = "Phone number of the customer", example = "9161086557")
    private long customerPhone;
    @ManyToMany
    @JoinTable(name = "wishlist_product")
    @Schema(name = "Products", description = "List of products in the customer's wishlist")
    private List<Product> products;
}
