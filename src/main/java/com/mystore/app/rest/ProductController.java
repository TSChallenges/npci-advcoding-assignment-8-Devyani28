package com.mystore.app.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mystore.app.entity.Product;
import com.mystore.app.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ResponseEntity<Object> addProduct(@RequestBody @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        
        if (product.getPrice() < 100) {
            return new ResponseEntity<>("Please don't add any product with price lesser than 100", HttpStatus.BAD_REQUEST);
        }

        if (product.getPrice() > 50000) {
            return new ResponseEntity<>("This platform doesn't allow high priced products. Prices must be <= 50000", HttpStatus.BAD_REQUEST);
        }

        Product p = productService.addProduct(product);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @GetMapping("")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Integer id) {
        Product p = productService.getProduct(id);
        if (p != null) {
            return new ResponseEntity<>(p, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody Product product) {
        Product p = productService.updateProduct(id, product);
        if (p != null) {
            return new ResponseEntity<>(p, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) {
        String message = productService.deleteProduct(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    // TODO: API to search products by name
    @GetMapping("/search")
    public List<Product> searchProductsByName(@RequestParam String name) {
        return productService.searchByName(name);
    }

    // TODO: API to filter products by category
    @GetMapping("/filter/category")
    public List<Product> filterProductsByCategory(@RequestParam String category) {
        return productService.filterByCategory(category);
    }

    // TODO: API to filter products by price range
    @GetMapping("/filter/price")
    public List<Product> filterProductsByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return productService.filterByPrice(minPrice, maxPrice);
    }

    // TODO: API to filter products by stock quantity range
    @GetMapping("/filter/stock")
    public List<Product> filterProductsByStock(@RequestParam Integer minStock, @RequestParam Integer maxStock) {
        return productService.filterByStock(minStock, maxStock);
    }

    @GetMapping("")
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "3") int pageSize, 
            @RequestParam(defaultValue = "price") String sortBy, 
            @RequestParam(defaultValue = "desc") String sortDir) {

        return productService.getAllProducts(page, pageSize, sortBy, sortDir);
    }









}
