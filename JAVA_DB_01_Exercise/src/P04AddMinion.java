import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class P04AddMinion {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws IOException, SQLException {
        connection = ConnectionSetUp.getConnection();
        System.out.println("Enter minion and villain: ");
        String[] minionData = reader.readLine().split(":\\s+")[1].split("\\s+");
        String minionName = minionData[0];
        int minionAge = Integer.parseInt(minionData[1]);
        String townName = minionData[2];
        String villainName = reader.readLine().split(":\\s+")[1];

        //Add townName if not present in DB
        int townId = findEntityIdByName("towns", townName);
        if (townId == 0) {
            PreparedStatement psTown = connection.prepareStatement("INSERT INTO towns (name) VALUES (?);");
            psTown.setString(1, townName);
            psTown.execute();
            townId = findEntityIdByName("towns", townName);
            System.out.printf("Town %s was added to the database.\n", townName);
        }
        //Add villain if not present in DB
        int villainId = findEntityIdByName("villains", villainName);
        if (villainId == 0) {
            PreparedStatement psVillain = connection.prepareStatement(
                    "INSERT INTO villains (name, evilness_factor) VALUES (?, 'evil');"
            );
            psVillain.setString(1, villainName);
            psVillain.execute();
            System.out.printf("Villain %s was added to the database.\n", villainName);
            villainId = findEntityIdByName("villains", villainName);
        }
        //Add minion to DB
        int minionId = findEntityIdByNameAndAge("minions", minionName, minionAge);
        if (minionId == 0) {
            PreparedStatement psAddMinion = connection.prepareStatement(
                    "INSERT INTO minions (name,age,town_id) VALUES (?, ?, ?);"
            );
            psAddMinion.setString(1, minionName);
            psAddMinion.setInt(2, minionAge);
            psAddMinion.setInt(3, townId);
            psAddMinion.execute();
            minionId = findEntityIdByNameAndAge("minions", minionName, minionAge);
        }
        // Set the new minion to be servant of the villain
        PreparedStatement psSetMinionToVillain = connection.prepareStatement(
                "INSERT INTO minions_villains VALUES (?,?);"
        );
        psSetMinionToVillain.setInt(1, minionId);
        psSetMinionToVillain.setInt(2, villainId);
        psSetMinionToVillain.execute();
        System.out.printf("Successfully added %s to be minion of %s.\n", minionName, villainName);
        System.out.println();
    }

    private static int findEntityIdByNameAndAge(String tableName, String minionName, int minionAge) throws SQLException {
        PreparedStatement psName = connection.prepareStatement(
                "SELECT id FROM " + tableName + " WHERE name = ? AND age = ?;"
        );
        psName.setString(1, minionName);
        psName.setInt(2, minionAge);
        ResultSet rs = psName.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        return 0;
    }

    private static int findEntityIdByName(String tableName, String entityName) throws SQLException {
        PreparedStatement psName = connection.prepareStatement("SELECT id FROM " + tableName + " WHERE name = ?;");
        psName.setString(1, entityName);
        ResultSet rs = psName.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
        return 0;
    }

}
