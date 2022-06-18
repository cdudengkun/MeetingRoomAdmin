package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.ProductPriceConfigDao;
import com.cjack.meetingroomadmin.model.ProductPriceConfigModel;
import com.cjack.meetingroomadmin.table.ProductPriceConfigTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPriceConfigService {

    @Autowired
    private ProductPriceConfigDao dao;

    public List<ProductPriceConfigModel> list(){
        return ModelUtils.copyListModel( dao.findAll(), ProductPriceConfigModel.class);
    }

    public void del( String ids){
        List<ProductPriceConfigTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ProductPriceConfigTable table = new ProductPriceConfigTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( ProductPriceConfigModel model){
        ProductPriceConfigTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, ProductPriceConfigTable.class);
        }
        dao.save( table);
    }
}
