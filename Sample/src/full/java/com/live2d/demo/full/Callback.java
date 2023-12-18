package com.live2d.demo.full;

import java.util.ArrayList;

public interface Callback {
    // Callback 인터페이스 내의 속이 없는 껍데기 함수

    // 날씨 네트워크 콜백
    void weatherCallback(int today_temp, int today_temp_min, int today_temp_max, String result_weather);

    //네비 네트워크 콜백
    void naviCallback(int desTime);

    void todayCallback(int todayMonthResult, ArrayList<Integer> todayResult);

    //오늘 날씨 콜백
    void todayWeatherCallback(String todayWeatherResult);

    //현재 온도 콜백
    void todayTempCallback(ArrayList<Integer> todayTemp);

    //오늘 최대 온도 콜백
    void todayTempMaxCallback(ArrayList<Integer> todayTempMaxResult);

    //오늘 최소 온도 콜백
    void todayTempMinCallback(ArrayList<Integer> todayTempMinResult);

    //도착까지 걸리는 시간 콜백
    void todayNaviCallback(ArrayList<Integer> todayNaviResult);
}
