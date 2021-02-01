package ecommerce.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce.productservice.entity.Product;
import ecommerce.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${product.jms.destination}")
    private String jmsQueue;


    @PostMapping("/addOne")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PostMapping("/addList")
    public List<Product> addProductList(@RequestBody List<Product> products) {
        return productRepository.saveAll(products);
    }

    @GetMapping("/getAll")
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @GetMapping("/info")
    public String getInfo() {
        return "product microservice";
    }

    @GetMapping("/sendToCart/{id}")
    public ResponseEntity<Product> sendToCart(@PathVariable long id) {

        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {

            ObjectMapper mapper = new ObjectMapper();

            String jsonInString = mapper.writeValueAsString(product.get());
            jmsTemplate.convertAndSend(jmsQueue, jsonInString);

            return new ResponseEntity<>(product.get(), HttpStatus.OK);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
