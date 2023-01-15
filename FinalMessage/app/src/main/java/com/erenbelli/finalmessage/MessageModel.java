package com.erenbelli.finalmessage;

import java.util.List;

public class MessageModel {
    private String msgName, msg, uid;

    public MessageModel(String msgName, String msg,  String uid){
        this.msgName = msgName;
        this.msg = msg;
        this.uid = uid;
    }

    public String getMsgName(){
        return msgName;
    }

    public void setMsgName(String msgName){
        this.msgName = msgName;

    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }


    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }
}
