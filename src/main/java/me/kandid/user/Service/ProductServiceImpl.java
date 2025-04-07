package me.kandid.user.Service;

import me.kandid.user.Model.Product.Product;
import me.kandid.user.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(String code){
        return productRepository.getProductByCode(code);
    }
}
