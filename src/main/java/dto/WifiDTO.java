package dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class WifiDTO {
    private double distance;        //거리
    private String id;     //관리번호
    private String district;     //자치구
    private String wifi_name;    //와이파이명
    private String road_address;    //도로명 주소
    private String detail_address;    //상세 주소
    private String instalization_floor;    //설치위치(층)
    private String instalization_type;   //설치 유형
    private String instalization_agency;  //설치 기관
    private String service_type;     //서비스 구분
    private String mesh_type;     //망 종류
    private String instalization_year; //설치년도
    private String indoor_outdoor;  // 실내외구분
    private String wifi_invironment;     // wifi 접속환경
    private String y_coord;                 // y좌표
    private String x_coord;                 // y좌표
    private String work_date;    // 작업일자

}