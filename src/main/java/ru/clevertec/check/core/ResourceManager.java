package ru.clevertec.check.core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceManager {
    private static final String[] PRODUCTS_HEADERS = {"id", "description", "price", "quantity_in_stock", "wholesale_product"};
    private static final String[] DISCOUNT_CARDS_HEADERS = {"id", "number", "discount_amount"};

    public static List<Product> getResourceProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        Reader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getResourceAsStream("/products.csv"))));

        return getProducts(products, reader);
    }

    public static List<DiscountCard> getResourceDiscountCards() throws IOException {
        List<DiscountCard> discountCards = new ArrayList<>();
        Reader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getResourceAsStream("/discountCards.csv"))));

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(DISCOUNT_CARDS_HEADERS)
                .setSkipHeaderRecord(true)
                .setDelimiter(";")
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(reader);
        for (CSVRecord record : records) {
            discountCards.add(new DiscountCard(Integer.parseInt(record.get(0)), Integer.parseInt(record.get(1)), Integer.parseInt(record.get(2))));
        }

        return discountCards;
    }

    public static List<Product> getPathProducts(String path) throws IOException {
        List<Product> products = new ArrayList<>();
        File file = new File(path);

        Reader reader = new BufferedReader(new FileReader(file));

        return getProducts(products, reader);
    }

    private static List<Product> getProducts(List<Product> products, Reader reader) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(PRODUCTS_HEADERS)
                .setSkipHeaderRecord(true)
                .setDelimiter(";")
                .build();
        Iterable<CSVRecord> records = csvFormat.parse(reader);
        for (CSVRecord record : records) {
            if (record.stream().count() == 5) {
                products.add(new Product(Integer.parseInt(record.get(0)), record.get(1), Double.parseDouble(record.get(2)), Integer.parseInt(record.get(3)), Boolean.parseBoolean(record.get(4))));
            }
        }

        return products;
    }

}
