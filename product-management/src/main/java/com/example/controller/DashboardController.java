package com.example.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.ProductService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final ProductService productService;

    @Autowired
    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        List<String> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("totalValue", productService.calculateTotalValue());
        model.addAttribute("avgPrice", productService.calculateAveragePrice());

        // counts per category
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String c : categories) {
            counts.put(c, productService.countByCategory(c));
        }
        model.addAttribute("countsByCategory", counts);

        model.addAttribute("lowStock", productService.findLowStockProducts(5)); // threshold example

        return "dashboard";
    }
}
