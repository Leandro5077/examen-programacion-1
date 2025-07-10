package com.example.crudweb.controller;

import com.example.crudweb.model.Product;
import com.example.crudweb.model.Category;
import com.example.crudweb.repository.ProductRepository;
import com.example.crudweb.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listProducts(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "products/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Product product, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "products/create";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "products/create";
        }

        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "products/edit";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "products/edit";
        }

        if (!productRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/products";
        }

        product.setId(id); // aseguramos que el ID no se pierda
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "Producto actualizado correctamente.");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        productRepository.delete(product);
        return "redirect:/products";
    }
}
