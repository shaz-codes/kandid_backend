package me.kandid.user.Service;

import me.kandid.user.Model.Product.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(String code);
}
