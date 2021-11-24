package hiberspring.common;

public final class Constants {

    public final static String PATH_TO_FILES = System.getProperty("user.dir") + "/src/main/resources/files/";
    public final static String BRANCHES_PATH = PATH_TO_FILES + "branches.json";
    public final static String EMPLOYEE_CARDS_PATH = PATH_TO_FILES + "employee-cards.json";
    public final static String EMPLOYEES_PATH = PATH_TO_FILES + "employees.xml";
    public final static String PRODUCTS_PATH = PATH_TO_FILES + "products.xml";
    public final static String TOWNS_PATH = PATH_TO_FILES + "towns.json";

    public final static String INCORRECT_DATA_MESSAGE = "Error: Invalid Data!";
    public final static String SUCCESSFUL_IMPORT_MESSAGE = "Successfully imported %s %s.";


    public static void getIncorrectDataMessage(StringBuilder sb) {
        sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
    }

    public static void getSuccessTownMessage(StringBuilder sb, String name) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Town", name))
                .append(System.lineSeparator());
    }

    public static void getSuccessBranchMessage(StringBuilder sb, String name) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Branch", name))
                .append(System.lineSeparator());
    }

    public static void getSuccessEmployeeCardMessage(StringBuilder sb, String cardNumber) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Employee Card", cardNumber))
                .append(System.lineSeparator());
    }

    public static void getSuccessProductMessage(StringBuilder sb, String productName) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Product", productName))
                .append(System.lineSeparator());
    }

    public static void getSuccessEmployeeMessage(StringBuilder sb, String firstName, String lastName) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Employee", firstName + " " + lastName))
                .append(System.lineSeparator());
    }

}
