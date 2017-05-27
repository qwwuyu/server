package com.qwwuyu.server.bean;

public class Card {
	private Integer id = -1;

	private Integer userId = -1;

	private String nick;

	private String title;

	private Long time;

	public Integer getId() {
		return id;
	}

	public Card setId(Integer id) {
		this.id = id;
		return this;
	}

	public Integer getUserId() {
		return userId;
	}

	public Card setUserId(Integer userId) {
		this.userId = userId;
		return this;
	}

	public String getNick() {
		return nick;
	}

	public Card setNick(String nick) {
		this.nick = nick == null ? null : nick.trim();
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Card setTitle(String title) {
		this.title = title == null ? null : title.trim();
		return this;
	}

	public Long getTime() {
		return time;
	}

	public Card setTime(Long time) {
		this.time = time;
		return this;
	}
}