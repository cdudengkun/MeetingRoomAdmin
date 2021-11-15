package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.EnterpriseServiceDao;
import com.cjack.meetingroomadmin.model.EnterpriseServiceModel;
import com.cjack.meetingroomadmin.table.EnterpriseServiceTable;
import com.cjack.meetingroomadmin.table.EnterpriseServiceTypeTable;
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
public class EnterpriseServiceService {

    @Autowired
    private EnterpriseServiceDao dao;

    public void list( LayPage layPage, EnterpriseServiceModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<EnterpriseServiceTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<EnterpriseServiceTable> pageTable = dao.findAll( specification, pageable);
        List<EnterpriseServiceModel> datas = new ArrayList<>();
        for( EnterpriseServiceTable table : pageTable.getContent()){
            EnterpriseServiceModel data = ModelUtils.copySignModel( table, EnterpriseServiceModel.class);
            data.setTypeId( table.getType().getId());
            data.setTypeName( table.getType().getName());
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<EnterpriseServiceTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            EnterpriseServiceTable table = new EnterpriseServiceTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( EnterpriseServiceModel model){
        EnterpriseServiceTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, EnterpriseServiceTable.class);
        }
        dao.save( table);
    }

    private Specification<EnterpriseServiceTable> handleConditon( EnterpriseServiceModel model){
        Specification<EnterpriseServiceTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTitle())){
                predicate.getExpressions().add( cb.equal( root.get("title"), model.getTitle()));
            }
            if( EmptyUtil.isNotEmpty( model.getTypeId())){
                Join<EnterpriseServiceTable, EnterpriseServiceTypeTable> join = root.join("type", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getTypeId()));
            }
            return predicate;
        };
        return specification;
    }

}
