package com.example.assigment_java6.controller;

import com.example.assigment_java6.domain.Product;
import com.example.assigment_java6.service.ProductService;
import com.example.assigment_java6.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable; // ✅ Đúng
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    //Injecting ProductService to handle tasks about Product
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/products")
    public ResponseEntity<Product> addCompany(@Valid  @RequestBody Product product) {
        Product newProdcuct = this.productService.handleCreateProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProdcuct);
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") long id){
        return ResponseEntity.ok(productService.handleDeleteProduct(id));

    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam("current") Optional<String> currentOptional, @RequestParam("pageSize")Optional<String> pageSizeOptional) {
        String sCurent=currentOptional.isPresent()?currentOptional.get():"";
        String sPageSize=pageSizeOptional.isPresent()?pageSizeOptional.get():"";
        int current=Integer.parseInt(sCurent);
        int pageSize=Integer.parseInt(sPageSize);
        Pageable pageable = PageRequest.of(current -1, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.handleGetAllProduct(pageable));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>>getProductsByName(@RequestParam("name") String name) {
        List<Product> products = this.productService.handleGetProductByString(name);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) {
        Product updatedProduct = this.productService.handleUpdateProduct(product);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}
//TODO:
//      +Check validate variable if true then return created variable else return message and status code:400
//      +Check barer token if it have then run task after that else return message,sts code with pause task running
