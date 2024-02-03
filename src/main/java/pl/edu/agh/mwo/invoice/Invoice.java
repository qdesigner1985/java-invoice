package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private Collection<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
            products.add(product);
    }

    public void addProduct(Product product, Integer quantity) {
        if (quantity == 0 || quantity < 0) {
            throw new IllegalArgumentException("Product quantity can't be null or less than zero ");
        }
        for (int i = 0; i < quantity; i++)
        {
            products.add(product);
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal subTotal= new BigDecimal(0);
        for (Product product : this.products) {
            subTotal = subTotal.add(product.getPrice());
        }
        return subTotal;
    }

    public BigDecimal getTax() {
        BigDecimal tax= new BigDecimal(0);
        for (Product product : this.products) {
            tax = tax.add(product.getPriceWithTax().subtract(product.getPrice()));
        }
        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal total= new BigDecimal(0);
        for (Product product : this.products) {
            total = total.add(product.getPriceWithTax());
        }
        return total;
    }
}
