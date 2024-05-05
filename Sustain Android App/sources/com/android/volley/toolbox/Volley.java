package com.android.volley.toolbox;

import android.content.Context;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import java.io.File;

public class Volley {
    private static final String DEFAULT_CACHE_DIR = "volley";

    public static RequestQueue newRequestQueue(Context context, BaseHttpStack baseHttpStack) {
        return newRequestQueue(context, (Network) baseHttpStack == null ? new BasicNetwork((BaseHttpStack) new HurlStack()) : new BasicNetwork(baseHttpStack));
    }

    @Deprecated
    public static RequestQueue newRequestQueue(Context context, HttpStack httpStack) {
        if (httpStack != null) {
            return newRequestQueue(context, (Network) new BasicNetwork(httpStack));
        }
        BaseHttpStack baseHttpStack = null;
        return newRequestQueue(context, (BaseHttpStack) null);
    }

    private static RequestQueue newRequestQueue(Context context, Network network) {
        final Context applicationContext = context.getApplicationContext();
        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache((DiskBasedCache.FileSupplier) new DiskBasedCache.FileSupplier() {
            private File cacheDir = null;

            public File get() {
                if (this.cacheDir == null) {
                    this.cacheDir = new File(applicationContext.getCacheDir(), Volley.DEFAULT_CACHE_DIR);
                }
                return this.cacheDir;
            }
        }), network);
        requestQueue.start();
        return requestQueue;
    }

    public static RequestQueue newRequestQueue(Context context) {
        BaseHttpStack baseHttpStack = null;
        return newRequestQueue(context, (BaseHttpStack) null);
    }
}
