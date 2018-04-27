package com.scienjus.smartqq;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qwwuyu.server.utils.CommUtil;
import com.scienjus.smartqq.callback.MessageCallback;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.DiscussMessage;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.Message;

public class Robot {
	private static Map<String, SmartQQClient> map = new HashMap<>();

	public static void addSmartQQ(String tag, String pwd) {
		SmartQQClient client = map.get(tag);
		if (client == null) {
			client = new SmartQQClient(tag);
			map.put(tag, client);
			if (!client.login(300000, new MyMessageCallback(client, tag, pwd))) {
				closeSmartQQ(tag);
			}
		}
	}

	public static void closeSmartQQ(String tag) {
		SmartQQClient client = map.remove(tag);
		if (client != null) {
			client.close();
		}
	}

	private static class MyMessageCallback implements MessageCallback, Closeable {
		private ExecutorService executor = Executors.newCachedThreadPool();
		private final SmartQQClient client;
		private final String tag;
		private final String pwd;
		private Long admin = 0L;
		private String nike;
		private Map<Long, Long> robotMap = new HashMap<>();
		private Map<Long, String> nikeMap = new HashMap<>();
		private Map<Long, Wait> waitMap = new HashMap<>();
		private Map<Long, ArrayList<String[]>> msgsMap = new HashMap<>();
		private Map<Long, Integer> flagMap = new HashMap<>();

		public MyMessageCallback(SmartQQClient client, String tag, String pwd) {
			this.client = client;
			this.tag = tag;
			this.pwd = pwd;
			checkLive();
		}

		@Override
		public void onMessage(Message message) {
			if(admin == 0 || admin == message.getUserId()) {
				onMessage(message.getContent(), message.getUserId(), message.getTo_uin(), (msg, chatId) -> client.sendMessageToFriend(chatId, msg));
			} else {
				onMessage(message.getContent(), message.getTo_uin(), message.getUserId(), (msg, chatId) -> client.sendMessageToFriend(chatId, msg));
			}
		}

		@Override
		public void onGroupMessage(GroupMessage message) {
			onMessage(message.getContent(), message.getUserId(), message.getGroupId(),
					(msg, chatId) -> client.sendMessageToGroup(chatId, msg));
		}

		@Override
		public void onDiscussMessage(DiscussMessage message) {
			onMessage(message.getContent(), message.getUserId(), message.getDiscussId(),
					(msg, chatId) -> client.sendMessageToDiscuss(chatId, msg));
		}

		private String lastMsg = null;

		private void onMessage(String content, long userId, long chatId, ISendMessage send) {
			lastTime = System.currentTimeMillis();
			if (content.startsWith(pwd)) {
				admin = userId;
				nike = "@" + content.substring(pwd.length());
				send(send, "收到id" + nike, chatId);
			}
			if (admin == 0L || content.equals(lastMsg)) {
				return;
			}
			lastMsg = content;
			Long robot = robotMap.get(chatId);
			if (robot == null && content.startsWith(nike)) {
				robot = userId;
				robotMap.put(chatId, robot);
				nikeMap.put(chatId, nike);
				flagMap.put(chatId, 0);
				send(send, "get", chatId);
			} else if (admin == userId) {
				if ("重新获取".equals(content)) {
					robotMap.remove(chatId);
					send(send, "重新获取id", chatId);
				} else if (content.startsWith(tag + "开始-")) {
					// 开始-钓鱼-大腿.+[积分|鱼饵][\D]+(\d+)分钟-探险-大腿.+[宠物|探险][\D]+(\d+)分钟
					if (!waitMap.containsKey(chatId)) {
						final ArrayList<String[]> msgs = new ArrayList<>();
						String[] command = content.substring((tag + "开始-").length()).split("-");
						if (command.length % 3 != 0) {
							send(send, "指令格式错误", chatId);
							return;
						}
						for (int i = 0; i < command.length; i += 3) {
							msgs.add(new String[] { command[i], command[i + 1], command[i + 2] });
						}
						msgsMap.put(chatId, msgs);
						final Wait wait = new Wait(new MyWaitCallBack(send, chatId));
						waitMap.put(chatId, wait);
						executor.execute(() -> {
							try {
								send.sendMessage("start", chatId);
								for (int i = 0; i < msgs.size(); i++) {
									CommUtil.sleep(1000);
									int t = Math.max(1, Integer.parseInt(msgs.get(i)[2]));
									wait.addTag(msgs.get(i)[0], t * 1000);
								}
							} catch (Exception e) {}
						});
					} else {
						send(send, "挂机过了", chatId);
					}
				} else if ((tag + "暂停").equals(content)) {
					Wait wait = waitMap.remove(chatId);
					ArrayList<String[]> remove = msgsMap.remove(chatId);
					if (remove != null) {
						remove.clear();
					}
					if (wait != null) {
						wait.stop();
						send(send, "stop", chatId);
					} else {
						send(send, "你没有开始", chatId);
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
				} else if (content.startsWith(tag + "-")) {
					final String txt = content.substring((tag + "-").length());
					send(send, txt, chatId);
				}
			}
			if (robot == userId) {
				final Integer flag = flagMap.get(chatId);
				String nike = nikeMap.get(chatId);
				Wait wait = waitMap.get(chatId);
				ArrayList<String[]> msgs = msgsMap.get(chatId);
				if (wait != null && msgs != null) {
					for (int i = 0; i < msgs.size(); i++) {
						int time = getTime(content, msgs.get(i)[1]);
						if (1800 != time) {
							wait.setTagTime(msgs.get(i)[0], time * 1000);
						}
					}
				}
			}
		}

		private void send(ISendMessage send, String msg, long chatId) {
			executor.execute(() -> send.sendMessage(msg, chatId));
		}

		@Override
		public void close() throws IOException {
			executor.shutdown();
			isLive = false;
			synchronized (lock) {
				lock.notify();
			}
		}

		private boolean isLive = true;
		private long lastTime;
		private Object lock = new Object();

		private void checkLive() {
			lastTime = System.currentTimeMillis();
			new Thread(() -> {
				while (isLive) {
					try {
						long nowTime = System.currentTimeMillis();
						if (nowTime - lastTime > 120L * 60 * 1000) {
							stop();
							return;
						}
						synchronized (lock) {
							lock.wait(10L * 60 * 1000);
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

	interface ISendMessage {
		void sendMessage(String msg, long chatId);
	}

	private static int getTime(String txt, String regex) {
		try {
			Pattern compile = Pattern.compile(regex);
			Matcher matcher = compile.matcher(txt);
			if (matcher.find()) {
				int time = Integer.parseInt(matcher.group(1));
				return Math.min(time, 1800);
			}
		} catch (Exception e) {}
		return 1800;
	}
}
