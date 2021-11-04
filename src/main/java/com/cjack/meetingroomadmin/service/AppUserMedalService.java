package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AppUserMedalDao;
import com.cjack.meetingroomadmin.model.AppUserMedalModel;
import com.cjack.meetingroomadmin.table.AppUserMedalTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserMedalService {

    @Autowired
    private AppUserMedalDao dao;

    public List<AppUserMedalModel> list(AppUserMedalModel model){
        return ModelUtils.copyListModel( dao.findAll(), AppUserMedalModel.class);
    }

    public void del( String ids){
        List<AppUserMedalTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AppUserMedalTable table = new AppUserMedalTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AppUserMedalModel model){
        dao.save( ModelUtils.copySignModel( model, AppUserMedalTable.class));
    }

    public AppUserMedalModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), AppUserMedalModel.class);
    }
}
