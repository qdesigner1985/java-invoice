package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;
import java.util.HashMap;

public class Invoice {
    private final HashMap<Product, Integer> productsQ  = new HashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
            productsQ.put(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (quantity == 0 || quantity < 0) {
            throw new IllegalArgumentException("Product quantity can't be null or less than zero ");
        }
        productsQ.put(product,quantity);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subTotal= new BigDecimal(0);
        for (Product product : this.productsQ.keySet()) {
            subTotal = subTotal.add(product.getPrice().multiply(BigDecimal.valueOf(productsQ.get(product))));
        }
        return subTotal;
    }

    public BigDecimal getTax() {
        BigDecimal tax= new BigDecimal(0);
        for (Product product : this.productsQ.keySet()) {
            tax = tax.add(product.getPriceWithTax().subtract(product.getPrice()));
        }
        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal total= new BigDecimal(0);
        for (Product product : this.productsQ.keySet()) {
            total = total.add(product.getPriceWithTax().multiply(BigDecimal.valueOf(productsQ.get(product))));
        }
        return total;
    }
}
