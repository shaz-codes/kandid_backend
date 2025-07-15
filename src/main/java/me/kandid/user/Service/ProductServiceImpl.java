package me.kandid.user.Service;

import jakarta.persistence.criteria.Predicate;
import me.kandid.user.Exceptions.ProductFilterNotFound;
import me.kandid.user.Model.Product.Discount;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Repository.DiscountRepository;
import me.kandid.user.Repository.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    // @Autowired
    // private InventoryService inventoryService;

    @Override
    public List<Product> getProducts() {
        List<Product> list = productRepository.findAllByActiveIsTrue();
        return setSellingPriceAfterCalculation(list);
    }

    @Override
    public Product getProduct(String code) {
        return productRepository.getProductByCode(code);
    }

    @Override
    public List<Product> getProductsByFilter(ProductFilter filter) throws ProductFilterNotFound {
        Specification<Product> spec = (r, q, cb) -> cb.isTrue(r.get("active"));

        if (filter == null) {
            throw new ProductFilterNotFound();
        }

        if (filter.getAesthetic() != null) {
            spec = spec.and(buildOrLike("aesthetic", filter.getAesthetic()));
        }
        if (filter.getMaterial() != null) {
            spec = spec.and(buildOrLike("material", filter.getMaterial()));
        }
        if (filter.getColor() != null) {
            spec = spec.and(buildOrLike("color", filter.getColor()));
        }
        if (filter.getOccasion() != null) {
            spec = spec.and(buildOrLike("occasion", filter.getOccasion()));
        }
        if (filter.getSleeve() != null) {
            spec = spec.and(buildOrLike("sleeve", filter.getSleeve()));
        }
        if (filter.getFit() != null) {
            spec = spec.and(buildOrLike("fit", filter.getFit()));
        }
        if (filter.getFitType() != null) {
            spec = spec.and(buildOrLike("fitType", filter.getFitType()));
        }
        if (filter.getPattern() != null) {
            spec = spec.and(buildOrLike("pattern", filter.getPattern()));
        }
        if (filter.getNeckline() != null) {
            spec = spec.and(buildOrLike("neckline", filter.getNeckline()));
        }
        if (filter.getClosure() != null) {
            spec = spec.and(buildOrLike("closure", filter.getClosure()));
        }
        if (filter.getClosureType() != null) {
            spec = spec.and(buildOrLike("closureType", filter.getClosureType()));
        }
        if (filter.getRiseStyle() != null) {
            spec = spec.and(buildOrLike("riseStyle", filter.getRiseStyle()));
        }
        if (filter.getStyle() != null) {
            spec = spec.and(buildOrLike("style", filter.getStyle()));
        }
        if (filter.getTrend() != null) {
            spec = spec.and(buildOrLike("trend", filter.getTrend()));
        }

        List<Product> products = productRepository.findAll(spec);

        return setSellingPriceAfterCalculation(products).stream()
                .filter(item -> item.getSellingPrice() >= filter.getPriceFrom()
                        && item.getSellingPrice() <= filter.getPriceTo())
                .toList();
    }

    @NotNull
    private List<Product> setSellingPriceAfterCalculation(List<Product> products) {
        products.forEach(item -> {
            String code = item.getCode();
            Instant now = Instant.now();
            Discount discount = discountRepository
                    .findDiscountByDiscountedFromAfterAndDiscountedToBeforeAndProductCode(now, now, code);
            if (discount != null) {
                item.setSellingPrice(discount.getDiscountedPrice());
            }
        });
        return products;
    }

    private Specification<Product> buildOrLike(String field, String[] list) {
        return (root, query, cb) -> {
            Predicate[] predicates = Arrays.stream(list)
                    .map(v -> cb.like(cb.lower(root.get(field)), "%" + v.toLowerCase() + "%"))
                    .toArray(Predicate[]::new);
            return cb.or(predicates);

        };

    }
}
