package ecommerce.cartservice.controller;

import ecommerce.cartservice.entity.Product;
import ecommerce.cartservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/getProducts")
    List<Product> getCartProduct() {
        return productRepository.findAll();
    }

    @GetMapping("/deleteOne/{id}")
    void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @GetMapping("/deleteAll")
    void deleteProducts() {
        productRepository.deleteAll();
    }

    @GetMapping("/info")
    String getInfo() {
        return "cart microservice";
    }
}
