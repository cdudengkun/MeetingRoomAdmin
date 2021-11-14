package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.DefineSignValueDao;
import com.cjack.meetingroomadmin.model.DefineSignKeyModel;
import com.cjack.meetingroomadmin.table.DefineSignKeyTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SingleKeyService {

    @Autowired
    private DefineSignValueDao dao;

    public List<DefineSignKeyModel> list(Integer type){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        DefineSignKeyTable condition = new DefineSignKeyTable();
        condition.setType( type);
        Example< DefineSignKeyTable> example = Example.of( condition);
        List<DefineSignKeyTable> tables = dao.findAll( example);
        List<DefineSignKeyModel> datas = new ArrayList<>();
        for( DefineSignKeyTable table : tables){
            DefineSignKeyModel data = ModelUtils.copySignModel( table, DefineSignKeyModel.class);
            datas.add( data);
        }
        return datas;
    }

    public void del( String ids){
        List<DefineSignKeyTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            DefineSignKeyTable table = new DefineSignKeyTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( DefineSignKeyModel model){
        DefineSignKeyTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, DefineSignKeyTable.class);
        }
        dao.save( table);
    }

}
