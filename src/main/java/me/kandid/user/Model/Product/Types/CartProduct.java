package me.kandid.user.Model.Product.Types;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Customer.Customer;
import me.kandid.user.Model.Product.ProductVariant;
import me.kandid.user.Model.Product.Visuals;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Schema(
        title = "Cart Items",
        description = "Product with it's quantity"
)
public class CartProduct {
    @EmbeddedId
    @Schema(
            title = "Randomly generated id",
            description = "Randomly generated id for internal tracking"
    )
    private CartProductId id;
    @Schema(
            title = "Customer's Phone",
            description = "Customer's Phone, used internally"
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerPhone")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(
            description = "Unique product sku identifier",
            required = true
    )
    @MapsId("productId")
    private ProductVariant sku;

    @Column(
            nullable = false
    )
    private int quantity;

    @Transient
    private List<AvailSizes> similarSizes;

    @Transient
    @Schema(
            description = "Product Code for the product",
            example = "PROD001-WHITE"
    )
    private String productCode;

    @Transient
    @Schema(
            description = "Product name/title",
            example = "Cotton Casual Shirt"
    )
    private String name;

    @Transient
    @Schema(
            description = "Detailed description of the product",
            example = "A comfortable cotton shirt perfect for casual occasions"
    )
    private String description;

    @Transient
    @Schema(description = "Brand name")
    private String brand;

    @Transient
    @Schema(description = "Visual assets (images, videos) for the product")
    private List<Visuals> visuals;

    //    @Transient
    @Transient
    @Schema(
            description = "Current selling price in paise (calculated dynamically with discounts)",
            example = "2999"
    )
    private double sellingPrice;

    @Transient
    @Schema(
            description = "Maximum Retail Price in paise",
            example = "3999"
    )
    private double mrp;

    @Transient
    @Schema(
            description = "Primary color",
            example = "Blue"
    )
    private String color;

    @Transient
    @Schema(
            description = "Available Stock",
            example = "3"
    )
    private int availableStock;

    public static CartProduct fromProduct(Product product, String sku, int quantity, long customerPhone) {
        CartProduct item = new CartProduct();
        item.id = new CartProductId(customerPhone, sku);
        item.sku = new ProductVariant();
        item.similarSizes = product.getInventory().stream().map(i -> new AvailSizes(i.getSku(), i.getAvailableStock()))
                .toList();
        item.availableStock = product.getInventory().stream().filter(q -> Objects.equals(q.getSku(), sku)).toList()
                .getFirst().getAvailableStock();
        item.productCode = product.getCode();
        item.name = product.getName();
        item.description = product.getDescription();
        item.color = product.getColor();
        item.mrp = product.getMrp();
        item.visuals = product.getVisuals();
        item.brand = product.getBrand().getDisplayName();
        item.sellingPrice = product.getSellingPrice();
        product.getDiscounts().forEach(d -> {
            Instant now = Instant.now();
            if (d.getDiscountedFrom().isBefore(now) && d.getDiscountedTo().isAfter(now)) {
                item.sellingPrice = d.getDiscountedPrice();
            }
        });
        item.quantity = quantity;
        return item;
    }

    @Data
    public static class AvailSizes {
        String sku;
        int availableStock;

        public AvailSizes(String sku, int availableStock) {
            this.sku = sku;
            this.availableStock = availableStock;
        }
    }
}

