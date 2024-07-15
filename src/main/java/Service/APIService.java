package Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.URL;

import dao.WifiDAO;
import static dao.WifiDAO.insertPublicWifi;

public class APIService {
    private static String API_URL = "http://openapi.seoul.go.kr:8088/7647726b6b6a736a3633515379554a/json/TbPublicWifiInfo/";
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static int WifiTotalCount() throws IOException{         //Wi-fi 갯수 구하기
        int cnt = 0;

        URL url = new URL(API_URL + "1/1");

        //URL 요청
        Request.Builder builder = new Request.Builder().url(url).get();

        //URL 응답
        Response response = okHttpClient.newCall(builder.build()).execute();

        try {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    JsonElement jsonElement = JsonParser.parseString(responseBody.string());

                    cnt = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                            .getAsJsonObject().get("list_total_count")
                            .getAsInt();

                    System.out.println("찾은 와이파이 개수 = " + cnt);
                }

            } else {
                System.out.println("API 호출 실패: " + response.code());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    public static int getPublicWifiJson() throws IOException {
        int totalCnt = WifiTotalCount();
        int start = 1, end = 1;
        int count = 0;

        try {
            for (int i = 0; i <= totalCnt / 1000; i++) {
                start = 1 + (1000 * i);
                end = (i + 1) * 1000;

                URL url = new URL(API_URL + start + "/" + end);

                Request.Builder builder = new Request.Builder().url(url).get();
                Response response = okHttpClient.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        JsonElement jsonElement = JsonParser.parseString(responseBody.string());

                        JsonArray jsonArray = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                                .getAsJsonObject().get("row")
                                .getAsJsonArray();

                        count += insertPublicWifi(jsonArray);   //데이터 로드 갯수

                    } else {
                        System.out.println("API 호출 실패: " + response.code());
                    }
                } else {
                    System.out.println("API 호출 실패: " + response.code());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}