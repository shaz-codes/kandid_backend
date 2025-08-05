package me.kandid.user.Repository;

import me.kandid.user.Model.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Product getProductByCode(String code);

    List<Product> getProductsByCodeContaining(String code);

    List<Product> getProductsByCodeContainingAndActive(String code, Boolean active);
}
