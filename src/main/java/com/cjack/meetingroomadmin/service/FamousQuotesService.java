package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.FamousQuotesDao;
import com.cjack.meetingroomadmin.model.FamousQuotesModel;
import com.cjack.meetingroomadmin.table.FamousQuotesTable;
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
public class FamousQuotesService {

    @Autowired
    private FamousQuotesDao dao;

    public void list( LayPage page){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Page<FamousQuotesTable> pageTable = dao.findAll( pageable);
        page.setData( ModelUtils.copyListModel( pageTable.getContent(), FamousQuotesModel.class));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public void del( String ids){
        List<FamousQuotesTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            FamousQuotesTable table = new FamousQuotesTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( FamousQuotesModel model){
        dao.save( ModelUtils.copySignModel( model, FamousQuotesTable.class));
    }
}
