package mostwanted.common;

public class Constants {


    public final static String DUPLICATE_DATA_MESSAGE = "Error: Duplicate Data!";

    public final static String INCORRECT_DATA_MESSAGE = "Error: Incorrect Data!";

    public final static String SUCCESSFUL_IMPORT_MESSAGE = "Successfully imported %s – %s.";

    public static void getIncorrectDataMessage(StringBuilder sb) {
        sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
    }

    public static void getDuplicateMessage(StringBuilder sb) {
        sb.append(DUPLICATE_DATA_MESSAGE).append(System.lineSeparator());
    }

    public static void getSuccessMessage(StringBuilder sb,String first, String second) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,first,second))
                .append(System.lineSeparator());
    }
}
