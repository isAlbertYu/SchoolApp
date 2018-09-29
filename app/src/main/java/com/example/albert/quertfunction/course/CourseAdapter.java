package com.example.albert.quertfunction.course;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.albert.crawlers.Course;
import com.example.albert.schoolapp.R;

import java.util.List;

/**
 * Created by Albert on 2018/8/11.
 */

public class CourseAdapter extends ArrayAdapter {
    private int resourceId;//每个list要装载的布局资源
    public CourseAdapter(@NonNull Context context,
                         @LayoutRes int resourceId, @NonNull List objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = (Course) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,
                parent, false);
        //设置课程名称
        TextView courseNameText = (TextView) view.findViewById(R.id.course_textView0);
        courseNameText.setText(course.getName()+":");

        //设置课程分数
        TextView courseScoreText = (TextView) view.findViewById(R.id.course_textView1);
        courseScoreText.setText(course.getScore()+"分");

        //设置课程学分
        TextView courseCreditText = (TextView) view.findViewById(R.id.course_textView2);
        courseCreditText.setText(course.getCredit()+"学分");

        //设置课程类别
        TextView courseTypeText = (TextView) view.findViewById(R.id.course_textView3);
        courseTypeText.setText(course.getType());

        //设置课程是否及格
        TextView courseIsPassedText = (TextView) view.findViewById(R.id.course_textView4);
        courseIsPassedText.setText(course.getIsPassed());

        return view;
    }
}
