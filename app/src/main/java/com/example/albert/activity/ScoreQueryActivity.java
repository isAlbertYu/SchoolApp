package com.example.albert.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.example.albert.crawlers.Course;
import com.example.albert.crawlers.CourseCrawler;
import com.example.albert.schoolapp.R;
import com.example.albert.quertfunction.course.CourseAdapter;

import java.util.List;

/**
 * Created by Albert on 2018/8/9.
 */

public class ScoreQueryActivity extends Activity{
    private ListView listView;
    private static List<Course> courseList;
    private static boolean loadedFlag = false;


    private static final String TAG = "ScoreQueryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorequery);
        listView = (ListView) findViewById(R.id.grade_listview);
        Log.i(TAG, "onCreate: 进入 ScoreQueryActivity");
        Intent intent = getIntent();
        courseList = (List<Course>) intent.getSerializableExtra("score");
        Log.i(TAG, "onCreate: courseList" + courseList);

        showListView(courseList);

    }

    /**
     * 使用courseList数据显示ListView
     * @param courseList
     */
    private void showListView(List<Course> courseList) {
        listView.setAdapter(new CourseAdapter(ScoreQueryActivity.this,
                R.layout.item_course, courseList));
    }

    /**
     * back回退按钮
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: run");
        super.onBackPressed();
        loadedFlag = false;
        Log.d(TAG, "onBackPressed: over");
    }



}
