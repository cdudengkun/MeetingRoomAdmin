package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.FeedBackDao;
import com.cjack.meetingroomadmin.model.FeedBackModel;
import com.cjack.meetingroomadmin.table.FeedBackTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackDao dao;

    public void list(LayPage layPage, FeedBackModel model){
        FeedBackTable condition = ModelUtils.copySignModel( model, FeedBackTable.class);
        Example<FeedBackTable> example = Example.of( condition);
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));
        Page<FeedBackTable> pageTable = dao.findAll( example, pageable);
        List<FeedBackModel> datas = new ArrayList<>();
        for(FeedBackTable table : pageTable.getContent()){
            FeedBackModel data = ModelUtils.copySignModel( table, FeedBackModel.class);

            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public void del( String ids){
        List<FeedBackTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            FeedBackTable table = new FeedBackTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( FeedBackModel model){
        FeedBackTable table = ModelUtils.copySignModel( model, FeedBackTable.class);
        dao.save( table);
    }

    public FeedBackModel get( Long id) {
        return ModelUtils.copySignModel( dao.findOne( id), FeedBackModel.class);
    }
}
