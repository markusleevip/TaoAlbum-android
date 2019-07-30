package cn.cloudfk.taoalbum.data.dto;

import java.io.Serializable;

public class ResultData implements Serializable {
    private Integer state;
    private String msg;
    private Object data;

    public static  int SUCCESS=1;
    public static int FAIL=0;

    public boolean isSuccess(){
        return state ==1;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
