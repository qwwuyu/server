package com.qwwuyu.server.robot;

import java.util.HashMap;
import java.util.Map;

public class Wait {
	private final CallBack callBack;
	private Map<String, TagRunnable> map = new HashMap<>();

	public Wait(CallBack callBack) {
		this.callBack = callBack;
	}

	public void addTag(String tag, long wait) {
		if (!map.containsKey(tag)) {
			TagRunnable tagRunnable = new TagRunnable(callBack, tag, wait);
			map.put(tag, tagRunnable);
			new Thread(tagRunnable).start();
		}
	}

	public void setTagTime(String tag, long wait) {
		TagRunnable tagRunnable = map.get(tag);
		if (tagRunnable != null) {
			tagRunnable.setTime(Math.max(1, wait));
		}
	}

	public void stop() {
		for (TagRunnable tagRunnable : map.values()) {
			if (tagRunnable != null) {
				tagRunnable.stop();
			}
		}
		map.clear();
	}

	private class TagRunnable implements Runnable {
		private final CallBack callBack;
		private final String tag;
		private final long wait;
		private final Object lock = new Object();
		private boolean live = true;
		private long newWait = 1;

		public TagRunnable(CallBack callBack, String tag, long wait) {
			this.callBack = callBack;
			this.tag = tag;
			this.wait = wait;
		}

		public void setTime(long wait) {
			if (1 == newWait) {
				newWait = wait;
				synchronized (lock) {
					lock.notify();
				}
			}
		}

		public void stop() {
			live = false;
			synchronized (lock) {
				lock.notify();
				if (1 == newWait) {
					lock.notify();
				}
			}
		}

		@Override
		public void run() {
			callBack.call(tag);
			while (live) {
				try {
					synchronized (lock) {
						lock.wait(wait);
						lock.wait(newWait);
						if (live) {
							callBack.call(tag);
						}
						newWait = 1;
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	public interface CallBack {
		void call(String tag);
	}
}
