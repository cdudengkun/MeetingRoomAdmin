package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AppConfigDao;
import com.cjack.meetingroomadmin.model.AppConfigModel;
import com.cjack.meetingroomadmin.table.AppConfigTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AppConfigService {

    @Autowired
    private AppConfigDao dao;

    public void save( AppConfigModel model){
        dao.save( ModelUtils.copySignModel( model, AppConfigTable.class));
    }

    public AppConfigModel get() {
        return ModelUtils.copySignModel( dao.findAll().get( 0), AppConfigModel.class);
    }
}
