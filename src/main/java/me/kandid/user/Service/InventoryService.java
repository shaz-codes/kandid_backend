package me.kandid.user.Service;

import me.kandid.user.Model.Product.ProductVariant;

import java.util.List;

public interface ProductVariantsService {

    ProductVariant createProductVariantOfProductCodeAndSize(String productCode, String size);

    List<ProductVariant> getAllVariantsByProductCode(String productCode);

    void deleteAllVariantsByProductCode(String productCode);

    ProductVariant getProductVariantBySku(String sku);
}
