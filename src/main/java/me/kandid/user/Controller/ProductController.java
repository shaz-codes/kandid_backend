package me.kandid.user.Controller;

import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.kandid.user.Model.Product.ElastiProduct;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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


    @PostMapping("/")
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
                                         @RequestBody(required = false) ProductFilter filter) {
        Map<String, Object> map = new HashMap<>();
        SearchHits<ElastiProduct> prod = productService.getProducts(q, filter, PageRequest.of(pg, pgsize));
        ElasticsearchAggregations aggregatedData = ((ElasticsearchAggregations) prod.getAggregations());
        Map<String, Object> poop = new HashMap<>();
        assert aggregatedData != null;
        aggregatedData.aggregationsAsMap().forEach((s, a) -> {
            Map<String, Long> o =
                    a.aggregation().getAggregate().sterms().buckets().array().stream()
                     .collect(Collectors.toMap(bucket -> bucket.key()
                                                               .stringValue(), MultiBucketBase::docCount));
            poop.put(s, o);
        });
        map.put("product", prod.stream().map(SearchHit::getContent).collect(Collectors.toList()));
        map.put("filters", poop);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam("q") String q) {
        return new ResponseEntity<>(
                productService.autocomplete(q).stream().map(SearchHit::getContent).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
