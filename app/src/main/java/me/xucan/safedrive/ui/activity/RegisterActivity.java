package me.xucan.safedrive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity implements MRequestListener{
    @ViewInject(R.id.et_name)
    private TextView etName;

    @ViewInject(R.id.et_phone)
    private EditText etPhone;

    @ViewInject(R.id.et_pwd)
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        x.view().inject(this);
    }

    public void register(View view){
        String name = etName.getText().toString();
        String pwd = etPwd.getText().toString();
        String phone = etPhone.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(phone)){
            Map<String, Object> map = new HashMap<>();
            User user = new User();
            user.setUserName(name);
            user.setPassword(pwd);
            user.setPhone(phone);
            map.put("user", user);
            new MJsonRequest(NetParams.URL_USER_REGISTER, map, this).startRequest();
        }else{
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(String requestUrl, JSONObject response) {
        User user = response.getObject("user", User.class);
        App.getInstance().setUserId(user.getUserId());
        App.getInstance().setToken(user.getToken());
        MySp.saveUser(user);
        RongManager.connect();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    @Override
    public void onError(String requestUrl, int errCode, String errMsg) {
        if (errCode == 101)
            Toast.makeText(this, "该手机号已注册", Toast.LENGTH_SHORT).show();
    }
}
