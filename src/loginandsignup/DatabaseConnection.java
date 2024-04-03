package loginandsignup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; 
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/portaljob";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean signup(String fullName, String email, String password, String phoneNumber, String city, String country, String Age) {
        try (Connection conn = connect()) {
            if (conn == null) {
                return false;
            }

            String sql = "INSERT INTO users (full_name, email, password, phone_number, city, country,Age) VALUES (?, ?, ?, ?, ?, ?,?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, phoneNumber);
                preparedStatement.setString(5, city);
                preparedStatement.setString(6, country);
                 preparedStatement.setString(7, Age);

                int rowsInserted = preparedStatement.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
public static boolean addCandidate(String nomp, String headp, String leaderp, String nump) {
    try (Connection conn = connect()) {
        if (conn == null) {
            return false;
        }

        String sql = "INSERT INTO candidate (nomp, headp, leaderp, nump) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, nomp);
            preparedStatement.setString(2, headp);
            preparedStatement.setString(3, leaderp);
            preparedStatement.setString(4, nump);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public static boolean addCandidateWithImage(String nomp, String headp, String leaderp, String nump, byte[] imageData) {
    try (Connection conn = connect()) {
        if (conn == null) {
            return false;
        }

        String sql = "INSERT INTO candidate (nomp, headp, leaderp, nump, partysign) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, nomp);
            preparedStatement.setString(2, headp);
            preparedStatement.setString(3, leaderp);
            preparedStatement.setString(4, nump);
            preparedStatement.setBytes(5, imageData);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public static boolean updateCandidate(String originalPartyName, String updatedNomp, String updatedHeadp, String updatedLeaderp, String updatedNump, byte[] updatedImageData) {
    try (Connection conn = connect()) {
        if (conn == null) {
            return false;
        }

        // Vérifier si la partie avec le nom d'origine existe
        if (!partyExists(originalPartyName)) {
            return false; // Partie non trouvée, échec de la mise à jour
        }

        String sql = "UPDATE candidate SET nomp=?, headp=?, leaderp=?, nump=?, partysign=? WHERE nomp=?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, updatedNomp);
            preparedStatement.setString(2, updatedHeadp);
            preparedStatement.setString(3, updatedLeaderp);
            preparedStatement.setString(4, updatedNump);
            preparedStatement.setBytes(5, updatedImageData);
            preparedStatement.setString(6, originalPartyName);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

private static boolean partyExists(String partyName) {
    try (Connection conn = connect()) {
        if (conn == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM candidate WHERE nomp=?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, partyName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public static boolean checkPartyExists(String partyName) {
    try (Connection conn = connect()) {
        if (conn == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM candidate WHERE nomp = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, partyName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }

    return false;
}
public static boolean login(String email, String password) {
    try (Connection conn = connect()) {
        if (conn == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM users WHERE email=? AND password=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    }

