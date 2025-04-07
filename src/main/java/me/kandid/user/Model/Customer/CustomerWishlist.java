package me.kandid.user.Model.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import me.kandid.user.Model.Product.Product;

import java.util.List;

@Data
@Entity
public class CustomerWishlist {
    @Id
    private long customerPhone;
    @ManyToMany
    @JoinTable(name = "wishlist_product")
    private List<Product> products;
}
