package me.xucan.safedrive.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import me.xucan.safedrive.App;


public class RequestManager {

    private String TAG = "RequestManager";

    private static RequestManager requestManager;

    private ImageLoader mImageLoader;

    private  static Context sContext;
    
    private RequestQueue mRequestQueue;


    public static RequestManager getInstance(){

        if (requestManager == null){
            requestManager = new RequestManager(App.getInstance().getContext());
        }
        return requestManager;
    }

    private RequestManager(Context context){
        sContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sContext);
        }
        return mRequestQueue;
    }


    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
    
    public void addRequest(Request request){
        request.setRetryPolicy(new DefaultRetryPolicy(300*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    	mRequestQueue.add(request);
    }

}
