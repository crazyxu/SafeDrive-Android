package me.xucan.safedrive.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import me.xucan.safedrive.App;
import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.bean.User;
import me.xucan.safedrive.db.MyDBManager;
import me.xucan.safedrive.db.MySp;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;
import me.xucan.safedrive.ui.activity.LoginActivity;
import me.xucan.safedrive.ui.widget.CustomItem;

public class MyFragment extends Fragment implements MRequestListener{

    @ViewInject(R.id.ci_name)
    private CustomItem ciName;

    @ViewInject(R.id.ci_phone)
    private CustomItem ciPhone;

    @ViewInject(R.id.ci_urgent_phone)
    private CustomItem ciUrgentPhone;

    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = MySp.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        x.view().inject(this, view);
        setUser();
        return view;
    }

    void set(String title, final SetDialogListener listener){
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle(title).create();
        final EditText editText = new EditText(getActivity());
        dialog.setView(editText);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = editText.getText().toString();
                if (TextUtils.isEmpty(txt))
                    Toast.makeText(getActivity(), "请填写内容", Toast.LENGTH_SHORT).show();
                else
                    if (listener != null)
                        listener.onSure(txt);
            }
        });
        dialog.show();
    }

    @Event(value = {R.id.ci_urgent_phone, R.id.ci_logout, R.id.ci_name, R.id.ci_phone})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.ci_urgent_phone:
                set("绑定紧急号码", new SetDialogListener(){

                    @Override
                    public void onSure(String txt) {
                        user.setUrgentPhone(txt);
                        updateUser();
                    }
                });
                break;
            case R.id.ci_name:
                set("修改用户名", new SetDialogListener(){

                    @Override
                    public void onSure(String txt) {
                        user.setUserName(txt);
                        updateUser();
                    }
                });
                break;
            case R.id.ci_phone:
                set("绑定手机号", new SetDialogListener(){

                    @Override
                    public void onSure(String txt) {
                        user.setPhone(txt);
                        updateUser();
                    }
                });
                break;
            case R.id.ci_logout:
                logout();
                break;
        }
    }

    private void updateUser(){
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        new MJsonRequest(NetParams.URL_USER_UPDATE, map, this).startRequest();
    }

    @Override
    public void onSuccess(String requestUrl, JSONObject response) {
        switch (requestUrl){
            case NetParams.URL_USER_UPDATE:
                setUser();
                MySp.saveUser(user);
                break;
        }
    }

    @Override
    public void onError(String requestUrl, int errCode, String errMsg) {
        switch (requestUrl){
            case NetParams.URL_USER_UPDATE:
                user = MySp.getUser();
                Toast.makeText(getActivity(), "设置失败" + errMsg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private interface SetDialogListener{
        void onSure(String txt);
    }



    void setUser(){
        if (user == null)
            return;
        ciName.setRightTv(user.getUserName());
        ciPhone.setRightTv(user.getPhone());
        if (!TextUtils.isEmpty(user.getUrgentPhone()))
            ciUrgentPhone.setRightTv(user.getUrgentPhone());
        else
            ciUrgentPhone.setRightTv("未绑定");
    }

    public void logout(){
        //清楚数据
        App.getInstance().setToken("");
        App.getInstance().setUserId(0);
        MySp.clear();
        MyDBManager.getInstance().deleteAll(DriveRecord.class);
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}
