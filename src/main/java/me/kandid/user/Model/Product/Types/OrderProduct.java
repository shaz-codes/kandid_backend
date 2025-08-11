package me.kandid.user.Model.Product.Types;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Visuals;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Schema(
        title = "Ordered product",
        description = "Items in a customer's order"
)
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private int returned;

    private String customerPhone;

    @Schema(
            description = "Unique product sku identifier",
            example = "PROD001-WHITE-XL",
            required = true
    )
    private String sku;

    @Schema(
            description = "Product Code for the product",
            example = "PROD001-WHITE"
    )
    private String productCode;

    @Schema(
            description = "Product name/title",
            example = "Cotton Casual Shirt"
    )
    private String name;

    @Schema(
            description = "Detailed description of the product",
            example = "A comfortable cotton shirt perfect for casual occasions"
    )
    private String description;

    @Schema(description = "Brand name")
    private String brand;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_item")
    @Schema(description = "Visual assets (images, videos) for the product")
    private List<Visuals> visuals;

    //    @Transient
    @Schema(
            description = "Current selling price in paise (calculated dynamically with discounts)",
            example = "2999"
    )
    private double pricePaid;

    @Schema(
            description = "Maximum Retail Price in paise",
            example = "3999"
    )
    private double mrp;

    @Schema(
            description = "Primary color",
            example = "Blue"
    )
    private String color;

    @Column(
            nullable = false,
            updatable = false
    )
    private int quantity;

    public static OrderProduct fromProduct(Product product, String sku, int quantity) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.sku = sku;
        orderProduct.productCode = product.getCode();
        orderProduct.name = product.getName();
        orderProduct.description = product.getDescription();
        orderProduct.color = product.getColor();
        orderProduct.mrp = product.getMrp();
        orderProduct.visuals = product.getVisuals();
        orderProduct.brand = product.getBrand().getDisplayName();
        orderProduct.pricePaid = product.getSellingPrice();
        product.getDiscounts().forEach(d -> {
            Instant now = Instant.now();
            if (d.getDiscountedFrom().isBefore(now) && d.getDiscountedTo().isAfter(now)) {
                orderProduct.setPricePaid(d.getDiscountedPrice());
            }
        });
        orderProduct.quantity = quantity;
        return orderProduct;
    }
}
