package com.hotsearch.hsbackend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.consumer.WebSocketServer;
import com.hotsearch.hsbackend.mapper.ProvincecountMapper;
import com.hotsearch.hsbackend.pojo.Newscount;
import com.hotsearch.hsbackend.pojo.Periodcount;
import com.hotsearch.hsbackend.pojo.Provincecount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class MyProvinceThread implements Runnable{
    private boolean stopMe = true;

    @Autowired
    public static ProvincecountMapper provincecountMapper; // 调用ProvincecountMapper

    public void stopMe() {  // 停止flag
        stopMe = false;
    }

    @Override
    public void run() {
        WebSocketServer wbs = new WebSocketServer();
        System.out.println("启动了province线程");

        while(stopMe){
            System.out.println("provinceThread查询一次信息");

            QueryWrapper<Provincecount> queryWrapper = new QueryWrapper<Provincecount>();
            // 在spark阶段已经分组求和过了
            queryWrapper.groupBy("province");
            queryWrapper.orderByDesc("count");
            // 查询结果存放在provincecounts列表中
            List<Provincecount> provincecounts = provincecountMapper.selectList(queryWrapper);


            // 先创建好最后要返回给前端的resp对象，其json格式为{"province":{["province1":"count1","province2":"count2",...]}}
            JSONObject resp = new JSONObject();

            // 先创建一个空的用于接收所有整理过的item
            List<JSONObject> data = new LinkedList<JSONObject>();

            // 逐个遍历provincecount内的每一项记录
            for(Provincecount provincecount: provincecounts){
                // 将当前provincecount记录的name和count打包成一个json对象item
                JSONObject item = new JSONObject();
                item.put("name", provincecount.getProvince());
                item.put("value", provincecount.getCount());

                // 将组装好的item加入data中
                data.add(item);
            }
            resp.put("province", data);

            String mesg = resp.toJSONString();
            wbs.onMessage(mesg);
            System.out.println("provinceThread发送一次信息\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
