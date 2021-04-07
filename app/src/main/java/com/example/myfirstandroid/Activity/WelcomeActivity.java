package com.example.myfirstandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.myfirstandroid.R;
import com.example.myfirstandroid.Utils.ApplicationUtil;
import com.example.myfirstandroid.Utils.SharedPreUtil;


/**
 *
 */

public class WelcomeActivity extends AppCompatActivity {

    final Message message = new Message();

    final Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    // 判断用户是否登录
                    boolean userIsLogin = (boolean) SharedPreUtil.getParam(WelcomeActivity.this, SharedPreUtil.IS_LOGIN, false);
                    Intent intent;
                    if (userIsLogin){
                        intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    }else {
                        intent = new Intent(WelcomeActivity.this, LoginOrRegisterActivity.class);
                    }
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    thread.interrupt();
                    break;
                default:
                    break;
            }
        }
    };

    final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                message.what = 1;
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    });





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        thread.start();

//        SharedPreUtil.setParam(WelcomeActivity.this, SharedPreUtil.IS_LOGIN, false);

        Button jumpButton = findViewById(R.id.btn_jump);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.what = 0;
                handler.sendMessage(message);
                boolean userIsLogin = (boolean) SharedPreUtil.getParam(WelcomeActivity.this, SharedPreUtil.IS_LOGIN, false);
                Intent intent;
                if (userIsLogin){
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(WelcomeActivity.this, LoginOrRegisterActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
        ApplicationUtil.getInstance().addActivity(this);
    }

}