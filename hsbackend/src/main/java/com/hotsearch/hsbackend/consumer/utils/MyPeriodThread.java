package com.hotsearch.hsbackend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.consumer.WebSocketServer;
import com.hotsearch.hsbackend.mapper.PeriodcountMapper;
import com.hotsearch.hsbackend.pojo.Newscount;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;

public class MyPeriodThread implements Runnable{
    private boolean stopMe = true;

    @Autowired
    public static PeriodcountMapper periodcountMapper; //调用PeriodcountMapper

    public void stopMe() {  // 停止flag
        stopMe = false;
    }

    private String TextFile = "./TextFile.txt";

    public void run() {
        WebSocketServer wbs = new WebSocketServer();
        System.out.println("启动了period线程");

        // 提前创建好词频统计需要的文件
        File textFile = new File(TextFile);

        while(stopMe) {
            System.out.println("WorldCloudThread查询一次信息");

            QueryWrapper<Newscount> queryWrapper = new QueryWrapper<Newscount>();
            queryWrapper.groupBy("name");
            queryWrapper.orderByDesc("count");
            // 查询结果存放在newscounts列表中
            List<Newscount> newscounts = MyNewsThread.newscountMapper.selectList(queryWrapper);
            // 先创建好最后要返回给前端的resp对象，其json格式为{"news":{["name1":"count1","name2":"count2",...]}}

            if(textFile.exists()){ // 如果文件存在就删除
                textFile.delete();
            }

            Integer cnt = 0;
            String lineText = "";
            // 逐个遍历newscount内的每一项记录
            for (Newscount newscount : newscounts) {
                lineText = newscount.getName();
                cnt = newscount.getCount();
                for (int i = 0; i < cnt; i++) {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(TextFile, true));
                        out.write(lineText + "\n");
                        out.close();
                    } catch (IOException e) {
                        System.out.println("exception occoured" + e);
                    }
                }
            }
            System.out.println("WorldCloudThread写了一次文件");

            // 生成词频
            // 新建FrequencyAnalyzer 对象
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            // 设置分词返回数量(频率最高的600个词)
            frequencyAnalyzer.setWordFrequenciesToReturn(600);
            // 最小分词长度
            frequencyAnalyzer.setMinWordLength(2);
            // 引入中文解析器
            frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

            // 可以直接从文件中读取
            List<WordFrequency> wordFrequencies;
            try {
                wordFrequencies = frequencyAnalyzer.load(TextFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            JSONObject resp = new JSONObject();
            resp.put("world", wordFrequencies);

            String mesg = resp.toJSONString();
            wbs.onMessage(mesg);
            System.out.println("WorldCloudThread发送一条信息\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }
}
