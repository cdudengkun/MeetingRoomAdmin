package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AdvertisementDao;
import com.cjack.meetingroomadmin.model.AdvertisementModel;
import com.cjack.meetingroomadmin.table.AdvertisementTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementDao dao;

    public List<AdvertisementModel> list(){
        return ModelUtils.copyListModel( dao.findAll(), AdvertisementModel.class);
    }

    public void del( String ids){
        List<AdvertisementTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AdvertisementTable table = new AdvertisementTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( AdvertisementModel model){
        AdvertisementTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, AdvertisementTable.class);
        }
        dao.save( table);
    }
}
