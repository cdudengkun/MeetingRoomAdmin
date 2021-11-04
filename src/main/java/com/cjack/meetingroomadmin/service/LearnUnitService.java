package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.LearnUnitDao;
import com.cjack.meetingroomadmin.model.LearnUnitModel;
import com.cjack.meetingroomadmin.table.LearnUnitTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LearnUnitService {

    @Autowired
    private LearnUnitDao dao;

    public List< LearnUnitModel> list(LearnUnitModel model){
        return ModelUtils.copyListModel( dao.findAll(), LearnUnitModel.class);
    }

    public void del( String ids){
        List<LearnUnitTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            LearnUnitTable table = new LearnUnitTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( LearnUnitModel model){
        dao.save( ModelUtils.copySignModel( model, LearnUnitTable.class));
    }

    public LearnUnitModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), LearnUnitModel.class);
    }
}
