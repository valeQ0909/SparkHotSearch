package com.hotsearch.hsbackend.consumer.utils;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.consumer.WebSocketServer;
import com.hotsearch.hsbackend.mapper.NewscountMapper;
import com.hotsearch.hsbackend.pojo.Newscount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class MyThread implements Runnable{
    private boolean stopMe = true;
    @Autowired
    public static NewscountMapper newscountMapper;

    @Autowired
    public void setNewscountMapper(NewscountMapper newscountMapper){
        MyThread.newscountMapper = newscountMapper;
    }

    public void stopMe() {
        stopMe = false;
    }

    public void run()  {
        System.out.println("<<: " + newscountMapper + ":>>");

        WebSocketServer wbs = new WebSocketServer();
        while(stopMe){
            QueryWrapper<Newscount> queryWrapper = new QueryWrapper<Newscount>();
            queryWrapper.groupBy("name");
            queryWrapper.orderByDesc("count");
            queryWrapper.last("limit 8");
            //查到所有结果后先分组，再求和，最后将结果返回
            System.out.println("here111");
            List<Newscount> newscounts = newscountMapper.selectList(queryWrapper);
            System.out.println(newscountMapper);

            System.out.println("here2222");
            List<JSONObject> data = new LinkedList<JSONObject>();
            JSONObject resp = new JSONObject();
            for(Newscount newscount: newscounts){
                JSONObject item = new JSONObject();
                item.put("name", newscount.getName());
                item.put("count", newscount.getCount());
                data.add(item);
            }
            resp.put("mydata", data);

            String mesg = resp.toJSONString();
            wbs.onMessage(mesg);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}

