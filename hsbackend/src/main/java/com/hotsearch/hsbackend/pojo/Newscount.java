package com.hotsearch.hsbackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Newscount {
    private String name;
    private Integer count;

    public String getName() {
        return name;
    }

    public Integer getCount(){
        return count;
    }
}
