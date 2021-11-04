package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.AboutUsDao;
import com.cjack.meetingroomadmin.model.AboutUsModel;
import com.cjack.meetingroomadmin.table.AboutUsTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AboutUsService {

    @Autowired
    private AboutUsDao dao;

    public List<AboutUsModel> list(){
        return ModelUtils.copyListModel( dao.findAll(), AboutUsModel.class);
    }

    public void del( String ids){
        List< AboutUsTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AboutUsTable table = new AboutUsTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AboutUsModel model){
        dao.save( ModelUtils.copySignModel( model, AboutUsTable.class));
    }

    public AboutUsModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), AboutUsModel.class);
    }
}
