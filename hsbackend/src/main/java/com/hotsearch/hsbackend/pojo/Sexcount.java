package com.hotsearch.hsbackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sexcount {
    private String sex;
    private Integer count;

    public String getSex() {
        return sex;
    }

    public Integer getCount(){
        return count;
    }


}
