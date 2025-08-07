package me.kandid.user.Model.Responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import me.kandid.user.Model.Product.Types.SearchableProduct;

import java.util.List;
import java.util.Map;

@Schema(
        title = "Search Result",
        description = "Returns search result along with filters"
)
@Data
public class SearchResult {
    @Schema(
            description = "List of Products"
    )
    List<SearchableProduct> products;
    @Schema(
            description = "Available filters with items"
    )
    Map<String, Map<String, Long>> filters;
    @Schema(
            description = "Total number of items present in the search result use it to calculate the appropriate pg " +
                    "and pgsize"
    )
    long total;
}
