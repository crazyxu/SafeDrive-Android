package me.xucan.safedrive.net;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xucan
 */
public interface MRequestListener {
	//请求成功
	void onSuccess(String requestUrl, JSONObject response);
	//请求失败
	void onError(String requestUrl, int errCode, String errMsg);
}
