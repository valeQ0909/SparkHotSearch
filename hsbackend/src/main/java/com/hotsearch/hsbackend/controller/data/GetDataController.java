package com.hotsearch.hsbackend.controller.data;

import com.alibaba.fastjson.JSONObject;
import com.hotsearch.hsbackend.service.data.GetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetDataController {

    @Autowired
    private GetDataService getDataService;

    @GetMapping ("/data/getdata/")
    public JSONObject getDataService(){
        return getDataService.getDataService();
    }
}
