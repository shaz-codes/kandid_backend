package me.kandid.user.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kandid.user.Exceptions.ProductNotFound;
import me.kandid.user.Exceptions.WishlistDoesNotExist;
import me.kandid.user.Model.Customer.*;
import me.kandid.user.Model.MessageCentral.Response;
import me.kandid.user.Model.Product.Types.Product;
import me.kandid.user.Repository.Customer.*;
import me.kandid.user.Repository.ProductRepository;
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
    private CustomerWishlistRepository customerWishlistRepository;

    @Autowired
    private CustomerOrdersRepository customerOrdersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OtpLoginRepository otpLoginRepository;


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
        System.out.println(res.body() + "\n" + res.statusCode());
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
    public boolean customerExist(long phone) {
        return customerRepository.existsByPhone(phone);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        createCustomerWishlist(customer.getPhone());
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
    public CustomerAddress addCustomerAddress(CustomerAddress customerAddress) {
        return customerAddressRepository.save(customerAddress);
    }

    @Override
    public CustomerAddress updateCustomerAddress(CustomerAddress customerAddress) {
        return customerAddressRepository.save(customerAddress);
    }

    @Override
    public void deleteCustomerAddress(CustomerAddress customerAddress) {
        customerAddressRepository.delete(customerAddress);
    }

    @Override
    public CustomerWishlist getCustomerWishlist(long customerPhone) {
        CustomerWishlist w = customerWishlistRepository.getByCustomerPhone(customerPhone);
        if (w == null) {
            throw new WishlistDoesNotExist(customerPhone);
        }
        w.setProducts(w.getProducts().stream()
                       .peek(p -> p.setInWishlist(
                               true))
                       .collect(
                               Collectors.toSet()));
        return w;
    }

    @Override
    public void createCustomerWishlist(long customerPhone) {
        CustomerWishlist customerWishlist = new CustomerWishlist();
        customerWishlist.setCustomerPhone(customerPhone);
        customerWishlistRepository.save(customerWishlist);
    }

    @Override
    public void addToCustomerWishlist(long customerPhone, String productCode) {
        CustomerWishlist customerWishlist = customerWishlistRepository.getByCustomerPhone(customerPhone);
        Set<Product> products = customerWishlist.getProducts();
        Product p = productRepository.getProductByCode(productCode);
        if (p == null) throw new ProductNotFound(productCode);
        products.add(p);
        customerWishlistRepository.save(customerWishlist);
    }

    @Override
    public void removeFromCustomerWishlist(long customerPhone, String productCode) {
        CustomerWishlist customerWishlist = customerWishlistRepository.getByCustomerPhone(customerPhone);
        Set<Product> products = customerWishlist.getProducts();
        Product p = productRepository.getProductByCode(productCode);
        if (p == null) throw new ProductNotFound(productCode);
        products.remove(p);
        customerWishlistRepository.save(customerWishlist);
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
    public List<CartItems> getCustomerCart(long customerPhone) {
        return customerCartRepository.findAllByCustomerPhone(customerPhone);
    }

    @Override
    public List<CartItems> addToCustomerCart(long customerPhone, CartItems cartItem) {
        cartItem.setCustomerPhone(customerPhone);
        customerCartRepository.save(cartItem);
        return customerCartRepository.findAllByCustomerPhone(customerPhone);
    }

    @Override
    public List<CartItems> editCustomerCart(long customerPhone, CartItems cartItem) {
        cartItem.setCustomerPhone(customerPhone);
        customerCartRepository.save(cartItem);
        return customerCartRepository.findAllByCustomerPhone(customerPhone);
    }

    @Override
    public List<CartItems> removeFromCustomerCart(long customerPhone, CartItems cartItem) {
        cartItem.setCustomerPhone(customerPhone);
        customerCartRepository.delete(cartItem);
        return customerCartRepository.findAllByCustomerPhone(customerPhone);
    }

}
