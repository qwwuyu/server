package com.qwwuyu.server.bean;

public class Note {
	private Integer id = -1;

	private Integer userId = -1;

	private Integer type = -1;

	private Integer auth = -1;

	private String nick;

	private String title;

	private String content;

	private String herf;

	private Long time;

	public Integer getId() {
		return id;
	}

	public Note setId(Integer id) {
		this.id = id;
		return this;
	}

	public Integer getUserId() {
		return userId;
	}

	public Note setUserId(Integer userId) {
		this.userId = userId;
		return this;
	}

	public Integer getType() {
		return type;
	}

	public Note setType(Integer type) {
		this.type = type;
		return this;
	}

	public Integer getAuth() {
		return auth;
	}

	public Note setAuth(Integer auth) {
		this.auth = auth;
		return this;
	}

	public String getNick() {
		return nick;
	}

	public Note setNick(String nick) {
		this.nick = nick == null ? null : nick.trim();
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Note setTitle(String title) {
		this.title = title == null ? null : title.trim();
		return this;
	}

	public String getContent() {
		return content;
	}

	public Note setContent(String content) {
		this.content = content == null ? null : content.trim();
		return this;
	}

	public String getHerf() {
		return herf;
	}

	public Note setHerf(String herf) {
		this.herf = herf == null ? null : herf.trim();
		return this;
	}

	public Long getTime() {
		return time;
	}

	public Note setTime(Long time) {
		this.time = time;
		return this;
	}
}