package com.qwwuyu.server.bean;

/**
 * 网络请求返回状态属性
 */
public class ResponseBean {
    /**
     * 返回状态码
     */
    private int state;
    /**
     * 返回消息
     */
    private String info;
    /**
     * 返回数据
     */
    private Object data;

    public ResponseBean() {
        super();
    }

    public ResponseBean(int statu, String info, Object data) {
        super();
        this.state = statu;
        this.info = info;
        this.data = data;
    }

    public int getStatu() {
        return state;
    }

    public ResponseBean setStatu(int statu) {
        this.state = statu;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public ResponseBean setInfo(String info) {
        this.info = info;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseBean setData(Object data) {
        this.data = data;
        return this;
    }
}
