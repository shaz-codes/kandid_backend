package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCartRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findAllByCustomerPhone(long customerPhone);
}
