package com.live2d.demo.full;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class S_Preference {


    /**
     * TODO [클래스 설명]
     * // -----------------------------------------
     * 1. 모바일 내부 데이터 저장 수행 클래스 (앱 종료 시에도 유지 / 앱 삭제 시 초기화)
     * // -----------------------------------------
     * 2. 저장된 전체 key 확인 : S_Preference.getTotalKey(getApplication()); // 저장된 전체 key 값 확인
     * // -----------------------------------------
     * 3. 전체 데이터 삭제 : S_Preference.clear(getApplication()); // 저장된 데이터 삭제
     * // -----------------------------------------
     * 4. String 저장 : S_Preference.setString(getApplication(), "Name", "Data_Twok2K"); //특정 데이터 저장한다
     * // -----------------------------------------
     * 5. String 호출 : S_Preference.getString(getApplication(), "Name"); //저장된 특정 데이터 불러온다
     * // -----------------------------------------
     * */





    // TODO [빠른 로직 찾기 : 주석 로직 찾기]
    /**
     * // -----------------------------------------
     * // [SEARCH FAST] : [프로세스 시작 및 종료 시 프리퍼런스 데이터 초기화]
     * // -----------------------------------------
     */





    //TODO [전역 변수]
    public static final String PREFERENCES_NAME = "rebuild_preference";
    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;





    // TODO [클래스 명칭 선언 실시]
    private static final String ACTIVITY_NAME = "S_Preference";




    //TODO [객체 생성]
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }




    //TODO [전체 key 값 저장]
    public static void setTotalKey(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();
    }




    //TODO [String 값 저장]
    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();

        // [전체 데이터에 키 저장]
        String data = "";
        data = getTotalKey(context);
        if(data.contains("["+key+"]") == false){
            data = data + "["+ key + "]";
            setTotalKey(context, "TotalKeyAllTwoK", data);
        }
    }




    //TODO [boolean 값 저장]
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
        editor.apply();

        // [전체 데이터에 키 저장]
        String data = "";
        data = getTotalKey(context);
        if(data.contains("["+key+"]") == false){
            data = data + "["+ key + "]";
            setTotalKey(context, "TotalKeyAllTwoK", data);
        }
    }




    //TODO [int 값 저장]
    public static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
        editor.apply();

        // [전체 데이터에 키 저장]
        String data = "";
        data = getTotalKey(context);
        if(data.contains("["+key+"]") == false){
            data = data + "["+ key + "]";
            setTotalKey(context, "TotalKeyAllTwoK", data);
        }
    }




    //TODO [전체 Key 값 호출]
    public static String getTotalKey(Context context) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString("TotalKeyAllTwoK", DEFAULT_VALUE_STRING);
        return value;
    }




    //TODO [String 값 호출]
    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }




    //TODO [boolean 값 호출]
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
        return value;
    }




    //TODO [int 값 호출]
    public static int getInt(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, DEFAULT_VALUE_INT);
        return value;
    }




    //TODO [전체 key 값에서 특정 값 삭제]
    public static void removeTotalKey(Context context, String key) {
        if(getTotalKey(context).contains("["+key+"]") == true){
            String data = getTotalKey(context);
            data = data.replace("["+ key + "]","");
            setTotalKey(context, "TotalKeyAllTwoK", data);
        }
    }




    //TODO [특정 key 값 데이터 삭제]
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();

        // [전체 데이터에서 키값 삭제]
        if(getTotalKey(context).contains("["+key+"]") == true){
            removeTotalKey(context,key);
        }
    }




    //TODO [전체 key 값 데이터 삭제 삭제]
    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();

        // [전체 데이터 삭제]
        String data = "";
        data = getTotalKey(context);
        if(data.length() > 0 && data.equals("") == false){
            setTotalKey(context, "TotalKeyAllTwoK", "");
        }
    }




    // TODO [인트로 화면 실행 시 초기화 해야하는 프리퍼런스 데이터 : A_Intro : onCreate]
    public static void introCreateClear(Context context) {
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> introCreateClear() :: 인트로 화면 실행 시 프리퍼런스 데이터 초기화]");
        Log.e("//===========//","================================================");
        Log.i("---","---");

        // -----------------------------------------
        // [SEARCH FAST] : [프로세스 시작 및 종료 시 프리퍼런스 데이터 초기화]
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 푸시 알림 JS 전달]
        S_Preference.setString(context, S_FinalData.PRE_ROOT_ACTIVITY, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 및 백그라운드 전환 시간]
        S_Preference.setString(context, S_FinalData.PRE_WV_RESUME_TIME, ""); // [포그라운드]
        S_Preference.setString(context, S_FinalData.PRE_WV_PAUSE_TIME, ""); // [백그라운드]
        // -----------------------------------------
    }





    // TODO [앱 종료 시 초기화 해야하는 프리퍼런스 데이터 : A_Main : onDestroy]
    public static void mainFinishClear(Context context) {
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> mainFinishClear() :: 앱 종료 시 프리퍼런스 데이터 초기화]");
        Log.e("//===========//","================================================");
        Log.i("---","---");

        // -----------------------------------------
        // [SEARCH FAST] : [프로세스 시작 및 종료 시 프리퍼런스 데이터 초기화]
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 푸시 알림 JS 전달]
        S_Preference.setString(context, S_FinalData.PRE_ROOT_ACTIVITY, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 및 백그라운드 전환 시간]
        S_Preference.setString(context, S_FinalData.PRE_WV_RESUME_TIME, ""); // [포그라운드]
        S_Preference.setString(context, S_FinalData.PRE_WV_PAUSE_TIME, ""); // [백그라운드]
        // -----------------------------------------
    }




    // TODO [프로세스 시작 시 초기화 해야하는 프리퍼런스 데이터 : S_Application : onCreate]
    public static void proccessCreateClear(Context context) {
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> proccessCreateClear() :: 프로세스 시작 시 프리퍼런스 데이터 초기화]");
        Log.e("//===========//","================================================");
        Log.i("---","---");

        // -----------------------------------------
        // [SEARCH FAST] : [프로세스 시작 및 종료 시 프리퍼런스 데이터 초기화]
        // -----------------------------------------
        // [스키마 및 일반 애플리케이션 실행 여부 : A_Intro]
        S_Preference.setString(context, S_FinalData.PRE_APP_NEW_TASK, "");
        // -----------------------------------------
        // [로그인 : 스키마 데이터 초기화 실시]
        S_Preference.setString(context, S_FinalData.PRE_SCHEME_DATA_LOGIN, "");
        // -----------------------------------------
        // [일반 : 스키마 데이터 초기화 실시]
        S_Preference.setString(context, S_FinalData.PRE_SCHEME_DATA_CALL, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 푸시 알림 JS 전달]
        S_Preference.setString(context, S_FinalData.PRE_ROOT_ACTIVITY, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 및 백그라운드 전환 시간]
        S_Preference.setString(context, S_FinalData.PRE_WV_RESUME_TIME, ""); // [포그라운드]
        S_Preference.setString(context, S_FinalData.PRE_WV_PAUSE_TIME, ""); // [백그라운드]
        // -----------------------------------------
    }





    // TODO [프로세스 종료 시 초기화 해야하는 프리퍼런스 데이터 : S_ProgramTaskService : onTaskRemoved]
    public static void proccessFinishClear(Context context) {
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> proccessFinishClear() :: 프로세스 종료 시 프리퍼런스 데이터 초기화]");
        Log.e("//===========//","================================================");
        Log.i("---","---");

        // -----------------------------------------
        // [SEARCH FAST] : [프로세스 시작 및 종료 시 프리퍼런스 데이터 초기화]
        // -----------------------------------------
        // [스키마 및 일반 애플리케이션 실행 여부 : A_Intro]
        S_Preference.setString(context, S_FinalData.PRE_APP_NEW_TASK, "");
        // -----------------------------------------
        // [로그인 : 스키마 데이터 초기화 실시]
        S_Preference.setString(context, S_FinalData.PRE_SCHEME_DATA_LOGIN, "");
        // -----------------------------------------
        // [일반 : 스키마 데이터 초기화 실시]
        S_Preference.setString(context, S_FinalData.PRE_SCHEME_DATA_CALL, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 푸시 알림 JS 전달]
        S_Preference.setString(context, S_FinalData.PRE_ROOT_ACTIVITY, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 및 백그라운드 전환 시간]
        S_Preference.setString(context, S_FinalData.PRE_WV_RESUME_TIME, ""); // [포그라운드]
        S_Preference.setString(context, S_FinalData.PRE_WV_PAUSE_TIME, ""); // [백그라운드]
        // -----------------------------------------
    }





    // TODO [메인 웹뷰 리로드 시 초기화 데이터 : A_Main : onResume]
    public static void webViewReloadClear(Context context) {
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> webViewReloadClear() :: 웹뷰 리로드 수행 프리퍼런스 데이터 초기화]");
        Log.e("//===========//","================================================");
        Log.i("---","---");

        // -----------------------------------------
        // [SEARCH FAST] : [프로세스 시작 및 종료 시 프리퍼런스 데이터 초기화]
        // -----------------------------------------
        // [스키마 및 일반 애플리케이션 실행 여부 : A_Intro]
        S_Preference.setString(context, S_FinalData.PRE_APP_NEW_TASK, "");
        // -----------------------------------------
        // [로그인 : 스키마 데이터 초기화 실시]
        S_Preference.setString(context, S_FinalData.PRE_SCHEME_DATA_LOGIN, "");
        // -----------------------------------------
        // [일반 : 스키마 데이터 초기화 실시]
        S_Preference.setString(context, S_FinalData.PRE_SCHEME_DATA_CALL, "");
        // -----------------------------------------
        // [메인 웹뷰 포그라운드 및 백그라운드 전환 시간]
        S_Preference.setString(context, S_FinalData.PRE_WV_RESUME_TIME, ""); // [포그라운드]
        S_Preference.setString(context, S_FinalData.PRE_WV_PAUSE_TIME, ""); // [백그라운드]
        // -----------------------------------------
    }


} // TODO [클래스 종료]