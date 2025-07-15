package me.kandid.user.Repository;

import me.kandid.user.Model.Product.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    List<ProductVariant> findAllBySkuContaining(String sku);

    List<ProductVariant> findAllByProductCode(String productCode);

    void deleteAllByProductCode(String productCode);

    ProductVariant findBySku(String sku);
}
