package me.kandid.user.Service;

import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Model.Product.Types.SearchableProduct;
import me.kandid.user.Model.Requests.OrderRequest;
import me.kandid.user.Model.Responses.SearchResult;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.URL;

public interface ProductService {

    Product getProduct(String code, long phone);

    SearchResult getProducts(String s, ProductFilter filter, Pageable pageable, long phone);

    SearchResponse<SearchableProduct> autocomplete(String q);

    URL checkout_prepaid(long customerPhone, OrderRequest orderRequest) throws IOException;

    URL checkout_confirmed(long customerPhone, long orderId) throws IOException;

    URL checkout_cod(long customerPhone, OrderRequest orderRequest) throws IOException;
}
