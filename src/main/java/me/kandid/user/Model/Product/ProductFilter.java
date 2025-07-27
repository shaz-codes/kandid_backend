package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Filter criteria for searching products based on various attributes")
public class ProductFilter {
    @Schema(
            description = "Filter by material types",
            example = "[\"cotton\", \"silk\", \"polyester\"]"
    )
    private List<String> material;

    @Schema(
            description = "Filter by colors",
            example = "[\"red\", \"blue\", \"black\"]"
    )
    private List<String> color;

    @Schema(
            description = "Filter by suitable occasions",
            example = "[\"casual\", \"formal\", \"party\"]"
    )
    private List<String> occasion;

    @Schema(
            description = "Filter by sleeve types",
            example = "[\"full\", \"half\", \"sleeveless\"]"
    )
    private List<String> sleeve;

    @Schema(
            description = "Filter by fit types",
            example = "[\"regular\", \"slim\", \"loose\"]"
    )
    private List<String> fit;

    @Schema(
            description = "Filter by Category",
            example = "[\"Clothing\", \"Topwear\", \"futuristic\"]"
    )
    private List<String> category;

    @Schema(
            description = "Filter by Sub Category",
            example = "[\"sustainable\", \"retro\", \"futuristic\"]"
    )
    private List<String> subCategory;

    @Schema(
            description = "Filter by specific fit type classifications",
            example = "[\"classic\", \"modern\"]"
    )
    private List<String> fitType;

    @Schema(
            description = "Filter by pattern designs",
            example = "[\"solid\", \"striped\", \"printed\"]"
    )
    private List<String> pattern;

    @Schema(
            description = "Filter by neckline styles",
            example = "[\"round\", \"v-neck\", \"collar\"]"
    )
    private List<String> neckline;

    @Schema(
            description = "Filter by closure mechanisms",
            example = "[\"button\", \"zip\", \"tie\"]"
    )
    private List<String> closure;

    @Schema(
            description = "Filter by closure types",
            example = "[\"front\", \"back\", \"side\"]"
    )
    private List<String> closureType;

    @Schema(
            description = "Filter by rise styles for bottoms",
            example = "[\"low\", \"mid\", \"high\"]"
    )
    private List<String> riseStyle;

    @Schema(
            description = "Filter by fashion style categories",
            example = "[\"contemporary\", \"traditional\", \"vintage\"]"
    )
    private List<String> style;

    @Schema(
            description = "Filter by aesthetic appeal categories",
            example = "[\"minimalist\", \"bold\", \"elegant\"]"
    )
    private List<String> aesthetic;

    @Schema(
            description = "Filter by current fashion trends",
            example = "[\"sustainable\", \"retro\", \"futuristic\"]"
    )
    private List<String> trend;

    @Schema(
            description = "Filter by Brands",
            example = "[\"NIKE\", \"XYZ\", \"PUMA\"]"
    )
    private List<String> brand;

    @Schema(
            description = "Minimum price filter",
            example = "1999",
            minimum = "0"
    )
    private Double priceFrom;

    @Schema(
            description = "Maximum price filter",
            example = "5999",
            minimum = "0"
    )
    private Double priceTo;
}
