package com.springboot.catdemo.controller.log;

import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 实时日志监控
 */
public class TailfLogThread extends Thread {

    private BufferedReader reader;
    private Session session;

    public TailfLogThread(InputStream in, Session session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;

    }

    @Override
    public void run() {
        String line;
        try {
            while((line = reader.readLine()) != null) {
                // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                if (line.replaceAll("\\s*", "").contains("[INFO]")) {
                    line = "<span class=\"infoText\">" + line + "</span>";
                } else if (line.contains("[DEBUG]")) {
                    line = "<span class=\"debugText\">" + line + "</span>";
                } else if (line.contains("[ERROR]")) {
                    line = "<span class=\"errorText\">" + line + "</span>";
                } else if (line.contains("[message:")) {
                    line = "<span class=\"messageText\">" + line + "</span>";
                } else {
                    line = "<span class=\"otherText\">" + line + "</span>";
                }
                line = line.replaceAll("\\t","&nbsp;");
                session.getBasicRemote().sendText(line + "<br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}