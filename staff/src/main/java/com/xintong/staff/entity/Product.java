package com.xintong.staff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "code", nullable = false, unique = true)
    private String code;

    @Column (name = "name", nullable = false)
    private String name;
    
    @Column (name = "description")
    private String description;

    @Column (name = "quantity", nullable = false)
    private int quantity;

    @Column (name = "pieces_per_box", nullable = false)
    private int piecesPerBox = 1;

    public Product() {
    }

    public Product(Long id, String code, String name, String description, int quantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPiecesPerBox() {
        return piecesPerBox;
    }

    public void setPiecesPerBox(int piecesPerBox) {
        this.piecesPerBox = piecesPerBox;
    }

    public int getBoxCount() {
        return quantity / Math.max(piecesPerBox, 1);
    }

    public int getLoosePieces() {
        return quantity % Math.max(piecesPerBox, 1);
    }
}
