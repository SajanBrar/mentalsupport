package com.android.volley.toolbox;

import android.os.SystemClock;
import android.text.TextUtils;
import com.android.volley.Cache;
import com.android.volley.Header;
import com.android.volley.VolleyLog;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DiskBasedCache implements Cache {
    private static final int CACHE_MAGIC = 538247942;
    private static final int DEFAULT_DISK_USAGE_BYTES = 5242880;
    static final float HYSTERESIS_FACTOR = 0.9f;
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final FileSupplier mRootDirectorySupplier;
    private long mTotalSize;

    public interface FileSupplier {
        File get();
    }

    public DiskBasedCache(final File file, int i) {
        this.mEntries = new LinkedHashMap(16, 0.75f, true);
        this.mTotalSize = 0;
        this.mRootDirectorySupplier = new FileSupplier() {
            public File get() {
                return file;
            }
        };
        this.mMaxCacheSizeInBytes = i;
    }

    public DiskBasedCache(FileSupplier fileSupplier, int i) {
        this.mEntries = new LinkedHashMap(16, 0.75f, true);
        this.mTotalSize = 0;
        this.mRootDirectorySupplier = fileSupplier;
        this.mMaxCacheSizeInBytes = i;
    }

    public DiskBasedCache(File file) {
        this(file, (int) DEFAULT_DISK_USAGE_BYTES);
    }

    public DiskBasedCache(FileSupplier fileSupplier) {
        this(fileSupplier, (int) DEFAULT_DISK_USAGE_BYTES);
    }

    public synchronized void clear() {
        File[] listFiles = this.mRootDirectorySupplier.get().listFiles();
        if (listFiles != null) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
        this.mEntries.clear();
        this.mTotalSize = 0;
        VolleyLog.d("Cache cleared.", new Object[0]);
    }

    public synchronized Cache.Entry get(String str) {
        CountingInputStream countingInputStream;
        CacheHeader cacheHeader = this.mEntries.get(str);
        if (cacheHeader == null) {
            return null;
        }
        File fileForKey = getFileForKey(str);
        try {
            countingInputStream = new CountingInputStream(new BufferedInputStream(createInputStream(fileForKey)), fileForKey.length());
            CacheHeader readHeader = CacheHeader.readHeader(countingInputStream);
            if (!TextUtils.equals(str, readHeader.key)) {
                VolleyLog.d("%s: key=%s, found=%s", fileForKey.getAbsolutePath(), str, readHeader.key);
                removeEntry(str);
                countingInputStream.close();
                return null;
            }
            Cache.Entry cacheEntry = cacheHeader.toCacheEntry(streamToBytes(countingInputStream, countingInputStream.bytesRemaining()));
            countingInputStream.close();
            return cacheEntry;
        } catch (IOException e) {
            VolleyLog.d("%s: %s", fileForKey.getAbsolutePath(), e.toString());
            remove(str);
            return null;
        } catch (Throwable th) {
            countingInputStream.close();
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0023, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0057 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initialize() {
        /*
            r9 = this;
            monitor-enter(r9)
            com.android.volley.toolbox.DiskBasedCache$FileSupplier r0 = r9.mRootDirectorySupplier     // Catch:{ all -> 0x005f }
            java.io.File r0 = r0.get()     // Catch:{ all -> 0x005f }
            boolean r1 = r0.exists()     // Catch:{ all -> 0x005f }
            r2 = 0
            if (r1 != 0) goto L_0x0024
            boolean r1 = r0.mkdirs()     // Catch:{ all -> 0x005f }
            if (r1 != 0) goto L_0x0022
            java.lang.String r1 = "Unable to create cache dir %s"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x005f }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ all -> 0x005f }
            r3[r2] = r0     // Catch:{ all -> 0x005f }
            com.android.volley.VolleyLog.e(r1, r3)     // Catch:{ all -> 0x005f }
        L_0x0022:
            monitor-exit(r9)
            return
        L_0x0024:
            java.io.File[] r0 = r0.listFiles()     // Catch:{ all -> 0x005f }
            if (r0 != 0) goto L_0x002c
            monitor-exit(r9)
            return
        L_0x002c:
            int r1 = r0.length     // Catch:{ all -> 0x005f }
        L_0x002d:
            if (r2 >= r1) goto L_0x005d
            r3 = r0[r2]     // Catch:{ all -> 0x005f }
            long r4 = r3.length()     // Catch:{ IOException -> 0x0057 }
            com.android.volley.toolbox.DiskBasedCache$CountingInputStream r6 = new com.android.volley.toolbox.DiskBasedCache$CountingInputStream     // Catch:{ IOException -> 0x0057 }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0057 }
            java.io.InputStream r8 = r9.createInputStream(r3)     // Catch:{ IOException -> 0x0057 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x0057 }
            r6.<init>(r7, r4)     // Catch:{ IOException -> 0x0057 }
            com.android.volley.toolbox.DiskBasedCache$CacheHeader r7 = com.android.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r6)     // Catch:{ all -> 0x0052 }
            r7.size = r4     // Catch:{ all -> 0x0052 }
            java.lang.String r4 = r7.key     // Catch:{ all -> 0x0052 }
            r9.putEntry(r4, r7)     // Catch:{ all -> 0x0052 }
            r6.close()     // Catch:{ IOException -> 0x0057 }
            goto L_0x005a
        L_0x0052:
            r4 = move-exception
            r6.close()     // Catch:{ IOException -> 0x0057 }
            throw r4     // Catch:{ IOException -> 0x0057 }
        L_0x0057:
            r3.delete()     // Catch:{ all -> 0x005f }
        L_0x005a:
            int r2 = r2 + 1
            goto L_0x002d
        L_0x005d:
            monitor-exit(r9)
            return
        L_0x005f:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.initialize():void");
    }

    public synchronized void invalidate(String str, boolean z) {
        Cache.Entry entry = get(str);
        if (entry != null) {
            entry.softTtl = 0;
            if (z) {
                entry.ttl = 0;
            }
            put(str, entry);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0069, code lost:
        if (r0.delete() == false) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006b, code lost:
        com.android.volley.VolleyLog.d("Could not clean up file %s", r0.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0078, code lost:
        initializeIfRootDirectoryDeleted();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0065 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void put(java.lang.String r8, com.android.volley.Cache.Entry r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            long r0 = r7.mTotalSize     // Catch:{ all -> 0x007d }
            byte[] r2 = r9.data     // Catch:{ all -> 0x007d }
            int r2 = r2.length     // Catch:{ all -> 0x007d }
            long r2 = (long) r2     // Catch:{ all -> 0x007d }
            long r0 = r0 + r2
            int r2 = r7.mMaxCacheSizeInBytes     // Catch:{ all -> 0x007d }
            long r2 = (long) r2     // Catch:{ all -> 0x007d }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x0020
            byte[] r0 = r9.data     // Catch:{ all -> 0x007d }
            int r0 = r0.length     // Catch:{ all -> 0x007d }
            float r0 = (float) r0     // Catch:{ all -> 0x007d }
            int r1 = r7.mMaxCacheSizeInBytes     // Catch:{ all -> 0x007d }
            float r1 = (float) r1
            r2 = 1063675494(0x3f666666, float:0.9)
            float r1 = r1 * r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x0020
            monitor-exit(r7)
            return
        L_0x0020:
            java.io.File r0 = r7.getFileForKey(r8)     // Catch:{ all -> 0x007d }
            r1 = 0
            r2 = 1
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x0065 }
            java.io.OutputStream r4 = r7.createOutputStream(r0)     // Catch:{ IOException -> 0x0065 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x0065 }
            com.android.volley.toolbox.DiskBasedCache$CacheHeader r4 = new com.android.volley.toolbox.DiskBasedCache$CacheHeader     // Catch:{ IOException -> 0x0065 }
            r4.<init>(r8, r9)     // Catch:{ IOException -> 0x0065 }
            boolean r5 = r4.writeHeader(r3)     // Catch:{ IOException -> 0x0065 }
            if (r5 == 0) goto L_0x004f
            byte[] r9 = r9.data     // Catch:{ IOException -> 0x0065 }
            r3.write(r9)     // Catch:{ IOException -> 0x0065 }
            r3.close()     // Catch:{ IOException -> 0x0065 }
            long r5 = r0.length()     // Catch:{ IOException -> 0x0065 }
            r4.size = r5     // Catch:{ IOException -> 0x0065 }
            r7.putEntry(r8, r4)     // Catch:{ IOException -> 0x0065 }
            r7.pruneIfNeeded()     // Catch:{ IOException -> 0x0065 }
            goto L_0x007b
        L_0x004f:
            r3.close()     // Catch:{ IOException -> 0x0065 }
            java.lang.String r8 = "Failed to write header for %s"
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ IOException -> 0x0065 }
            java.lang.String r3 = r0.getAbsolutePath()     // Catch:{ IOException -> 0x0065 }
            r9[r1] = r3     // Catch:{ IOException -> 0x0065 }
            com.android.volley.VolleyLog.d(r8, r9)     // Catch:{ IOException -> 0x0065 }
            java.io.IOException r8 = new java.io.IOException     // Catch:{ IOException -> 0x0065 }
            r8.<init>()     // Catch:{ IOException -> 0x0065 }
            throw r8     // Catch:{ IOException -> 0x0065 }
        L_0x0065:
            boolean r8 = r0.delete()     // Catch:{ all -> 0x007d }
            if (r8 != 0) goto L_0x0078
            java.lang.String r8 = "Could not clean up file %s"
            java.lang.Object[] r9 = new java.lang.Object[r2]     // Catch:{ all -> 0x007d }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ all -> 0x007d }
            r9[r1] = r0     // Catch:{ all -> 0x007d }
            com.android.volley.VolleyLog.d(r8, r9)     // Catch:{ all -> 0x007d }
        L_0x0078:
            r7.initializeIfRootDirectoryDeleted()     // Catch:{ all -> 0x007d }
        L_0x007b:
            monitor-exit(r7)
            return
        L_0x007d:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.put(java.lang.String, com.android.volley.Cache$Entry):void");
    }

    public synchronized void remove(String str) {
        boolean delete = getFileForKey(str).delete();
        removeEntry(str);
        if (!delete) {
            VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
        }
    }

    private String getFilenameForKey(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(str.substring(0, length).hashCode());
        return valueOf + String.valueOf(str.substring(length).hashCode());
    }

    public File getFileForKey(String str) {
        return new File(this.mRootDirectorySupplier.get(), getFilenameForKey(str));
    }

    private void initializeIfRootDirectoryDeleted() {
        if (!this.mRootDirectorySupplier.get().exists()) {
            VolleyLog.d("Re-initializing cache after external clearing.", new Object[0]);
            this.mEntries.clear();
            this.mTotalSize = 0;
            initialize();
        }
    }

    private void pruneIfNeeded() {
        if (this.mTotalSize >= ((long) this.mMaxCacheSizeInBytes)) {
            if (VolleyLog.DEBUG) {
                VolleyLog.v("Pruning old cache entries.", new Object[0]);
            }
            long j = this.mTotalSize;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator<Map.Entry<String, CacheHeader>> it = this.mEntries.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                CacheHeader cacheHeader = (CacheHeader) it.next().getValue();
                if (getFileForKey(cacheHeader.key).delete()) {
                    this.mTotalSize -= cacheHeader.size;
                } else {
                    VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", cacheHeader.key, getFilenameForKey(cacheHeader.key));
                }
                it.remove();
                i++;
                if (((float) this.mTotalSize) < ((float) this.mMaxCacheSizeInBytes) * HYSTERESIS_FACTOR) {
                    break;
                }
            }
            if (VolleyLog.DEBUG) {
                VolleyLog.v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i), Long.valueOf(this.mTotalSize - j), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    private void putEntry(String str, CacheHeader cacheHeader) {
        if (!this.mEntries.containsKey(str)) {
            this.mTotalSize += cacheHeader.size;
        } else {
            this.mTotalSize += cacheHeader.size - this.mEntries.get(str).size;
        }
        this.mEntries.put(str, cacheHeader);
    }

    private void removeEntry(String str) {
        CacheHeader remove = this.mEntries.remove(str);
        if (remove != null) {
            this.mTotalSize -= remove.size;
        }
    }

    static byte[] streamToBytes(CountingInputStream countingInputStream, long j) throws IOException {
        long bytesRemaining = countingInputStream.bytesRemaining();
        if (j >= 0 && j <= bytesRemaining) {
            int i = (int) j;
            if (((long) i) == j) {
                byte[] bArr = new byte[i];
                new DataInputStream(countingInputStream).readFully(bArr);
                return bArr;
            }
        }
        throw new IOException("streamToBytes length=" + j + ", maxLength=" + bytesRemaining);
    }

    /* access modifiers changed from: package-private */
    public InputStream createInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /* access modifiers changed from: package-private */
    public OutputStream createOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    static class CacheHeader {
        final List<Header> allResponseHeaders;
        final String etag;
        final String key;
        final long lastModified;
        final long serverDate;
        long size;
        final long softTtl;
        final long ttl;

        private CacheHeader(String str, String str2, long j, long j2, long j3, long j4, List<Header> list) {
            this.key = str;
            this.etag = "".equals(str2) ? null : str2;
            this.serverDate = j;
            this.lastModified = j2;
            this.ttl = j3;
            this.softTtl = j4;
            this.allResponseHeaders = list;
        }

        CacheHeader(String str, Cache.Entry entry) {
            this(str, entry.etag, entry.serverDate, entry.lastModified, entry.ttl, entry.softTtl, getAllResponseHeaders(entry));
        }

        private static List<Header> getAllResponseHeaders(Cache.Entry entry) {
            if (entry.allResponseHeaders != null) {
                return entry.allResponseHeaders;
            }
            return HttpHeaderParser.toAllHeaderList(entry.responseHeaders);
        }

        static CacheHeader readHeader(CountingInputStream countingInputStream) throws IOException {
            if (DiskBasedCache.readInt(countingInputStream) == DiskBasedCache.CACHE_MAGIC) {
                return new CacheHeader(DiskBasedCache.readString(countingInputStream), DiskBasedCache.readString(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readHeaderList(countingInputStream));
            }
            throw new IOException();
        }

        /* access modifiers changed from: package-private */
        public Cache.Entry toCacheEntry(byte[] bArr) {
            Cache.Entry entry = new Cache.Entry();
            entry.data = bArr;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.lastModified = this.lastModified;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            entry.responseHeaders = HttpHeaderParser.toHeaderMap(this.allResponseHeaders);
            entry.allResponseHeaders = Collections.unmodifiableList(this.allResponseHeaders);
            return entry;
        }

        /* access modifiers changed from: package-private */
        public boolean writeHeader(OutputStream outputStream) {
            try {
                DiskBasedCache.writeInt(outputStream, DiskBasedCache.CACHE_MAGIC);
                DiskBasedCache.writeString(outputStream, this.key);
                String str = this.etag;
                if (str == null) {
                    str = "";
                }
                DiskBasedCache.writeString(outputStream, str);
                DiskBasedCache.writeLong(outputStream, this.serverDate);
                DiskBasedCache.writeLong(outputStream, this.lastModified);
                DiskBasedCache.writeLong(outputStream, this.ttl);
                DiskBasedCache.writeLong(outputStream, this.softTtl);
                DiskBasedCache.writeHeaderList(this.allResponseHeaders, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.d("%s", e.toString());
                return false;
            }
        }
    }

    static class CountingInputStream extends FilterInputStream {
        private long bytesRead;
        private final long length;

        CountingInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.length = j;
        }

        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bytesRead += (long) read;
            }
            return read;
        }

        /* access modifiers changed from: package-private */
        public long bytesRead() {
            return this.bytesRead;
        }

        /* access modifiers changed from: package-private */
        public long bytesRemaining() {
            return this.length - this.bytesRead;
        }
    }

    private static int read(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    static int readInt(InputStream inputStream) throws IOException {
        return (read(inputStream) << 24) | (read(inputStream) << 0) | 0 | (read(inputStream) << 8) | (read(inputStream) << 16);
    }

    static void writeLong(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) (j >>> 0)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static long readLong(InputStream inputStream) throws IOException {
        return ((((long) read(inputStream)) & 255) << 0) | 0 | ((((long) read(inputStream)) & 255) << 8) | ((((long) read(inputStream)) & 255) << 16) | ((((long) read(inputStream)) & 255) << 24) | ((((long) read(inputStream)) & 255) << 32) | ((((long) read(inputStream)) & 255) << 40) | ((((long) read(inputStream)) & 255) << 48) | ((255 & ((long) read(inputStream))) << 56);
    }

    static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        writeLong(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    static String readString(CountingInputStream countingInputStream) throws IOException {
        return new String(streamToBytes(countingInputStream, readLong(countingInputStream)), "UTF-8");
    }

    static void writeHeaderList(List<Header> list, OutputStream outputStream) throws IOException {
        if (list != null) {
            writeInt(outputStream, list.size());
            for (Header next : list) {
                writeString(outputStream, next.getName());
                writeString(outputStream, next.getValue());
            }
            return;
        }
        writeInt(outputStream, 0);
    }

    static List<Header> readHeaderList(CountingInputStream countingInputStream) throws IOException {
        int readInt = readInt(countingInputStream);
        if (readInt >= 0) {
            List<Header> emptyList = readInt == 0 ? Collections.emptyList() : new ArrayList<>();
            for (int i = 0; i < readInt; i++) {
                emptyList.add(new Header(readString(countingInputStream).intern(), readString(countingInputStream).intern()));
            }
            return emptyList;
        }
        throw new IOException("readHeaderList size=" + readInt);
    }
}
