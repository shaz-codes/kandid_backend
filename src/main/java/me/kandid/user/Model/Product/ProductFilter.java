package me.kandid.user.Model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Filter criteria for searching products based on various attributes")
public class ProductFilter {
    @Schema(
            description = "Filter by material types",
            example = "[\"cotton\", \"silk\", \"polyester\"]"
    )
    private String[] material;

    @Schema(
            description = "Filter by colors",
            example = "[\"red\", \"blue\", \"black\"]"
    )
    private String[] color;

    @Schema(
            description = "Filter by suitable occasions",
            example = "[\"casual\", \"formal\", \"party\"]"
    )
    private String[] occasion;

    @Schema(
            description = "Filter by sleeve types",
            example = "[\"full\", \"half\", \"sleeveless\"]"
    )
    private String[] sleeve;

    @Schema(
            description = "Filter by fit types",
            example = "[\"regular\", \"slim\", \"loose\"]"
    )
    private String[] fit;

    @Schema(
            description = "Filter by specific fit type classifications",
            example = "[\"classic\", \"modern\"]"
    )
    private String[] fitType;

    @Schema(
            description = "Filter by pattern designs",
            example = "[\"solid\", \"striped\", \"printed\"]"
    )
    private String[] pattern;

    @Schema(
            description = "Filter by neckline styles",
            example = "[\"round\", \"v-neck\", \"collar\"]"
    )
    private String[] neckline;

    @Schema(
            description = "Filter by closure mechanisms",
            example = "[\"button\", \"zip\", \"tie\"]"
    )
    private String[] closure;

    @Schema(
            description = "Filter by closure types",
            example = "[\"front\", \"back\", \"side\"]"
    )
    private String[] closureType;

    @Schema(
            description = "Filter by rise styles for bottoms",
            example = "[\"low\", \"mid\", \"high\"]"
    )
    private String[] riseStyle;

    @Schema(
            description = "Filter by fashion style categories",
            example = "[\"contemporary\", \"traditional\", \"vintage\"]"
    )
    private String[] style;

    @Schema(
            description = "Filter by aesthetic appeal categories",
            example = "[\"minimalist\", \"bold\", \"elegant\"]"
    )
    private String[] aesthetic;

    @Schema(
            description = "Filter by current fashion trends",
            example = "[\"sustainable\", \"retro\", \"futuristic\"]"
    )
    private String[] trend;

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
