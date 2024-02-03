package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
        if (this.name == null || this.name == "") {
            throw new IllegalArgumentException("Product name can't be null or empty");
        }
        if (this.price == null || this.price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price can't be null or less than zero");
        }
    }

    public String getName() {
       return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getTaxPercent() {
        return this.taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return this.price.add(this.price.multiply(this.taxPercent));
    }
}
