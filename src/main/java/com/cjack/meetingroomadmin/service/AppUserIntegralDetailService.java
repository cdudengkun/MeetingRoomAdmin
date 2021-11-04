package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AppUserIntegralDetailDao;
import com.cjack.meetingroomadmin.model.AppUserIntegralDetailModel;
import com.cjack.meetingroomadmin.table.AppUserIntegralDetailTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserIntegralDetailService {

    @Autowired
    private AppUserIntegralDetailDao dao;

    public List<AppUserIntegralDetailModel> list(AppUserIntegralDetailModel model){
        return ModelUtils.copyListModel( dao.findAll(), AppUserIntegralDetailModel.class);
    }

    public void del( String ids){
        List<AppUserIntegralDetailTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AppUserIntegralDetailTable table = new AppUserIntegralDetailTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AppUserIntegralDetailModel model){
        dao.save( ModelUtils.copySignModel( model, AppUserIntegralDetailTable.class));
    }

    public AppUserIntegralDetailModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), AppUserIntegralDetailModel.class);
    }
}
