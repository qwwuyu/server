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
			lastTime = System.currentTimeMillis();
			String content = message.getContent();
			if (content.startsWith(pwd)) {
				admin = message.getUserId();
				nike = "@" + content.substring(pwd.length());
				executor.execute(() -> client.sendMessageToFriend(admin, "收到id" + nike));
			}
		}

		private String lastMsg = null;

		@Override
		public void onGroupMessage(GroupMessage message) {
			if (admin == 0L || message.getContent().equals(lastMsg)) {
				return;
			}
			lastTime = System.currentTimeMillis();
			lastMsg = message.getContent();
			String content = message.getContent();
			long groupId = message.getGroupId();
			Long robot = robotMap.get(groupId);
			if (robot == null) robot = 0L;
			if (0L == robot && content.startsWith(nike)) {
				robot = message.getUserId();
				robotMap.put(groupId, robot);
				nikeMap.put(groupId, nike);
				flagMap.put(groupId, 0);
				executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "收到机器id"));
			} else if (admin == message.getUserId()) {
				if ("重新获取".equals(content)) {
					robotMap.put(groupId, null);
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "重新获取id"));
				} else if (content.startsWith(tag + "开始-")) {
					// 开始-钓鱼-大腿.+[积分|鱼饵][\D]+(\d+)分钟-探险-大腿.+[宠物|探险][\D]+(\d+)分钟
					// 开始-进入副本 神陨星域-asd.+[副本|荒古][\D]+(\d+)分钟-双修-asd.+[双修|活动][\D]+(\d+)分钟
					if (!waitMap.containsKey(groupId)) {
						final ArrayList<String[]> msgs = new ArrayList<>();
						String[] command = content.substring((tag + "开始-").length()).split("-");
						if (command.length % 3 != 0) {
							executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "指令格式错误"));
							return;
						}
						for (int i = 0; i < command.length; i += 3) {
							msgs.add(new String[] { command[i], command[i + 1], command[i + 2] });
						}
						msgsMap.put(groupId, msgs);
						final Wait wait = new Wait(new MyWaitCallBack(client, groupId));
						waitMap.put(groupId, wait);
						executor.execute(() -> {
							try {
								client.sendMessageToGroup(message.getGroupId(), "挂机开始");
								for (int i = 0; i < msgs.size(); i++) {
									CommUtil.sleep(1000);
									int t = Math.max(1, Integer.parseInt(msgs.get(i)[2]));
									wait.addTag(msgs.get(i)[0], t * 60 * 1000);
								}
							} catch (Exception e) {}
						});
					} else {
						executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "挂机过了"));
					}
				} else if ((tag + "暂停").equals(content)) {
					Wait wait = waitMap.remove(groupId);
					ArrayList<String[]> remove = msgsMap.remove(groupId);
					if (remove != null) {
						remove.clear();
					}
					if (wait != null) {
						wait.stop();
						executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "挂机暂停"));
					} else {
						executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "你没有开始"));
					}
				} else if ((tag + "停止").equals(content)) {
					for (Wait wait : waitMap.values()) {
						wait.stop();
					}
					waitMap.clear();
					executor.execute(() -> {
						client.sendMessageToGroup(message.getGroupId(), "程序结束");
						closeSmartQQ(tag);
					});
				} else if (content.startsWith(tag + "-")) {
					final String txt = content.substring((tag + "-").length());
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), txt));
				}
			}
			if (robot == message.getUserId()) {
				final Integer flag = flagMap.get(groupId);
				String nike = nikeMap.get(groupId);
				if (content.contains(nike + " 您的宠物已经精疲力竭了") && flag < 2) {
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), flag == 0 ? "使用 小精力瓶" : "使用 大精力瓶", "进入副本 神陨星域"));
				} else if (content.contains(nike + " 精力不足！双修需要") && flag < 2) {
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), flag == 0 ? "使用 小精力瓶" : "使用 大精力瓶", "双修"));
				} else if (content.contains(nike + " 您背包里并没有小精力瓶呢")) {
					flagMap.put(groupId, 1);
				} else if (content.contains(nike + " 您背包里并没有大精力瓶呢")) {
					flagMap.put(groupId, 2);
				} else if (content.contains(nike + " 您的宠物宝宝心情实在是太差了")) {
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "玩耍"));
				} else if (content.contains(nike + " 您还没有鱼竿呢")) {
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "购买鱼竿 稀有鱼竿", "钓鱼"));
				} else if (content.contains(nike + " 您的稀有鱼饵已经用完啦")) {
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "购买鱼饵 稀有鱼饵 100", "钓鱼"));
				} else if (content.contains(nike + " 运气爆棚啦")) {
					executor.execute(() -> client.sendMessageToGroup(message.getGroupId(), "假装看不见", "钓鱼"));
				} else if (content.contains("分钟")) {
					Wait wait = waitMap.get(groupId);
					ArrayList<String[]> msgs = msgsMap.get(groupId);
					if (wait != null && msgs != null) {
						for (int i = 0; i < msgs.size(); i++) {
							int time = getTime(content, msgs.get(i)[1]);
							if (31 != time) {
								wait.setTagTime(msgs.get(i)[0], time * 60 * 1000);
							}
						}
					}
				}
			}
		}

		@Override
		public void onDiscussMessage(DiscussMessage message) {}

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
						if (nowTime - lastTime > 40L * 60 * 1000) {
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
		private final SmartQQClient client;
		private final long groupId;

		public MyWaitCallBack(SmartQQClient client, long groupId) {
			this.client = client;
			this.groupId = groupId;
		}

		@Override
		public void call(String tag) {
			client.sendMessageToGroup(groupId, tag);
		}
	}

	private static int getTime(String txt, String regex) {
		try {
			Pattern compile = Pattern.compile(regex);
			Matcher matcher = compile.matcher(txt);
			if (matcher.find()) {
				int time = Integer.parseInt(matcher.group(1));
				return Math.min(time, 31);
			}
		} catch (Exception e) {}
		return 31;
	}
}
