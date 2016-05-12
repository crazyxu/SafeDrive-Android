package me.xucan.safedrive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import me.xucan.safedrive.App;
import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.User;
import me.xucan.safedrive.db.MySp;
import me.xucan.safedrive.message.RongManager;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    @ViewInject(R.id.et_name)
    private EditText etName;

    @ViewInject(R.id.et_pwd)
    private EditText etPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        autoLogin();

    }

    private void autoLogin(){
        User user = MySp.getUser();
        if (user != null && user.getUserId() != 0){
            App.getInstance().setUserId(user.getUserId());
            App.getInstance().setToken(user.getToken());
            RongManager.connect();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }
    }

    public void register(View v){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void login(View v){
        String name = etName.getText().toString();
        String pwd = etPwd.getText().toString().trim();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "phone and password could not be null", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setPhone(name);
        user.setPassword(pwd);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        new MJsonRequest(NetParams.URL_USER_LOGIN, map, new MRequestListener() {
            @Override
            public void onSuccess(String requestUrl, JSONObject response) {
                //登陆成功
                User user = response.getObject("user", User.class);
                App.getInstance().setUserId(user.getUserId());
                App.getInstance().setToken(user.getToken());
                MySp.saveUser(user);
                RongManager.connect();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(String requestUrl, int errCode, String errMsg) {

            }
        }).startRequest();
    }

}

