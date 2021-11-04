package com.cjack.meetingroomadmin.controller;

/**
 * Created by root on 6/10/19.
 */
public class BaseEndpoint {

    public boolean isAdd( Long id){
        if( id == null || id.equals( 0l)){
            return true;
        }
        return false;
    }
}
