package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.EnterpriseSupportDao;
import com.cjack.meetingroomadmin.model.EnterpriseSupportModel;
import com.cjack.meetingroomadmin.table.EnterpriseSupportTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static com.cjack.meetingroomadmin.util.CustomerStringUtil.toLikeStr;

@Service
public class EnterpriseSupportService {

    @Autowired
    private EnterpriseSupportDao dao;

    public void list( LayPage layPage, EnterpriseSupportModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<EnterpriseSupportTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<EnterpriseSupportTable> pageTable = dao.findAll( specification, pageable);
        List<EnterpriseSupportModel> datas = new ArrayList<>();
        for( EnterpriseSupportTable table : pageTable.getContent()){
            EnterpriseSupportModel data = ModelUtils.copySignModel( table, EnterpriseSupportModel.class);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<EnterpriseSupportTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            EnterpriseSupportTable table = new EnterpriseSupportTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( EnterpriseSupportModel model){
        EnterpriseSupportTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, EnterpriseSupportTable.class);
        }
        dao.save( table);
    }

    private Specification<EnterpriseSupportTable> handleConditon(EnterpriseSupportModel model){
        Specification< EnterpriseSupportTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( root.get("title"), toLikeStr( model.getName())));
            }
            return predicate;
        };
        return specification;
    }

}
