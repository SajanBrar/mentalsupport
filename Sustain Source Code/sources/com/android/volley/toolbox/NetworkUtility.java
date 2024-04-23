package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.ClientError;
import com.android.volley.Header;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;

final class NetworkUtility {
    private static final int SLOW_REQUEST_THRESHOLD_MS = 3000;

    private NetworkUtility() {
    }

    static void logSlowRequests(long j, Request<?> request, byte[] bArr, int i) {
        if (VolleyLog.DEBUG || j > 3000) {
            Object[] objArr = new Object[5];
            objArr[0] = request;
            objArr[1] = Long.valueOf(j);
            objArr[2] = bArr != null ? Integer.valueOf(bArr.length) : "null";
            objArr[3] = Integer.valueOf(i);
            objArr[4] = Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount());
            VolleyLog.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]", objArr);
        }
    }

    static NetworkResponse getNotModifiedNetworkResponse(Request<?> request, long j, List<Header> list) {
        Cache.Entry cacheEntry = request.getCacheEntry();
        if (cacheEntry == null) {
            return new NetworkResponse(304, (byte[]) null, true, j, list);
        }
        return new NetworkResponse(304, cacheEntry.data, true, j, HttpHeaderParser.combineHeaders(list, cacheEntry));
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0035 A[SYNTHETIC, Splitter:B:19:0x0035] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static byte[] inputStreamToBytes(java.io.InputStream r5, int r6, com.android.volley.toolbox.ByteArrayPool r7) throws java.io.IOException {
        /*
            java.lang.String r0 = "Error occurred when closing InputStream"
            com.android.volley.toolbox.PoolingByteArrayOutputStream r1 = new com.android.volley.toolbox.PoolingByteArrayOutputStream
            r1.<init>(r7, r6)
            r6 = 1024(0x400, float:1.435E-42)
            r2 = 0
            byte[] r6 = r7.getBuf(r6)     // Catch:{ all -> 0x0031 }
        L_0x000e:
            int r3 = r5.read(r6)     // Catch:{ all -> 0x002f }
            r4 = -1
            if (r3 == r4) goto L_0x0019
            r1.write(r6, r2, r3)     // Catch:{ all -> 0x002f }
            goto L_0x000e
        L_0x0019:
            byte[] r3 = r1.toByteArray()     // Catch:{ all -> 0x002f }
            if (r5 == 0) goto L_0x0028
            r5.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0028
        L_0x0023:
            java.lang.Object[] r5 = new java.lang.Object[r2]
            com.android.volley.VolleyLog.v(r0, r5)
        L_0x0028:
            r7.returnBuf(r6)
            r1.close()
            return r3
        L_0x002f:
            r3 = move-exception
            goto L_0x0033
        L_0x0031:
            r3 = move-exception
            r6 = 0
        L_0x0033:
            if (r5 == 0) goto L_0x003e
            r5.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x003e
        L_0x0039:
            java.lang.Object[] r5 = new java.lang.Object[r2]
            com.android.volley.VolleyLog.v(r0, r5)
        L_0x003e:
            r7.returnBuf(r6)
            r1.close()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.NetworkUtility.inputStreamToBytes(java.io.InputStream, int, com.android.volley.toolbox.ByteArrayPool):byte[]");
    }

    static void attemptRetryOnException(Request<?> request, RetryInfo retryInfo) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int timeoutMs = request.getTimeoutMs();
        try {
            retryPolicy.retry(retryInfo.errorToRetry);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{retryInfo.logPrefix, Integer.valueOf(timeoutMs)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{retryInfo.logPrefix, Integer.valueOf(timeoutMs)}));
            throw e;
        }
    }

    static class RetryInfo {
        /* access modifiers changed from: private */
        public final VolleyError errorToRetry;
        /* access modifiers changed from: private */
        public final String logPrefix;

        private RetryInfo(String str, VolleyError volleyError) {
            this.logPrefix = str;
            this.errorToRetry = volleyError;
        }
    }

    static RetryInfo shouldRetryException(Request<?> request, IOException iOException, long j, HttpResponse httpResponse, byte[] bArr) throws VolleyError {
        if (iOException instanceof SocketTimeoutException) {
            return new RetryInfo("socket", new TimeoutError());
        }
        if (iOException instanceof MalformedURLException) {
            throw new RuntimeException("Bad URL " + request.getUrl(), iOException);
        } else if (httpResponse != null) {
            int statusCode = httpResponse.getStatusCode();
            VolleyLog.e("Unexpected response code %d for %s", Integer.valueOf(statusCode), request.getUrl());
            if (bArr == null) {
                return new RetryInfo("network", new NetworkError());
            }
            int i = statusCode;
            byte[] bArr2 = bArr;
            NetworkResponse networkResponse = new NetworkResponse(i, bArr2, false, SystemClock.elapsedRealtime() - j, httpResponse.getHeaders());
            if (statusCode == 401 || statusCode == 403) {
                return new RetryInfo("auth", new AuthFailureError(networkResponse));
            }
            if (statusCode >= 400 && statusCode <= 499) {
                throw new ClientError(networkResponse);
            } else if (statusCode >= 500 && statusCode <= 599 && request.shouldRetryServerErrors()) {
                return new RetryInfo("server", new ServerError(networkResponse));
            } else {
                throw new ServerError(networkResponse);
            }
        } else if (request.shouldRetryConnectionErrors()) {
            return new RetryInfo("connection", new NoConnectionError());
        } else {
            throw new NoConnectionError(iOException);
        }
    }
}
