package me.kandid.user.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kandid.user.Exceptions.CustomerNotFound;
import me.kandid.user.Exceptions.ProductNotFound;
import me.kandid.user.Model.Customer.*;
import me.kandid.user.Model.MessageCentral.Response;
import me.kandid.user.Model.Product.Types.CartProduct;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Repository.Customer.*;
import me.kandid.user.Repository.ProductRepository;
import me.kandid.user.Repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerCartRepository customerCartRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private WislistProductRepository wislistProductRepository;

    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OtpLoginRepository otpLoginRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;


    @Override
    public Customer getCustomer(long phone) {
        return customerRepository.getCustomerByPhone(phone);
    }


    @Override
    public String sendOTP(String phone) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest body = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody()).setHeader("authToken",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDLTlENkE1QzJDREE0MTQ5MSIsImlhdCI6MTc0NDAwNzM1MywiZXhwIjoxOTAxNjg3MzUzfQ.c7RSTI2P2O-YHp2bhum2jmv5vDILN74tEiQmIYKzn1YicnbC4XmKDXwMMKpFTzdsYMITg5oA8Tq6z7XWTkapuw")
                .uri(URI.create(
                        "https://cpaas.messagecentral.com/verification/v3/send?countryCode=91&flowType=SMS&mobileNumber="
                                + phone))
                .build();
        HttpResponse<String> res = client.send(body, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        Response re = mapper.readValue(res.body(), Response.class);
        OtpLogin otp = new OtpLogin();
        otp.setPhone(re.getData().getMobileNumber());
        otp.setVerificationId(re.getData().getVerificationId());
        otpLoginRepository.save(otp);
        return String.valueOf(re.getData().getVerificationId());
    }

    @Override
    public long verifyOTP(String id, String code) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest body = HttpRequest.newBuilder().GET().setHeader("authToken",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDLTlENkE1QzJDREE0MTQ5MSIsImlhdCI6MTc0NDAwNzM1MywiZXhwIjoxOTAxNjg3MzUzfQ.c7RSTI2P2O-YHp2bhum2jmv5vDILN74tEiQmIYKzn1YicnbC4XmKDXwMMKpFTzdsYMITg5oA8Tq6z7XWTkapuw")
                .uri(URI.create(
                        "https://cpaas.messagecentral.com/verification/v3/validateOtp?&verificationId=" + id
                                + "&code=" + code))
                .build();
        HttpResponse<String> res = client.send(body, HttpResponse.BodyHandlers.ofString());
        //
        // String ex ="{\n" +
        // " \"responseCode\": 200,\n" +
        // " \"message\": \"SUCCESS\",\n" +
        // " \"data\": {\n" +
        // " \"verificationId\": 66512,\n" +
        // " \"mobileNumber\": null,\n" +
        // " \"verificationStatus\": \"VERIFICATION_COMPLETED\",\n" +
        // " \"responseCode\": \"200\",\n" +
        // " \"errorMessage\": null,\n" +
        // " \"transactionId\": \"5dfe41af-d59f-4253-9bcb-166daccb2382\",\n" +
        // " \"authToken\": null\n" +
        // " }\n" +
        // "}";
        ObjectMapper mapper = new ObjectMapper();
        Response re = mapper.readValue(res.body(), Response.class);
        if (res.statusCode() == 200) {
            if (re.getResponseCode() == 200) {
                return otpLoginRepository.findByVerificationId(re.getData().getVerificationId()).getPhone();
            } else throw new Exception(re.getMessage());
        } else if (res.statusCode() == 401) {
            throw new Exception("MessageCentral Authentication Failed");
        } else throw new Exception(re.getMessage());
    }

    @Override
    public boolean customerCreate(long phone) {
        Customer customer = new Customer();
        customer.setPhone(phone);
        customerRepository.save(customer);
        return true;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public CustomerAddress getCustomerAddress(long id) {
        return customerAddressRepository.getCustomerAddressById(id);
    }

    @Override
    public List<CustomerAddress> getAllCustomerAddress(long customerPhone) {
        return customerAddressRepository.getCustomerAddressesByCustomerPhone(customerPhone);
    }

    @Override
    public CustomerAddress addCustomerAddress(CustomerAddress customerAddress, long customerPhone) {

        return customerAddressRepository.save(customerAddress);
    }

    @Override
    public CustomerAddress updateCustomerAddress(CustomerAddress customerAddress, long customerPhone) {
        return customerAddressRepository.save(customerAddress);
    }

    @Override
    public void deleteCustomerAddress(CustomerAddress customerAddress, long customerPhone) {
        customerAddressRepository.delete(customerAddress);
    }

    @Override
    public Set<Product> getCustomerWishlist(long customerPhone) {
        Set<WishlistProducts> w = wislistProductRepository.getAllByCustomer_Phone(customerPhone);
        return w.stream()
                .map(WishlistProducts::getProduct)
                .peek(p -> p.setInWishlist(
                        true))
                .collect(
                        Collectors.toSet());
    }

    @Override
    public void addToCustomerWishlist(long customerPhone, String productCode) {
        Customer customer = customerRepository.findById(customerPhone).orElseThrow(() -> new CustomerNotFound(customerPhone));
        Product product = productRepository.findById(productCode).orElseThrow(() -> new ProductNotFound(productCode));
        WishlistProductsId wishlistProductsId = new WishlistProductsId(customer.getPhone(), product.getCode());
        WishlistProducts wishlistProducts = new WishlistProducts();
        wishlistProducts.setCustomer(customer);
        wishlistProducts.setProduct(product);
        wishlistProducts.setId(wishlistProductsId);
        wislistProductRepository.save(wishlistProducts);
    }

    @Override
    public void removeFromCustomerWishlist(long customerPhone, String productCode) {
        Customer customer = customerRepository.findById(customerPhone).orElseThrow(() -> new CustomerNotFound(customerPhone));
        Product product = productRepository.findById(productCode).orElseThrow(() -> new ProductNotFound(productCode));
        WishlistProductsId id = new WishlistProductsId(customer.getPhone(), product.getCode());
        wislistProductRepository.deleteById(id);
    }

    @Override
    public List<CustomerOrder> getAllCustomerOrders(long customerPhone) {
        return customerOrdersRepository.getCustomerOrdersByCustomerPhone(customerPhone);
    }

    @Override
    public CustomerOrder getCustomerOrderById(long id, long customerPhone) {
        return customerOrdersRepository.getCustomerOrderByIdAndCustomerPhone(id, customerPhone);
    }

    @Override
    public List<CartProduct> getCustomerCart(long customerPhone) {
        customerRepository.findById(customerPhone).orElseThrow(() -> new CustomerNotFound(customerPhone));
        return customerCartRepository.findAllByCustomer_Phone(customerPhone).stream()
                .map(i -> CartProduct.fromProduct(
                        productRepository.getProductByCode(
                                i.getSku().substring(0, i.getSku().lastIndexOf('-'))),
                        i.getSku(),
                        i.getQuantity(), customerPhone)).toList();
    }

    @Override
    public List<CartProduct> addToCustomerCart(long customerPhone, CartProduct cartItem) {
        Customer customer = customerRepository.findById(customerPhone).orElseThrow(() -> new CustomerNotFound(customerPhone));
        cartItem.setCustomer(customer);
        if (productVariantRepository.findBySku(cartItem.getSku()) == null)
            throw new ProductNotFound(cartItem.getSku());
        CartProduct existingCp = customerCartRepository.findCartProductByCustomer_PhoneAndSku(customerPhone,
                cartItem.getSku());
        if (existingCp != null) {
            cartItem.setId(existingCp.getId());
        }
        customerCartRepository.save(cartItem);
        return customerCartRepository.findAllByCustomer_Phone(customerPhone).stream()
                .map(i -> CartProduct.fromProduct(
                        productRepository.getProductByCode(
                                i.getSku().substring(0, i.getSku().lastIndexOf('-'))),
                        i.getSku(),
                        i.getQuantity(), customerPhone)).toList();
    }

    @Override
    public List<CartProduct> editCustomerCart(long customerPhone, CartProduct cartItem) {
        customerRepository.findById(customerPhone).orElseThrow(() -> new CustomerNotFound(customerPhone));
//        cartItem.setCustomerPhone(customerPhone);
        if (productVariantRepository.findBySku(cartItem.getSku()) == null)
            throw new ProductNotFound(cartItem.getSku());
        if (cartItem.getQuantity() == 0) customerCartRepository.delete(cartItem);
        else customerCartRepository.save(cartItem);
        return customerCartRepository.findAllByCustomer_Phone(customerPhone).stream()
                .map(i -> CartProduct.fromProduct(
                        productRepository.getProductByCode(
                                i.getSku().substring(0, i.getSku().lastIndexOf('-'))),
                        i.getSku(),
                        i.getQuantity(), customerPhone)).toList();
    }

    @Override
    public List<CartProduct> removeFromCustomerCart(long customerPhone, CartProduct cartItem) {
//        customerCartRepository.deleteById(cartItem.getId());
        return customerCartRepository.findAllByCustomer_Phone(customerPhone).stream()
                .map(i -> CartProduct.fromProduct(
                        productRepository.getProductByCode(
                                i.getSku().substring(0, i.getSku().lastIndexOf('-'))),
                        i.getSku(),
                        i.getQuantity(), customerPhone)).toList();
    }

}
