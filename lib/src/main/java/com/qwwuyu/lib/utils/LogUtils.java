package com.qwwuyu.lib.utils;


import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogUtils {
    public static final int V = 2, D = 3, I = 4, W = 5, E = 6, A = 7;
    private static final char[] T    = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};
    private static final int    JSON = 0x10, XML = 0x20;

    @Retention(RetentionPolicy.SOURCE)
    private @interface TYPE {
    }

    private static final Format FORMAT        = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ");
    private static final String LINE_SEP      = System.getProperty("line.separator");
    private static final String TOP_BORDER    = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String LEFT_BORDER   = "║ ";
    private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final int    MAX_LEN       = 4000;
    private static final String NULL          = "null";

    public static final boolean LOG  = true;
    public static String dir      = null;
    private static String logTag    = null;
    private static boolean logHead   = true;
    private static boolean logBorder = false;
    private static int     logFilter = V;
    private static ExecutorService executor;
    private static OnErrorListener onErrorListener;
    private static String head_sep = " : ";

    /** ======================== 使用Log工具 ======================== */
    public static class Builder {
        public Builder() {
        }

        public Builder setLogDir(String dir) {
            LogUtils.dir = dir;
            return this;
        }

        public Builder setLogTag(String tag) {
            LogUtils.logTag = tag;
            return this;
        }

        public Builder enableLogHead(boolean enable) {
            LogUtils.logHead = enable;
            return this;
        }

        public Builder enableLogBorder(boolean enable) {
            LogUtils.logBorder = enable;
            return this;
        }

        public Builder setLogFilter(@TYPE final int level) {
            LogUtils.logFilter = level;
            return this;
        }

        public Builder setHeadSep(String head_sep) {
            LogUtils.head_sep = head_sep;
            return this;
        }

        public Builder onErrorListener(OnErrorListener onErrorListener) {
            LogUtils.onErrorListener = onErrorListener;
            return this;
        }
    }

    public static void i(Object contents) {
        log4(I, logTag, contents);
    }

    public static void i(String tag, String format, Object... args) {
        log4(I, tag, format, args);
    }

    public static void json(@TYPE int type, String tag, String contents) {
        log4(JSON | type, tag, contents);
    }

    public static void xml(@TYPE int type, String tag, String contents) {
        log4(XML | type, tag, contents);
    }

    public static void onError(final Throwable e) {
        onError(e, -1);
    }

    public static void onError(final Throwable e, int flag) {
        if (LOG && logFilter < E) {
            if (onErrorListener != null) {
                onErrorListener.onError(e, flag);
            }
        }
    }

    public static void printStackTrace(Throwable e) {
        if (LOG && logFilter < E) {
            e.printStackTrace();
        }
    }

    public static void logThread(String tag) {
        if (LOG) {
            log(I, logHead, tag, 3, "ThreadName=%b", Thread.currentThread().getName());
        }
    }

    /** 处理日志 */
    private static void log4(int type, String tag, Object format, Object... args) {
        log(type, logHead, tag, 4, format, args);
    }

    /** 处理日志 */
    public static void log(@TYPE int type, boolean logHead, String tag, int stackTrace, Object format, Object... args) {
        if (!LOG) return;
        int type_low = type & 0x0f, type_high = type & 0xf0;
        if (type_low < logFilter) return;
        final String[] tagAndHead = processTagAndHead(logHead, tag, stackTrace);
        String body = processBody(type_high, format, args);
        print2Console(type_low, tagAndHead[0], tagAndHead[1] + body);
        if (dir != null) print2File(type_low, tagAndHead[0], tagAndHead[2] + body);
    }

    /** 处理TAG和位子 */
    private static String[] processTagAndHead(boolean logHead, String tag, int stackTrace) {
        if (tag == null) tag = logTag;
        if (logHead || tag == null) {
            StackTraceElement targetElement = new Throwable().getStackTrace()[stackTrace];
            String className = targetElement.getClassName();
            String[] classNameInfo = className.split("\\.");
            if (classNameInfo.length > 0) {
                className = classNameInfo[classNameInfo.length - 1];
            }
            if (className.contains("$")) {
                className = className.split("\\$")[0];
            }
            if (tag == null) tag = className;
            String fileName = targetElement.getFileName();
            if (fileName == null) fileName = className + ".java";
            if (logHead) {
//                String head = String.format(Locale.getDefault(), " %s(%s:%d)", targetElement.getMethodName(), fileName, targetElement.getLineNumber());
                String head = targetElement.toString();
                return new String[]{tag, head + head_sep, " [" + head + "]: "};
            }
        }
        return new String[]{tag, "", ": "};
    }

    /** 处理内容 */
    private static String processBody(final int type, Object format, final Object... args) {
        if (format == null) {
            return NULL;
        }
        String body = format.toString();
        if (type == JSON) {
            return formatJson(body);
        } else if (type == XML) {
            return formatXml(body);
        } else if (args.length == 0) {
            return body;
        } else {
            return String.format(body, args);
        }
    }

    /** 打印日志到控制台 */
    private static void print2Console(final int type, final String tag, String msg) {
        if (logBorder) print(type, tag, TOP_BORDER);
        if (logBorder) msg = addLeftBorder(msg);
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            print(type, tag, msg.substring(0, MAX_LEN));
            String sub;
            int index = MAX_LEN;
            for (int i = 1; i < countOfSub; i++) {
                sub = msg.substring(index, index + MAX_LEN);
                print(type, tag, logBorder ? LEFT_BORDER + sub : sub);
                index += MAX_LEN;
            }
            sub = msg.substring(index, len);
            print(type, tag, logBorder ? LEFT_BORDER + sub : sub);
        } else {
            print(type, tag, msg);
        }
        if (logBorder) print(type, tag, BOTTOM_BORDER);
    }

    private static void print(final int type, final String tag, final String msg) {
        if (tag != null) {
            System.out.println(String.format("%s：%s", tag, msg));
        } else {
            System.out.println(msg);
        }
    }

    /** 打印日志到文件 */
    private static void print2File(final int type, final String tag, final String msg) {
        String format = FORMAT.format(new Date(System.currentTimeMillis()));
        String date = format.substring(0, 5);
        String time = format.substring(6);
        final String fullPath = dir + date + ".txt";
        if (!createOrExistsFile(fullPath)) {
            return;
        }
        final String content = time + T[type - V] + "/" + tag + msg + LINE_SEP;
        if (executor == null) {
            synchronized (LogUtils.class) {
                if (executor == null) {
                    executor = Executors.newSingleThreadExecutor();
                }
            }
        }
        executor.execute(() -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath, true))) {
                bw.write(content);
            } catch (IOException ignored) {
            }
        });
    }

    private static boolean createOrExistsFile(final String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) return file.isFile();
            return createOrExistsDir(file.getParentFile()) && file.createNewFile();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static String formatJson(String json) {
        return json;
    }

    private static String formatXml(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEP);
        } catch (Exception ignored) {
        }
        return xml;
    }

    private static String addLeftBorder(final String msg) {
        StringBuilder sb = new StringBuilder();
        String[] lines = msg.split(LINE_SEP);
        for (String line : lines) {
            sb.append(LEFT_BORDER).append(line).append(LINE_SEP);
        }
        return sb.toString();
    }

    private static String getStackTraceString(Throwable tr) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    public interface OnErrorListener {
        void onError(Throwable throwable, int flag);
    }

    public static class crashErrorListener implements OnErrorListener {
        @Override
        public void onError(final Throwable throwable, int flag) {
            throw new RuntimeException(throwable);
        }
    }
}