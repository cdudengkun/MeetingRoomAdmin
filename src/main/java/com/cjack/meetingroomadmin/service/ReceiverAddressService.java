package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.ReceiverAddressDao;
import com.cjack.meetingroomadmin.model.ReceiverAddressModel;
import com.cjack.meetingroomadmin.table.ReceiverAddressTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiverAddressService {

    @Autowired
    private ReceiverAddressDao dao;

    public List<ReceiverAddressModel> list(ReceiverAddressModel model){
        return ModelUtils.copyListModel( dao.findAll(), ReceiverAddressModel.class);
    }

    public void del( String ids){
        List< ReceiverAddressTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ReceiverAddressTable table = new ReceiverAddressTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( ReceiverAddressModel model){
        dao.save( ModelUtils.copySignModel( model, ReceiverAddressTable.class));
    }

    public ReceiverAddressModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), ReceiverAddressModel.class);
    }
}
