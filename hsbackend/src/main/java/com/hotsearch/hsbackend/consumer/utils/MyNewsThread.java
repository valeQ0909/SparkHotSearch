package com.hotsearch.hsbackend.consumer.utils;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.consumer.WebSocketServer;
import com.hotsearch.hsbackend.mapper.NewscountMapper;
import com.hotsearch.hsbackend.pojo.Newscount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class MyNewsThread implements Runnable{
    private boolean stopMe = true;

    @Autowired
    public static NewscountMapper newscountMapper;  // 调用NewscountMapper

    public void stopMe() {  // 停止flag
        stopMe = false;
    }

    public void run()  {
        System.out.println("<<: " + newscountMapper + ":>>");

        WebSocketServer wbs = new WebSocketServer();
        System.out.println("启动news线程");
        while(stopMe){
            System.out.println("newsThread查询一次信息");

            QueryWrapper<Newscount> queryWrapper = new QueryWrapper<Newscount>();
            // 在spark阶段已经分组求和过了
            queryWrapper.groupBy("name");
            queryWrapper.orderByDesc("count");
            queryWrapper.last("limit 8");
            // 查询结果存放在newscounts列表中
            List<Newscount> newscounts = newscountMapper.selectList(queryWrapper);


            // 先创建好最后要返回给前端的resp对象，其json格式为{"news":{["name1":"count1","name2":"count2",...]}}
            JSONObject resp = new JSONObject();

            // 先创建一个空的用于接收所有整理过的item
            List<JSONObject> data = new LinkedList<JSONObject>();

            // 逐个遍历newscount内的每一项记录
            for(Newscount newscount: newscounts){
                // 将当前newscount记录的name和count打包成一个json对象item
                JSONObject item = new JSONObject();
                item.put("name", newscount.getName());
                item.put("count", newscount.getCount());

                // 将组装好的item加入data中
                data.add(item);
            }
            resp.put("news", data);

            String mesg = resp.toJSONString();
            wbs.onMessage(mesg);
            System.out.println("newsThread发送一次信息\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


}

