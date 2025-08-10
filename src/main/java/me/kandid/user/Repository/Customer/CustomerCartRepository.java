package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Product.Types.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCartRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findAllByCustomerPhone(long customerPhone);

    void deleteAllByCustomerPhone(long customerPhone);
}
