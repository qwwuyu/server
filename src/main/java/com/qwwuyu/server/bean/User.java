package com.qwwuyu.server.bean;

public class User {
	private Integer id = -1;

	private String name;

	private String pwd;

	private String nick;

	private Integer auth = -1;

	private String ip;

	private String token;

	private String apptoken;

	private Integer time = -1;

	private Integer apptime = -1;

	public User() {
		super();
	}

	public User(Integer id, String name, String pwd, String nick, Integer auth, String ip, String token,
			String apptoken, Integer time, Integer apptime) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.nick = nick;
		this.auth = auth;
		this.ip = ip;
		this.token = token;
		this.apptoken = apptoken;
		this.time = time;
		this.apptime = apptime;
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

	public Integer getAuth() {
		return auth;
	}

	public User setAuth(Integer auth) {
		this.auth = auth;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public User setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
		return this;
	}

	public String getToken() {
		return token;
	}

	public User setToken(String token) {
		this.token = token == null ? null : token.trim();
		return this;
	}

	public String getApptoken() {
		return apptoken;
	}

	public User setApptoken(String apptoken) {
		this.apptoken = apptoken == null ? null : apptoken.trim();
		return this;
	}

	public Integer getTime() {
		return time;
	}

	public User setTime(Integer time) {
		this.time = time;
		return this;
	}

	public Integer getApptime() {
		return apptime;
	}

	public User setApptime(Integer apptime) {
		this.apptime = apptime;
		return this;
	}
}