package me.kandid.user.Service;

import me.kandid.user.Exceptions.ProductNotFound;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Model.Product.Types.SearchableProduct;
import me.kandid.user.Model.Responses.SearchResult;
import me.kandid.user.Repository.Customer.CustomerWishlistRepository;
import me.kandid.user.Repository.ProductRepository;
import me.kandid.user.Utils.Utils;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.aggregations.StringTermsBucket;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private OpenSearchClient openSearchClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    CustomerWishlistRepository customerWishlistRepository;

    @Override
    public Product getProduct(String code, long phone) {
        System.out.println(code.split("-")[0]);
        List<Product> products = productRepository.getProductsByCodeContainingAndActive(code.split("-")[0], true);
        if (products.isEmpty()) {
            throw new ProductNotFound(code);
        }
        Product product = products.stream().filter(p -> p.getCode().equals(code)).findFirst()
                                  .orElse(products.getFirst());
        product.setColors(
                products.stream().map(p -> new Product.Colors(p.getCode(), p.getColor(), p.getColorCode())).toList());
        if (phone > 0) {
            product.setInWishlist(customerWishlistRepository.isProductInWishlist(phone, code) == 1);
        }
        return product;
    }

    @Override
    public SearchResult getProducts(String s, ProductFilter filter, Pageable pageable, long phone) {
        List<String> fields = new ArrayList<>();
        Field[] f = SearchableProduct.class.getDeclaredFields();
        for (Field field : f) {
            if (Modifier.isPrivate(field.getModifiers())) {
                fields.add(field.getName());
            }
        }

//        TODO: add price filtering
        fields.remove("available");
        fields.remove("sellingPrice");
        fields.remove("mrp");
        fields.remove("inWishlist");

        String indexName = "products";
        try {
            SearchRequest searchRequest =
                    new SearchRequest.Builder().index(indexName)
                                               .query(
                                                       q -> q.bool(
                                                               b -> {
                                                                   if (s != null && !s.trim().isEmpty()) {
                                                                       b.must(m -> m.multiMatch(
                                                                               mm -> mm.query(s).fuzziness("AUTO")));
                                                                   }
                                                                   if (filter != null) {
                                                                       b.filter(Utils.createFilter(filter));
                                                                   }
                                                                   b.must(m -> m.match(mm -> mm.field("active")
                                                                                               .query(FieldValue.of(
                                                                                                       true))));
                                                                   return b;
                                                               }))
                                               .size(pageable.getPageSize())
                                               .from(pageable.getPageNumber())
                                               .aggregations(
                                                       fields.stream()
                                                             .filter(v -> !v.equals("code") && !v.equals(
                                                                     "visuals") && !v.equals(
                                                                     "description") && !v.equals(
                                                                     "name") && !v.equals("colorName") && !v.equals(
                                                                     "active"))
                                                             .collect(
                                                                     Collectors.toMap((v) -> v,
                                                                             (v) -> Aggregation.of(
                                                                                     a -> a.terms(
                                                                                             ta -> ta.field(
                                                                                                     v.concat(
                                                                                                             ".keyword")))))))
                                               .sort(sort -> {
                                                   if (filter.getSortType() != null) {
                                                       if (filter.getSortType().equals("ASC")) {
                                                           return sort.field(
                                                                   a -> a.field("sellingPrice").order(SortOrder.Asc));
                                                       } else if (filter.getSortType().equals("DSC")) {
                                                           return sort.field(
                                                                   a -> a.field("sellingPrice").order(SortOrder.Desc));
                                                       }
                                                   }
                                                   return sort.score(ss -> ss.order(SortOrder.Asc));
                                               })
                                               .build();
            SearchResponse<SearchableProduct> p = openSearchClient.search(searchRequest, SearchableProduct.class);
            SearchResult sr = new SearchResult();
            List<SearchableProduct> products = p.hits().hits().stream().map(Hit::source).toList();
            if (phone > 0) {
                products = products.stream().peek(t -> t.setInWishlist(
                        customerWishlistRepository.isProductInWishlist(phone, t.getCode()) == 1)).toList();
            }
            sr.setProducts(products);
            sr.setFilters(p.aggregations().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                    o -> o.getValue().sterms().buckets().array().stream()
                          .collect(Collectors.toMap(StringTermsBucket::key,
                                  StringTermsBucket::docCount)))));
            assert p.hits().total() != null;
            sr.setTotal(p.hits().total().value());
            return sr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SearchResponse<SearchableProduct> autocomplete(String a) {
        String indexName = "products";
//        TODO: boost wishlisted items
        try {
            SearchRequest searchRequest =
                    new SearchRequest.Builder().index(indexName)
                                               .query(
                                                       q -> q.bool(b -> b.should(
                                                                                 s -> s.match(v -> v.field("name.autocomplete")
                                                                                                    .query(FieldValue.of(a))
                                                                                 ))
                                                                         .should(s -> s.match(v -> v.field("name")
                                                                                                    .query(FieldValue.of(
                                                                                                            a))
                                                                                                    .fuzziness(
                                                                                                            "AUTO")))
                                                                         .should(s -> s.matchPhrasePrefix(
                                                                                 v -> v.field("name").query(a)))))
                                               .size(30)
                                               .build();
            SearchResponse<SearchableProduct> p = openSearchClient.search(searchRequest, SearchableProduct.class);
            System.out.println(p.hits().hits().stream().map(Hit::score).toList());
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, List<Map<String, String>>> getAllCategoriesWithImages(String gender) {
        try {
            SearchResponse<Void> response = openSearchClient.search(s -> s
                            .size(0)
                            .query(q -> q.bool(b -> b.filter(f -> f.term(t -> t.field("gender").value(FieldValue.of(gender))))))
                            .aggregations("categories", a -> a
                                    .terms(t -> t.field("category.keyword"))
                                    .aggregations("subcategories", sa -> sa
                                            .terms(t -> t.field("subCategory.keyword"))
                                            .aggregations("sample_visual", img -> img
                                                    .topHits(th -> th
                                                            .size(1)
                                                            .source(src -> src.filter(f -> f.includes("visuals")))
                                                    )
                                            )
                                    )
                            ),
                    Void.class
            );
            Map<String, List<Map<String, String>>> pp = response.aggregations().get("categories").sterms().buckets()
                                                                .array()
                                                                .stream().collect(Collectors
                            .toMap(StringTermsBucket::key,
                                    sc -> sc.aggregations().get("subcategories").sterms().buckets().array().stream()
                                            .map(sca -> Map.of(sca.key(),
                                                    sca.aggregations().get("sample_visual").topHits().hits().hits()
                                                       .getFirst()
                                                       .source().toJson().asJsonObject().get("visuals").asJsonArray()
                                                       .getFirst().asJsonObject().get("dataURL").toString().replace(
                                                               "\"", "")))
                                            .toList()));

            System.out.println(pp);
            return pp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
