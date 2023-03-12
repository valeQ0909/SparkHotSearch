package com.hotsearch.hsbackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provincecount {
    private String province;
    private Integer count;

    public String getProvince(){
        return province;
    }

    public Integer getCount(){
        return count;
    }

}
