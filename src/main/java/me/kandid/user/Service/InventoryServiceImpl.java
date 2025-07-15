package me.kandid.user.Service;

import me.kandid.user.Model.Product.ProductVariant;
import me.kandid.user.Repository.Customer.CustomerOrdersRepository;
import me.kandid.user.Repository.Customer.OrderItemsRepository;
import me.kandid.user.Repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private CustomerOrdersRepository customerOrderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Override
    public ProductVariant createProductVariantOfProductCodeAndSize(String productCode, String size) {
        ProductVariant variant = new ProductVariant();
        variant.setSku(productCode + "_" + size);
        return productVariantRepository.save(variant);
    }

    @Override
    public List<ProductVariant> getAllVariantsByProductCode(String productCode) {
        return productVariantRepository.findAllByProductCode(productCode);
    }

    @Override
    public List<ProductVariant> getAllVariantsByProductCodeWithInventory(String productCode) {
        List<ProductVariant> variants = productVariantRepository.findAllByProductCode(productCode);
        // TODO: inventory with orders and shit
        int ordered = 0;
        int returned = 0;
        variants.forEach(variant -> {
        });
        return variants;
    }

    @Override
    public void deleteAllVariantsByProductCode(String productCode) {
        productVariantRepository.deleteAllByProductCode(productCode);
    }

    @Override
    public ProductVariant getProductVariantBySku(String sku) {
        return productVariantRepository.findBySku(sku);
    }
}
