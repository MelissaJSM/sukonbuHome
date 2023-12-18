package com.live2d.demo.full;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);





        FileWriter writer;

        try {

            // 외부 저장 공간 root 하위에 myApp이라는 폴더 경로 획득

            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sukonbuError";

            File dir = new File(dirPath); // 객체 생성

            if(!dir.exists()){ // 폴더가 없으면

                dir.mkdir(); // 만들어 준다.

            }

            // myApp 폴더 밑에 myfile.txt 파일 지정

            File file = new File(dir+"/sukonbuExceoption.txt");

            if(!file.exists()){ // 파일이 없다면

                file.createNewFile(); // 새로 만들어 준다.

            }

            // 파일에 쓰기

            writer = new FileWriter(file, true);

            writer.write(String.valueOf(errorReport));

            writer.flush();

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        Intent intent = new Intent(myContext, ErrorActivity.class);
        intent.putExtra("error", errorReport.toString());
        myContext.startActivity(intent);


        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}