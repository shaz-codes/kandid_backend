package me.kandid.user.Service;

import me.kandid.user.Model.Product.ElastiProduct;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface ProductService {

    Product getProduct(String code);

    SearchHits<ElastiProduct> getProducts(String s, ProductFilter filter, Pageable pageable);

    SearchHits<ElastiProduct> autocomplete(String q);
}
