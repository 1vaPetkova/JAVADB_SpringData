package exam.common;

import java.math.BigDecimal;

public final class Constants {


    public final static String CUSTOMERS_PATH = "src/main/resources/files/json/customers.json";
    public final static String LAPTOPS_PATH = "src/main/resources/files/json/laptops.json";
    public final static String SHOPS_PATH = "src/main/resources/files/xml/shops.xml";
    public final static String TOWNS_PATH = "src/main/resources/files/xml/towns.xml";

    public final static String INCORRECT_DATA_MESSAGE = "Invalid %s";
    public final static String SUCCESSFUL_IMPORT_MESSAGE = "Successfully imported %s %s";


    public static void getIncorrectDataMessage(StringBuilder sb, String entity) {
        sb.append(String.format(INCORRECT_DATA_MESSAGE, entity)).append(System.lineSeparator());
    }

    public static void getSuccessTownMessage(StringBuilder sb, String townName) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Town", townName))
                .append(System.lineSeparator());
    }

    public static void getSuccessShopMessage(StringBuilder sb, String shopName, BigDecimal income) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Shop", shopName + " - " + String.format("%.0f", income)))
                .append(System.lineSeparator());
    }

    public static void getSuccessCustomerMessage(StringBuilder sb, String firstName, String lastName, String email) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Customer", firstName + " " + lastName + " - " + email))
                .append(System.lineSeparator());
    }

    public static void getSuccessLaptopMessage(StringBuilder sb, String macAddress, Double cpuSpeed, Integer ram, Integer storage) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Customer",
                        macAddress + " - " + String.format("%.2f",cpuSpeed) + " - " + ram + " - " + storage))
                .append(System.lineSeparator());
    }

}
