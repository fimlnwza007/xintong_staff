package com.xintong.staff.controller;

import com.xintong.staff.entity.Product;
import com.xintong.staff.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    // 1. แสดงหน้าหลักรายการสินค้า + ระบบค้นหา
    @GetMapping
    public String index(@RequestParam(required = false) String keyword,
                        @RequestParam(defaultValue = "id") String sort,
                        Model model) {
        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword);
        } else {
            products = productService.getAllProducts();
        }

        if ("name".equals(sort)) {
            products.sort(Comparator.comparing(Product::getName,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
        } else {
            products.sort(Comparator.comparing(Product::getId,
                    Comparator.nullsLast(Comparator.naturalOrder())));
        }
        
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("newProduct", new Product()); // สำหรับฟอร์มเพิ่มสินค้า
        return "products"; // คืนค่าไฟล์ products.html ใน templates
    }

    // 2. บันทึกการเพิ่มสินค้าใหม่
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.createProduct(product);
        return "redirect:/products";
    }

    // 3. กดเพิ่มจำนวน (+1)
    @PostMapping("/{id}/increase")
    public String increaseQuantity(@PathVariable Long id) {
        productService.adjustQuantity(id, 1);
        return "redirect:/products";
    }

    // 4. กดลดจำนวน (-1)
    @PostMapping("/{id}/decrease")
    public String decreaseQuantity(@PathVariable Long id) {
        try {
            productService.adjustQuantity(id, -1);
        } catch (Exception e) {
            // ป้องกันเมื่อจำนวนกลายเป็นติดลบ
        }
        return "redirect:/products";
    }

    @PostMapping("/{id}/quantity")
    public String updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        productService.updateQuantity(id, quantity);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
        return "product-edit";
    }

    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @ModelAttribute Product product,
                              @RequestParam int boxes,
                              @RequestParam int pieces,
                              @RequestParam int piecesPerBox) {
        productService.updateProduct(id, product);
        productService.updateStock(id, boxes, pieces, piecesPerBox);
        return "redirect:/products";
    }

    // 5. ลบรายการสินค้า
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}