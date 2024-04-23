package com.nitj.sustain;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class JavaScriptInterface {
    private Context context;

    public JavaScriptInterface(Context context2) {
        this.context = context2;
    }

    @JavascriptInterface
    public void getBase64FromBlobData(final String str, final String str2) throws IOException {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Save As");
        final EditText editText = new EditText(this.context);
        editText.setInputType(1);
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    JavaScriptInterface.this.convertBase64StringAndStoreIt(str, str2, editText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public static String getBase64StringFromBlobUrl(String str, String str2) {
        if (!str.startsWith("blob")) {
            return "javascript: console.log('It is not a Blob URL');";
        }
        return "javascript: var xhr = new XMLHttpRequest();xhr.open('GET', '" + str + "', true);xhr.setRequestHeader('Content-type','" + str2 + "');xhr.responseType = 'blob';xhr.onload = function(e) {    if (this.status == 200) {        var blobData = this.response;        var reader = new FileReader();        reader.readAsDataURL(blobData);        reader.onloadend = function() {            base64data = reader.result;            Android.getBase64FromBlobData(base64data, '" + str2 + "');        }    }};xhr.send();";
    }

    /* access modifiers changed from: private */
    public void convertBase64StringAndStoreIt(String str, String str2, String str3) throws IOException {
        String str4 = str2.split("/")[1];
        byte[] decode = Base64.decode(str.replaceFirst("^data:" + str2 + ";base64,", ""), 0);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + str3);
        byte[] bytes = new String(decode, "UTF-8").getBytes(Charset.forName("UTF-8"));
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        if (file.exists()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Context context2 = this.context;
            intent.setDataAndType(FileProvider.getUriForFile(context2, this.context.getApplicationContext().getPackageName() + ".provider", file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(str4));
            intent.addFlags(1);
            PendingIntent activity = PendingIntent.getActivity(this.context, 1, intent, 335544320);
            final NotificationManager notificationManager = (NotificationManager) this.context.getSystemService("notification");
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel m = JavaScriptInterface$$ExternalSyntheticApiModelOutline0.m("MYCHANNEL", "name", 2);
                JavaScriptInterface$$ExternalSyntheticApiModelOutline0.m();
                Notification build = JavaScriptInterface$$ExternalSyntheticApiModelOutline0.m(this.context, "MYCHANNEL").setContentTitle("Download Complete").setContentText(str3).setContentIntent(activity).setChannelId("MYCHANNEL").setSmallIcon(17301634).build();
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(m);
                    notificationManager.notify(1, build);
                }
            } else {
                NotificationCompat.Builder contentText = new NotificationCompat.Builder(this.context, "MYCHANNEL").setDefaults(-1).setWhen(System.currentTimeMillis()).setSmallIcon(17301646).setContentTitle("Download Complete").setContentText(str3);
                if (notificationManager != null) {
                    notificationManager.notify(1, contentText.build());
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            notificationManager.cancel(1);
                        }
                    }, 1000);
                }
            }
        }
        Toast.makeText(this.context, "Download Completed.", 0).show();
    }
}
