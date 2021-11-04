package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AppConfigDao;
import com.cjack.meetingroomadmin.model.AppConfigModel;
import com.cjack.meetingroomadmin.table.AppConfigTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AppConfigService {

    @Autowired
    private AppConfigDao dao;

    public List<AppConfigModel> list(AppConfigModel model){
        return ModelUtils.copyListModel( dao.findAll(), AppConfigModel.class);
    }

    public void del( String ids){
        List<AppConfigTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AppConfigTable table = new AppConfigTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AppConfigModel model){
        dao.save( ModelUtils.copySignModel( model, AppConfigTable.class));
    }

    public AppConfigModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), AppConfigModel.class);
    }
}
