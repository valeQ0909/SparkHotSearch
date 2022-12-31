package com.hotsearch.hsbackend.controller.data;

import com.alibaba.fastjson.JSONObject;
import com.hotsearch.hsbackend.service.data.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/data/test/")
    public JSONObject getDataService(){
        return testService.testService();
    }
}
