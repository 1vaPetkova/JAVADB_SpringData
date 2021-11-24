package softuni.exam.common;

public final class Constants {


    public final static String PASSENGERS_PATH = "src/main/resources/files/json/passengers.json";
    public final static String TOWNS_PATH = "src/main/resources/files/json/towns.json";
    public final static String PLANES_PATH = "src/main/resources/files/xml/planes.xml";
    public final static String TICKETS_PATH = "src/main/resources/files/xml/tickets.xml";

    public final static String INCORRECT_DATA_MESSAGE = "Error: Invalid %s";
    public final static String SUCCESSFUL_IMPORT_MESSAGE = "Successfully imported %s %s.";


    public static void getIncorrectDataMessage(StringBuilder sb, String entity) {
        sb.append(String.format(INCORRECT_DATA_MESSAGE,entity)).append(System.lineSeparator());
    }

    public static void getSuccessTownMessage(StringBuilder sb, String name, Integer population) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Town", name + " - " + population))
                .append(System.lineSeparator());
    }

    public static void getSuccessPassengerMessage(StringBuilder sb, String lastName, String email) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Passenger", lastName + " - " + email))
                .append(System.lineSeparator());
    }

    public static void getSuccessPlaneMessage(StringBuilder sb, String registerNumber) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Plane", registerNumber))
                .append(System.lineSeparator());
    }

    public static void getSuccessTicketMessage(StringBuilder sb, String fromTown, String toTown) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Plane", fromTown + " - " + toTown))
                .append(System.lineSeparator());
    }

}
