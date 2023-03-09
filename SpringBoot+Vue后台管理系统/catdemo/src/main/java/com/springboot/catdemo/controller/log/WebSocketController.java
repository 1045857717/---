package com.springboot.catdemo.controller.log;

import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;

@ServerEndpoint("/log/{logNum}/{logType}")
@RestController
public class WebSocketController {

    private Process process;
    private InputStream inputStream;

    /**
     * 新的WebSocket请求开启
     */
    @OnOpen
    public void onOpen(@PathParam(value = "logNum") String logNum, @PathParam(value = "logType") String logType , Session session) {
        try {
            // https://blog.csdn.net/footless_bird/article/details/116274515
            String fileName;
            switch (logType) {
                case "INFO":
                    fileName = " logs/info/catdemo_INFO.log";
                    break;
                case "DEBUG":
                    fileName = " logs/debug/catdemo_DEBUG.log";
                    break;
                case "ERROR":
                    fileName = " logs/error/catdemo_ERROR.log";
                    break;
                default:
                    fileName = " logs/all/catdemo_All.log";
                    break;
            }
            if (logNum != null && !("".equals(logNum)) && !(logNum.equals("null"))) {
                process = Runtime.getRuntime().exec("tail -n " + logNum + fileName);
            } else {
                process = Runtime.getRuntime().exec("tail -f " + fileName);
            }
            inputStream = process.getInputStream();
            TailfLogThread thread = new TailfLogThread(inputStream, session);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * WebSocket请求关闭
     */
    @OnClose
    public void onClose() {
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (process != null)
            process.destroy();
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端消息：" + message);

    }
    @OnError
    public void onError(Throwable thr) {
        thr.printStackTrace();
    }
}