package com.example.albert.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.albert.quertfunction.Query;
import com.example.albert.quertfunction.QueryAdapter;
import com.example.albert.schoolapp.R;
import com.example.albert.service.MyService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private GridView gridView;
    private List<Query> queryList = new ArrayList<>();

    private MyService.GetGradeBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("@#MainActivity", "onServiceConnected--方法执行!");
            binder = (MyService.GetGradeBinder) service;
            //MainActivity.this.testStr = binder.getGrade();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("@#MainActivity", "onCreate --方法调用");

        serviceWorking();

        initQueryView();
        QueryAdapter adapter = new QueryAdapter(MainActivity.this,
                R.layout.item_grid_icon_old, queryList);
        gridView = (GridView) findViewById(R.id.grid_query);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(MainActivity.this,
                                ScoreQueryActivity.class );
                        intent0.putExtra("score", (Serializable) binder.getGrade());
                        startActivity(intent0);
                        //Toast.makeText(MainActivity.this, "getGrade="+binder.getGrade(), Toast.LENGTH_SHORT).show();
                        break;

                    case 7:
                        Intent intent7 = new Intent(MainActivity.this,
                                RegActivity.class );
                        startActivity(intent7);
                        break;

                    case 8:
                        Intent intent8 = new Intent(MainActivity.this,
                                MyCameraActivity.class );
                        startActivity(intent8);
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "This="+position, Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

    }

    /**
     * 启动服务，去获取成绩，课表，查电费等等...
     */
    private void serviceWorking() {
        Log.d("@#MainActivity", "serviceWorking --方法被调用!");
        Intent bindIntent = new Intent(this, MyService.class);
        Bundle b1 = new Bundle();
        b1.putString("param", "get_score");
        bindIntent.putExtras(b1);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        startService(bindIntent);
        Log.d("@#MainActivity", "serviceWorking --方法结束!");
    }

    /**
     * 初始化查询gridview视图
     */
    private void initQueryView() {
            Query scoreQuery = new Query("成绩查询", "看看考了多少分");
            queryList.add(scoreQuery);

            Query courseQuery = new Query("课表查询", "在哪上课？");
            queryList.add(courseQuery);

            Query eleChargeQuery = new Query("电费查询", "还有多少电？");
            queryList.add(eleChargeQuery);

            Query borrowBookQuery = new Query("已借书籍查询", "借了啥书？");
            queryList.add(borrowBookQuery);

            Query bookQuery = new Query("馆藏书籍查询", "有啥书？");
            queryList.add(bookQuery);

            Query newsQuery = new Query("校园资讯", "最近发生了啥？");
            queryList.add(newsQuery);

            Query weatherQuery = new Query("天气查询", "晴或雨");
            queryList.add(weatherQuery);

            Query mnist = new Query("数字识别", "123");
            queryList.add(mnist);

            Query dete = new Query("目标检测", "456");
            queryList.add(dete);
    }


    /**
     * back回退按钮
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);
    }

}



