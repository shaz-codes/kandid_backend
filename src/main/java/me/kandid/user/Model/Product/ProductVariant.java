package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Schema(description = "Product variant representing different sizes and inventory for a product")
public class ProductVariant {
    @Id
    @Schema(description = "Unique SKU (Stock Keeping Unit) identifier", example = "PROD001-M")
    private String sku;

    @Column(nullable = false)
    @Schema(description = "Size of the product variant", example = "M", required = true)
    private String size;

    @Column(nullable = false)
    @Schema(description = "Product code this variant belongs to", example = "PROD001", required = true)
    private String productCode;

    @Schema(description = "Available stock quantity for this variant", example = "50", minimum = "0")
    private int availableStock;
}
