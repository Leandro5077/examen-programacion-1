package com.example.crudweb.controller;

import com.example.crudweb.model.Category;
import com.example.crudweb.model.Product;
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
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @GetMapping
    public String listCategories(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Category> categoryPage = categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);

        // Puedes verificar si tiene elementos así:
        boolean tieneElementos = categoryPage.hasContent();
        System.out.println("¿categoryPage tiene elementos? " + tieneElementos);

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "categories/list";
    }
    // Show form to create new category
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/create";
    }

    // Save new or edited category
    @PostMapping("/save")
    public String saveCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categories/list";
        }

        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Categoría guardada correctamente.");
        return "redirect:/categories";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
            return "redirect:/categories";
        }

        model.addAttribute("category", category);
        return "categories/edit";
    }

    // Update category
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id,
                                @Valid Category category,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            category.setId(id);
            model.addAttribute("category", category);
            return "categories/edit";
        }
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    // Delete category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
        }
        return "redirect:/categories";
    }
}
