package com.cjack.meetingroomadmin.config;

import java.util.List;

/**
 * Created by root on 5/25/19.
 */
public class LayPage {

    //返回分页数据
    private Integer code = 0;
    private Integer count;
    private List data;

    //获取分页信息
    private Integer page;
    private Integer limit;

    public LayPage(){

    }

    public LayPage( List data){
        this.data = data;
        this.count = data.size();
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
