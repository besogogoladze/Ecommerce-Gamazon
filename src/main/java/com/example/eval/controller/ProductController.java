package com.example.eval.controller;

import com.example.eval.model.Product;
import com.example.eval.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Method to return the HTML page with a list of products, sorted by margin if
    // specified
    @GetMapping("/products/list")
    public String getAllProducts(Model model, @RequestParam(required = false) String sortOrder) {
        List<Product> products = productService.getAllProducts();

        if (sortOrder != null) {
            if (sortOrder.equalsIgnoreCase("asc")) {
                products.sort(
                        (product1, product2) -> product2.getSalePrice().compareTo(product1.getSalePrice()));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                products.sort(
                        (product1, product2) -> product1.getSalePrice().compareTo(product2.getSalePrice()));
            } else {
                products = productService.getAllProducts();
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("sortOrder", sortOrder);

        return "products/list";
    }

    // Get products list by id using API
    @GetMapping("/api/products/list")
    public ResponseEntity<List<Product>> getAllProductsList(Model model) {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get product by id on home page
    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }

    // Get product by id on detail page
    @GetMapping("/products/add")
    public String getAddProductPage() {
        return "products/add";
    }

    // Add new products
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/products/list";
    }

    // Delete products
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products/list";
    }

}
