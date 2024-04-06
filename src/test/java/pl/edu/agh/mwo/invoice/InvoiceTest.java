package pl.edu.agh.mwo.invoice;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import pl.edu.agh.mwo.invoice.product.*;

import java.math.BigDecimal;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasNumber() {
        int number = invoice.getNumber();
        Assert.assertTrue(number > 0);
    }

    @Test
    public void testInvoiceHasNumberGratherThanZero(){
        Assert.assertTrue(new Invoice().getNumber()>0);
    }

    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        Assert.assertNotEquals(invoice.getNumber(), new Invoice().getNumber());
    }

    @Test
    public void testTheSameInvoiceHasTheSameNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testNextInvoicesHaveNextNumbers() {
        Assert.assertEquals(invoice.getNumber() + 1, new Invoice().getNumber());
    }

    @Test
    public void testInvoiceSummaryEmpty() {
        String faktura = "FV nr: " + invoice.getNumber() + "\n" +
                "Ilość produktów: 0";
        Assert.assertEquals(invoice.getSummary(), faktura);
    }

    @Test
    public void testInvoiceSummaryOneProduct() {
        invoice.addProduct(new DairyProduct("P1", new BigDecimal(12)), 5);
        String faktura = "FV nr: " + invoice.getNumber() + "\n" +
                "P1\t5\t12\n" +
                "Ilość produktów: 1";
        Assert.assertEquals(invoice.getSummary(), faktura);
    }

    @Test
    public void testInvoiceSummaryDuplicates() {
        DairyProduct prod1 = new DairyProduct("Product 1", new BigDecimal(15));
        DairyProduct prod2 = new DairyProduct("Product 2", new BigDecimal(3));
        DairyProduct prod3 = new DairyProduct("Product 3", new BigDecimal(8));
        invoice.addProduct(prod1, 4);
        invoice.addProduct(prod1, 4);
        invoice.addProduct(prod1);
        invoice.addProduct(prod2, 1);
        invoice.addProduct(prod2, 1);
        invoice.addProduct(prod2);
        invoice.addProduct(prod3, 6);
        invoice.addProduct(prod3, 6);
        invoice.addProduct(prod3);
        String sb1 = "FV nr: " + invoice.getNumber() + "\n" +
                "Product 1\t9\t15\n" +
                "Product 2\t3\t3\n" +
                "Product 3\t13\t8\n" +
                "Ilość produktów: 3";
        Assert.assertEquals(invoice.getSummary(), sb1);
    }

    @Test
    public void testAddProductsDuplicates() {
        DairyProduct prod1 = new DairyProduct("Product 1", new BigDecimal(15));
        DairyProduct prod2 = new DairyProduct("Product 2", new BigDecimal(3));
        DairyProduct prod3 = new DairyProduct("Product 3", new BigDecimal(8));
        invoice.addProduct(prod1, 3);
        invoice.addProduct(prod2, 5);
        invoice.addProduct(prod3, 8);
        invoice.addProduct(prod3, 8);
        invoice.addProduct(prod2, 5);
        invoice.addProduct(prod1, 3);
        Assert.assertEquals(invoice.getProductsNumber(), 3);
    }

    @Test
    public void testPricesProductsWithExciseMotherInLawDay() {
        DairyProduct prod1 = new DairyProduct("Product 1", new BigDecimal("10"));
        BottleOfWine bottleOfWine1 = new BottleOfWine("Bottle of wine 1", new BigDecimal("11"));
        BottleOfWine bottleOfWine2 = new BottleOfWine("Bottle of wine 2", new BigDecimal("18"));
        FuelCanister fuelCanister1 = new FuelCanister("Fuel canister 1", new BigDecimal("200"));
        FuelCanister fuelCanister2 = new FuelCanister("Fuel canister 2", new BigDecimal("200"));
        invoice.setInvoiceDate(LocalDate.parse("2024-03-05"));
        invoice.addProduct(prod1);
        invoice.addProduct(bottleOfWine1);
        invoice.addProduct(bottleOfWine2);
        invoice.addProduct(fuelCanister1);
        invoice.addProduct(fuelCanister2);
        Assert.assertEquals(invoice.getGrossTotal(), new BigDecimal("468.71"));
    }

    @Test
    public void testPricesProductsWithExciseOrdinaryDay() {
        DairyProduct prod1 = new DairyProduct("Product 1", new BigDecimal("10"));
        BottleOfWine bottleOfWine1 = new BottleOfWine("Bottle of wine 1", new BigDecimal("11"));
        BottleOfWine bottleOfWine2 = new BottleOfWine("Bottle of wine 2", new BigDecimal("18"));
        FuelCanister fuelCanister1 = new FuelCanister("Fuel canister 1", new BigDecimal("200"));
        FuelCanister fuelCanister2 = new FuelCanister("Fuel canister 2", new BigDecimal("200"));
        invoice.setInvoiceDate(LocalDate.parse("2023-11-10"));
        invoice.addProduct(prod1);
        invoice.addProduct(bottleOfWine1);
        invoice.addProduct(bottleOfWine2);
        invoice.addProduct(fuelCanister1);
        invoice.addProduct(fuelCanister2);
        Assert.assertEquals(invoice.getGrossTotal(), new BigDecimal("560.71"));
    }
}