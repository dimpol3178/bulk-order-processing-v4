package com.bdo.exam.config;

import com.bdo.exam.entity.Product;
import com.bdo.exam.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DatabaseSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Seeding tbl_products reference database in PostgreSQL...");
            productRepository.saveAll(List.of(
                    new Product("P001", "Wireless Keyboard"),
                    new Product("P002", "Bluetooth Headset"),
                    new Product("P003", "Gaming Mouse"),
                    new Product("P004", "USB-C Docking Station")
            ));
            log.info("Product database successfully seeded.");
        }
    }
}
