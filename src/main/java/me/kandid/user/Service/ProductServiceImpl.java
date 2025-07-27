package me.kandid.user.Service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import me.kandid.user.Model.Product.ElastiProduct;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Repository.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public Product getProduct(String code) {
        return productRepository.getProductByCode(code);
    }

    @Override
    public SearchHits<ElastiProduct> getProducts(String s, ProductFilter filter, Pageable pageable) {
        List<String> fields = new ArrayList<>();
        Field[] f = ElastiProduct.class.getDeclaredFields();
        for (Field field : f) {
            if (Modifier.isPrivate(field.getModifiers())) {
                fields.add(field.getName());
            }
        }

//        TODO: add price filtering
        fields.remove("available");
        fields.remove("sellingPrice");
        fields.remove("mrp");
        fields.remove("name");
        NativeQueryBuilder queryBuilder =
                NativeQuery.builder().withQuery(q -> q.bool(b -> {
                            if (s == null || s.isBlank())
                                b.must(m -> m.matchAll(ma -> ma));
                            else {
                                b.should(m -> m.multiMatch(mm -> mm.fields(fields).fuzziness("AUTO").query(s)));
                                b.should(m -> m.matchPhrasePrefix(mm -> mm.field("name").query(s)));
                                b.should(m -> m.matchPhrasePrefix(mm -> mm.field("description").query(s)));
                            }
                            if (filter != null) b.filter(createFilter(filter));
                            return b;
                        }
                ));
        for (String field : fields) {
            if (field.equals("code") || field.equals("visuals") || field.equals("description") || field.equals("name"))
                continue;
            Aggregation agg = Aggregation.of(
                    a -> a.terms(TermsAggregation.of(t -> t.field(field + ".keyword"))));
            queryBuilder.withAggregation(field, agg).withPageable(pageable);
        }
        org.springframework.data.elasticsearch.core.query.Query query = queryBuilder.build();

        return elasticsearchOperations.search(query, ElastiProduct.class);
    }

    @NotNull
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
        System.out.println(filters);
        return filters;
    }

    @Override
    public SearchHits<ElastiProduct> autocomplete(String q) {
        org.springframework.data.elasticsearch.core.query.Query query = new NativeQuery(Query.of(
                p -> p.matchPhrasePrefix(m -> m.field(
                        "name").query(q)))).setPageable(Pageable.ofSize(5));
        return elasticsearchOperations.search(query, ElastiProduct.class);
    }

    private Query createFieldQ(String field, List<String> list) {
        return Query.of(
                q -> q
                        .terms(
                                t -> t.field(field + ".keyword")
                                      .terms(
                                              tt -> tt
                                                      .value(list.stream().map(FieldValue::of).toList()
                                                      )
                                      )
                        )
        );
    }

}
