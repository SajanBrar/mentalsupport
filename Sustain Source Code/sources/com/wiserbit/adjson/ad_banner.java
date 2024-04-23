package com.wiserbit.adjson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.net.URL;

public class ad_banner {
    View LinearLayout;
    private final Context _context;
    private WebView ad_mWebView;
    /* access modifiers changed from: private */
    public ADJSONListner mAdEventListener;
    boolean opened = false;
    ViewGroup rootView;

    public ad_banner(Context context) {
        this._context = context;
    }

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
        }
    }

    public boolean IsOpen() {
        return this.opened;
    }

    public void CloseBanner() {
        this.rootView.removeView(this.LinearLayout);
    }

    public void LoadBanner(String str, final String str2) {
        Display defaultDisplay = ((WindowManager) this._context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.y;
        this.rootView = (ViewGroup) ((Activity) this._context).findViewById(16908290);
        View inflate = View.inflate(this._context, R.layout.ad_banner, (ViewGroup) null);
        this.LinearLayout = inflate;
        this.rootView.addView(inflate);
        WebView webView = (WebView) this.rootView.findViewById(R.id.activity_ad_webview);
        this.ad_mWebView = webView;
        webView.setWebChromeClient(new WebChromeClient());
        this.ad_mWebView.getSettings().setJavaScriptEnabled(true);
        this.ad_mWebView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((WebView) view).getHitTestResult();
                if (ad_banner.this.mAdEventListener == null) {
                    return false;
                }
                ad_banner.this.mAdEventListener.onClick(str2);
                return false;
            }
        });
        this.ad_mWebView.loadUrl(str);
        this.ad_mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, String str) {
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                new Intent("android.intent.action.VIEW", Uri.parse(str));
                webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }
        });
        ImageButton imageButton = (ImageButton) this.rootView.findViewById(R.id.button_close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ad_banner.this.rootView.removeView(ad_banner.this.LinearLayout);
                if (ad_banner.this.mAdEventListener != null) {
                    ad_banner.this.mAdEventListener.onClose();
                }
            }
        });
        imageButton.getBackground().setAlpha(64);
        this.opened = true;
    }
}
