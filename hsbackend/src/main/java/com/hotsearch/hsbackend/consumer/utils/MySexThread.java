package com.hotsearch.hsbackend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.consumer.WebSocketServer;
import com.hotsearch.hsbackend.mapper.SexcountMapper;
import com.hotsearch.hsbackend.pojo.Newscount;
import com.hotsearch.hsbackend.pojo.Provincecount;
import com.hotsearch.hsbackend.pojo.Sexcount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class MySexThread implements Runnable{
    private boolean stopMe = true;

    @Autowired
    public static SexcountMapper sexcountMapper; // 调用SexcountMapper

    public void stopMe() {  // 停止flag
        stopMe = false;
    }

    @Override
    public void run() {
        WebSocketServer wbs = new WebSocketServer();
        System.out.println("启动了sex线程");
        while(stopMe){
            System.out.println("sexThread查询一次信息");
            QueryWrapper<Sexcount> queryWrapper = new QueryWrapper<Sexcount>();
            queryWrapper.groupBy("sex");
            queryWrapper.orderByDesc("count");
            // 查询结果存放在sexcounts列表中
            List<Sexcount> sexcounts = sexcountMapper.selectList(queryWrapper);


            // 先创建好最后要返回给前端的resp对象，其json格式为{"sex":{["sex1":"count1","sex2":"count2",...]}}
            JSONObject resp = new JSONObject();

            // 先创建一个空的用于接收所有整理过的item
            List<JSONObject> data = new LinkedList<JSONObject>();

            // 逐个遍历sexcount内的每一项记录
            for(Sexcount sexcount: sexcounts){
                // 将当前sexcount记录的sex和count打包成一个json对象item
                JSONObject item = new JSONObject();
                item.put("sex", sexcount.getSex());
                item.put("value", sexcount.getCount());

                // 将组装好的item加入data中
                data.add(item);
            }
            resp.put("sex", data);

            String mesg = resp.toJSONString();
            wbs.onMessage(mesg);
            System.out.println("sexThread发送一次信息\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
