package com.hhtc.dialer.call.client;



import com.hhtc.dialer.call.util.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class SignallingTransfer {

    private Socket client;

    public SignallingTransfer(Socket client) {
        this.client = client;
        client.connect();
    }

    /**
     * 创建房间
     *
     * @param roomId
     */
    public void createAndJoinRoom(String roomId) {
        try {
            JSONObject message = new JSONObject();
            message.put("room", roomId);
            sendSignalling("createAndJoinRoom", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 挂电话
     *
     * @param name
     * @param from
     */
    public void turnDown(String name, String from) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", name);
            message.put("from", from);
            sendSignalling("turnDown", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打电话
     *
     * @param name
     * @param from
     */
    public void makeCall(String name, String from) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", name);
            message.put("from", from);
            sendSignalling("makeCall", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除旧的连接
     *
     * @param oldName
     */
    public void removeName(String oldName) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", oldName);
            sendSignalling("removeName", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登陆
     *
     * @param name
     */
    public void login(String name) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", name);
            sendSignalling("login", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ack
     */
    public void connect() {
        try {
            JSONObject message = new JSONObject();
            message.put("name", DataUtils.getTelName());
            sendSignalling("login", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接听电话
     *
     * @param name
     * @param from
     */
    public void consentCall(String name, String from) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", name);
            message.put("from", from);
            sendSignalling("consentCall", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 离开房间
     *
     * @param name
     * @param from
     */
    public void leaveRoom(String name, String from) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", name);
            message.put("from", from);
            sendSignalling("leaveRoom", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 占线
     *
     * @param name
     * @param from
     */
    public void busyRoom(String name, String from) {
        try {
            JSONObject message = new JSONObject();
            message.put("name", name);
            message.put("from", from);
            sendSignalling("busyRoom", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void exitRoom(String socketId, String roomId) {
        try {
            JSONObject message = new JSONObject();
            message.put("from", socketId);
            message.put("room", roomId);
            sendSignalling("exit", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSignalling(String action, JSONObject message) {
        client.emit(action, message);
    }
}
