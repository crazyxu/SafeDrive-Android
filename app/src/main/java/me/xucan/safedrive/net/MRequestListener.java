package me.xucan.safedrive.net;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xucan
 */
public interface MRequestListener {
	void onSuccess(String requestUrl, JSONObject response);
	void onError(String requestUrl, int errCode, String errMsg);

}
