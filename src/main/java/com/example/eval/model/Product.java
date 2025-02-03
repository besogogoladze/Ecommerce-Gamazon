package com.example.eval.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal purchasePrice;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSalePrice() {
        final BigDecimal TVA = BigDecimal.valueOf(1.20); // 20% TVA
        final BigDecimal PROFIT_MARGIN = BigDecimal.valueOf(1.05); // +5%

        BigDecimal salePrice = purchasePrice.multiply(PROFIT_MARGIN); // +5% of purchase price

        switch (type) {
            case "Livre", "High Tech" -> salePrice = salePrice.multiply(BigDecimal.ONE.subtract(TVA)); // Apply TVA
                                                                                                       // reduction
            case "Bureau" -> {
                salePrice = purchasePrice.multiply(PROFIT_MARGIN); // Apply +5% profit margin first
                salePrice = salePrice.subtract(BigDecimal.valueOf(100)); // Then subtract 100 EUR
                salePrice = salePrice.multiply(BigDecimal.ONE.subtract(TVA)); // Finally, apply VAT reduction
            }
        }

        return salePrice.setScale(2, RoundingMode.HALF_UP);
    }

}
