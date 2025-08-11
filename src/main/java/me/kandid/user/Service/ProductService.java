package me.kandid.user.Service;

import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Model.Product.Types.SearchableProduct;
import me.kandid.user.Model.Responses.SearchResult;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Product getProduct(String code, long phone);

    SearchResult getProducts(String s, ProductFilter filter, Pageable pageable, long phone);

    SearchResponse<SearchableProduct> autocomplete(String q);

    Map<String, List<Map<String, String>>> getAllCategoriesWithImages(String gender);
}
