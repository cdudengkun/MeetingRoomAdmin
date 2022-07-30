package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.AdvertisementDao;
import com.cjack.meetingroomadmin.dao.ProductPriceConfigDao;
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

    @Autowired
    private ProductPriceConfigDao priceDao;

    public List<AdvertisementModel> list(){
        List<AdvertisementTable> tables = dao.findAll();
        List<AdvertisementModel> datas = new ArrayList<>();
        for( AdvertisementTable table : tables){
            AdvertisementModel data = ModelUtils.copySignModel( table, AdvertisementModel.class);
            if( table.getPriceConfigTable() != null){
                data.setPriceId( table.getPriceConfigTable().getId());
            }
            datas.add( data);
        }
        return datas;
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
        if( EmptyUtil.isNotEmpty( model.getPriceId())){
            table.setPriceConfigTable( priceDao.getOne( model.getPriceId()));
        }else{
            table.setPriceConfigTable( null);
        }
        dao.save( table);
    }
}
