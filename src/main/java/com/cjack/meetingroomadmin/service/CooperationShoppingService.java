package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.CooperationShoppingDao;
import com.cjack.meetingroomadmin.model.CooperationShoppingModel;
import com.cjack.meetingroomadmin.table.CooperationShoppingTable;
import com.cjack.meetingroomadmin.table.DefineSignKeyTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CooperationShoppingService {

    @Autowired
    private CooperationShoppingDao dao;

    public void list(LayPage page, CooperationShoppingModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<CooperationShoppingTable> specification = handleConditon( model);
        Page<CooperationShoppingTable> pageTable = dao.findAll( specification, pageable);
        page.setData( ModelUtils.copyListModel( pageTable.getContent(), CooperationShoppingModel.class));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<CooperationShoppingTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            CooperationShoppingTable table = dao.getOne( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( CooperationShoppingModel model){
        dao.save( ModelUtils.copySignModel( model, CooperationShoppingTable.class));
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<CooperationShoppingTable> handleConditon(CooperationShoppingModel model){
        Specification< CooperationShoppingTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getCompanyName())){
                predicate.getExpressions().add( cb.like( root.get("companyName"), "%" + model.getCompanyName() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( root.get("name"), "%" + model.getName() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getShop())){
                predicate.getExpressions().add( cb.like( root.get("shop"), "%" + model.getShop() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getTypeId())){
                Join<CooperationShoppingTable, DefineSignKeyTable> join = root.join("type", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getTypeId()));
            }
            return predicate;
        };
        return specification;
    }
}