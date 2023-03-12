package com.hotsearch.hsbackend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.consumer.WebSocketServer;
import com.hotsearch.hsbackend.mapper.AgecountMapper;
import com.hotsearch.hsbackend.pojo.Agecount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class MyAgeThread implements Runnable {
    private boolean stopMe = true;

    @Autowired
    public static AgecountMapper agecountMapper; // 调用AgecountMapper

    public void stopMe() {  // 停止flag
        stopMe = false;
    }

    public void run() {
        WebSocketServer wbs = new WebSocketServer();
        System.out.println("启动了age线程");
        while(stopMe){
            System.out.println("ageThread查询一次信息");

            QueryWrapper<Agecount> queryWrapper = new QueryWrapper<Agecount>();
            // 在spark阶段已经分组求和过了
            queryWrapper.groupBy("age");
            queryWrapper.orderByDesc("count");

            // 查询结果存放在agecounts列表中
            List<Agecount> agecounts = agecountMapper.selectList(queryWrapper);

            // 先创建好最后要返回给前端的resp对象，其json格式为{"age":{["age1":"count1","age2":"count2",...]}}
            JSONObject resp = new JSONObject();

            // 先创建一个空的用于接收所有整理过的item
            List<JSONObject> data = new LinkedList<JSONObject>();

            // 逐个遍历agecounts内的每一项记录
            for(Agecount agecount: agecounts){
                // 将当前agecount记录的age和count打包成一个json对象item
                JSONObject item = new JSONObject();
                item.put("age", agecount.getAge());
                item.put("count", agecount.getCount());

                // 将组装好的item加入data中
                data.add(item);
            }
            resp.put("age", data);
            String mesg = resp.toJSONString();
            wbs.onMessage(mesg);
            System.out.println("ageThread发送一次信息\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
}
