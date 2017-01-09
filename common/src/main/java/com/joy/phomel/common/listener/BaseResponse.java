package com.joy.phomel.common.listener;

import java.io.Serializable;

/**
 * Created by phomel on 2017/1/9.
 */

public class BaseResponse<T> implements Serializable {


    private static final long serialVersionUID = 4422417601354903261L;
    private int resultCode;
    private String resultMessage;
    private T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
