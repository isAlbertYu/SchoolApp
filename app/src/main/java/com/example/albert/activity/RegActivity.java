package com.example.albert.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.albert.ai.MnistDemo;
import com.example.albert.schoolapp.R;

import java.util.Arrays;

/**
 * Created by Albert on 2018/9/5.
 */

public class RegActivity extends Activity {
    // Used to load the 'native-lib' library on application startup.
/*    static {
        System.loadLibrary("native-lib");//可以去掉
    }*/


    private static final String TAG = "MyCameraActivity";
    private static final String MODEL_FILE = "file:///android_asset/mnist.pb"; //模型存放路径
    TextView txt;
    ImageView imageView;
    Bitmap bitmap;
    MnistDemo preTF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // Example of a call to a native method
        txt=(TextView)findViewById(R.id.txt_id);
        imageView =(ImageView)findViewById(R.id.imageView1);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
        imageView.setImageBitmap(bitmap);
        preTF = new MnistDemo(getAssets(),MODEL_FILE);//输入模型存放路径，并加载TensoFlow模型
    }

    public void click01(View v){
        String res="结果为：";
        int[] result= preTF.getPredict(bitmap);
        Log.e(TAG, "click01: result[]: " + Arrays.toString(result));
        for (int i=0;i<result.length;i++) {
            Log.i(TAG, res+result[i]);
            res=res+String.valueOf(result[i]) + " ";
        }
        txt.setText(res);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
/*
    public native String stringFromJNI();//可以去掉
*/

}
