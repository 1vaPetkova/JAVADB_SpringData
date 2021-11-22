package softuni.exam.common;

public class Constants {


    public static final String PLAYERS_PATH = "src/main/resources/files/json/players.json";
    public static final String PICTURES_PATH = "src/main/resources/files/xml/pictures.xml";
    public static final String TEAMS_PATH = "src/main/resources/files/xml/teams.xml";

    public final static String INVALID_ENTITY_MESSAGE = "Invalid %s";

    public final static String SUCCESSFUL_IMPORT_MESSAGE = "Successfully imported %s- %s";


    public static void getIncorrectDataMessage(StringBuilder sb, String entity) {
        sb.append(String.format(INVALID_ENTITY_MESSAGE, entity)).append(System.lineSeparator());
    }

    public static void getSuccessPictureMessage(StringBuilder sb, String url) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "picture", url))
                .append(System.lineSeparator());
    }

    public static void getSuccessTeamMessage(StringBuilder sb, String name) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "team", name))
                .append(System.lineSeparator());
    }


    public static void getSuccessPlayerMessage(StringBuilder sb, String firstName, String lastName) {
        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "player", firstName + " " + lastName))
                .append(System.lineSeparator());
    }

}
