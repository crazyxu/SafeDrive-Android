package me.xucan.safedrive.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author xucan
 */
public class MJsonRequest extends JsonRequest<JSONObject> {
	private String url;
	private JSONObject jsonObj;
	private MRequestListener listener;
	
	public MJsonRequest(final String url, Map<String, Object> params, final MRequestListener listener){
		super(Method.POST, url, params.toString(), new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub

			}
			
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				if (error != null) {
					Log.e("VolleyError", error.getMessage());
				}
				listener.onError(url, -1, "请求提交失败，稍后再试");
			}
		});
		this.jsonObj = jsonObj;
		this.url = url;
		this.listener = listener;
		this.setRetryPolicy(new DefaultRetryPolicy(10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}
	






	/* (non-Javadoc)
	 * @see com.android.volley.toolbox.JsonRequest#parseNetworkResponse(com.android.volley.NetworkResponse)
	 */
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(JSON.parseObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        } 
	}

}
