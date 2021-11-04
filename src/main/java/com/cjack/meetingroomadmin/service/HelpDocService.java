package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.HelpDocDao;
import com.cjack.meetingroomadmin.model.HelpDocModel;
import com.cjack.meetingroomadmin.table.HelpDocTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelpDocService {

    @Autowired
    private HelpDocDao dao;

    public List<HelpDocModel> list(){
        return ModelUtils.copyListModel( dao.findAll(), HelpDocModel.class);
    }

    public void del( String ids){
        List<HelpDocTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            HelpDocTable table = new HelpDocTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( HelpDocModel model){
        dao.save( ModelUtils.copySignModel( model, HelpDocTable.class));
    }
}
