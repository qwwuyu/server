package com.qwwuyu.server.helper;

import com.qwwuyu.server.utils.CommUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class FileDHelper {
    public static final int DOWNLOADING = 0;
    public static final int DOWNLOAD_OK = 1;
    public static final int DOWNLOAD_ERROR = 2;
    private static DownloadThread thread;
    private static DownloadProgress progress;

    public static boolean download(URLConnection con, File file, Object lock) throws IOException {
        int total = con.getContentLength();
        synchronized (FileDHelper.class) {
            if (thread == null) {
                progress = new DownloadProgress(total);
                thread = new DownloadThread(con, file, lock, progress);
                thread.start();
            } else {
                return false;
            }
        }
        return true;
    }

    public static DownloadProgress getProgress() {
        return progress;
    }

    public static void cancelDownload() {
        synchronized (FileDHelper.class) {
            if (thread != null) thread.shutdown();
        }
    }

    public static String getNameForDisposition(URLConnection con, URL url) {
        String fieldValue = con.getHeaderField("Content-Disposition");
        String filename = null;
        if (fieldValue != null) {
            filename = fieldValue.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
        }
        if (CommUtil.isEmpty(filename)) {
            filename = FilenameUtils.getName(url.getPath());
        }
        try {
            filename = URLDecoder.decode(filename, "UTF-8");
        } catch (Exception ignored) {
        }
        return filename;
    }

    private static void end(DownloadProgress progress, Object lock, Exception e) {
        progress.state = e == null ? 1 : 2;
        progress.e = e;
        synchronized (FileDHelper.class) {
            thread = null;
        }
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private static class DownloadThread extends Thread {
        private URLConnection con;
        private File file;
        private DownloadProgress progress;
        private Object lock;
        private InputStream is;

        public DownloadThread(URLConnection con, File file, Object lock, DownloadProgress progress) {
            this.con = con;
            this.file = file;
            this.progress = progress;
            this.lock = lock;
        }

        @Override
        public void run() {
            try (FileOutputStream fos = new FileOutputStream(file); InputStream is = con.getInputStream()) {
                this.is = is;
                byte[] bytes = new byte[100 * 1024];
                int read;
                while ((read = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, read);
                    progress.progress += read;
                }
                this.is = null;
                end(progress, lock, null);
            } catch (Exception e) {
                this.is = null;
                end(progress, lock, e);
            }
        }

        public void shutdown() {
            if (is != null) {
                CommUtil.closeStream(is);
            }
        }
    }

    public static class DownloadProgress {
        public int progress;
        public int total;
        public int state = DOWNLOADING;
        public Exception e;

        public DownloadProgress(int total) {
            this.total = total;
        }

        public String progressText() {
            return CommUtil.getFileSize(progress) + "/" + (total > 0 ? CommUtil.getFileSize(total) : "*");
        }

        public String errorText() {
            return e == null ? "" : e.getMessage();
        }
    }
}
