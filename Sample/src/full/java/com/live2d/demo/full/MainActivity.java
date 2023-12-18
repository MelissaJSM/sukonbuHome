/*
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at http://live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.demo.full;

import static android.os.SystemClock.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.OkHttpClient;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Request;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Response;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.ResponseBody;
import com.live2d.demo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    // 이거 원래는 확장에 activity 임.


    ////////////////////////////////////////////////////////////////////////////////////////////
    //스크린 영역(어드민 필요)

    Handler secondOutHandler = new Handler();

    Handler fiveInHandler = new Handler();

    Handler fiveOutHandler = new Handler();

    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ComponentName compName;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    int timeOut = 0;

    int outTimeOut = 0;
    int inTimeOut = 0;

    boolean screen = false;

    ////////////////////////////////////////////////////////////////////////////////////////////
    //블루투스 영역

    BluetoothAdapter mBluetoothAdapter;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;


    Handler bluetoothCheckHandler = new Handler();


    String hightechMAC = "해당 블루투스의 mac 주소를 넣으면 자동으로 잡습니다.";

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    ////////////////////////////////////////////////////////////////////////////////////////////
    //음성인식 영역

    // 안쓰고 싶었는데 재시작 하는 과정에서 변수값 초기화가있어서 해야할듯...
    SharedPreferences sukonbuPref;
    SharedPreferences.Editor sukonbuEditor;

    boolean nowPlaying = false;

    boolean outFiveReady = false;
    boolean inFiveReady = false;

    int todayTemp;
    int todayTempMin;
    int todayTempMax;
    String todayWeather;

    //강제오류 유발용 디버그
    int exceptionDelay = 0;

    int desTime;

    int count = 0; //음성 카운트


    String awakeString = null;


    MediaPlayer mediaPlayer_navi; // 이건 네비게이션 안내용 사운드

    ArrayList<Integer> arraySound = new ArrayList<>();

    TypedArray arraySound_navi;  // 저장된 스트링 사운드배열 가져오기


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));


        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);       // OpenGL ES 2.0を利用

        glRenderer = new GLRenderer();

        glSurfaceView.setRenderer(glRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(glSurfaceView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // 스크린 영역
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, DeviceAdmin.class);
        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            //원래 여기에 뭐 끄는게있었던거같다.
        }
        else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please Click On Activate");
            startActivityForResult(intent, RESULT_ENABLE);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ? View.SYSTEM_UI_FLAG_LOW_PROFILE : View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY));
        }
        else {
            getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars() | WindowInsets.Type.statusBars());

            getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        arraySound_navi = getResources().obtainTypedArray(R.array.sukonbu);

        sukonbuPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        sukonbuEditor = sukonbuPref.edit();
        inFiveReady = sukonbuPref.getBoolean("inFiveReady", false);


        WifiManager.WifiLock wifiLock = null;
        //실행
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiLock = wifiManager.createWifiLock("wifilock");
        wifiLock.setReferenceCounted(true);
        wifiLock.acquire();


        ////////////////////////////////////////////////////////////////////////////////////////////
        //블루투스 영역

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        //초기설정란
        bluetoothOn(); // 첫 안드로이드가 부팅되고나서 블루투스 연결 및 스캔 시작 소스

        // 해당 데이터를 수신 받는 장소
        mBluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                        readMessage = readMessage.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "");
                        System.out.println("해당 내용을 수신하였습니다 : " + readMessage);
                        readData(readMessage);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //여기서 시스템 수신 받은내용을 처리함.
                }
            }
        };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_ADVERTISE, android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION

            }, 0);

        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_ADVERTISE, android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION

            }, 0);
        }


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {


                    ////////////////////////////화면이 켜지면 동작 시작/////////////////////////////////


                    //나갔다가 다시 들어온건지 확인

                    if (awakeString.contains(Constants.sukonbu_bt_in)) {
                        sleep(3000);
                        System.out.println("안으로 들어오는거 캐치 완료");

                        // 날짜에 따른 시간 설정이 필요합니다.
                        Date nowDate = new Date();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E", Locale.KOREAN);
                        String toDayE = simpleDateFormat.format(nowDate);

                        simpleDateFormat = new SimpleDateFormat("HH", Locale.KOREAN);
                        int toDayHH = Integer.parseInt(simpleDateFormat.format(nowDate));

                        if (toDayE.contains("토") || toDayE.contains("일")) {
                            //주말 데이터 사용
                            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_in_etc);
                            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_in_etc);
                        }
                        else {
                            //아침 오후 밤 새벽을 나눠야 한다.
                            // 아침 : 07~11
                            // 오후 : 12 ~ 20
                            // 밤 : 21 ~ 23
                            // 새벽 : 0~ 6

                            if (toDayHH >= 07 && toDayHH <= 11) {
                                //아침
                                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_in_morning);
                                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_in_morning);
                            }
                            else if (toDayHH >= 12 && toDayHH <= 17) {
                                //오후
                                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_in_afternoon);
                                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_in_afternoon);
                            }
                            else if (toDayHH >= 18 && toDayHH <= 23) {
                                //저녁
                                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_in_dinner);
                                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_in_dinner);
                            }
                            else if (toDayHH >= 0 && toDayHH <= 6) {
                                //새벽
                                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_in_dawn);
                                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_in_dawn);
                            }
                        }
                        awakeString = null;
                        mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer_navi.start();


                                mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    public void onCompletion(MediaPlayer mp) {
                                        // TODO Auto-generated method stub
                                        mediaPlayer_navi.pause();
                                        mediaPlayer_navi.release();

                                        // 아 이것때문에 sharedpref 써야하나.........
                                        sukonbuEditor.putBoolean("inFiveReady", true);
                                        sukonbuEditor.commit();
                                        secondOut();
                                    }
                                });
                            }
                        });


                    }


                    else if (awakeString.contains(Constants.sukonbu_bt_wake)) {
                        arraySound.clear();
                        awakeString = null;
                        //네트워크 데이터 수집을 해야하므로 콜백 처리하는게 훨씬 안정적임.
                        System.out.println("콜백 전에 동작");                 // 1. 먼저 찍힘
                        Callee weatherCallee = new Callee();               // Callee 객체 생성
                        weatherCallee.weatherWork(mCallback);                // Callee 내의 weatherWork를 호출 ( 1. 해당 callee 로그가 찍힘)
                    }


                    ///////////////////////////여기까지///////////////////////////////////////////////

                }
            }
        };
        registerReceiver(receiver, intentFilter);


        if (inFiveReady) { // 나갔을때니까

            inFiveCount();
        }



        // 오류 강제유발 디버그용

        /*
        Handler exceptionHandler = new Handler();
        exceptionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exceptionDelay++;
                if(exceptionDelay>10){
                    throw new RuntimeException();
                }


                fiveOutHandler.postDelayed(this, 1000);
            }
        }, 0);

         */


    }

    @Override
    protected void onStart() {
        super.onStart();

        LAppDelegate.getInstance().onStart(this);

    }


    //이 소스를 잘 참고하면 정답이 나온다.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LAppDelegate.getInstance().onTouchBegan(pointX, pointY);
                break;
            //터치가 끝난 부분이 onTouchEnd 로 가서
            case MotionEvent.ACTION_UP:
                LAppDelegate.getInstance().onTouchEnd(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                LAppDelegate.getInstance().onTouchMoved(pointX, pointY);
                break;
        }
        return super.onTouchEvent(event);
    }

    private GLSurfaceView glSurfaceView;
    private GLRenderer glRenderer;


    @SuppressLint("MissingPermission")
    void bluetoothOn() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();

                bluetoothCheckHandler.postDelayed(new Runnable() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    public void run() {

                        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                        BluetoothDevice mBluetoothDevice = bluetoothManager.getAdapter().getRemoteDevice(hightechMAC);
                        // 여기에 1초마다 핸들러 필요함.
                        if (!isConnected(mBluetoothDevice)) {
                            System.out.println("블루투스 연결 실패 상태");

                            bluetoothCheckHandler.removeMessages(0);
                            connectSelectedDevice();
                        }
                        else {

                            System.out.println("블루투스 연결 상태");
                            //뭐 할게 없다.
                        }

                        bluetoothCheckHandler.postDelayed(this, 1000);


                    }
                }, 0);
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);

                //bluetoothOn();
                //mBluetoothAdapter.enable();
                //안드 보안문제인거같음. 현재 이 소스가 동작하지 않음.

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                }
                else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("MissingPermission")
    void connectSelectedDevice() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothDevice mBluetoothDevice = bluetoothManager.getAdapter().getRemoteDevice(hightechMAC);
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
            System.out.println("최종적으로 연결 완료");
            bluetoothOn();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            connectSelectedDevice();

            //에러 발생시 다시한번 도전하는 방식으로 가보자고
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }


    public boolean isConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            boolean connected = (boolean) m.invoke(device, (Object[]) null);
            return connected;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void readData(String data) {

        if (data.contains(Constants.sukonbu_bt_cut)) { // C = 리밋 차단모드 동작

            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_cut);

            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_cut);
            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();


                    mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer_navi.pause();
                            mediaPlayer_navi.release();
                        }
                    });
                }
            });
        }
        else if (data.contains(Constants.sukonbu_bt_cutre)) { // C = 리밋 차단모드 해제

            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_cutre);

            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_cutre);
            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();


                    mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer_navi.pause();
                            mediaPlayer_navi.release();
                        }
                    });
                }
            });
        }
        else if (data.contains(Constants.sukonbu_bt_in)) { // C = 밖에서 안으로 들어갈때

            if (outFiveReady) {
                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_in_re);
                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_in_re);
                awakeString = null;
                mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer_navi.start();


                        mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                // TODO Auto-generated method stub
                                mediaPlayer_navi.pause();
                                mediaPlayer_navi.release();
                                outFiveReady = false;
                                outTimeOut = 0;
                                fiveOutHandler.removeMessages(0);
                            }
                        });
                    }
                });

            }
            else {
                awakeString = "I";
                wakeLock.acquire();
                wakeLock.release();
            }

        }
        else if (data.contains(Constants.sukonbu_bt_out)) { // C = 안에서 밖으로 나갈때

            if (inFiveReady) {
                // 나갔다가 5분이내로 다시 들어온게 맞을때
                inFiveReady = false;
                inTimeOut = 0;
                fiveInHandler.removeMessages(0);
                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_out_re);
                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_out_re);

            }
            else {
                // 그냥 나갈때
                LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_out);

                mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_out);
            }
            ////////////////////////////////////////////////////////////////////////////////////////


            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();


                    mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer_navi.pause();
                            mediaPlayer_navi.release();

                            //나갔을때 카운트 핸들러 필요..
                            outFiveReady = true;
                            outFiveCount();


                            //스크린 오프
                        }
                    });
                }
            });
            ////////////////////////////////////////////////////////////////////////////////////////
        }
        else if (data.contains(Constants.sukonbu_bt_sleep)) { // 수면 시작

            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_sleep);

            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_sleep);
            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();


                    mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer_navi.pause();
                            mediaPlayer_navi.release();

                            deviceManger.lockNow();


                        }
                    });
                }
            });
        }
        else if (data.contains(Constants.sukonbu_bt_toilet)) { // C = 리밋 차단모드 동작

            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_toilet);

            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_toileton);
            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();


                    mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer_navi.pause();
                            mediaPlayer_navi.release();


                        }
                    });
                }
            });
        }
        else if (data.contains(Constants.sukonbu_bt_toiletout)) { // C = 리밋 차단모드 동작

            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_toiletout);

            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_toiletoff);
            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();


                    mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer_navi.pause();
                            mediaPlayer_navi.release();


                        }
                    });
                }
            });
        }
        else if (data.contains(Constants.sukonbu_bt_wake)) { // W 침대에서 일어날때
            awakeString = "W";
            wakeLock.acquire();
            wakeLock.release();
            sleep(5000);


        }
    }

    private void secondOut() {
        secondOutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                timeOut++;
                if (timeOut > 3) {
                    // 어디보자... 여기에 boolean 값 넣어야 할텐데
                    PackageManager packageManager = getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                    ComponentName componentName = intent.getComponent();
                    Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                    startActivity(mainIntent);
                    System.exit(0);

                }
                secondOutHandler.postDelayed(this, 1000);
            }
        }, 0);
    }


    private void countSound() {
        if (nowPlaying == false) {
            System.out.println("스타팅 사운드");
            mediaPlayer_navi = MediaPlayer.create(MainActivity.this, arraySound_navi.getResourceId(arraySound.get(count), -1));
            mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer_navi.start();
                    nowPlaying = true;
                }
            });
        }


        mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                //재생할 비디오가 남아있을 경우
                count++;
                if (count < arraySound.size()) {
                    System.out.println("배열 진입 : 출력시작");
                    System.out.println("출력되는 카운트 : " + count + "\n총 배열 사이즈 : " + arraySound.size() + "\n 배열내부의 값 : " + arraySound.get(count));
                    mediaPlayer_navi.release();
                    mediaPlayer_navi = MediaPlayer.create(MainActivity.this, arraySound_navi.getResourceId(arraySound.get(count), -1));
                    mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer_navi.start();
                            countSound();
                        }
                    });
                }
                else {
                    System.out.println("배열 오버 : 출력종료");
                    count = 0;
                    nowPlaying = false;
                    mediaPlayer_navi.pause();
                    mediaPlayer_navi.release();

                    secondOut();
                }
            }

        });
    }


    Callback mCallback = new Callback() {     // 최종 정리된 다음 콜백이 찍히도록 됨.

        // 날씨 네트워크 처리 콜백
        @Override
        public void weatherCallback(int temp, int temp_min, int temp_max, String weather) {
            System.out.println("현재 온도는? : " + temp);
            System.out.println("현재 최소 온도는? : " + temp_min);
            System.out.println("현재 최대 온도는? : " + temp_max);
            System.out.println("현재 날씨는? : " + weather);

            todayTemp = temp;
            todayTempMax = temp_max;
            todayTempMin = temp_min;
            todayWeather = weather;

            Callee naviCallee = new Callee();               // Callee 객체 생성
            naviCallee.naviWork(mCallback);                // Callee 내의 doWork를 호출 ( 2. 해당 callee 로그가 찍힘)

        }

        //네비 네트워크 처리 콜백
        @Override
        public void naviCallback(int Time) {
            desTime = Time;
            System.out.println("회사까지 도착 예정 시간 :  " + desTime + "분 소요 예정");

            // 여기서 파싱해서 재생 요청하면 끝.


            //순서 : 날짜 - 오늘 날씨 - 오늘 온도 - 최대 온도 - 최소 온도 - 도착까지 얼마나

            // 오늘 날짜에 대한 정보를 받아오는 작업을 만들어야함!!!

            Date nowDate = new Date();


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM", Locale.KOREAN);
            int todayMonth = Integer.parseInt(simpleDateFormat.format(nowDate));

            simpleDateFormat = new SimpleDateFormat("dd", Locale.KOREAN);
            int today = Integer.parseInt(simpleDateFormat.format(nowDate));


            Callee todayCallee = new Callee();
            todayCallee.todayWork(mCallback, todayMonth, today); // 오늘의 날씨는 어떠한지 처리하러 가자.

        }

        public void todayCallback(int todayMonthResult, ArrayList<Integer> todayResult) {
            // 오늘 날짜에 대한 정보를 배열에 추가 시키도록 한다.

            //0번배열은 추후 추가할 예정
            arraySound.add(29); // 아침인사하는 스콘부

            arraySound.add(todayMonthResult); // month 변수

            arraySound.add(30); // 월


            arraySound.addAll(todayResult); // day

            arraySound.add(31); // 일


            Callee todayWeatherCallee = new Callee();
            todayWeatherCallee.todayWeatherWork(mCallback, todayWeather); // 오늘의 날씨는 어떠한지 처리하러 가자.
        }


        // 오늘 날씨가 어떤지 정리하는 콜백
        @SuppressLint("Recycle")
        @Override
        public void todayWeatherCallback(String todayWeatherResult) {
            // 오늘 날씨에 대한 정보를 배열에 추가 시키도록 한다.

            arraySound.add(32); // 오늘의 날씨는

            TypedArray arrayWeatherString; // 알람 분 배열
            arrayWeatherString = getResources().obtainTypedArray(R.array.weatherData); // 날짜의 요일 string 데이터
            for (int i = 0; i < arrayWeatherString.length(); i++) {
                if (todayWeatherResult.contains(arrayWeatherString.getString(i))) {
                    arraySound.add(i + 13);
                }
            }

            arraySound.add(33); // 이고 현재 온도는

            if (todayTemp < 0) {
                arraySound.add(36); //앞에 영하 붙이는 작업
                todayTemp = Math.abs(todayTemp);
            }

            Callee todayTempCallee = new Callee();
            todayTempCallee.todayTempWork(mCallback, todayTemp);                //현재 온도에 대한 정보를 정리하러 가자.

        }

        // 현재 온도에 대하여 정리하는 콜백
        public void todayTempCallback(ArrayList<Integer> todayTempResult) {
            // 오늘 온도에 대한 정보를 배열에 추가 시키도록 한다.

            arraySound.addAll(todayTempResult); // nowTemp

            arraySound.add(34); // 도 이며 최대 온도는

            if (todayTempMax < 0) {
                arraySound.add(36); //앞에 영하 붙이는 작업
                todayTempMax = Math.abs(todayTempMax);
            }

            Callee todayTempMaxCallee = new Callee();
            todayTempMaxCallee.todayTempMaxWork(mCallback, todayTempMax);                //현재 최대온도에 대한 정보를 정리하러 가자.
        }

        //오늘 최대 온도에 대하여 정리하는 콜백
        public void todayTempMaxCallback(ArrayList<Integer> todayTempMaxResult) {
            // 오늘 최대 온도에 대한 정보를 배열에 추가 시키도록 한다.


            arraySound.addAll(todayTempMaxResult); // tempMax

            arraySound.add(35); // 도 이고 최소 온도는


            if (todayTempMin < 0) {
                arraySound.add(36); //앞에 영하 붙이는 작업
                todayTempMin = Math.abs(todayTempMin);
            }

            Callee todayTempMinCallee = new Callee();
            todayTempMinCallee.todayTempMinWork(mCallback, todayTempMin);                //현재 최대온도에 대한 정보를 정리하러 가자.
        }

        //오늘 최소 온도에 대하여 정리하는 콜백
        public void todayTempMinCallback(ArrayList<Integer> todayTempMinResult) {
            // 오늘 최대 온도에 대한 정보를 배열에 추가 시키도록 한다.


            arraySound.addAll(todayTempMinResult); //tempMin

            arraySound.add(37); // 입니다. 회사 까지 도착 시간은

            Callee todayNaviCallee = new Callee();
            todayNaviCallee.todayNaviWork(mCallback, desTime);                //회사까지 도착 시간에 대한 정보를 정리하러 가자.
        }

        //도착 시간에 따른 시간을 정리하는 콜백
        public void todayNaviCallback(ArrayList<Integer> todayNaviResult) {
            // 오늘 도착시간에 대한 정보를 배열에 추가 시키도록 한다.


            arraySound.addAll(todayNaviResult); // naviResult

            arraySound.add(38); // 분 소요 될 듯 싶어요. 준비해서 출발하도록 하죠!

            for (int i = 0; i < arraySound.size(); i++) {
                System.out.println(i + "번째 음성 처리 자료 : " + arraySound.get(i));
            }

            //그리고 재생시킨다. 그리고 재생이 끝나면 재부팅 시킨다.
            LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_wake);
            countSound();

        }


    };

    private void inFiveCount() {
        fiveInHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                inTimeOut++;
                if (inTimeOut > 300) {
                    // 차단 해제
                    inFiveReady = false;
                    fiveInHandler.removeMessages(0);
                    inTimeOut = 0;
                    return;

                }
                fiveInHandler.postDelayed(this, 1000);
            }
        }, 0);
    }

    private void outFiveCount() {
        fiveOutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                outTimeOut++;
                if (outTimeOut > 300) {
                    // 차단 해제
                    outFiveReady = false;
                    fiveOutHandler.removeMessages(0);
                    outTimeOut = 0;
                    // 다시 재생하고 아웃.

                    LAppLive2DManager.getInstance().onTaping(Constants.sukonbu_out_five);

                    mediaPlayer_navi = MediaPlayer.create(MainActivity.this, R.raw.sukonbu_out_five);
                    mediaPlayer_navi.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer_navi.start();


                            mediaPlayer_navi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                public void onCompletion(MediaPlayer mp) {
                                    // TODO Auto-generated method stub
                                    mediaPlayer_navi.pause();
                                    mediaPlayer_navi.release();
                                    deviceManger.lockNow();

                                }
                            });
                        }
                    });


                    return;

                }
                fiveOutHandler.postDelayed(this, 1000);
            }
        }, 0);
    }


}
