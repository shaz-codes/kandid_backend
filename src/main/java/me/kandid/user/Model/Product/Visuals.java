package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@Schema(description = "Visual assets entity for storing product images and videos")
public class Visuals {
    @Id
    @Schema(description = "Unique visual asset identifier", example = "1")
    private long id;

    @Schema(description = "Type of visual asset", example = "image", allowableValues = { "image", "video", "360view" })
    private String type;

    @Schema(description = "URL or data URI for the visual asset", example = "https://example.com/images/product1.jpg")
    private String dataURL;
}
