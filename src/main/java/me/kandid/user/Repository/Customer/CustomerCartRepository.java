package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Product.Types.CartProduct;
import me.kandid.user.Model.Product.Types.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCartRepository extends JpaRepository<CartProduct, CartProductId> {
    List<CartProduct> findAllByCustomer_Phone(long customerPhone);

    void deleteAllByCustomer_Phone(long customerPhone);

    CartProduct findCartProductByCustomer_PhoneAndSku(long customerPhone, String sku);
}
