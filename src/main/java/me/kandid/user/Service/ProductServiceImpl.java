package me.kandid.user.Service;

import me.kandid.user.Exceptions.ProductNotFound;
import me.kandid.user.Exceptions.ProductNotInStock;
import me.kandid.user.Model.Customer.Customer;
import me.kandid.user.Model.Customer.CustomerAddress;
import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Model.Enums.OrderTypes;
import me.kandid.user.Model.Product.ProductFilter;
import me.kandid.user.Model.Product.ProductVariant;
import me.kandid.user.Model.Product.Types.CartProduct;
import me.kandid.user.Model.Product.Types.OrderProduct;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Model.Product.Types.SearchableProduct;
import me.kandid.user.Model.Requests.OrderRequest;
import me.kandid.user.Model.Responses.SearchResult;
import me.kandid.user.Repository.Customer.*;
import me.kandid.user.Repository.ProductRepository;
import me.kandid.user.Repository.ProductVariantRepository;
import me.kandid.user.Utils.Utils;
import okhttp3.*;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.aggregations.StringTermsBucket;
import org.opensearch.client.opensearch._types.aggregations.TermsAggregation;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${payu.merchantkey}")
    private String merchantKey;

    @Value("${payu.salt}")
    private String salt;


    @Value("${payu.payments.url}")
    private String payuPaymentsUrl;

    @Value("${backend.url}")
    private String backendUrl;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private OpenSearchClient openSearchClient;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    private CustomerCartRepository customerCartRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

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
                                                                                     a -> a.terms(TermsAggregation.of(
                                                                                             ta -> ta.field(
                                                                                                     v.concat(
                                                                                                             ".keyword"))))))))
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
    public URL checkout_prepaid(long customerPhone, OrderRequest orderRequest) throws IOException {
        validateOrderRequest(orderRequest);

        Customer customer = customerRepository.getCustomerByPhone(customerPhone);
        long orderId = System.currentTimeMillis();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(orderId);
        customerOrder.setCustomerPhone(customerPhone);
        customerOrder.setType(orderRequest.getOrderType());

        CustomerAddress address = customerAddressRepository.getCustomerAddressById(orderRequest.getAddress().getId());
        if (address == null) throw new RuntimeException("Address ID not found");
        customerOrder.setCustomerAddress(address);

        List<ProductVariant> variants = new ArrayList<>();
        List<OrderProduct> orderProducts = prepareOrderItems(orderRequest, String.valueOf(customerPhone), variants);
        double bill = calculateBill(orderProducts);
        customerOrder.setBillAmount(bill);
        customerOrder.setItems(orderProducts);
        customerOrder.setStatus("PENDING");
        customerOrder.setPaymentStatus("PENDING");

        // Prepare PayU payment parameters
        String txnId = "ORD" + orderId;
        String firstName = getFirstName(customer.getName());
        String lastName = getLastName(customer.getName());
        String amount = String.valueOf(customerOrder.getBillAmount());
        Map<String, String> params = Utils.createHashMap(
                merchantKey, amount, firstName, customer.getEmail(),
                String.valueOf(customer.getPhone()), "Payment for order on Kandid",
                backendUrl + "success", backendUrl + "failure", txnId
        );

        params.put("hash", Utils.generateHashPaymentsAPI(params, salt));
        params.put("lastname", lastName);

        // Send request
        Request request = new Request.Builder()
                .url(URI.create(payuPaymentsUrl).toURL())
                .post(RequestBody.create(Utils.encodeParams(params),
                        MediaType.parse("application/x-www-form-urlencoded")))
                .build();

        try (Response response = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
                .newCall(request).execute()) {

            if (response.isSuccessful() && response.body() != null) {
                throw new RuntimeException(response.body().string());
            }

            URL paymentUrl = URI.create(Objects.requireNonNull(response.header("Location"))).toURL();
            customerOrder.setPaymentLink(paymentUrl);

            customerOrdersRepository.save(customerOrder);
            return paymentUrl;
        }
    }

    @Override
    public URL checkout_confirmed(long customerPhone, long orderId) throws IOException {
        CustomerOrder customerOrder = customerOrdersRepository.getCustomerOrderById(orderId);
        double bill = customerOrder.getBillAmount();
        if (customerOrder.getType().equals(OrderTypes.TRY_AND_BUY)) bill = bill - 35.0;
        Customer customer = customerRepository.getCustomerByPhone(customerPhone);
        String firstName = getFirstName(customer.getName());
        String lastName = getLastName(customer.getName());
        String amount = String.valueOf(bill); // Consider using actual `bill` if dynamic pricing

        customerOrder.setPaymentStatus("PENDING");
        Map<String, String> params = Utils.createHashMap(
                merchantKey, amount, firstName, customer.getEmail(),
                String.valueOf(customer.getPhone()), "Payment for try and buy on Kandid",
                backendUrl + "success", backendUrl + "failure", "ORD" + customerOrder.getId()
        );

        params.put("hash", Utils.generateHashPaymentsAPI(params, salt));
        params.put("lastname", lastName);

        Request request = new Request.Builder()
                .url(URI.create(payuPaymentsUrl).toURL())
                .post(RequestBody.create(Utils.encodeParams(params),
                        MediaType.parse("application/x-www-form-urlencoded")))
                .build();

        try (Response response = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
                .newCall(request).execute()) {

            if (response.isSuccessful() && response.body() != null) {
                throw new RuntimeException(response.body().string());
            }

            URL paymentUrl = URI.create(Objects.requireNonNull(response.header("Location"))).toURL();
            customerOrder.setPaymentLink(paymentUrl);
            customerOrdersRepository.save(customerOrder);
            return paymentUrl;
        }
    }

    @Override
    public URL checkout_cod(long customerPhone, OrderRequest orderRequest) throws IOException {
        validateOrderRequest(orderRequest);

        Customer customer = customerRepository.getCustomerByPhone(customerPhone);
        long orderId = System.currentTimeMillis();

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(orderId);
        customerOrder.setCustomerPhone(customerPhone);
        customerOrder.setType(orderRequest.getOrderType());

        CustomerAddress address = customerAddressRepository.getCustomerAddressById(orderRequest.getAddress().getId());
        if (address == null) throw new RuntimeException("Address ID not found");
        customerOrder.setCustomerAddress(address);

        List<ProductVariant> variants = new ArrayList<>();
        List<OrderProduct> orderProducts = prepareOrderItems(orderRequest, String.valueOf(customerPhone), variants);
        double bill = calculateBill(orderProducts);
        customerOrder.setBillAmount(bill);
        customerOrder.setItems(orderProducts);

        if (orderRequest.getOrderType() == OrderTypes.REGULAR) {
            customerOrder.setStatus("PLACED");
            customerOrder.setPaymentStatus("NOT_ATTEMPTED");

            productVariantRepository.save(variants.getFirst());
            customerOrdersRepository.save(customerOrder);
            return URI.create("http://localhost:3000/profile/orders/details/" + customerOrder.getId() +
                    "?status=success").toURL(); //
        }

// Payment flow for non-REGULAR order types
        customerOrder.setStatus("PENDING");
        customerOrder.setPaymentStatus("PENDING");


        String txnId = "ORD" + orderId;
        String firstName = getFirstName(customer.getName());
        String lastName = getLastName(customer.getName());
        String amount = "35.0"; // Consider using actual `bill` if dynamic pricing
        Map<String, String> params = Utils.createHashMap(
                merchantKey, amount, firstName, customer.getEmail(),
                String.valueOf(customer.getPhone()), "Payment for try and buy on Kandid",
                backendUrl + "success", backendUrl + "failure", txnId
        );

        params.put("hash", Utils.generateHashPaymentsAPI(params, salt));
        params.put("lastname", lastName);

        Request request = new Request.Builder()
                .url(URI.create(payuPaymentsUrl).toURL())
                .post(RequestBody.create(Utils.encodeParams(params),
                        MediaType.parse("application/x-www-form-urlencoded")))
                .build();

        try (Response response = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
                .newCall(request).execute()) {

            if (response.isSuccessful() && response.body() != null) {
                throw new RuntimeException(response.body().string());
            }

            URL paymentUrl = URI.create(Objects.requireNonNull(response.header("Location"))).toURL();
            customerOrder.setPaymentLink(paymentUrl);

            productVariantRepository.save(variants.getFirst());
            customerOrdersRepository.save(customerOrder);
            return paymentUrl;
        }
    }


