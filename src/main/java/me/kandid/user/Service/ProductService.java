package me.kandid.user.Service;

import me.kandid.user.Exceptions.ProductFilterNotFound;
import me.kandid.user.Model.Product.Product;
import me.kandid.user.Model.Product.ProductFilter;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(String code);

    List<Product> getProductsByFilter(ProductFilter filter) throws ProductFilterNotFound;
}
