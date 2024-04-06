package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.time.LocalDate;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

    public static LocalDate currentDate = LocalDate.now();
    public static int lastNumber;
    private final int number;

    public Invoice() {
        this.number = ++lastNumber;
    }

    private final Map<Product, Integer> products = new LinkedHashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }
        if (products.containsKey(product)) {
            products.put(product, (products.get(product) + 1));
        } else {
            products.put(product, 1);
        }
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        if (products.containsKey(product)) {
            products.put(product, (products.get(product) + quantity));
        } else {
            products.put(product, quantity);
        }
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return number;
    }

    public String getSummary() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("FV nr: ").append(this.number).append("\n");
        for (Product product : products.keySet()) {
            sb1.append(product.getName()).append("\t").append(products.get(product)).append("\t")
                    .append(product.getPrice()).append("\n");
        }
        sb1.append("Ilość produktów: ").append(products.size());

        return sb1.toString();
    }

    public int getProductsNumber() {
        return products.size();
    }

    public void setInvoiceDate(LocalDate date) {
        Invoice.currentDate = date;
    }
}