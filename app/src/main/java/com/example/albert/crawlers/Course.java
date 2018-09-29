package com.example.albert.crawlers;

import java.io.Serializable;

/**
 * Created by Albert on 2018/8/11.
 */

public class Course implements Serializable {

    private String type;//课程类别
    private String term;//选修学期
    private String number;//课程编号
    private String name;//课程名称
    private String credit;//学分
    private String code;//教学班号
    private String score;//考试成绩
    private String isPassed;//是否及格
    private String property;//考试性质
    private String date;//考试日期
    private String remarks;//备注

    public Course() {

    }

    public Course(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public Course(String type, String term, String number, String name,
                  String credit, String code, String score, String isPassed,
                  String property, String date, String remarks) {
        super();
        this.type = type;
        this.term = term;
        this.number = number;
        this.name = name;
        this.credit = credit;
        this.code = code;
        this.score = score;
        this.isPassed = isPassed;
        this.property = property;
        this.date = date;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Course [type=" + type + "\n term=" + term + "\n number="
                + number + "\n name=" + name + "\n credit=" + credit
                + "\n code=" + code + "\n score=" + score + "\n isPassed="
                + isPassed + "\n property=" + property + "\n date=" + date
                + "\n remarks=" + remarks + "]";
    }

    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCredit() {
        return credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getIsPassed() {
        return isPassed;
    }
    public void setIsPassed(String isPassed) {
        this.isPassed = isPassed;
    }

}
