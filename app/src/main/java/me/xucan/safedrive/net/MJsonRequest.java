package me.xucan.safedrive.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import me.xucan.safedrive.util.EncodeUtil;

/**
 * @author xucan
 */
public class MJsonRequest {
	private String url;
	private MRequestListener listener;
	private Map<String, Object> params;
	private StringRequest request;
	
	public MJsonRequest(final String url, Map<String, Object> params, final MRequestListener listener){
		this.url = url;
		this.listener = listener;
		this.params = params;
		
	}
	public void startRequest(){
		request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("response","url" + "..." + response);
				JSONObject object = JSON.parseObject(response);
				if (object != null && object.containsKey("status")){
					if (object.getIntValue("status") == 200){
						listener.onSuccess(url, object.getJSONObject("body"));
					}else{
						listener.onError(url, object.getIntValue("status"), object.getString("errorMsg"));
					}

				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				int statusCode = 0;
				if (error.networkResponse != null)
					statusCode = error.networkResponse.statusCode;
				listener.onError(url, statusCode, "请求提交失败！");
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				String request = "";
				Map<String, String> map = new HashMap<>();
				for (String key  : params.keySet()){
					Object obj = params.get(key);
					String clazz = obj.getClass().toString();
					//基本类型
					if (clazz.equals(String.class.toString()) || clazz.equals(int.class.toString()) ||
							clazz.equals(long.class.toString()) || clazz.equals(boolean.class.toString())){
						map.put(key, EncodeUtil.toUtf8(String.valueOf(obj)));
						request += key + "=" + String.valueOf(obj);
					}else{
						map.put(key, EncodeUtil.toUtf8(JSON.toJSONString(obj)));
						request += key + "=" + JSON.toJSONString(obj);
					}

				}
				Log.i("request",url+ "..." + request);
				return map;
			}
		};
		RequestManager.getInstance().addRequest(request);
	}
	


}
