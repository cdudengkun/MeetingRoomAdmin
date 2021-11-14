package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.EnterpriseServiceTypeDao;
import com.cjack.meetingroomadmin.model.EnterpriseServiceTypeModel;
import com.cjack.meetingroomadmin.table.ConsultInfoTable;
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
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnterpriseServiceTypeService {

    @Autowired
    private EnterpriseServiceTypeDao dao;

    public void list( LayPage layPage, EnterpriseServiceTypeModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<EnterpriseServiceTypeTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<EnterpriseServiceTypeTable> pageTable = dao.findAll( specification, pageable);
        List<EnterpriseServiceTypeModel> datas = new ArrayList<>();
        for( EnterpriseServiceTypeTable table : pageTable.getContent()){
            EnterpriseServiceTypeModel data = ModelUtils.copySignModel( table, EnterpriseServiceTypeModel.class);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<EnterpriseServiceTypeTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            EnterpriseServiceTypeTable table = new EnterpriseServiceTypeTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( EnterpriseServiceTypeModel model){
        EnterpriseServiceTypeTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, EnterpriseServiceTypeTable.class);
        }
        dao.save( table);
    }

    private Specification<EnterpriseServiceTypeTable> handleConditon( EnterpriseServiceTypeModel model){
        Specification< EnterpriseServiceTypeTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.equal( root.get("name"), model.getName()));
            }
            return predicate;
        };
        return specification;
    }

}
