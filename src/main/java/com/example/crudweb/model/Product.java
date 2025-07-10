package com.example.crudweb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
