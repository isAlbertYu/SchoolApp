package com.example.albert.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.albert.activity.LoginActivity;
import com.example.albert.crawlers.Course;
import com.example.albert.crawlers.CourseCrawler;
import com.example.albert.utils.RSAUtil;

import java.util.List;

/**
 * Created by Albert on 2018/8/22.
 */

public class MyService extends IntentService {
    private final String TAG = "@#MyService";
    private List<Course> courseList;

    private GetGradeBinder mBinder = new GetGradeBinder();

    public MyService() {
        super("MyService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind--方法被调用!");
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate--方法被调用!");
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d("@#MyService", "onStartCommand--方法被调用!");
        return super.onStartCommand(intent, flags, startId);
    }

    //Service被关闭之前回调
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestory--方法被调用!");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("@#MyService", "onHandleIntent --方法被调用!");
        String action = intent.getExtras().getString("param");

        switch (action) {
            case "get_score":
                Log.d("@#MyService", "onHandleIntent:(get_score) --方法被调用!");
                String[] info = this.getUnameandPwsfromPre();
                Log.i(TAG, "onHandleIntent: 学号密码： " + info[0] + info[1]);

                RSAUtil rsa = new RSAUtil(MyService.this);
                String encryptedName = rsa.RSAEncrypt(info[0]);
                String encryptedPsw = rsa.RSAEncrypt(info[1]);

                courseList = CourseCrawler.getGrade(encryptedName, encryptedPsw);

                Log.i(TAG, "onHandleIntent: 成绩信息： " + courseList.toString());
                break;
            default:
                break;
        }
    }

    /**
     * 从SharedPreferences中读取学号密码
     * @return 存储学号密码的String数组
     */
    private String[] getUnameandPwsfromPre() {
        String[] info = new String[2];
        SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
        info[0] = pref.getString("username", "");
        info[1] = pref.getString("password", "0");
        return info;
    }

    public class GetGradeBinder extends Binder {
        public List<Course> getGrade() {
            //return courseList;
            Log.d("@#MyService", "getGrade: run" + courseList.toString());
            return MyService.this.courseList;
        }
    };


}
