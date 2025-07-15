package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Schema(description = "Product entity representing a fashion item with all its attributes and specifications")
public class Product {
    @Id
    @Schema(description = "Unique product code identifier", example = "PROD001", required = true)
    private String code;

    @Schema(description = "Product name/title", example = "Cotton Casual Shirt")
    private String name;

    @Schema(description = "Detailed description of the product", example = "A comfortable cotton shirt perfect for casual occasions")
    private String description;

    @Schema(description = "Whether the product is active in the system", example = "true")
    private Boolean active;

    @Schema(description = "Whether the product is available for purchase (derived from inventory)", example = "true")
    private Boolean available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @Schema(description = "Brand information for this product")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visuals_id")
    @Schema(description = "Visual assets (images, videos) for the product")
    private Visuals visuals;

    @Transient
    @Schema(description = "Current selling price in paise (calculated dynamically with discounts)", example = "2999")
    private double sellingPrice;

    @Schema(description = "Maximum Retail Price in paise", example = "3999")
    private double mrp;

    @Schema(description = "Primary product category", example = "Clothing")
    private String category;

    @Schema(description = "Sub-category classification", example = "Shirts")
    private String subCategory;

    @OneToMany(fetch = FetchType.LAZY)
    @Schema(description = "Available inventory variants (sizes and stock)")
    private List<ProductVariant> inventory;

    // Product specification details
    @Schema(description = "Material composition", example = "Cotton")
    private String material;

    @Schema(description = "Primary color", example = "Blue")
    private String color;

    @Schema(description = "Suitable occasions", example = "Casual")
    private String occasion;

    @Schema(description = "Sleeve type", example = "Full Sleeve")
    private String sleeve;

    @Schema(description = "Fit type", example = "Regular Fit")
    private String fit;

    @Schema(description = "Specific fit type classification", example = "Classic")
    private String fitType;

    @Schema(description = "Pattern design", example = "Solid")
    private String pattern;

    @Schema(description = "Neckline style", example = "Round Neck")
    private String neckline;

    @Schema(description = "Closure mechanism", example = "Button")
    private String closure;

    @Schema(description = "Type of closure", example = "Front Button")
    private String closureType;

    @Schema(description = "Rise style for bottoms", example = "Mid Rise")
    private String riseStyle;

    @Schema(description = "Fashion style category", example = "Contemporary")
    private String style;

    @Schema(description = "Aesthetic appeal category", example = "Minimalist")
    private String aesthetic;

    @Schema(description = "Current fashion trend", example = "Sustainable Fashion")
    private String trend;
}
