package com.xintong.staff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xintong.staff.entity.Product;
import com.xintong.staff.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // search products by name or code
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(keyword, keyword);
    }

    //create a new product
    public Product createProduct(Product product) {
        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            product.setCode(generateNextCode());
        }
        if (product.getPiecesPerBox() < 1) {
            product.setPiecesPerBox(1);
        }
        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า ID: " + id));
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = getProduct(id);
        product.setCode(updatedProduct.getCode());
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateStock(Long id, int boxes, int pieces, int piecesPerBox) {
        if (boxes < 0 || pieces < 0 || piecesPerBox < 1) {
            throw new IllegalArgumentException("จำนวนสินค้าไม่ถูกต้อง");
        }

        Product product = getProduct(id);
        product.setPiecesPerBox(piecesPerBox);
        product.setQuantity((boxes * piecesPerBox) + pieces);
        return productRepository.save(product);
    }

    private String generateNextCode() {
        int nextNumber = productRepository.findAll().stream()
                .map(Product::getCode)
                .filter(code -> code != null && code.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        return String.format("%06d", nextNumber);
    }

    @Transactional
    public Product adjustQuantity(Long id, Integer amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า ID: " + id));

        int newQuantity = product.getQuantity() + amount;
        if (newQuantity < 0) {
            throw new RuntimeException("จำนวนสินค้าไม่พอให้หักลบ");
        }

        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateQuantity(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("จำนวนสินค้าต้องไม่ติดลบ");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า ID: " + id));
        product.setQuantity(quantity);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("ไม่พบสินค้า ID: " + id);
        }
        productRepository.deleteById(id);
    }

}
