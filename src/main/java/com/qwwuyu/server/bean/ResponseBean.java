package com.qwwuyu.server.bean;

/**
 * 网络请求返回状态属性
 */
public class ResponseBean {
    /** 返回状态码 */
    private int statu;
    /** 返回消息 */
    private String info;
    /** 返回数据 */
    private Object data;

    public ResponseBean() {
        super();
    }

    public ResponseBean(int statu, String info, Object data) {
        super();
        this.statu = statu;
        this.info = info;
        this.data = data;
    }

    public int getStatu() {
        return statu;
    }

    public ResponseBean setStatu(int statu) {
        this.statu = statu;
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

    public static ResponseBean getSuccessBean() {
        ResponseBean bean = new ResponseBean();
        bean.statu = 1;
        bean.info = "请求成功";
        return bean;
    }

    public static ResponseBean getErrorBean() {
        ResponseBean bean = new ResponseBean();
        bean.statu = -1;
        bean.info = "服务器内部错误";
        return bean;
    }
}
