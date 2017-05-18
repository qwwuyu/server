package com.qwwuyu.server.bean;

public class User {
	private Integer id = -1;

	private String name;

	private String pwd;

	private String nick;

	private String token;

	private Integer auth = -1;

	public User(Integer id, String name, String pwd, String nick, String token, Integer auth) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.nick = nick;
		this.token = token;
		this.auth = auth;
	}

	public User() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public User setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name == null ? null : name.trim();
		return this;
	}

	public String getPwd() {
		return pwd;
	}

	public User setPwd(String pwd) {
		this.pwd = pwd == null ? null : pwd.trim();
		return this;
	}

	public String getNick() {
		return nick;
	}

	public User setNick(String nick) {
		this.nick = nick == null ? null : nick.trim();
		return this;
	}

	public String getToken() {
		return token;
	}

	public User setToken(String token) {
		this.token = token == null ? null : token.trim();
		return this;
	}

	public Integer getAuth() {
		return auth;
	}

	public User setAuth(Integer auth) {
		this.auth = auth;
		return this;
	}
}