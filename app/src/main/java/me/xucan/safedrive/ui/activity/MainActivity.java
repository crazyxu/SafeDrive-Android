package me.xucan.safedrive.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import me.xucan.safedrive.App;
import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.User;
import me.xucan.safedrive.message.RongManager;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;
import me.xucan.safedrive.ui.fragment.IndexFragment;
import me.xucan.safedrive.ui.fragment.MyFragment;
import me.xucan.safedrive.ui.fragment.RecordsFragment;
import me.xucan.safedrive.ui.fragment.SimulationFragment;

public class MainActivity extends AppCompatActivity {
    //导航栏标题
    private String arrTitle[] = {"安全驾驶", "模拟行驶", "行驶记录", "我"};
    //导航栏图标
    private int arrIcon[] = {R.mipmap.ic_car_gray, R.mipmap.ic_fxp_gray, R.mipmap.ic_records_gray
        ,R.mipmap.ic_user_gray};
    //选择状态下的图标
    private int arrIconSelected[] = {R.mipmap.ic_car_blue, R.mipmap.ic_fxp_blue, R.mipmap.ic_records_blue
            ,R.mipmap.ic_user_blue};
    //
    private Class arrFragment[] = {IndexFragment.class, SimulationFragment.class, RecordsFragment.class,
            MyFragment.class};
    @ViewInject(R.id.tab_host)
    private FragmentTabHost tabHost;



    //当前选中页面
    private int curId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initView();
        testLogin();
    }

    void testLogin(){
        User user = new User();
        user.setUserName("徐灿");
        user.setPassword("123456");
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        new MJsonRequest(NetParams.URL_USER_LOGIN, map, new MRequestListener() {
            @Override
            public void onSuccess(String requestUrl, JSONObject response) {
                //登陆成功
                User user = response.getObject("user", User.class);
                App.getInstance().setUserId(user.getUserId());
                App.getInstance().setToken(user.getToken());
                RongManager.connect();
            }

            @Override
            public void onError(String requestUrl, int errCode, String errMsg) {

            }
        }).startRequest();
    }

    void testRegister(){
        User user = new User();
        user.setUserName("system");
        user.setPassword("123456");
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        new MJsonRequest(NetParams.URL_USER_REGISTER, map, new MRequestListener() {
            @Override
            public void onSuccess(String requestUrl, JSONObject response) {
                User user = response.getObject("user", User.class);
                App.getInstance().setUserId(user.getUserId());
            }

            @Override
            public void onError(String requestUrl, int errCode, String errMsg) {

            }
        }).startRequest();
    }

    void initView(){
        tabHost.setup(this, getSupportFragmentManager(), R.id.layout);
        for (int i = 0; i < arrTitle.length; i++){
            tabHost.addTab(tabHost.newTabSpec(String.valueOf(i)).setIndicator(getIndicator(i)),
                    arrFragment[i], null);
        }
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int clickedId = Integer.valueOf(tabId);
                tabClick(clickedId,true);
                tabClick(curId,false);
                curId = clickedId;
            }
        });
        //默认选中
        tabClick(curId,true);
        tabHost.setCurrentTab(curId);
    }

    //切换tab颜色和图标
    void tabClick(int tabId , boolean clickedId){
        View view = tabHost.getTabWidget().getChildTabViewAt(tabId);
        TextView textView = ((TextView)view.findViewById(R.id.item_tv));
        ImageView imageView = ((ImageView)view.findViewById(R.id.item_iv));
        if (clickedId){
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            imageView.setImageResource(arrIconSelected[tabId]);
        }else {
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorGray));
            imageView.setImageResource(arrIcon[tabId]);
        }
    }

    /**
     * 底部导航Item
     * @param position
     * @return
     */
    View getIndicator(int position){
        View rootVIew = getLayoutInflater().inflate(R.layout.item_tab_host,null);
        TextView tv = (TextView)rootVIew.findViewById(R.id.item_tv);
        ImageView iv = (ImageView)rootVIew.findViewById(R.id.item_iv);
        tv.setText(arrTitle[position]);
        iv.setImageResource(arrIcon[position]);
        return rootVIew;
    }
}
