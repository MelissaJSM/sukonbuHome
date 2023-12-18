package com.live2d.demo.full;

import android.util.Log;

import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.MediaType;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.OkHttpClient;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Request;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.RequestBody;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Response;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Callee {

    String result_weather;
    int today_temp;
    int today_temp_min;
    int today_temp_max;

    double latitude = "현재 위치의 자표를 적습니다.";
    double longtitude = "현재 위치의 좌표를 적습니다.";

    // 이거 나중에 지워도되나 체크 해봐야할듯??

    public void Callee() {
    }

    public void weatherWork(Callback mCallback) {
        new Thread(() -> {
            weather_net_connect(mCallback);
        }).start();
        // 이 시점에서 MainActivity가 지정한 callback                                                             호출. 세 번째 "callback1" Log가 찍히겠지.
    }

    public void naviWork(Callback mCallback) {
        navi_time(mCallback);
    }

    public void todayWork(Callback mCallback, int todayMonth, int today) {
        // 오늘 날짜에 대한 처리 완료 후 콜백

        int todayMonthResult = todayMonth;

        ArrayList<Integer> arrayToday = numParsing(today);
        mCallback.todayCallback(todayMonthResult, arrayToday); // 월, 일
    }

    public void todayWeatherWork(Callback mCallback, String todayWeather) {
        // 오늘 날씨에 대한 작업 완료 후 전달
        String todayWeatherResult = todayWeather;
        mCallback.todayWeatherCallback(todayWeatherResult); // 처리 완료된 오늘 날씨를 저장하자.
    }

    public void todayTempWork(Callback mCallback, int todayTemp) {
        // 오늘 온도에 대한 작업 완료 후 전달


        ArrayList<Integer> arrayTodayTemp = numParsing(todayTemp);
        mCallback.todayTempCallback(arrayTodayTemp);
    }

    public void todayTempMaxWork(Callback mCallback, int todayTempMax) {
        //오늘 최대 온도에 대한 작업 완료 후 전달
        ArrayList<Integer> todayTempMaxResult = numParsing(todayTempMax);
        mCallback.todayTempMaxCallback(todayTempMaxResult);
    }

    public void todayTempMinWork(Callback mCallback, int todayTempMin) {
        //오늘 최소 온도에 대한 작업 완료 후 전달
        ArrayList<Integer> todayTempMinResult = numParsing(todayTempMin);
        mCallback.todayTempMinCallback(todayTempMinResult);
    }

    public void todayNaviWork(Callback mCallback, int todayNavi) {
        //오늘 도착시간에 대한 작업 완료 후 전달
        ArrayList<Integer> todayNaviResult = numParsing(todayNavi);
        mCallback.todayNaviCallback(todayNaviResult);
    }


    private void weather_net_connect(Callback mCallback) {

        String weather_key = "여기에 openweathermap api 키를 입력하면 됩니다.";

        String weather_json;
        String url;

        try {

            url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longtitude + "&appid=" + weather_key + "&units=metric"; // 오늘 예보


            System.out.println(url);


            System.out.println("url 입력 전송중");

            // OkHttp 클라이언트 객체 생성
            OkHttpClient client = new OkHttpClient();

            // GET 요청 객체 생성
            Request.Builder builder = new Request.Builder().url(url).get();
            Request request = builder.build();

            // OkHttp 클라이언트로 GET 요청 객체 전송
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 응답 받아서 처리
                ResponseBody body = response.body();
                if (body != null) {

                    weather_json = body.string();
                    System.out.println("Response:" + weather_json); // 대답 받는 부위

                    JSONParse_today(weather_json, mCallback);
                }
            }
            else
                //성공적으로 못 받았으면 여기에 입력한다.
                System.err.println("Error Occurred");


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("이번엔 주소가 잘못되었나본데");

        }


    }


    private void JSONParse_today(String weather_value, Callback mCallback) throws JSONException {


        JSONObject original_JSON;
        try {
            original_JSON = new JSONObject(weather_value); //전체 배열 가져오기 중복 없음!
        } catch (NullPointerException e) {
            System.out.println("original_JSON 에서 null 값 발생으로 인한 리턴");
            return;
        }

        JSONArray weather_JSON = original_JSON.optJSONArray("weather"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!
        if (weather_JSON == null) { // null 값 검증
            System.out.println("resultData_JSON 0값 발생했음 ");
            return;
        }
        JSONObject weather_JSON_in = weather_JSON.optJSONObject(0); // 이 배열이 동일값 배열 쭉 나열하는거일텐데... 아마 여기서 몇번째 값만 추출하면 내일 날씨 알 수도 있다.!
        System.out.println("weather_JSON_in 의 배열 값 : " + weather_JSON_in + " 그리고 i 의 값 : " + 0);
        System.out.println("weather_JSON_in 배열 길이 : " + weather_JSON_in.length());
        if (weather_JSON_in == null) {
            System.out.println("jo 값 마저 null 발생함.");
            return;
        }
        result_weather = weather_JSON_in.optString("main", "");

        ////////////////////////////////////////////////////////////////////////////////////////////


        JSONObject main_JSON = original_JSON.optJSONObject("main"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!
        if (main_JSON == null) { // null 값 검증
            System.out.println("main_JSON 0값 발생했음 ");
            return;
        }
        System.out.println("main_JSON 내용 : " + main_JSON);

        today_temp = main_JSON.optInt("temp", 0);
        today_temp_min = main_JSON.optInt("temp_min", 0);
        today_temp_max = main_JSON.optInt("temp_max", 0);  // 최대 최소는 3시간마다 알려주는 데이터를 이용하여 작업하자.


        // 여기에 도착경로 예상 시스템 함수를 넣어서 콜백을 그쪽으로 넘길 예정.
        mCallback.weatherCallback(today_temp, today_temp_min, today_temp_max, result_weather);

        // 오늘 날씨와 현재온도 최대온도 최소온도 정리


    }


    private void navi_time(Callback mCallback) {


        new Thread(() -> {

            double start_lat = latitude;
            double start_long = longtitude;


            double des_lat = "도착지의 좌표를 입력하시면됩니다.";
            double des_long = "도착지의 좌표를 입력하시면 됩니다.";

            String appKey = "tmap API 키를 입력하시면 됩니다.";

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"tollgateFareOption\":16,\"roadType\":32,\"directionOption\":1,\"endX\":\"" + des_long + "\",\"endY\":\"" + des_lat + "\",\"endRpFlag\":\"G\",\"reqCoordType\":\"WGS84GEO\",\"startX\":\"" + start_long + "\",\"startY\":\"" + start_lat + "\",\"uncetaintyP\":1,\"uncetaintyA\":1,\"uncetaintyAP\":1,\"carType\":0,\"gpsInfoList\":\"126.939376564495,37.470947057194365,120430,20,50,5,2,12,1_126.939376564495,37.470947057194365,120430,20,50,5,2,12,1\",\"detailPosFlag\":\"2\",\"resCoordType\":\"WGS84GEO\",\"sort\":\"index\",\"totalValue\":2}");
            Request request = new Request.Builder().url("https://apis.openapi.sk.com/tmap/routes?version=1&callback=function").post(body).addHeader("accept", "application/json").addHeader("content-type", "application/json").addHeader("appKey", appKey).build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String message = null;
            try {
                message = response.body().string();
            } catch (IOException e) {
                System.out.println("리스폰스 값 자체가 null 화 되어버림2");
                return;
            }
            System.out.println("네비 파싱전 메시지 : " + message);


            navi_parse(message, mCallback);


        }).start();


    }

    private void navi_parse(String message, Callback mCallback) {

        JSONObject original_JSON;
        try {
            original_JSON = new JSONObject(message); //전체 배열 가져오기 중복 없음!
            System.out.println("original_JSON 정리 : " + original_JSON);
        } catch (NullPointerException | JSONException e) {
            System.out.println("original_JSON 에서 null 값 발생으로 인한 리턴");
            return;
        }

        JSONArray matchedPoints_JSON = original_JSON.optJSONArray("features"); // 전체 배열 다음 다음 배열

        //배열 검증용?
        for (int i = 0; i < matchedPoints_JSON.length(); i++) {
            JSONObject array_json = matchedPoints_JSON.optJSONObject(i); // 전체 배열 다음 다음 배열


            JSONObject properties = array_json.optJSONObject("properties"); // 전체 배열 중 resultdata 아래 있는 데이터 가져오기) 중복 없음!

            System.out.println("거리 값          " + (Integer.parseInt(properties.optString("totalTime", ""))) / 60);


            // 0은 내가 출근할때

            int desTime = (Integer.parseInt(properties.optString("totalTime", ""))) / 60;

            mCallback.naviCallback(desTime);

        }
    }


    private ArrayList<Integer> numParsing(int num) { // 배열 함수 (최적화용)

        ArrayList<Integer> numArray = new ArrayList<Integer>();

        if (num / 10 != 0) { // 십의 자리가 존재하는 경우
            if (num / 10 == 1) {
                numArray.add(10);
            }
            else {
                numArray.add(num / 10);
                numArray.add(10);
            }

            //십 다음 일의 자리 처리
            if (num % 10 != 0) {
                numArray.add(num % 10);
            }
        }
        //일의 자리만 있는 경우 처리
        else {
            numArray.add(num % 10);
        }
        return numArray;
    }

}
