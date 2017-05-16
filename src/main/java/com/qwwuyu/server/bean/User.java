package com.qwwuyu.server.bean;

public class User {
    private Integer id;

    private String name;

    private String pwd;

    private String nick;

    private String token;

    private Integer auth;
    
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }
}