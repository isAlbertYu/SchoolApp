package com.example.albert.crawlers;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Albert on 2018/8/14.
 */

public class CourseCrawler {
    private CookiesManager cookiesManager = new CookiesManager();

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .cookieJar(cookiesManager)
            .build();

    /**
     * 一个标准的get请求
     * @param url
     * @return
     */
    private Response get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response;
            /**** 在外部关闭 Response ****/
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * 一个标准的post
     * 提交表单键值对
     * @throws Exception
     */
    public Response post(String url, RequestBody formBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)//装载表单
                .header("Accept", "text/html, application/xhtml+xml, image/jxr, */*")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response;
            /**** 在外部关闭 Response ****/
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 以get方式登录页面
     */
    public void accessingPage(String url) {
        Response response = get(url);
        response.close();
    }

    /**
     * 以get方式登录页面
     * 并返回页面html
     */
    public String htmlOfAccessingPage(String url) {
        Response response = get(url);
        String html = null;
        try {
            html = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.close();
        return html;
    }

    /**
     * 用表单数据登录网站
     * 将表单数据post到网站中
     * okhttp自动处理302重定向问题
     * 向重定向的URI中重新发送post请求
     * 根据返回的页面判断是否登录成功
     * (登录成功与失败所返回的页面的title标签的内容不一样)
     * @param url
     * @param formBody
     * @return 登录成功返回true,登录失败返回false
     */
    public boolean loginWebsite(String url, RequestBody formBody) {
        Response response = post(url, formBody);
        //System.out.println(response.code());
        String html = null;
        try {
            html = response.body().string();//发送登录请求后服务器传来的页面html
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(html);
        Elements title = doc.select("title");

        if (title.text().equals("智慧华中大 | 统一身份认证系统")) {//登录失败，学号密码错误
            return false;
        } else {  //登录成功
            return true;
        }
    }

    /**
     * 构建表单数据
     * @param username
     * @param password
     * @return
     */
    public RequestBody buildFormData(String username, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("code", "code")
                .add("lt", "LT-NeusoftAlwaysValidTicket")
                .add("execution", "e1s1")
                .add("_eventId", "submit")
                .build();
        return formBody;
    }

    /**
     * 打印cookie
     * @param url
     */
    public void printCookie(HttpUrl url) {
        List<Cookie> cookies = cookiesManager.loadForRequest(url);
        for (Cookie cookie : cookies) {
            System.out.println(cookie);
        }
    }

    /*********************解析部分*********************/
    private Course getOneGrade(List<String> proList) {
        return new Course(proList.get(0),proList.get(1),proList.get(2),
                proList.get(3),proList.get(4),proList.get(5),
                proList.get(6),proList.get(7),proList.get(8),
                proList.get(9),proList.get(10));
    }

    private List<Course> getAllGrade(String stringOfHtml) {
        ArrayList<Course> finalCourseList = new ArrayList<Course>();

        Document doc = Jsoup.parse(stringOfHtml);
        Elements courseList = doc.select("tr.t_con"); //提取出所有的课程块

        for (Element course : courseList) {
            List<String> proList = new ArrayList<>();
            for (Element element : course.select("td")) {//提取出每个课程块中每行的信息
                proList.add(element.text());
            }
            Course co = getOneGrade(proList);
            finalCourseList.add(co);
        }
        return finalCourseList;
    }

    /**
     * 获取成绩的总接口:getGrade
     * @return 课程信息的list列表
     */
    public static List<Course> getGrade(String username, String password) {
        Log.d("@#CourseCrawler", "getGrade: --方法调用1 ");
        CourseCrawler crawler = new CourseCrawler();
        //访问登录页面，不登录,以获取cookie
        crawler.accessingPage(WebInfo.loginURLString);

        //创建用户表单数据
        RequestBody userForm = crawler.buildFormData(username, password);

        //登录(携带上次访问所获取的cookie)，post表单数据
        crawler.loginWebsite(WebInfo.loginURLString, userForm);

        //查成绩
        String htmlString = crawler.htmlOfAccessingPage(WebInfo.gradeURLString);

        List<Course> coursesList =  crawler.getAllGrade(htmlString);
        Log.d("@#CourseCrawler", "getGrade: --方法调用2 ");
        Log.e("@#CourseCrawler", "getGrade: == " + coursesList.toString());
        return coursesList;
    }

    /**
     * 打印课程信息列表
     * @param finalCourseList
     */
    public static void showAllGrade(List<Course> finalCourseList) {
        for (Course course : finalCourseList) {
            System.out.println(course.toString());
            System.out.println("++++++++++++++++++++++++");

        }
    }

    /**
     * 返回课程信息列表字符串
     * @param finalCourseList
     */
    public static String getStringOfAllGrade(List<Course> finalCourseList) {
        String res = "";
        for (Course course : finalCourseList) {
            res = res + course.toString();
        }
        return res;
    }
}
