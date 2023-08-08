package pdfthymeleaf;

import com.lowagie.text.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PDFThymeleafExample {

    public static void main(String[] args) throws IOException, DocumentException {
        PDFThymeleafExample thymeleaf2Pdf = new PDFThymeleafExample();
        String html = thymeleaf2Pdf.parseThymeleafTemplate();
        thymeleaf2Pdf.generatePdfFromHtml(html);
    }

    public void generatePdfFromHtml(String html) throws IOException, DocumentException {
        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    private String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("invoice_number", UUID.randomUUID());
        Address sender = new Address();
        sender.setCompany("Bujanovac DOO");
        sender.setAddress("Bujanovac BB, jer ulice nemamo");
        sender.setPhone("Ne javljam se na telefon");
        sender.setOib("Ovo je kod nas PIB");
        context.setVariable("sender", sender);
        Address buyer = new Address();
        buyer.setCompany("VRANJE DOO");
        buyer.setAddress("Kod glavnu postu");
        buyer.setPhone("Nemamo fixi");
        buyer.setOib("Sta je OIB");
        context.setVariable("buyer", buyer);
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("zuto");
        product.setPrice("1000");
        product.setQuantity("1 gram");
        products.add(product);
        Product product1 = new Product();
        product1.setName("belo");
        product1.setPrice("750");
        product1.setQuantity("1 gram");
        products.add(product1);
        Product product2 = new Product();
        product2.setName("zeleno");
        product2.setPrice("10");
        product2.setQuantity("5 grama");
        products.add(product2);
        context.setVariable("products", products);
        AtomicInteger sum = new AtomicInteger(0);
        products.forEach(p -> {
            sum.set(sum.get() + Integer.parseInt(p.getPrice()));
        });
        context.setVariable("sum", sum);

        return templateEngine.process("thymeleaf_template", context);
    }
}

class Address {
    private String company;
    private String address;
    private String phone;
    private String oib;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }
}

class Product {
    private String name;
    private String quantity;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
