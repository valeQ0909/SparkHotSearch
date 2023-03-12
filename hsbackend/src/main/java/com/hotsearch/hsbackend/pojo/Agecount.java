package com.hotsearch.hsbackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agecount {
    private String age;
    private Integer count;

    public String getAge(){
        return age;
    }

    public Integer getCount(){
        return count;
    }

}
