package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@Schema(description = "Brand entity representing fashion brand information")
public class Brand {
    @Id
    @Schema(description = "Unique brand identifier", example = "1")
    private long id;

    @Schema(description = "Brand display name", example = "PUMA")
    private String displayName;

    @Schema(description = "Brand description", example = "Premium fashion brand for modern lifestyle")
    private String description;

    @Schema(description = "Full legal name of the brand", example = "PUMA India Ltd")
    private String fullName;

    @Schema(description = "URL to the brand logo", example = "https://example.com/logos/kandid.png")
    private String logoUrl;

    @Schema(description = "Office address of the brand", example = "123 Fashion Street, Mumbai, India")
    private String officeAddress;

    @Schema(description = "Store address of the brand", example = "456 Shopping Mall, Delhi, India")
    private String storeAddress;

    @Schema(description = "GST identification number", example = "27ABCDE1234F1Z5")
    private String gstId;

    @Schema(description = "Official website URL", example = "https://in.puma.com")
    private String website;
}
