package me.kandid.user.Service;

import me.kandid.user.Model.Customer.*;
import me.kandid.user.Model.Product.Product;
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

    @Override
    public Customer getCustomer(long phone) {
        return customerRepository.getCustomerByPhone(phone);
    }

    @Override
    public String sendOTP(long phone) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest body = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody()).setHeader("authToken","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDLTlENkE1QzJDREE0MTQ5MSIsImlhdCI6MTc0NDAwNzM1MywiZXhwIjoxOTAxNjg3MzUzfQ.c7RSTI2P2O-YHp2bhum2jmv5vDILN74tEiQmIYKzn1YicnbC4XmKDXwMMKpFTzdsYMITg5oA8Tq6z7XWTkapuw").uri(URI.create("https://cpaas.messagecentral.com/verification/v3/send?countryCode=91&flowType=SMS&mobileNumber=" + phone)).build();
        HttpResponse<String> res = client.send(body, HttpResponse.BodyHandlers.ofString());
//        System.out.println(res.body());
        // TODO decode res to return verification id
        return res.body();
    }

    @Override
    public String verifyOTP(String id, String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest body = HttpRequest.newBuilder().GET().setHeader("authToken","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDLTlENkE1QzJDREE0MTQ5MSIsImlhdCI6MTc0NDAwNzM1MywiZXhwIjoxOTAxNjg3MzUzfQ.c7RSTI2P2O-YHp2bhum2jmv5vDILN74tEiQmIYKzn1YicnbC4XmKDXwMMKpFTzdsYMITg5oA8Tq6z7XWTkapuw").uri(URI.create("https://cpaas.messagecentral.com/verification/v3/validateOtp?&verificationId="+ id + "&code=" + code)).build();
        HttpResponse<String> res = client.send(body, HttpResponse.BodyHandlers.ofString());
//        System.out.println(res.body());
        // TODO decode res to return verification id
        return res.body();
    }

    @Override
    public boolean customerExist(long id) {
        return customerRepository.existsById(id);
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
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.getCustomerByEmail(email);
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
        return customerWishlistRepository.getByCustomerPhone(customerPhone);
    }

    @Override
    public void createCustomerWishlist(long customerPhone) {
        CustomerWishlist customerWishlist = new CustomerWishlist();
        customerWishlist.setCustomerPhone(customerPhone);
        customerWishlistRepository.save(customerWishlist);
    }

    @Override
    public CustomerWishlist addToCustomerWishlist(long customerPhone, String productCode) {
        CustomerWishlist customerWishlist = customerWishlistRepository.getByCustomerPhone(customerPhone);
        List<Product> products = customerWishlist.getProducts();
        products.add(productRepository.getProductByCode(productCode));
        return  customerWishlistRepository.save(customerWishlist);
    }

    @Override
    public CustomerWishlist removeFromCustomerWishlist(long customerPhone, String productCode) {
        CustomerWishlist customerWishlist = customerWishlistRepository.getByCustomerPhone(customerPhone);
        List<Product> products = customerWishlist.getProducts();
        products.remove(productRepository.getProductByCode(productCode));
        return  customerWishlistRepository.save(customerWishlist);
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
        return customerCartRepository.findAllByCustomerPhone(customerPhone);
    }

    @Override
    public void removeFromCustomerCart(long customerPhone, CartItems cartItem) {
        cartItem.setCustomerPhone(customerPhone);
        customerCartRepository.delete(cartItem);
    }
}
