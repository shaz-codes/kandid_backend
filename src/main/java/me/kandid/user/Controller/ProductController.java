package me.kandid.user.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
@Tag(
        name = "Products",
        description = "APIs for retrieving product information"
)
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "all")
    @Operation(
            summary = "Get All Products",
            description = "Retrieve a complete list of all available products in the system. This endpoint returns all " +
                    "active products with their details including pricing, brand information, and product variants."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved all products",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Product.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Products Found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = productService.getProducts();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{code}")
    @Operation(
            summary = "Get Product by Code",
            description = "Retrieve a specific product using its unique product code. This endpoint is typically used to display detailed product information on individual product pages."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found and returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product with the specified code was not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<Product> getProductById(
            @Parameter(
                    description = "Unique product code identifier",
                    example = "PROD001",
                    required = true
            ) @PathVariable String code) {
        Product product = productService.getProduct(code);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping("filtered")
    @Operation(
            summary = "Filter Products",
            description = "Retrieve products based on various filter criteria such as material, color, occasion, price range, and other product attributes. This endpoint supports multiple filter options to help users find products matching their preferences."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Products matching the filter criteria found",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Product.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No products found matching the specified filter criteria",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<?> filterProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Filter criteria for product search",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductFilter.class)
                    )
            ) @RequestBody ProductFilter filter) {
        List<Product> list = productService.getProductsByFilter(filter);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
