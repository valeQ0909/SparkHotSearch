package com.hotsearch.hsbackend.consumer;

import com.hotsearch.hsbackend.consumer.utils.MyThread;
import com.hotsearch.hsbackend.mapper.NewscountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.concurrent.ConcurrentHashMap;



@Component
@ServerEndpoint("/websocket/test/")  // 注意不要以'/'结尾
public class WebSocketServer {
    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); //一个线程安全的哈希表
    private Session session = null;

    @Autowired
    public static NewscountMapper newscountMapper;

    @Autowired
    public void setNewscountMapper(NewscountMapper newscountMapper){
        MyThread.newscountMapper = newscountMapper;
    }

    MyThread thread1 = new MyThread();
    Thread thread = new Thread(thread1);

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // 建立连接
        this.session = session;
        users.put(1, this);

        System.out.println("connected!");
        //单独开一个线程用来监测数据库变化
        thread.start();
    }

    @OnClose
    public void onClose() {
        thread1.stopMe();
        // 关闭链接
        users.remove(this);
        System.out.println("disconnected!");
    }


    //这里单纯用来接收数据库变化的信息
    @OnMessage
    public void onMessage(String message){
        // 从Client接收消息
        System.out.println("recevie message!");
        System.out.println(message);
        try {
            sendMessage(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("send: "+ message);
        WebSocketServer.users.get(1).session.getBasicRemote().sendText(message);
        }
    }



