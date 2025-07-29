package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;


@Data
public class SearchableProduct {

    public static SearchableProduct fromProduct(Product product) {
        SearchableProduct np = new SearchableProduct();
        np.sellingPrice = product.getSellingPrice();
        product.getDiscounts().forEach(d -> {
            Instant now = Instant.now();
            if (d.getDiscountedFrom().isBefore(now) && d.getDiscountedTo().isAfter(now)) {
                np.sellingPrice = d.getDiscountedPrice();
            }
        });
        np.code = product.getCode();
        np.name = product.getName();
        np.description = product.getDescription();
        np.aesthetic = product.getAesthetic();
        np.category = product.getCategory();
        np.brand = product.getBrand().getDisplayName();
        np.material = product.getMaterial();
        np.color = product.getColor();
        np.closure = product.getClosure();
        np.closureType = product.getClosureType();
        np.fit = product.getFit();
        np.fitType = product.getFitType();
        np.mrp = product.getMrp();
        np.neckline = product.getNeckline();
        np.pattern = product.getPattern();
        np.riseStyle = product.getRiseStyle();
        np.style = product.getStyle();
        np.trend = product.getTrend();
        np.occasion = product.getOccasion();
        np.sleeve = product.getSleeve();
        np.subCategory = product.getSubCategory();
        return np;
    }

    @Id
    @Schema(
            description = "Unique product code identifier",
            example = "PROD001",
            required = true
    )
    private String code;

//    private String name;

    @Schema(
            description = "Product name/title",
            example = "Cotton Casual Shirt"
    )
    private String name;

    private List<Visuals> visuals;

    @Schema(
            description = "Detailed description of the product",
            example = "A comfortable cotton shirt perfect for casual occasions"
    )
    private String description;

    @Schema(
            description = "Whether the product is available for purchase (derived from inventory)",
            example = "true"
    )
    private Boolean available;


    @Schema(description = "Brand information for this product")
    private String brand;

    @Schema(
            description = "Current selling price in paise (calculated dynamically with discounts)",
            example = "2999"
    )
    private double sellingPrice;

    @Schema(
            description = "Maximum Retail Price in paise",
            example = "3999"
    )
    private double mrp;

    @Schema(
            description = "Primary product category",
            example = "Clothing"
    )
    private String category;

    @Schema(
            description = "Sub-category classification",
            example = "Shirts"
    )
    private String subCategory;

    // Product specification details
    @Schema(
            description = "Material composition",
            example = "Cotton"
    )
    private String material;

    @Schema(
            description = "Primary color",
            example = "Blue"
    )
    private String color;

    @Schema(
            description = "Suitable occasions",
            example = "Casual"
    )
    private String occasion;

    @Schema(
            description = "Sleeve type",
            example = "Full Sleeve"
    )
    private String sleeve;

    @Schema(
            description = "Fit type",
            example = "Regular Fit"
    )
    private String fit;

    @Schema(
            description = "Specific fit type classification",
            example = "Classic"
    )
    private String fitType;

    @Schema(
            description = "Pattern design",
            example = "Solid"
    )
    private String pattern;

    @Schema(
            description = "Neckline style",
            example = "Round Neck"
    )
    private String neckline;

    @Schema(
            description = "Closure mechanism",
            example = "Button"
    )
    private String closure;

    @Schema(
            description = "Type of closure",
            example = "Front Button"
    )
    private String closureType;

    @Schema(
            description = "Rise style for bottoms",
            example = "Mid Rise"
    )
    private String riseStyle;

    @Schema(
            description = "Fashion style category",
            example = "Contemporary"
    )
    private String style;

    @Schema(
            description = "Aesthetic appeal category",
            example = "Minimalist"
    )
    private String aesthetic;

    @Schema(
            description = "Current fashion trend",
            example = "Sustainable Fashion"
    )
    private String trend;
}
