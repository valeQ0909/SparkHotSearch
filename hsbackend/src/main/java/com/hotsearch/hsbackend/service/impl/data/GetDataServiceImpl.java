package com.hotsearch.hsbackend.service.impl.data;

import com.alibaba.fastjson.JSONObject;
import com.hotsearch.hsbackend.service.data.GetDataService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetDataServiceImpl implements GetDataService {
    @Override
    public JSONObject getDataService() {
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        JSONObject item1 = new JSONObject();
        item1.put("title","冰箱");
        item1.put("num",1827);
        items.add(item1);
        JSONObject item2 = new JSONObject();
        item2.put("title","洗衣机");
        item2.put("num",342);
        items.add(item2);
        JSONObject item3 = new JSONObject();
        item3.put("title","电视机");
        item3.put("num",541);
        items.add(item3);
        JSONObject item4 = new JSONObject();
        item4.put("title","微波炉");
        item4.put("num",1347);
        items.add(item4);
        JSONObject item5 = new JSONObject();
        item5.put("title","烤箱");
        item5.put("num",2431);
        items.add(item5);
        JSONObject item6 = new JSONObject();
        item6.put("title","空调");
        item6.put("num",876);
        items.add(item6);
        JSONObject item7 = new JSONObject();
        item7.put("title","洗碗机");
        item7.put("num",1578);
        items.add(item7);

        resp.put("chartData", items);

        return resp;
    }
}
