package com.hotsearch.hsbackend.service.impl.data;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotsearch.hsbackend.mapper.NewscountMapper;
import com.hotsearch.hsbackend.pojo.Newscount;
import com.hotsearch.hsbackend.service.data.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private NewscountMapper newscountMapper;
    @Override
    public JSONObject testService() {
        QueryWrapper<Newscount> queryWrapper = new QueryWrapper<Newscount>();
        // 在spark阶段已经分组求和过了
        queryWrapper.groupBy("name");
        queryWrapper.orderByDesc("count");
        // 每次返回8条数据
        queryWrapper.last("limit 8");

        //查到所有结果后先分组，再求和，最后将结果返回
        List<Newscount> newscounts = newscountMapper.selectList(queryWrapper);

        List<JSONObject> data = new LinkedList<JSONObject>();
        JSONObject resp = new JSONObject();
        for(Newscount newscount: newscounts){
            JSONObject item = new JSONObject();
            item.put("name", newscount.getName());
            item.put("count", newscount.getCount());
            data.add(item);
        }
        resp.put("mydata", data);

        return resp;
    }
}
