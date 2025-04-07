package com.example.assigment_java6.service;
import com.example.assigment_java6.domain.Product;

import com.example.assigment_java6.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductService {
    //Handle product logic
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    //To create product
    public Product handleCreateProduct(Product product) {
        return productRepository.save(product);
    }
    //To delete product
    public String handleDeleteProduct(long id) {
        productRepository.deleteById(id);
        return "Product deleted";
    }
    //To find product by id,if not found to return empty
    public Product handleGetProductById(long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        return null;
    }
    //To find product by string
    public List<Product> handleGetProductByString(String name) {
        return this.productRepository.findByNameContainingIgnoreCase(name);
    }
    //To get all product
    public List<Product> handleGetAllProduct(Pageable pageable) {
        Page<Product> products = this.productRepository.findAll(pageable);
        return products.getContent() ;
    }
    public Product handleUpdateProduct(Product product) {
        Product productUpdate= this.handleGetProductById(product.getId());
        if (productUpdate!=null) {
            productUpdate.setName(product.getName());
            productUpdate.setDescription(product.getDescription());
            productUpdate.setPrice(product.getPrice());
            productUpdate.setCategory(product.getCategory());
            productUpdate.setImage(product.getImage());
            productUpdate.setStock(product.getStock());
            productUpdate = productRepository.save(productUpdate);
        }
        return productUpdate;
    }

}
