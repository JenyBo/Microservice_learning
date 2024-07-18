package com.example.businessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CacheManager cacheManager;

    private static final String TOPIC = "product_topic";

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping
    public Product save(
//            @RequestHeader("userId") String userId,
            @RequestBody Product product) {
//        String key = "requestCount_" + userId;
//
//        AtomicInteger requestCount = Objects.requireNonNull(cacheManager.getCache("requestsCache"))
//                .get(key, AtomicInteger::new);
//
//        assert requestCount != null;
//        if (requestCount.incrementAndGet() > 5) {
//            throw new TooManyRequestsException("Too many requests, please try again later.");
//        }

        Product savedProduct = productService.save(product);
        kafkaTemplate.send(TOPIC, "Product saved: " + savedProduct.toString());
        return savedProduct;
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return productService.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }
}

