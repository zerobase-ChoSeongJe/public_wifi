<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="dao.WifiDAO" %>
<%@ page import="java.util.*" %>
<%@ page import="dto.WifiDTO" %>

<html>
<head>
    <title>와이파이 정보 구하기</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<%
    String lat = request.getParameter("lat") == null ? "0.0" : request.getParameter("lat");
    String lnt = request.getParameter("lnt") == null ? "0.0" : request.getParameter("lnt");
%>

<div class="input">
    <span>LAT:</span>
    <input type="text" id="lat" value="<%=lat%>">

    <span>LNT:</span>
    <input type="text" id="lnt" value="<%=lnt%>">

    <button id="btn_cur_position"><span>내 위치 가져오기</span></button>
    <button id="btn_nearest_wifi"><span>근처 Wifi 정보 보기</span></button>
</div>


<div>
    <table>
        <thead>
        <tr>
            <th>거리(km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>

            <th>도로명 주소</th>
            <th>상세 주소</th>

            <th>설치 위치(층)</th>
            <th>설치 기관</th>
            <th>설치 유형</th>

            <th>서비스 구분</th>
            <th>망 종류</th>
            <th>설치 년도</th>
            <th>실내 외 구분</th>
            <th>WIFI 접속 환경</th>

            <th>x좌표</th>
            <th>y좌표</th>
            <th>작업일자</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (!("0.0").equals(lat) && !("0.0").equals(lnt)) {
                WifiDAO wifiDAO = new WifiDAO();
                List<WifiDTO> list = wifiDAO.getNearestWifiList(lat, lnt);

                if (list != null) {
                    for (WifiDTO wifiDTO : list) {
        %>
        <tr>
            <td><%=wifiDTO.getDistance()%></td>
            <td><%=wifiDTO.getId()%></td>
            <td><%=wifiDTO.getDistrict()%></td>
            <td><%=wifiDTO.getId()%></td>
            <td><%=wifiDTO.getRoad_address()%></td>
            <td><%=wifiDTO.getDetail_address()%></td>
            <td><%=wifiDTO.getInstalization_floor()%></td>
            <td><%=wifiDTO.getInstalization_agency()%></td>
            <td><%=wifiDTO.getInstalization_type()%></td>
            <td><%=wifiDTO.getService_type()%></td>
            <td><%=wifiDTO.getMesh_type()%></td>
            <td><%=wifiDTO.getInstalization_year()%></td>
            <td><%=wifiDTO.getIndoor_outdoor()%></td>
            <td><%=wifiDTO.getWifi_invironment()%></td>
            <td><%=wifiDTO.getY_coord()%></td>
            <td><%=wifiDTO.getX_coord()%></td>
            <td><%=wifiDTO.getWork_date()%></td>
        </tr>
        <% } %>
        <% } %>
        <% } else { %>
        <td colspan='17'> 위치 정보를 입력하신 후에 조회해 주세요. </td>
        <% } %>
        </tbody>
    </table>
</div>

<script>
    let getCurPosition = document.getElementById("btn_cur_position");
    let getNearestWifi = document.getElementById("btn_nearest_wifi");

    let lat = null;
    let lnt = null;

    window.onload = () => {
        lat = document.getElementById("lat").value;
        lnt = document.getElementById("lnt").value;
    }

    getCurPosition.addEventListener("click", function () {
        if ('geolocation' in navigator) {
            navigator.geolocation.getCurrentPosition(function (position){
                let latitude = position.coords.latitude;
                let longitude = position.coords.longitude;
                document.getElementById("lat").value = latitude;
                document.getElementById("lnt").value = longitude;
            })
        } else{
            alert("위치 정보를 확인할 수 없으니 직접 입력해주시기 바랍니다.")
        }
    });

    getNearestWifi.addEventListener("click", function (){
        let latitude = document.getElementById("lat").value;
        let longitude = document.getElementById("lnt").value;

        if (latitude !== "" || longitude !== "") {
            window.location.assign("http://localhost:8080/public_wifi_war_exploded/?lat=" + latitude + "&lnt=" + longitude);
        } else {
            alert("위치 정보를 입력하신 후에 조회해주세요.")
        }
    })
</script>

</body>
</html>