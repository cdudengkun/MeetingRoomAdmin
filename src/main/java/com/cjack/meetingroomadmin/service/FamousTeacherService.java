package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.FamousTeacherDao;
import com.cjack.meetingroomadmin.model.FamousTeacherModel;
import com.cjack.meetingroomadmin.table.FamousTeacherTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FamousTeacherService {

    @Autowired
    private FamousTeacherDao dao;

    public void list(LayPage page){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Page<FamousTeacherTable> pageTable = dao.findAll( pageable);
        page.setData( ModelUtils.copyListModel( pageTable.getContent(), FamousTeacherModel.class));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public void del( String ids){
        List<FamousTeacherTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            FamousTeacherTable table = new FamousTeacherTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( FamousTeacherModel model){
        dao.save( ModelUtils.copySignModel( model, FamousTeacherTable.class));
    }
}
