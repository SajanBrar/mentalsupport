package com.wiserbit.adjson;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

public class ad_manager {
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    String _JsonURL = "https://webintoapp.com/ads/";
    String _ad_display = "true";
    String _ad_display_global_exit = "true";
    String _ad_display_global_running = "true";
    boolean _ad_first_round = true;
    String _ad_mode = "banner ";
    /* access modifiers changed from: private */
    public int _ad_timming_delay = PathInterpolatorCompat.MAX_NUM_POINTS;
    /* access modifiers changed from: private */
    public int _ad_timming_next_call = 60000;
    String _ad_url = "";
    String _app_id = "";
    String _app_mode = "live";
    Boolean _b_post_click = false;
    Context _context;
    String _install_key = "";
    String _request_id = "";
    String _session_id = "";
    private WebView ad_mWebView;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    /* access modifiers changed from: private */
    public int interval;
    private ADJSONListner mAdEventListener;
    MyRunnable obj;
    SharedPreferences prefs = null;

    public void _print(String str) {
    }

    public void setAdEventListener(ADJSONListner aDJSONListner) {
        this.mAdEventListener = aDJSONListner;
    }

    private static String getRandomString(int i) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(36)));
        }
        return sb.toString();
    }

    public ad_manager(Context context, String str, String str2, int i) {
        this._ad_timming_delay = i * 1000;
        this._ad_timming_next_call = 60000;
        this._context = context;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences.getString("installkey", (String) null) == null) {
            defaultSharedPreferences.edit().putString("installkey", getRandomString(32)).apply();
        }
        this._install_key = defaultSharedPreferences.getString("installkey", (String) null);
        this._app_id = str2;
        this._JsonURL = str;
        String str3 = (this._JsonURL + "ads-start.json") + "?ai=" + this._app_id + "&ik=" + this._install_key;
        _print("START:" + str3);
        Volley.newRequestQueue(this._context).add(new StringRequest(0, str3, new Response.Listener<String>() {
            public void onResponse(String str) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (jSONObject.has("ad_display_running") && !jSONObject.isNull("ad_display_running")) {
                        ad_manager.this._ad_display_global_running = jSONObject.getString("ad_display_running");
                    }
                    if (jSONObject.has("ad_display_exit") && !jSONObject.isNull("ad_display_exit")) {
                        ad_manager.this._ad_display_global_exit = jSONObject.getString("ad_display_exit");
                    }
                    if (jSONObject.has("session_id") && !jSONObject.isNull("session_id")) {
                        ad_manager.this._session_id = jSONObject.getString("session_id");
                    }
                    if (jSONObject.has("app_mode") && !jSONObject.isNull("app_mode")) {
                        ad_manager.this._app_mode = jSONObject.getString("app_mode");
                    }
                    if (ad_manager.this._app_mode.equals("finish")) {
                        Toast.makeText(ad_manager.this._context, "The current version of this App is not available anymore. Please update to the new version or contact the administrator.", 1).show();
                        ((Activity) ad_manager.this._context).finish();
                    }
                    if (jSONObject.has("next_call") && !jSONObject.isNull("next_call")) {
                        int unused = ad_manager.this._ad_timming_next_call = Integer.parseInt(jSONObject.getString("next_call")) * 1000;
                    }
                    ad_manager.this._print("SES:" + ad_manager.this._session_id);
                    String str2 = (ad_manager.this._JsonURL + "ads-running.json") + "?ai=" + ad_manager.this._app_id + "&ik=" + ad_manager.this._install_key + "&si=" + ad_manager.this._session_id;
                    ad_manager.this._print(str2);
                    ad_manager ad_manager = ad_manager.this;
                    ad_manager.obj = new MyRunnable(str2);
                    ad_manager.this.handler.post(ad_manager.this.obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Error Get JSON", "Error:" + volleyError.toString());
            }
        }));
    }

    public void PauseRunning() {
        this.handler.removeCallbacks(this.obj);
    }

    public void UnPauseRunning() {
        this.handler.postDelayed(this.obj, (long) this._ad_timming_next_call);
    }

    public void PostClick(String str, String str2, String str3) {
        if (!this._b_post_click.booleanValue()) {
            RequestQueue newRequestQueue = Volley.newRequestQueue(this._context);
            final String str4 = str;
            final String str5 = str2;
            final String str6 = str3;
            newRequestQueue.add(new StringRequest(1, this._JsonURL + "ads-click.json", new Response.Listener<String>() {
                public void onResponse(String str) {
                    Log.d("Click successed", "Success");
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("Error click", "Error:" + volleyError.toString());
                }
            }) {
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                /* access modifiers changed from: protected */
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap hashMap = new HashMap();
                    hashMap.put("si", str4);
                    hashMap.put("ri", str5);
                    hashMap.put("ai", str6);
                    return hashMap;
                }
            });
            this._b_post_click = true;
        }
    }

    public void popup(final Boolean bool) {
        if (this._ad_display_global_exit.equals("false")) {
            if (bool.booleanValue()) {
                ((Activity) this._context).finish();
            } else {
                return;
            }
        }
        String str = (this._JsonURL + "ads-exit.json") + "?ai=" + this._app_id + "&ik=" + this._install_key + "&si=" + this._session_id;
        _print("POPUP:" + str);
        ad_popup ad_popup = new ad_popup();
        ad_popup.setAdEventListener(new ADJSONListner() {
            public void onOpen() {
            }

            public void onClick(String str) {
                ad_manager ad_manager = ad_manager.this;
                ad_manager._print("CLICKED POPUP:" + ad_manager.this._session_id + " req:" + str);
                ad_manager ad_manager2 = ad_manager.this;
                ad_manager2.PostClick(ad_manager2._session_id, str, ad_manager.this._app_id);
            }

            public void onClose() {
                if (bool.booleanValue()) {
                    ((Activity) ad_manager.this._context).finish();
                }
            }
        });
        ad_popup.LoadPopup(this._context, str, bool);
    }

    public class MyRunnable implements Runnable {
        private String JsonURL;
        ad_banner ad_banner;

        public MyRunnable(String str) {
            this.JsonURL = str;
        }

        public void call_adjson() {
            ad_manager.this._b_post_click = false;
            if (!ad_manager.this._ad_display_global_running.equals("false")) {
                Volley.newRequestQueue(ad_manager.this._context).add(new StringRequest(0, this.JsonURL, new Response.Listener<String>() {
                    public void onResponse(String str) {
                        try {
                            JSONObject jSONObject = new JSONObject(str);
                            if (jSONObject.has("ad_display") && !jSONObject.isNull("ad_display")) {
                                ad_manager.this._ad_display = jSONObject.getString("ad_display");
                            }
                            if (ad_manager.this._ad_display.equals("true")) {
                                if (jSONObject.has("ad_mode") && !jSONObject.isNull("ad_mode")) {
                                    ad_manager.this._ad_mode = jSONObject.getString("ad_mode");
                                }
                                if (jSONObject.has("ad_url") && !jSONObject.isNull("ad_url")) {
                                    ad_manager.this._ad_url = jSONObject.getString("ad_url");
                                }
                                if (jSONObject.has("request_id") && !jSONObject.isNull("request_id")) {
                                    ad_manager.this._request_id = jSONObject.getString("request_id");
                                }
                                if (ad_manager.this._ad_mode.equals("banner")) {
                                    if (MyRunnable.this.ad_banner != null) {
                                        MyRunnable.this.ad_banner.CloseBanner();
                                        MyRunnable.this.ad_banner = null;
                                    }
                                    MyRunnable myRunnable = MyRunnable.this;
                                    myRunnable.ad_banner = new ad_banner(ad_manager.this._context);
                                    MyRunnable.this.ad_banner.setAdEventListener(new ADJSONListner() {
                                        public void onClose() {
                                        }

                                        public void onOpen() {
                                        }

                                        public void onClick(String str) {
                                            ad_manager ad_manager = ad_manager.this;
                                            ad_manager._print("CLICKED BANNER:" + ad_manager.this._session_id + " req:" + str);
                                            ad_manager.this.PostClick(ad_manager.this._session_id, str, ad_manager.this._app_id);
                                        }
                                    });
                                    MyRunnable.this.ad_banner.LoadBanner(ad_manager.this._ad_url, ad_manager.this._request_id);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error Get JSON", "Error:" + volleyError.toString());
                    }
                }));
            }
        }

        public void run() {
            if (!ad_manager.this._ad_first_round) {
                call_adjson();
            } else {
                ad_manager ad_manager = ad_manager.this;
                int unused = ad_manager.interval = ad_manager._ad_timming_delay;
                ad_manager.this._ad_first_round = false;
            }
            ad_manager.this.handler.postDelayed(this, (long) ad_manager.this.interval);
            if (ad_manager.this.interval == ad_manager.this._ad_timming_delay) {
                ad_manager ad_manager2 = ad_manager.this;
                int unused2 = ad_manager2.interval = ad_manager2._ad_timming_next_call;
            }
        }
    }
}
