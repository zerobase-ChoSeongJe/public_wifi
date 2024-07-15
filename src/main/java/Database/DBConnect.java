package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnect {
    public static Connection connectDB() {

        // SQLite 데이터베이스 파일 위치
        String dbFilePath = "C:/zerobase/public_wifi/wifi_spot.db";
        String url = "jdbc:sqlite:" + dbFilePath;

        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");  // JDBC 드라이버 로드
            connection = DriverManager.getConnection(url);

            // 테이블 생성
            createWifiTable(connection);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    private static void createWifiTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS public_wifi ( " +
                "id TEXT PRIMARY KEY, " +
                "district TEXT, " +
                "wifi_name TEXT, " +
                "road_address TEXT, " +
                "detail_address TEXT, " +
                "instalization_floor TEXT, " +
                "instalization_type TEXT, " +
                "instalization_agency TEXT, " +
                "service_type TEXT, " +
                "mesh_type TEXT, " +
                "instalization_year TEXT, " +
                "indoor_outdoor TEXT, " +
                "wifi_invironment TEXT, " +
                "y_coord TEXT, " +
                "x_coord TEXT, " +
                "work_date TEXT )";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
    }

    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
