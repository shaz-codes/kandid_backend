package me.kandid.user.Service;

import me.kandid.user.Configurations.SampleClient;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.SearchableProduct;
import me.kandid.user.Repository.ProductRepository;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.aggregations.TermsAggregation;
import org.opensearch.client.opensearch._types.query_dsl.Query;
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
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product getProduct(String code) {
        return productRepository.getProductByCode(code);
    }

    @Override
    public SearchResponse<SearchableProduct> getProducts(String s, ProductFilter filter, Pageable pageable) {
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

        String indexName = "products";
        try {
            OpenSearchClient openSearchClient = SampleClient.create();
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
                                                                       b.filter(createFilter(filter));
                                                                   }
                                                                   return b;
                                                               }))
                                               .size(pageable.getPageSize())
                                               .from(pageable.getPageNumber())
                                               .aggregations(
                                                       fields.stream()
                                                             .filter(v -> !v.equals("code") && !v.equals(
                                                                     "visuals") && !v.equals(
                                                                     "description") && !v.equals(
                                                                     "name"))
                                                             .collect(
                                                                     Collectors.toMap((v) -> v,
                                                                             (v) -> Aggregation.of(
                                                                                     a -> a.terms(TermsAggregation.of(
                                                                                             ta -> ta.field(
                                                                                                     v.concat(
                                                                                                             ".keyword"))))))))
                                               .build();
            SearchResponse<SearchableProduct> p = openSearchClient.search(searchRequest, SearchableProduct.class);
            System.out.println(p.hits().hits().stream().map(Hit::score).toList());
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SearchResponse<SearchableProduct> autocomplete(String a) {
        String indexName = "products";
        try {
            OpenSearchClient openSearchClient = SampleClient.create();
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


    private List<Query> createFilter(ProductFilter filter) {
        List<Query> filters = new ArrayList<>();

        if (filter.getAesthetic() != null) {
            filters.add(createFieldQ("aesthetic", filter.getAesthetic()));
        }
        if (filter.getMaterial() != null) {
            filters.add(createFieldQ("material", filter.getMaterial()));
        }
        if (filter.getColor() != null) {
            filters.add(createFieldQ("color", filter.getColor()));
        }
        if (filter.getOccasion() != null) {
            filters.add(createFieldQ("occasion", filter.getOccasion()));
        }
        if (filter.getSleeve() != null) {
            filters.add(createFieldQ("sleeve", filter.getSleeve()));
        }
        if (filter.getFit() != null) {
            filters.add(createFieldQ("fit", filter.getFit()));
        }
        if (filter.getFitType() != null) {
            filters.add(createFieldQ("fitType", filter.getFitType()));
        }
        if (filter.getPattern() != null) {
            filters.add(createFieldQ("pattern", filter.getPattern()));
        }
        if (filter.getNeckline() != null) {
            filters.add(createFieldQ("neckline", filter.getNeckline()));
        }
        if (filter.getClosure() != null) {
            filters.add(createFieldQ("closure", filter.getClosure()));
        }
        if (filter.getClosureType() != null) {
            filters.add(createFieldQ("closureType", filter.getClosureType()));
        }
        if (filter.getRiseStyle() != null) {
            filters.add(createFieldQ("riseStyle", filter.getRiseStyle()));
        }
        if (filter.getStyle() != null) {
            filters.add(createFieldQ("style", filter.getStyle()));
        }
        if (filter.getTrend() != null) {
            filters.add(createFieldQ("trend", filter.getTrend()));
        }
        if (filter.getCategory() != null) {
            filters.add(createFieldQ("category", filter.getCategory()));
        }
        if (filter.getSubCategory() != null) {
            filters.add(createFieldQ("subCategory", filter.getSubCategory()));
        }
        if (filter.getBrand() != null) {
            filters.add(createFieldQ("brand", filter.getBrand()));
        }
        return filters;
    }


    private Query createFieldQ(String field, List<String> list) {
        return Query.of(
                q -> q
                        .terms(
                                t -> t.field(field)
                                      .terms(
                                              tt -> tt
                                                      .value(list.stream().map(FieldValue::of).toList()
                                                      )
                                      )
                        )
        );
    }

}
