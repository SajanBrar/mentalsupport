package com.wiserbit.adjson;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.net.URL;

public class ad_popup {
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
            this.dialog.dismiss();
            this.opened = false;
        }
    }

    public boolean IsOpen() {
        return this.opened;
    }

    public void LoadPopup(final Context context, String str, final Boolean bool) {
        Volley.newRequestQueue(context).add(new StringRequest(0, str, new Response.Listener<String>() {
            /* JADX WARNING: Removed duplicated region for block: B:12:0x0053 A[Catch:{ JSONException -> 0x0242 }] */
            /* JADX WARNING: Removed duplicated region for block: B:15:0x0063 A[Catch:{ JSONException -> 0x0242 }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onResponse(java.lang.String r27) {
                /*
                    r26 = this;
                    r1 = r26
                    java.lang.String r0 = "ad_button_text_color"
                    java.lang.String r2 = "ad_text_color"
                    java.lang.String r3 = "ad_button_text"
                    java.lang.String r4 = "ad_price"
                    java.lang.String r5 = "ad_rating_color"
                    java.lang.String r6 = "ad_rating"
                    java.lang.String r7 = "ad_url"
                    java.lang.String r8 = "ad_icon"
                    java.lang.String r9 = "ad_desc"
                    java.lang.String r10 = "ad_title"
                    java.lang.String r11 = "request_id"
                    java.lang.String r12 = "ad_display"
                    java.lang.String r13 = "ad_button_background_color"
                    org.json.JSONObject r14 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0242 }
                    r15 = r27
                    r14.<init>(r15)     // Catch:{ JSONException -> 0x0242 }
                    java.lang.String r15 = "0"
                    java.lang.String r16 = "WebIntoApp"
                    java.lang.String r17 = "Convert your Website / HTML files into your own mobile App for Android and iOS, online & within a minute!"
                    java.lang.String r18 = ""
                    java.lang.String r19 = "https://www.webintoapp.com"
                    java.lang.String r20 = "5"
                    java.lang.String r21 = "#fdcc0d"
                    java.lang.String r22 = "FREE | DEDICATED"
                    java.lang.String r23 = "#ffffff"
                    java.lang.String r24 = "Click Here"
                    boolean r25 = r14.has(r12)     // Catch:{ JSONException -> 0x0242 }
                    r27 = r15
                    java.lang.String r15 = "true"
                    if (r25 == 0) goto L_0x004c
                    boolean r25 = r14.isNull(r12)     // Catch:{ JSONException -> 0x0242 }
                    if (r25 != 0) goto L_0x004c
                    java.lang.String r12 = r14.getString(r12)     // Catch:{ JSONException -> 0x0242 }
                    goto L_0x004d
                L_0x004c:
                    r12 = r15
                L_0x004d:
                    boolean r12 = r12.equals(r15)     // Catch:{ JSONException -> 0x0242 }
                    if (r12 != 0) goto L_0x0063
                    java.lang.Boolean r0 = r6     // Catch:{ JSONException -> 0x0242 }
                    boolean r0 = r0.booleanValue()     // Catch:{ JSONException -> 0x0242 }
                    if (r0 == 0) goto L_0x0062
                    android.content.Context r0 = r4     // Catch:{ JSONException -> 0x0242 }
                    android.app.Activity r0 = (android.app.Activity) r0     // Catch:{ JSONException -> 0x0242 }
                    r0.finish()     // Catch:{ JSONException -> 0x0242 }
                L_0x0062:
                    return
                L_0x0063:
                    boolean r12 = r14.has(r11)     // Catch:{ JSONException -> 0x0242 }
                    if (r12 == 0) goto L_0x0074
                    boolean r12 = r14.isNull(r11)     // Catch:{ JSONException -> 0x0242 }
                    if (r12 != 0) goto L_0x0074
                    java.lang.String r15 = r14.getString(r11)     // Catch:{ JSONException -> 0x0242 }
                    goto L_0x0076
                L_0x0074:
                    r15 = r27
                L_0x0076:
                    boolean r11 = r14.has(r10)     // Catch:{ JSONException -> 0x0242 }
                    if (r11 == 0) goto L_0x0086
                    boolean r11 = r14.isNull(r10)     // Catch:{ JSONException -> 0x0242 }
                    if (r11 != 0) goto L_0x0086
                    java.lang.String r16 = r14.getString(r10)     // Catch:{ JSONException -> 0x0242 }
                L_0x0086:
                    r10 = r16
                    boolean r11 = r14.has(r9)     // Catch:{ JSONException -> 0x0242 }
                    if (r11 == 0) goto L_0x0098
                    boolean r11 = r14.isNull(r9)     // Catch:{ JSONException -> 0x0242 }
                    if (r11 != 0) goto L_0x0098
                    java.lang.String r17 = r14.getString(r9)     // Catch:{ JSONException -> 0x0242 }
                L_0x0098:
                    r9 = r17
                    boolean r11 = r14.has(r8)     // Catch:{ JSONException -> 0x0242 }
                    if (r11 == 0) goto L_0x00aa
                    boolean r11 = r14.isNull(r8)     // Catch:{ JSONException -> 0x0242 }
                    if (r11 != 0) goto L_0x00aa
                    java.lang.String r18 = r14.getString(r8)     // Catch:{ JSONException -> 0x0242 }
                L_0x00aa:
                    boolean r8 = r14.has(r7)     // Catch:{ JSONException -> 0x0242 }
                    if (r8 == 0) goto L_0x00ba
                    boolean r8 = r14.isNull(r7)     // Catch:{ JSONException -> 0x0242 }
                    if (r8 != 0) goto L_0x00ba
                    java.lang.String r19 = r14.getString(r7)     // Catch:{ JSONException -> 0x0242 }
                L_0x00ba:
                    r7 = r19
                    boolean r8 = r14.has(r6)     // Catch:{ JSONException -> 0x0242 }
                    if (r8 == 0) goto L_0x00cc
                    boolean r8 = r14.isNull(r6)     // Catch:{ JSONException -> 0x0242 }
                    if (r8 != 0) goto L_0x00cc
                    java.lang.String r20 = r14.getString(r6)     // Catch:{ JSONException -> 0x0242 }
                L_0x00cc:
                    boolean r6 = r14.has(r5)     // Catch:{ JSONException -> 0x0242 }
                    if (r6 == 0) goto L_0x00dc
                    boolean r6 = r14.isNull(r5)     // Catch:{ JSONException -> 0x0242 }
                    if (r6 != 0) goto L_0x00dc
                    java.lang.String r21 = r14.getString(r5)     // Catch:{ JSONException -> 0x0242 }
                L_0x00dc:
                    boolean r5 = r14.has(r4)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 == 0) goto L_0x00ec
                    boolean r5 = r14.isNull(r4)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 != 0) goto L_0x00ec
                    java.lang.String r22 = r14.getString(r4)     // Catch:{ JSONException -> 0x0242 }
                L_0x00ec:
                    r4 = r22
                    boolean r5 = r14.has(r3)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 == 0) goto L_0x00fe
                    boolean r5 = r14.isNull(r3)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 != 0) goto L_0x00fe
                    java.lang.String r24 = r14.getString(r3)     // Catch:{ JSONException -> 0x0242 }
                L_0x00fe:
                    r3 = r24
                    boolean r5 = r14.has(r2)     // Catch:{ JSONException -> 0x0242 }
                    java.lang.String r6 = "#000000"
                    if (r5 == 0) goto L_0x0113
                    boolean r5 = r14.isNull(r2)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 != 0) goto L_0x0113
                    java.lang.String r2 = r14.getString(r2)     // Catch:{ JSONException -> 0x0242 }
                    goto L_0x0114
                L_0x0113:
                    r2 = r6
                L_0x0114:
                    boolean r5 = r14.has(r0)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 == 0) goto L_0x0124
                    boolean r5 = r14.isNull(r0)     // Catch:{ JSONException -> 0x0242 }
                    if (r5 != 0) goto L_0x0124
                    java.lang.String r6 = r14.getString(r0)     // Catch:{ JSONException -> 0x0242 }
                L_0x0124:
                    boolean r0 = r14.has(r13)     // Catch:{ JSONException -> 0x0242 }
                    if (r0 == 0) goto L_0x0134
                    boolean r0 = r14.isNull(r13)     // Catch:{ JSONException -> 0x0242 }
                    if (r0 != 0) goto L_0x0134
                    java.lang.String r23 = r14.getString(r13)     // Catch:{ JSONException -> 0x0242 }
                L_0x0134:
                    boolean r0 = r14.has(r13)     // Catch:{ JSONException -> 0x0242 }
                    if (r0 == 0) goto L_0x0144
                    boolean r0 = r14.isNull(r13)     // Catch:{ JSONException -> 0x0242 }
                    if (r0 != 0) goto L_0x0144
                    java.lang.String r23 = r14.getString(r13)     // Catch:{ JSONException -> 0x0242 }
                L_0x0144:
                    android.app.Dialog r0 = new android.app.Dialog     // Catch:{ JSONException -> 0x0242 }
                    android.content.Context r5 = r4     // Catch:{ JSONException -> 0x0242 }
                    r0.<init>(r5)     // Catch:{ JSONException -> 0x0242 }
                    int r5 = com.wiserbit.adjson.R.layout.ad_popup     // Catch:{ JSONException -> 0x0242 }
                    r0.setContentView(r5)     // Catch:{ JSONException -> 0x0242 }
                    r0.setTitle(r10)     // Catch:{ JSONException -> 0x0242 }
                    int r5 = com.wiserbit.adjson.R.id.adjson_title     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r5 = r0.findViewById(r5)     // Catch:{ JSONException -> 0x0242 }
                    android.widget.TextView r5 = (android.widget.TextView) r5     // Catch:{ JSONException -> 0x0242 }
                    r5.setText(r10)     // Catch:{ JSONException -> 0x0242 }
                    int r8 = android.graphics.Color.parseColor(r2)     // Catch:{ JSONException -> 0x0242 }
                    r5.setTextColor(r8)     // Catch:{ JSONException -> 0x0242 }
                    int r5 = com.wiserbit.adjson.R.id.adjson_description     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r5 = r0.findViewById(r5)     // Catch:{ JSONException -> 0x0242 }
                    android.widget.TextView r5 = (android.widget.TextView) r5     // Catch:{ JSONException -> 0x0242 }
                    r5.setText(r9)     // Catch:{ JSONException -> 0x0242 }
                    int r8 = android.graphics.Color.parseColor(r2)     // Catch:{ JSONException -> 0x0242 }
                    r5.setTextColor(r8)     // Catch:{ JSONException -> 0x0242 }
                    int r5 = com.wiserbit.adjson.R.id.adjson_price     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r5 = r0.findViewById(r5)     // Catch:{ JSONException -> 0x0242 }
                    android.widget.TextView r5 = (android.widget.TextView) r5     // Catch:{ JSONException -> 0x0242 }
                    r5.setText(r4)     // Catch:{ JSONException -> 0x0242 }
                    int r2 = android.graphics.Color.parseColor(r2)     // Catch:{ JSONException -> 0x0242 }
                    r5.setTextColor(r2)     // Catch:{ JSONException -> 0x0242 }
                    int r2 = com.wiserbit.adjson.R.id.adjson_cta     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r2 = r0.findViewById(r2)     // Catch:{ JSONException -> 0x0242 }
                    android.widget.Button r2 = (android.widget.Button) r2     // Catch:{ JSONException -> 0x0242 }
                    r2.setText(r3)     // Catch:{ JSONException -> 0x0242 }
                    int r3 = android.graphics.Color.parseColor(r6)     // Catch:{ JSONException -> 0x0242 }
                    r2.setTextColor(r3)     // Catch:{ JSONException -> 0x0242 }
                    int r3 = com.wiserbit.adjson.R.drawable.corner_button     // Catch:{ JSONException -> 0x0242 }
                    r2.setBackgroundResource(r3)     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.drawable.Drawable r3 = r2.getBackground()     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.drawable.GradientDrawable r3 = (android.graphics.drawable.GradientDrawable) r3     // Catch:{ JSONException -> 0x0242 }
                    int r4 = android.graphics.Color.parseColor(r23)     // Catch:{ JSONException -> 0x0242 }
                    r3.setColor(r4)     // Catch:{ JSONException -> 0x0242 }
                    int r3 = com.wiserbit.adjson.R.id.adjson_rating     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r3 = r0.findViewById(r3)     // Catch:{ JSONException -> 0x0242 }
                    androidx.appcompat.widget.AppCompatRatingBar r3 = (androidx.appcompat.widget.AppCompatRatingBar) r3     // Catch:{ JSONException -> 0x0242 }
                    float r4 = java.lang.Float.parseFloat(r20)     // Catch:{ JSONException -> 0x0242 }
                    r3.setRating(r4)     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.drawable.Drawable r3 = r3.getProgressDrawable()     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.drawable.LayerDrawable r3 = (android.graphics.drawable.LayerDrawable) r3     // Catch:{ JSONException -> 0x0242 }
                    r4 = 2
                    android.graphics.drawable.Drawable r4 = r3.getDrawable(r4)     // Catch:{ JSONException -> 0x0242 }
                    int r5 = android.graphics.Color.parseColor(r21)     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.PorterDuff$Mode r6 = android.graphics.PorterDuff.Mode.SRC_ATOP     // Catch:{ JSONException -> 0x0242 }
                    r4.setColorFilter(r5, r6)     // Catch:{ JSONException -> 0x0242 }
                    r4 = 0
                    android.graphics.drawable.Drawable r5 = r3.getDrawable(r4)     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.PorterDuff$Mode r6 = android.graphics.PorterDuff.Mode.SRC_ATOP     // Catch:{ JSONException -> 0x0242 }
                    r8 = -7829368(0xffffffffff888888, float:NaN)
                    r5.setColorFilter(r8, r6)     // Catch:{ JSONException -> 0x0242 }
                    r5 = 1
                    android.graphics.drawable.Drawable r3 = r3.getDrawable(r5)     // Catch:{ JSONException -> 0x0242 }
                    int r5 = android.graphics.Color.parseColor(r21)     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.PorterDuff$Mode r6 = android.graphics.PorterDuff.Mode.SRC_ATOP     // Catch:{ JSONException -> 0x0242 }
                    r3.setColorFilter(r5, r6)     // Catch:{ JSONException -> 0x0242 }
                    com.wiserbit.adjson.ad_popup$DownloadImageTask r3 = new com.wiserbit.adjson.ad_popup$DownloadImageTask     // Catch:{ JSONException -> 0x0242 }
                    com.wiserbit.adjson.ad_popup r5 = com.wiserbit.adjson.ad_popup.this     // Catch:{ JSONException -> 0x0242 }
                    int r6 = com.wiserbit.adjson.R.id.adjson_app_icon     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r6 = r0.findViewById(r6)     // Catch:{ JSONException -> 0x0242 }
                    android.widget.ImageView r6 = (android.widget.ImageView) r6     // Catch:{ JSONException -> 0x0242 }
                    r3.<init>(r6)     // Catch:{ JSONException -> 0x0242 }
                    java.lang.String[] r5 = new java.lang.String[]{r18}     // Catch:{ JSONException -> 0x0242 }
                    r3.execute(r5)     // Catch:{ JSONException -> 0x0242 }
                    com.wiserbit.adjson.ad_popup$1$1 r3 = new com.wiserbit.adjson.ad_popup$1$1     // Catch:{ JSONException -> 0x0242 }
                    r3.<init>(r7, r15, r0)     // Catch:{ JSONException -> 0x0242 }
                    r2.setOnClickListener(r3)     // Catch:{ JSONException -> 0x0242 }
                    com.wiserbit.adjson.ad_popup$1$2 r2 = new com.wiserbit.adjson.ad_popup$1$2     // Catch:{ JSONException -> 0x0242 }
                    r2.<init>()     // Catch:{ JSONException -> 0x0242 }
                    r0.setOnShowListener(r2)     // Catch:{ JSONException -> 0x0242 }
                    com.wiserbit.adjson.ad_popup$1$3 r2 = new com.wiserbit.adjson.ad_popup$1$3     // Catch:{ JSONException -> 0x0242 }
                    r2.<init>()     // Catch:{ JSONException -> 0x0242 }
                    r0.setOnDismissListener(r2)     // Catch:{ JSONException -> 0x0242 }
                    android.view.Window r2 = r0.getWindow()     // Catch:{ JSONException -> 0x0242 }
                    r3 = -1
                    r5 = -2
                    r2.setLayout(r3, r5)     // Catch:{ JSONException -> 0x0242 }
                    android.view.Window r2 = r0.getWindow()     // Catch:{ JSONException -> 0x0242 }
                    android.graphics.drawable.ColorDrawable r3 = new android.graphics.drawable.ColorDrawable     // Catch:{ JSONException -> 0x0242 }
                    r3.<init>(r4)     // Catch:{ JSONException -> 0x0242 }
                    r2.setBackgroundDrawable(r3)     // Catch:{ JSONException -> 0x0242 }
                    int r2 = com.wiserbit.adjson.R.id.button_close     // Catch:{ JSONException -> 0x0242 }
                    android.view.View r2 = r0.findViewById(r2)     // Catch:{ JSONException -> 0x0242 }
                    android.widget.ImageButton r2 = (android.widget.ImageButton) r2     // Catch:{ JSONException -> 0x0242 }
                    com.wiserbit.adjson.ad_popup$1$4 r3 = new com.wiserbit.adjson.ad_popup$1$4     // Catch:{ JSONException -> 0x0242 }
                    r3.<init>(r0)     // Catch:{ JSONException -> 0x0242 }
                    r2.setOnClickListener(r3)     // Catch:{ JSONException -> 0x0242 }
                    r0.show()     // Catch:{ JSONException -> 0x0242 }
                    goto L_0x0246
                L_0x0242:
                    r0 = move-exception
                    r0.printStackTrace()
                L_0x0246:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.wiserbit.adjson.ad_popup.AnonymousClass1.onResponse(java.lang.String):void");
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Error Get JSON", "Error:" + volleyError.toString());
            }
        }));
    }

    public static void changeDialogBackground(Dialog dialog2, final int i) {
        dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                ((AlertDialog) dialogInterface).getWindow().getDecorView().setBackgroundColor(i);
            }
        });
    }
}
