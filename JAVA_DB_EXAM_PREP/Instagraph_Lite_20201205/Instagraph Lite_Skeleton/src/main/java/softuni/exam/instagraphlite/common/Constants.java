package softuni.exam.instagraphlite.common;

public class Constants {


    public static final String PICTURES_PATH = "src/main/resources/files/pictures.json";
    public static final String USERS_PATH = "src/main/resources/files/users.json";
    public static final String POSTS_PATH = "src/main/resources/files/posts.xml";

    public final static String INVALID_ENTITY_MESSAGE = "Invalid %s";

    public final static String SUCCESSFUL_IMPORT_PICTURE_MESSAGE = "Successfully imported Picture, with size %.2f";
    public final static String SUCCESSFUL_IMPORT_USER_MESSAGE = "Successfully imported User: %s";
    public final static String SUCCESSFUL_IMPORT_POST_MESSAGE = "Successfully imported Post, made by %s";

    public static void getIncorrectDataMessage(StringBuilder sb, String entity) {
        sb.append(String.format(INVALID_ENTITY_MESSAGE, entity)).append(System.lineSeparator());
    }

    public static void getSuccessPictureMessage(StringBuilder sb, Double size) {
        sb.append(String.format(SUCCESSFUL_IMPORT_PICTURE_MESSAGE, size))
                .append(System.lineSeparator());
    }

    public static void getSuccessUserMessage(StringBuilder sb, String username) {
        sb.append(String.format(SUCCESSFUL_IMPORT_USER_MESSAGE, username))
                .append(System.lineSeparator());
    }

    public static void getSuccessPostMessage(StringBuilder sb, String username) {
        sb.append(String.format(SUCCESSFUL_IMPORT_POST_MESSAGE, username))
                .append(System.lineSeparator());
    }
}
