package com.cjack.meetingroomadmin.model;

import lombok.Data;

@Data
public class BookVersionModel {
    public BookVersionModel( Long id, String name){
        this.id = id;
        this.name = name;
    }
    private Long id;
    private String name;
}
