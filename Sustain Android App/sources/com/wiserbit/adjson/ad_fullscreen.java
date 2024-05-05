package com.wiserbit.adjson;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class ad_fullscreen {
    String _request_id = "";
    Dialog dialog;
    /* access modifiers changed from: private */
    public ADJSONListner mAdEventListener;
    boolean opened = false;

    public void setAdEventListener(ADJSONListner aDJSONListner) {
        this.mAdEventListener = aDJSONListner;
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView imageView) {
            this.bmImage = imageView;
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(String... strArr) {
            try {
                return BitmapFactory.decodeStream(new URL(strArr[0]).openStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            this.bmImage.setImageBitmap(bitmap);
        }
    }

    public void Close() {
        if (this.opened) {
            this.opened = false;
            this.dialog.dismiss();
        }
    }

    public boolean IsOpen() {
        return this.opened;
    }

    public void LoadFullscreen(final Context context, String str) {
        Volley.newRequestQueue(context).add(new StringRequest(0, str, new Response.Listener<String>() {
            public void onResponse(String str) {
                try {
                    String string = new JSONObject(str).getString("app_title");
                    Context context = context;
                    Toast.makeText(context, "WE:" + string, 1).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ad_fullscreen.this.dialog = new Dialog(context, 16973834);
                ad_fullscreen.this.dialog.setContentView(R.layout.ad_fullscreen);
                ad_fullscreen ad_fullscreen = ad_fullscreen.this;
                new DownloadImageTask((ImageView) ad_fullscreen.dialog.findViewById(R.id.image)).execute(new String[]{"https://webintoapp.com/favicons/android-icon-192x192.png"});
                ((ImageView) ad_fullscreen.this.dialog.findViewById(R.id.image)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.webintoapp.com")));
                        if (ad_fullscreen.this.mAdEventListener != null) {
                            ad_fullscreen.this.mAdEventListener.onClick(ad_fullscreen.this._request_id);
                        }
                        ad_fullscreen.this.dialog.dismiss();
                    }
                });
                ad_fullscreen.this.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialogInterface) {
                        ad_fullscreen.this.opened = true;
                        if (ad_fullscreen.this.mAdEventListener != null) {
                            ad_fullscreen.this.mAdEventListener.onOpen();
                        }
                    }
                });
                ((ImageButton) ad_fullscreen.this.dialog.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ad_fullscreen.this.mAdEventListener != null) {
                            ad_fullscreen.this.mAdEventListener.onClose();
                        }
                        ad_fullscreen.this.opened = false;
                        ad_fullscreen.this.dialog.dismiss();
                    }
                });
                ad_fullscreen.this.dialog.show();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Error Get JSON", "Error:" + volleyError.toString());
            }
        }));
    }
}
