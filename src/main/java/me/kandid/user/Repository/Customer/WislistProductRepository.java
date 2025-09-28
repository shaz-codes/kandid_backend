package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.WishlistProducts;
import me.kandid.user.Model.Customer.WishlistProductsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface WislistProductRepository extends JpaRepository<WishlistProducts, WishlistProductsId> {
    Set<WishlistProducts> getAllByCustomer_Phone(long customerPhone);
}
