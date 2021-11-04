package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.AppUserAccountDao;
import com.cjack.meetingroomadmin.model.AppUserAccountModel;
import com.cjack.meetingroomadmin.table.AppUserAccountTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserAccountService {

    @Autowired
    private AppUserAccountDao dao;

    public List<AppUserAccountModel> list(AppUserAccountModel model){
        return ModelUtils.copyListModel( dao.findAll(), AppUserAccountModel.class);
    }

    public void del( String ids){
        List<AppUserAccountTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AppUserAccountTable table = new AppUserAccountTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AppUserAccountModel model){
        dao.save( ModelUtils.copySignModel( model, AppUserAccountTable.class));
    }

    public AppUserAccountModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), AppUserAccountModel.class);
    }
}
