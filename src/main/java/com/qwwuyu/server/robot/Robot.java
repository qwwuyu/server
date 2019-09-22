package com.qwwuyu.server.robot;

import com.qwwuyu.server.utils.CommUtil;
import com.qwwuyu.server.utils.LimitQueue;
import com.qwwuyu.server.utils.TLRobot;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Robot {
    private static Logger logger = Logger.getLogger("qq");

    public static void addSmartQQ(String tag, String pwd) {
    }

    public static void closeSmartQQ(String tag) {
    }

    private static class MyMessageCallback implements Closeable {
        private final ExecutorService executor = Executors.newCachedThreadPool();
        private final String tag;
        private final String pwd;
        private Long admin = 0L;
        private boolean hint = true;
        private boolean log = false;
        private String tlKey;

        private Map<Long, Wait> waitMap = new HashMap<>();
        private Map<Long, ArrayList<String[]>> msgsMap = new HashMap<>();

        private LimitQueue<MsgItem> items = new LimitQueue<>(10);
        private List<Long> users = new ArrayList<>();
        private String[] cmds = new String[10];
        private List<String> receive = new ArrayList<>();
        private List<String> reply = new ArrayList<>();

        private Map<Long, Integer> numMap = new HashMap<>();
        private Map<Long, Long> timeMap = new HashMap<>();
        private int time = 0;

        public MyMessageCallback(String tag, String pwd) {
            this.tag = tag;
            this.pwd = pwd;
            checkLive();
        }

        private synchronized void onMessage(final String content, long userId, long chatId, ISendMessage send) {
            lastLiveTime = System.currentTimeMillis();
            if (content == null) return;
            MsgItem lastItem = items.getLast();
            items.offer(new MsgItem(content, userId, chatId));
            log(String.format(Locale.CHINA, "chatId=%s userId=%s content=%s", chatId, userId, content));
            if (content.startsWith(pwd + "-")) {
                String[] setting = content.split("-");
                if (setting.length == 4) {
                    admin = userId;
                    hint = "1".equals(setting[1]);
                    log = "1".equals(setting[2]);
                    tlKey = setting[3];
                    send(send, "hint:" + hint + " log:" + log, chatId);
                }
                return;
            } else if (admin == 0L || content.equals("") || (lastItem != null && userId == lastItem.userId && chatId == lastItem.chatId && content.equals(lastItem.content))) {
                return;
            }
            if (admin == userId) {
                if (content.startsWith(tag + "开始-")) {
                    // 开始-钓鱼-x-10
                    if (!waitMap.containsKey(chatId)) {
                        final ArrayList<String[]> msgs = new ArrayList<>();
                        String[] command = content.substring((tag + "开始-").length()).split("-");
                        if (command.length % 3 != 0) {
                            sendHint(send, "指令格式错误", chatId);
                            return;
                        }
                        for (int i = 0; i < command.length; i += 3) {
                            msgs.add(new String[]{command[i], command[i + 1], command[i + 2]});
                        }
                        msgsMap.put(chatId, msgs);
                        final Wait wait = new Wait(new MyWaitCallBack(send, chatId));
                        waitMap.put(chatId, wait);
                        executor.execute(() -> {
                            try {
                                if (!hint) send.sendMessage("start", chatId);
                                for (int i = 0; i < msgs.size(); i++) {
                                    CommUtil.sleep(1000);
                                    int t = Math.max(1, Integer.parseInt(msgs.get(i)[2]));
                                    wait.addTag(msgs.get(i)[0], t * 1000);
                                }
                            } catch (Exception e) {
                            }
                        });
                    } else {
                        sendHint(send, "挂机过了", chatId);
                    }
                } else if ((tag + "暂停").equals(content)) {
                    Wait wait = waitMap.remove(chatId);
                    ArrayList<String[]> remove = msgsMap.remove(chatId);
                    if (remove != null) {
                        remove.clear();
                    }
                    if (wait != null) {
                        wait.stop();
                        sendHint(send, "stop", chatId);
                    } else {
                        sendHint(send, "你没有开始", chatId);
                    }
                } else if ((tag + "停止").equals(content)) {
                    for (Wait wait : waitMap.values()) {
                        wait.stop();
                    }
                    waitMap.clear();
                    executor.execute(() -> {
                        send.sendMessage("end", chatId);
                        closeSmartQQ(tag);
                    });
                } else if (content.startsWith(tag + ":")) {
                    final String txt = content.substring((tag + ":").length());
                    send(send, txt, chatId);
                } else if (content.startsWith(tag + "-")) {
                    final String question = content.substring((tag + "-").length());
                    robot(send, question, userId, chatId);
                } else if (content.startsWith("bind")) {
                    final String txt = content.substring("bind".length());
                    for (int i = 0; i < items.size() - 1; i++) {
                        MsgItem msgItem = items.get(i);
                        if (msgItem != null && msgItem.content.contains(txt)) {
                            if (!users.contains(msgItem.userId)) {
                                users.add(msgItem.userId);
                            }
                            send(send, "bind:" + msgItem.content, chatId);
                            break;
                        }
                    }
                } else if (content.startsWith("unbind")) {
                    final String txt = content.substring("unbind".length());
                    for (int i = 0; i < items.size() - 1; i++) {
                        MsgItem msgItem = items.get(i);
                        if (msgItem != null && msgItem.content.contains(txt)) {
                            if (users.contains(msgItem.userId)) {
                                users.remove(msgItem.userId);
                            }
                            send(send, "unbind:" + msgItem.content, chatId);
                            break;
                        }
                    }
                } else if (content.startsWith(tag + "re-")) {
                    cmds[0] = content.substring((tag + "re-").length());
                } else if (content.startsWith(tag + "rc-")) {
                    cmds[1] = content.substring((tag + "rc-").length());
                } else if (content.startsWith(tag + "rs-")) {
                    cmds[2] = content.substring((tag + "rs-").length());
                } else if (content.startsWith(tag + "reply-")) {
                    final String txt = content.substring((tag + "reply-").length());
                    String[] replys = txt.split("-");
                    receive.clear();
                    reply.clear();
                    if (replys.length % 2 == 0) {
                        for (int i = 0; i < replys.length; i += 2) {
                            receive.add(replys[i]);
                            reply.add(replys[i + 1]);
                        }
                    }
                } else if ("clear".equals(content)) {
                    users.clear();
                    cmds[0] = null;
                    cmds[1] = null;
                    sendHint(send, "clear over", chatId);
                } else if (content.startsWith(tag + "num-")) {
                    final String txt = content.substring((tag + "num-").length());
                    int num = Integer.parseInt(txt);
                    numMap.put(chatId, num);
                } else if (content.startsWith(tag + "time-")) {
                    final String txt = content.substring((tag + "time-").length());
                    int time = Integer.parseInt(txt);
                    this.time = time;
                }
                // other
            } else if (CommUtil.isExist(cmds[0]) && content.equals(cmds[0])) {
                users.add(userId);
            } else if (CommUtil.isExist(cmds[1]) && content.contains(cmds[1])) {
                users.add(userId);
            } else if (CommUtil.isExist(cmds[2]) && content.startsWith(cmds[2] + "-")) {
                final String question = content.substring((cmds[2] + "-").length());
                robot(send, question, userId, chatId);
            } else if (!receive.isEmpty()) {
                int index = receive.indexOf(content);
                if (index != -1) {
                    send(send, reply.get(index), chatId);
                }
            } else if (users.contains(userId)) {
                if ("gun".equals(content)) {
                    users.remove(userId);
                } else {
                    robot(send, content, userId, chatId);
                }
            } else if (numMap.containsKey(chatId) && content.matches("^\\d+$")) {
                Integer num = numMap.get(chatId);
                Long time = timeMap.get(userId);
                if (time != null && System.currentTimeMillis() - time < this.time) return;
                int x = Integer.parseInt(content);
                if (x < num) {
                    send(send, num + "高了", chatId);
                } else if (x > num) {
                    send(send, num + "低了", chatId);
                } else {
                    send(send, num + "对了", chatId);
                    numMap.remove(chatId);
                }
            }
        }

        private void log(String message) {
            if (log) logger.info(message);
        }

        private void robot(ISendMessage send, String question, long userId, long chatId) {
            if (CommUtil.isExist(tlKey) && tlKey.length() > 3) {
                executor.execute(() -> {
                    String result = TLRobot.requestTL(tlKey, String.valueOf(userId), question);
                    send.sendMessage(result, chatId);
                });
            }
        }

        private void send(ISendMessage send, String msg, long chatId) {
            executor.execute(() -> send.sendMessage(msg, chatId));
        }

        private void sendHint(ISendMessage send, String msg, long chatId) {
            if (!hint) {
                send(send, msg, chatId);
            }
        }

        @Override
        public void close() throws IOException {
            executor.shutdown();
            isLive = false;
            synchronized (liveLock) {
                liveLock.notify();
            }
        }

        /**
         *
         */
        private boolean isLive = true;
        private long lastLiveTime;
        private Object liveLock = new Object();

        private void checkLive() {
            lastLiveTime = System.currentTimeMillis();
            new Thread(() -> {
                while (isLive) {
                    try {
                        long nowTime = System.currentTimeMillis();
                        if (nowTime - lastLiveTime > 120L * 60 * 1000) {
                            stop();
                            return;
                        }
                        synchronized (liveLock) {
                            liveLock.wait(10L * 60 * 1000);
                        }
                    } catch (Exception e) {
                        stop();
                    }
                }
            }).start();
        }

        private void stop() {
            for (Wait wait : waitMap.values()) {
                wait.stop();
            }
            waitMap.clear();
            logger.info("stop in checkLive");
            executor.execute(() -> closeSmartQQ(tag));
        }
    }

    private static class MyWaitCallBack implements Wait.CallBack {
        private final ISendMessage send;
        private final long chatId;

        public MyWaitCallBack(ISendMessage send, long chatId) {
            this.send = send;
            this.chatId = chatId;
        }

        @Override
        public void call(String tag) {
            send.sendMessage(tag, chatId);
        }
    }

    private static class MsgItem {
        public MsgItem(String content, long userId, long chatId) {
            this.content = content;
            this.userId = userId;
            this.chatId = chatId;
        }

        public String content;
        public long userId;
        public long chatId;

    }

    interface ISendMessage {
        void sendMessage(String msg, long chatId);
    }
}
