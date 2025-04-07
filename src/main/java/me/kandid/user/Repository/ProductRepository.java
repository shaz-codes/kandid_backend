package me.kandid.user.Repository;

import me.kandid.user.Model.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product getProductByCode(String code);
}
