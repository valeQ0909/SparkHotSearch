package com.hotsearch.hsbackend.consumer;

import com.hotsearch.hsbackend.consumer.utils.*;
import com.hotsearch.hsbackend.mapper.*;
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
    public void setNewscountMapper(NewscountMapper newscountMapper){
        MyNewsThread.newscountMapper = newscountMapper;
    }
    @Autowired
    public void setAgecountMapper(AgecountMapper agecountMapper){
        MyAgeThread.agecountMapper = agecountMapper;
    }
    @Autowired
    public void setPeriodcountMapper(PeriodcountMapper periodcountMapper){
        MyPeriodThread.periodcountMapper = periodcountMapper;
    }
    @Autowired
    public void setProvincecountMapper(ProvincecountMapper provincecountMapper){
        MyProvinceThread.provincecountMapper = provincecountMapper;
    }
    @Autowired
    public void setSexcountMapper(SexcountMapper sexcountMapper){
        MySexThread.sexcountMapper = sexcountMapper;
    }


    com.hotsearch.hsbackend.consumer.utils.MyNewsThread MyNewsThread = new MyNewsThread();
    com.hotsearch.hsbackend.consumer.utils.MyAgeThread MyAgeThread = new MyAgeThread();
    com.hotsearch.hsbackend.consumer.utils.MyPeriodThread MyPeriodThread = new MyPeriodThread();
    com.hotsearch.hsbackend.consumer.utils.MyProvinceThread MyProvinceThread = new MyProvinceThread();
    com.hotsearch.hsbackend.consumer.utils.MySexThread MySexThread = new MySexThread();


    Thread newsThread = new Thread(MyNewsThread);
    Thread ageThread = new Thread(MyAgeThread);
    Thread periodThread = new Thread(MyPeriodThread);
    Thread provinceThread = new Thread(MyProvinceThread);
    Thread sexThread = new Thread(MySexThread);


    @OnOpen
    public void onOpen(Session session) throws IOException {
        // 建立连接
        this.session = session;
        users.put(1, this); // 添加一个连接
        System.out.println("connected!");

        //单独开一个线程用来监测数据库变化
        newsThread.start();
        ageThread.start();
        periodThread.start();
        provinceThread.start();
        sexThread.start();
    }

    @OnClose
    public void onClose() {
        // 停止线程的工作
        MyNewsThread.stopMe();
        MyProvinceThread.stopMe();
        MyPeriodThread.stopMe();
        MySexThread.stopMe();
        MyAgeThread.stopMe();

        // 关闭链接
        users.remove(this);
        System.out.println("disconnected!");
    }


    //这里单纯用来接收数据库变化的信息
    @OnMessage
    public void onMessage(String message){
        // 从Client接收消息
        System.out.println("recevie message!");
        System.out.println("要发送的消息是: " + message);
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
        //WebSocketServer.users.get(1).session.getBasicRemote().sendText(message);
        synchronized (WebSocketServer.users.get(1).session){
            WebSocketServer.users.get(1).session.getBasicRemote().sendText(message);
        }
    }

}



