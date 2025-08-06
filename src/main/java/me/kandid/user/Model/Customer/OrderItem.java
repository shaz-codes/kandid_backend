package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Model.Product.Visuals;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Schema(
        title = "Order Items",
        description = "Items in a customer's order"
)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private boolean returned;

    private long orderId;

    private String customerPhone;

    @Schema(
            description = "Unique product sku identifier",
            example = "PROD001-WHITE-XL",
            required = true
    )
    private String sku;

    @Schema(

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

    public static OrderItem fromProduct(Product product, String sku, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.sku = sku;
        orderItem.productCode = product.getCode();
        orderItem.name = product.getName();
        orderItem.description = product.getDescription();
        orderItem.color = product.getColor();
        orderItem.mrp = product.getMrp();
        orderItem.visuals = product.getVisuals();
        orderItem.brand = product.getBrand().getDisplayName();
        orderItem.pricePaid = product.getSellingPrice();
        product.getDiscounts().forEach(d -> {
            Instant now = Instant.now();
            if (d.getDiscountedFrom().isBefore(now) && d.getDiscountedTo().isAfter(now)) {
                orderItem.setPricePaid(d.getDiscountedPrice());
            }
        });
        orderItem.quantity = quantity;
        return orderItem;
    }
}
