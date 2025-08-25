package me.kandid.user.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Model.Product.Types.SearchableProduct;
import me.kandid.user.Model.Responses.SearchResult;
import me.kandid.user.Service.ProductService;
import me.kandid.user.Utils.Utils;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://kandid.me")
@RequestMapping("/products")
@Tag(
        name = "Products",
        description = "APIs for retrieving product information"
)
public class ProductController {
    @Autowired
    private ProductService productService;

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
            ) @PathVariable String code, @RequestHeader(
                    name = HttpHeaders.AUTHORIZATION,
                    required = false
            ) String token) {
        long phone = -1;
        if (token != null) {
            phone = Utils.decodePhoneFromJWT(token);
        }
        Product product = productService.getProduct(code, phone);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(@RequestParam(required = false) String gender) {

        return new ResponseEntity<>(productService.getAllCategoriesWithImages(gender), HttpStatus.OK);
    }


    @Operation(
            summary = "Search with filters and paging"
    )
    @PostMapping()
    public ResponseEntity<?> getProducts(@RequestParam(
                                                 value = "search",
                                                 required = false
                                         ) String q,
                                         @RequestParam(
                                                 value = "pg"
                                         )
                                         int pg,
                                         @RequestParam(
                                                 value = "pgsize"
                                         )
                                         int pgsize,
                                         @RequestBody(required = false) ProductFilter filter, @RequestHeader(
                    name = HttpHeaders.AUTHORIZATION,
                    required = false
            ) String token) {
        long phone = -1;
        if (token != null) {
            phone = Utils.decodePhoneFromJWT(token);
        }
        SearchResult prod = productService.getProducts(q, filter, PageRequest.of(pg, pgsize),
                phone);
        return new ResponseEntity<>(prod,
                HttpStatus.OK);
    }

    @Operation(
            summary = "Autocomplete",
            description = "for autocomplete in the searchbar"
    )
    @GetMapping("autocomplete")
    public ResponseEntity<List<SearchableProduct>> autoComplete(@RequestParam("q") String q) {
        return new ResponseEntity<>(productService.autocomplete(q).hits().hits().stream().map(Hit::source).toList(),
                HttpStatus.OK);
    }
}