//    Helper functions

    private void processOrder(int quantity, List<ProductVariant> variants, List<OrderProduct> orderProducts,
                              String sku) {
        ProductVariant variant = productVariantRepository.findBySku(sku);
        if (variant == null) {
            throw new ProductNotFound(sku);
        }
        if (variant.getAvailableStock() - quantity < 0) {
            throw new ProductNotInStock(sku);
        }
        variant.setAvailableStock(variant.getAvailableStock() - quantity);
        Product p = productRepository.getProductByCode(variant.getProductCode());
        OrderProduct orderProduct = OrderProduct.fromProduct(p, variant.getSku(), quantity);
        orderProducts.add(orderProduct);
        variants.add(variant);
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request.getAddress() == null) throw new RuntimeException("Address is null");
        if (request.getOrderType() == null) throw new RuntimeException("OrderType is null");
        if (request.getBuynow() == null) throw new RuntimeException("BuyNow is null");
    }

    private List<OrderProduct> prepareOrderItems(OrderRequest request, String phone, List<ProductVariant> variants) {
        List<OrderProduct> items = new ArrayList<>();
        if (Boolean.TRUE.equals(request.getBuynow())) {
            processOrder(request.getQuantity(), variants, items, request.getSku());
        } else {
            List<CartProduct> cartItems = customerCartRepository.findAllByCustomerPhone(Long.parseLong(phone));
            if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");
            for (CartProduct item : cartItems) {
                processOrder(item.getQuantity(), variants, items, item.getSku());
            }
        }
        return items;
    }

    private double calculateBill(List<OrderProduct> items) {
        double bill = items.stream().mapToDouble(i -> i.getPricePaid() * i.getQuantity()).sum();
        return bill <= 0 ? 100 : bill;
    }

    private String getFirstName(String fullName) {
        return fullName.contains(" ") ? fullName.split(" ")[0] : fullName;
    }

    private String getLastName(String fullName) {
        int index = fullName.indexOf(' ');
        return index != -1 ? fullName.substring(index + 1) : "";
    }
}
