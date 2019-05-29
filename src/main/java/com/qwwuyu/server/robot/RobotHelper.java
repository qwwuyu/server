package com.qwwuyu.server.robot;

import org.apache.log4j.Logger;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.message.components.ComponentImageBase64;

import com.sun.net.httpserver.HttpServer;

public class RobotHelper {
	private static Logger logger = Logger.getLogger("qq");
	private HttpServer httpServer;

	private RobotHelper() {}

	private static class RobotHelperHolder {
		private static RobotHelper instance = new RobotHelper();
	}

	public static RobotHelper getInstance() {
		return RobotHelperHolder.instance;
	}

	private static void log(String message) {
		logger.info(message);
	}

	public synchronized void openRobot() throws Exception {
		if (httpServer != null) {
			throw new Exception("httpServer已经存在");
		}
		try {
			PicqBotX bot = new PicqBotX(new PicqConfig(12311).setDebug(false));
			bot.addAccount("kere", "127.0.0.1", 12310);
			bot.getEventManager().registerListeners(new TestListener());
			httpServer = bot.startBot();
		} catch (Exception e) {
			logger.info("httpServer启动失败", e);
			throw new Exception("httpServer启动失败");
		}
	}

	public synchronized void closeRobot() throws Exception {
		if (httpServer == null) {
			throw new Exception("httpServer不存在");
		}
		try {
			httpServer.stop(0);
			httpServer = null;
		} catch (Exception e) {
			throw new Exception("httpServer关闭失败");
		}
	}

	private static class TestListener extends IcqListener {
		@EventHandler
		public void onPMEvent(EventPrivateMessage event) {
			log("接到消息:" + event.getMessage());

			if (event.getMessage().equals("测试回复")) {
				event.respond("回复消息");
			}

			if (event.getMessage().equals("🐎")) {
				event.respond("🐎");
			}

			if (event.getMessage().equals("测试私聊")) {
				// event.getHttpApi().sendPrivateMsg(0, "私聊消息");
			}

			if (event.getMessage().equals("测试图片")) {
				event.respond(new MessageBuilder()
						.add("图片前面的消息")
						.newLine()
						.add(new ComponentImageBase64(
								"iVBORw0KGgoAAAANSUhEUgAAABQAAAAVCAIAAADJt1n/AAAAKElEQVQ4EWPk5+RmIBcwkasRpG9UM4mhNxpgowFGMARGEwnBIEJVAAAdBgBNAZf+QAAAAABJRU5ErkJggg=="))
						.newLine().add("图片后面的").newLine().add("换行之后的消息").toString());
			}
		}
	}
}
