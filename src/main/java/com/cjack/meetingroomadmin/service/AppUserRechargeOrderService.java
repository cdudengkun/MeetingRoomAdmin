package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AppUserRechargeOrderDao;
import com.cjack.meetingroomadmin.model.AppUserRechargeOrderModel;
import com.cjack.meetingroomadmin.table.AppUserRechargeOrderTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserRechargeOrderService {

    @Autowired
    private AppUserRechargeOrderDao dao;

    public List< AppUserRechargeOrderModel> list(AppUserRechargeOrderModel model){
        return ModelUtils.copyListModel( dao.findAll(), AppUserRechargeOrderModel.class);
    }

    public void del( String ids){
        List<AppUserRechargeOrderTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AppUserRechargeOrderTable table = new AppUserRechargeOrderTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AppUserRechargeOrderModel model){
        dao.save( ModelUtils.copySignModel( model, AppUserRechargeOrderTable.class));
    }

    public AppUserRechargeOrderModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), AppUserRechargeOrderModel.class);
    }
}
