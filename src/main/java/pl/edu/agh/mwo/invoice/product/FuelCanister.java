package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import pl.edu.agh.mwo.invoice.Invoice;

public class FuelCanister extends Product {
    public FuelCanister(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.23"), new BigDecimal("5.56"));
    }

    @Override
    public BigDecimal getPriceWithTax() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        String saleDate = "05.03";
        String currentDate = Invoice.currentDate.format(formatter);
        if (currentDate.equals(saleDate)) {
            return this.getPrice().add(getExcise());
        } else {
            return super.getPriceWithTax();
        }
    }
}
