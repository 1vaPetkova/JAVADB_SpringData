package com.example.football.common;

public class Constants {


    public static final String TEAMS_PATH = "src/main/resources/files/json/teams.json";
    public static final String TOWNS_PATH = "src/main/resources/files/json/towns.json";
    public static final String PLAYERS_PATH = "src/main/resources/files/xml/players.xml";
    public static final String STATS_PATH = "src/main/resources/files/xml/stats.xml";

    public final static String INVALID_ENTITY_MESSAGE = "Invalid %s";

    public final static String SUCCESSFUL_IMPORT_TOWN_MESSAGE = "Successfully imported Town %s - %d";
    public final static String SUCCESSFUL_IMPORT_TEAM_MESSAGE = "Successfully imported Team %s - %s";
    public final static String SUCCESSFUL_IMPORT_STAT_MESSAGE = "Successfully imported Stat %.2f-%.2f-%.2f";
    public final static String SUCCESSFUL_IMPORT_PLAYER_MESSAGE = "Successfully imported Player %s %s - %s";


    public static void getIncorrectDataMessage(StringBuilder sb, String entity) {
        sb.append(String.format(INVALID_ENTITY_MESSAGE, entity)).append(System.lineSeparator());
    }

    public static void getSuccessTownMessage(StringBuilder sb, String name, Integer population) {
        sb.append(String.format(SUCCESSFUL_IMPORT_TOWN_MESSAGE, name, population))
                .append(System.lineSeparator());
    }

    public static void getSuccessTeamMessage(StringBuilder sb, String teamName, Integer fanBase) {
        sb.append(String.format(SUCCESSFUL_IMPORT_TEAM_MESSAGE, teamName, fanBase))
                .append(System.lineSeparator());
    }

    public static void getSuccessStatMessage(StringBuilder sb, Double passing, Double shooting, Double endurance) {
        sb.append(String.format(SUCCESSFUL_IMPORT_STAT_MESSAGE, passing, shooting, endurance))
                .append(System.lineSeparator());
    }

    public static void getSuccessPlayerMessage(StringBuilder sb, String firstName, String lastName, String position) {
        sb.append(String.format(SUCCESSFUL_IMPORT_PLAYER_MESSAGE, firstName, lastName, position))
                .append(System.lineSeparator());
    }

}
