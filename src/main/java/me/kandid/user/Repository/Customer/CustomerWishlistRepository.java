package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.CustomerWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerWishlistRepository extends JpaRepository<CustomerWishlist, Integer> {
    CustomerWishlist getByCustomerPhone(long customerPhone);

    @Query(
            value = "SELECT COUNT(*) > 0 FROM wishlist_product wp WHERE wp.phone = :phone AND wp.product_code = :code",
            nativeQuery = true
    )
    Integer isProductInWishlist(@Param("phone") long phone, @Param("code") String code);
}
