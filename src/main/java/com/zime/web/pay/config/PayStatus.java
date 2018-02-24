package com.zime.web.pay.config;

/**
 * Created by LJH on 2017/11/6.
 */
public class PayStatus {


    //1成功  0和小于0失败
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public PayStatus(int code, String msg) {

        this.code = code;
        this.msg = msg;
    }


    public static PayStatus payStatus(int code, String msg) {


        return new PayStatus(code, msg);
    }


    public static PayStatus success(String msg) {

        return new PayStatus(1, msg);
    }

    public static PayStatus success() {

        return new PayStatus(1, null);
    }

    public static PayStatus fail(int code, String msg) {

        return new PayStatus(code, msg);
    }

    public static PayStatus fail(String msg) {

        return new PayStatus(0, msg);
    }


}
