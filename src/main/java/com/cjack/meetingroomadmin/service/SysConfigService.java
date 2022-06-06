package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.SysConfigDao;
import com.cjack.meetingroomadmin.model.SysConfigModel;
import com.cjack.meetingroomadmin.table.SysConfigTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysConfigService {

    @Autowired
    private SysConfigDao dao;

    public List<SysConfigModel> list(){
        return ModelUtils.copyListModel( dao.findAll(), SysConfigModel.class);
    }

    public void del( String ids){
        List<SysConfigTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            SysConfigTable table = new SysConfigTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( SysConfigModel model){
        SysConfigTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, SysConfigTable.class);
        }
        dao.save( table);
    }
}
