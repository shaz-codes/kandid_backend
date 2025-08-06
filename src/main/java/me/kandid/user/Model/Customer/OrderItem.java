package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Product.Visuals;

import java.util.List;

@Data
@Entity
@Schema(
        title = "Order Items",
        description = "Items in a customer's order"
)
public class OrderItems {
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
    @JoinColumn(name = "product_code")
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

}
