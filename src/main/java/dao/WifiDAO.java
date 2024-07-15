package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dto.WifiDTO;
import Database.DBConnect;

public class WifiDAO {
    public static Connection connection;
    public static ResultSet resultSet;
    public static PreparedStatement preparedStatement;

    public WifiDAO() {

    }

    public static int insertPublicWifi(JsonArray jsonArray) {
        connection = null;
        preparedStatement = null;
        resultSet = null;

        int count = 0;

        /* 쿼리 문 설정 */
        try {
            connection = DBConnect.connectDB();
            connection.setAutoCommit(false);    //Auto-Commit 해제

            /* Insert 진행 */
            String sql = " insert into public_wifi "
                    + " ( id, district, wifi_name, road_address, detail_address, "
                    + " instalization_floor, instalization_type, instalization_agency, service_type, mesh_type, "
                    + " instalization_year, indoor_outdoor, wifi_invironment, y_coord, x_coord, work_date) "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject data = (JsonObject) jsonArray.get(i).getAsJsonObject();

                preparedStatement.setString(1, data.get("id").getAsString());
                preparedStatement.setString(2, data.get("district").getAsString());
                preparedStatement.setString(3, data.get("wifi_name").getAsString());
                preparedStatement.setString(4, data.get("road_address").getAsString());
                preparedStatement.setString(5, data.get("detail_address").getAsString());
                preparedStatement.setString(6, data.get("instalization_floor").getAsString());
                preparedStatement.setString(7, data.get("instalization_type").getAsString());
                preparedStatement.setString(8, data.get("instalization_agency").getAsString());
                preparedStatement.setString(9, data.get("service_type").getAsString());
                preparedStatement.setString(10, data.get("mesh_type").getAsString());
                preparedStatement.setString(11, data.get("instalization_year").getAsString());
                preparedStatement.setString(12, data.get("indoor_outdoor").getAsString());
                preparedStatement.setString(13, data.get("wifi_invironment").getAsString());
                preparedStatement.setString(14, data.get("y_coord").getAsString());
                preparedStatement.setString(15, data.get("x_coord").getAsString());
                preparedStatement.setString(16, data.get("work_date").getAsString());

                preparedStatement.addBatch();
                preparedStatement.clearParameters();

                //1000개 기준으로 임시 batch 실행
                if ((i + 1) % 1000 == 0) {
                    int[] result = preparedStatement.executeBatch();
                    count += result.length;    //배치한 완료 개수
                    connection.commit();
                }
            }

            int[] result = preparedStatement.executeBatch();
            count += result.length;    //배치한 완료 개수
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        } finally {
            DBConnect.close(connection, preparedStatement, resultSet);
        }

        return count;
    }

    public List<WifiDTO> getNearestWifiList(String lat, String lnt) {

        connection = null;
        preparedStatement = null;
        resultSet = null;

        List<WifiDTO> list = new ArrayList<>();

        try {

            connection = DBConnect.connectDB();


            String sql = " SELECT *, " +
                    " round(6371*acos(cos(radians(?))*cos(radians(x_coord))*cos(radians(y_coord) " +
                    " -radians(?))+sin(radians(?))*sin(radians(x_coord))), 4) " +
                    " AS distance " +
                    " FROM public_wifi " +
                    " ORDER BY distance " +
                    " LIMIT 20;";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, Double.parseDouble(lat));
            preparedStatement.setDouble(2, Double.parseDouble(lnt));
            preparedStatement.setDouble(3, Double.parseDouble(lat));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                        .distance(resultSet.getDouble("distance"))
                        .id(resultSet.getString("id"))
                        .district(resultSet.getString("district"))
                        .wifi_name(resultSet.getString("wifi_name"))
                        .road_address(resultSet.getString("road_address"))
                        .detail_address(resultSet.getString("detail_address"))
                        .instalization_floor(resultSet.getString("instalization_floor"))
                        .instalization_type(resultSet.getString("instalization_type"))
                        .instalization_agency(resultSet.getString("instalization_agency"))
                        .service_type(resultSet.getString("service_type"))
                        .mesh_type(resultSet.getString("mesh_type"))
                        .instalization_year(resultSet.getString("instalization_year"))
                        .indoor_outdoor(resultSet.getString("indoor_outdoor"))
                        .wifi_invironment(resultSet.getString("wifi_invironment"))
                        .y_coord(resultSet.getString("y_coord"))
                        .x_coord(resultSet.getString("x_coord"))
                        .work_date(String.valueOf(resultSet.getTimestamp("work_date").toLocalDateTime()))
                        .build();

                list.add(wifiDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(connection, preparedStatement, resultSet);
        }

        return list;
    }

    public List<WifiDTO> selectWifiList(String mgrNo, double distance) {

        connection = null;
        preparedStatement = null;
        resultSet = null;

        List<WifiDTO> list = new ArrayList<>();

        try {
            connection = DBConnect.connectDB();
            String sql = " select * from public_wifi where x_swifi_mgr_no = ? ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mgrNo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                        .distance(distance)
                        .id(resultSet.getString("id"))
                        .district(resultSet.getString("district"))
                        .wifi_name(resultSet.getString("wifi_name"))
                        .road_address(resultSet.getString("road_address"))
                        .detail_address(resultSet.getString("detail_address"))
                        .instalization_floor(resultSet.getString("instalization_floor"))
                        .instalization_type(resultSet.getString("instalization_type"))
                        .instalization_agency(resultSet.getString("instalization_agency"))
                        .service_type(resultSet.getString("service_type"))
                        .mesh_type(resultSet.getString("mesh_type"))
                        .instalization_year(resultSet.getString("instalization_year"))
                        .indoor_outdoor(resultSet.getString("indoor_outdoor"))
                        .wifi_invironment(resultSet.getString("wifi_invironment"))
                        .y_coord(resultSet.getString("y_coord"))
                        .x_coord(resultSet.getString("x_coord"))
                        .work_date(String.valueOf(resultSet.getTimestamp("work_date").toLocalDateTime()))
                        .build();
                list.add(wifiDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(connection, preparedStatement, resultSet);
        }

        return list;
    }

    public WifiDTO selectWifi(String mgrNo) {
        WifiDTO wifiDTO = new WifiDTO();

        connection = null;
        preparedStatement = null;
        resultSet = null;

        try {
            connection = DBConnect.connectDB();
            String sql = " select * from public_wifi where x_swifi_mgr_no = ? ";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, mgrNo);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                wifiDTO = WifiDTO.builder()
                        .id(resultSet.getString("id"))
                        .district(resultSet.getString("district"))
                        .wifi_name(resultSet.getString("wifi_name"))
                        .road_address(resultSet.getString("road_address"))
                        .detail_address(resultSet.getString("detail_address"))
                        .instalization_floor(resultSet.getString("instalization_floor"))
                        .instalization_type(resultSet.getString("instalization_type"))
                        .instalization_agency(resultSet.getString("instalization_agency"))
                        .service_type(resultSet.getString("service_type"))
                        .mesh_type(resultSet.getString("mesh_type"))
                        .instalization_year(resultSet.getString("instalization_year"))
                        .indoor_outdoor(resultSet.getString("indoor_outdoor"))
                        .wifi_invironment(resultSet.getString("wifi_invironment"))
                        .y_coord(resultSet.getString("y_coord"))
                        .x_coord(resultSet.getString("x_coord"))
                        .work_date(String.valueOf(resultSet.getTimestamp("work_date").toLocalDateTime()))
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.close(connection, preparedStatement, resultSet);
        }

        return wifiDTO;
    }
}