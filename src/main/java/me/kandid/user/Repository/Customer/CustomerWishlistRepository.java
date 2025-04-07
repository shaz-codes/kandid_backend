package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.CustomerWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerWishlistRepository extends JpaRepository<CustomerWishlist, Integer> {
    CustomerWishlist getByCustomerPhone(long customerPhone);
}
