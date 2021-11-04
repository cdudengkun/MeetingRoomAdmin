package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class BookStageModel {

    public BookStageModel( Long id, String name){
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
}
