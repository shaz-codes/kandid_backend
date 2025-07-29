package me.kandid.user.Service;

import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.SearchableProduct;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product getProduct(String code);

    SearchResponse<SearchableProduct> getProducts(String s, ProductFilter filter, Pageable pageable);

    SearchResponse<SearchableProduct> autocomplete(String q);
}
