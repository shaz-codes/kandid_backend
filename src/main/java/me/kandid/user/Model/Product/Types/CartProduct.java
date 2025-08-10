package me.kandid.user.Model.Product.Types;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Visuals;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Schema(
        title = "Cart Items",
        description = "Product with it's quantity"
)
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            title = "Randomly generated id",
            description = "Randomly generated id for internal tracking"
    )
    private long id;
    @Schema(
            title = "Customer's Phone",
            description = "Customer's Phone, used internally"
    )
    private long customerPhone;

    @Schema(
            description = "Unique product sku identifier",
            example = "PROD001-WHITE-XL",
            required = true
    )
    private String sku;

    @Column(
            nullable = false
    )
    private int quantity;

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

    public static CartProduct fromProduct(Product product, String sku, int quantity, long id) {
        CartProduct item = new CartProduct();
        item.id = id;
        item.sku = sku;
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
}
