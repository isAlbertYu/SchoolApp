package com.example.albert.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.albert.crawlers.CourseCrawler;
import com.example.albert.crawlers.WebInfo;
import com.example.albert.schoolapp.R;
import com.example.albert.utils.RSAUtil;

import okhttp3.RequestBody;

/**
 * Created by Albert on 2018/8/7.
 */
//启动页面
public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView mBtnLogin;//Login按钮

    private View progress;//进度条

    private View mInputLayout;//账号密码框外布局

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;//账号密码框内布局
    private EditText editTextName, editTextPsw;//账号密码的编辑框

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    private static final int LOGIN_EVENT = 1;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_EVENT:
                    Object[] objArr = ((Object[]) msg.obj);
                    if ((boolean) objArr[0]) {//登录成功,则保存用户信息然后跳转页面
                        editor = pre.edit();
                        //保存学号、密码
                        editor.putString("username", (String) objArr[1]);
                        editor.putString("password", (String) objArr[2]);
                        editor.apply();



                        //跳转界面
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                    } else {//登录失败
                        Log.d("LA", "la run: isSuccess "+ (boolean) objArr[0]);
                        myToast("学号或者密码错误");

                        inputLayoutShrinkAnimator(mInputLayout, false);//输入框的伸展动画
                        mName.setVisibility(View.VISIBLE);//开启账号输入框
                        mPsw.setVisibility(View.VISIBLE);//开启密码输入框


                    }


                    break;

                default:
                    break;
            }
        }
    };


    /**
     * 果冻效果震动的弹出动画
     */
    private LinearInterpolator jellyInterpolator = new LinearInterpolator() {
        private final float factor = 0.15f;

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input)
                    * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 控件的初始化:
     * 1.获取句柄
     * 2.设置监听事件
     */
    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);

        editTextName = (EditText) mInputLayout.findViewById(R.id.edittext_name);
        editTextPsw = (EditText) mInputLayout.findViewById(R.id.edittext_password);

        pre = getSharedPreferences("user_data", MODE_PRIVATE);


        //设置login按钮的监听事件
        mBtnLogin.setOnClickListener(this);
    }

    /**
     * 实现了login按钮的监听事件
     * 按下按钮后：
     * 1.判断输入框是否空，空则提示，不作网络响应；直至不为空后进入第2步
     * 2.做出动画,然后提交表单数据到服务器
     * 3.接收服务器传回的响应，查看是否登录成功；如果登录失败，则作出提示，并且不作页面跳转；
     * 4.如果登录成功，则保存信息然后进行页面跳转至主页
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (checkInputIsEmpty()) {
            myToast("学号或者密码不能为空");
            return;
        }


        mName.setVisibility(View.INVISIBLE);//隐藏账号输入框
        mPsw.setVisibility(View.INVISIBLE);//隐藏密码输入框
        inputLayoutShrinkAnimator(mInputLayout, true);//输入框的收缩动画

        sendData();

    }

    /**
     * 学号密码输入框的收缩与伸展动画
     * @param view
     */
    private void inputLayoutShrinkAnimator(final View view, boolean flag) {
        AnimatorSet set = new AnimatorSet();


        ObjectAnimator animator = null;
        if (flag) {//收缩
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {//收缩动画结束之后
                    progress.setVisibility(View.VISIBLE);
                    progressLayoutPopupAnimator(progress);
                    mInputLayout.setVisibility(View.INVISIBLE);

                }
            });
            animator = ObjectAnimator.ofFloat(view,
                    "scaleX", 1f, 0.5f);
        } else {//伸展
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {//收缩动画结束之后
                    progress.setVisibility(View.GONE);
                    //progressLayoutPopupAnimator(progress);
                    mInputLayout.setVisibility(View.VISIBLE);
                }
            });
            animator = ObjectAnimator.ofFloat(view,
                    "scaleX", 0.5f, 1f);
        }

        set.setDuration(200);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(animator);
        set.start();

    }

    /**
     * 进度条的弹出动画
     * @param view
     */
    private void progressLayoutPopupAnimator(final View view) {
        AnimatorSet progressAnimationSet = new AnimatorSet();

        ObjectAnimator animator= ObjectAnimator.ofFloat(view,
                "scaleX", 0.5f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,
                "scaleY", 0.5f, 1f);

        progressAnimationSet.setDuration(1000);
        progressAnimationSet.setInterpolator(jellyInterpolator);;
        progressAnimationSet.playTogether(animator, animator2);
        progressAnimationSet.start();
    }

    /**
     *检查输入框输入是否为空
     */
    private boolean checkInputIsEmpty() {
        String name = getUsername();
        String psw = getPassword();
        if (name.equals("") || psw.equals("")) {//有空
            return true;//是空
        }
        return false;//都不是空
    }


    /**
     * 将学号密码加密发送
     */
    private void sendData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String name = LoginActivity.this.getUsername();
                String psw = LoginActivity.this.getPassword();
                //加密
                long s1 = System.nanoTime();
                RSAUtil rsa = new RSAUtil(LoginActivity.this);
                String encryptedName = rsa.RSAEncrypt(name);
                String encryptedPsw = rsa.RSAEncrypt(psw);
                long e1 = System.nanoTime();
                Log.d("sendData", "run: 加密时间 "+ (e1-s1));

                Log.d("LA", "la run: "+encryptedName);

                //登录
                CourseCrawler crawler = new CourseCrawler();
                //访问登录页面，不登录,以获取cookie
                crawler.accessingPage(WebInfo.loginURLString);
                //创建用户表单数据
                RequestBody userForm = crawler.buildFormData(encryptedName, encryptedPsw);
                //登录(携带上次访问所获取的cookie)，post表单数据
                boolean isSuccess = crawler.loginWebsite(WebInfo.loginURLString, userForm);

                //将是否成功登录标志，学号，密码打包发给主线程
                Object[] out = new Object[] {isSuccess, name, psw};

                Message message = new Message();
                message.what = LOGIN_EVENT;
                message.obj = out;
                handler.sendMessage(message);


            }
        }.start();
    }

    /**
     * 获取编辑框的用户名
     * @return
     */
    private String getUsername() {
        return editTextName.getText().toString().trim();
    }

    /**
     * 获取编辑框的密码
     * @return
     */
    private String getPassword() {
        return editTextPsw.getText().toString().trim();
    }

    /**
     * 提示弹窗
     * @param str 错误提示信息
     */
    private void myToast(String str) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_login,
                (ViewGroup) findViewById(R.id.lly_toast));
        //ImageView img_logo = (ImageView) view.findViewById(R.id.img_logo);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, -500);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


}
