package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.PolicyInterpretationDao;
import com.cjack.meetingroomadmin.model.PolicyInterpretationModel;
import com.cjack.meetingroomadmin.table.PolicyInterpretationTable;
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
public class PolicyInterpretationService {

    @Autowired
    private PolicyInterpretationDao dao;

    public void list( LayPage layPage, PolicyInterpretationModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<PolicyInterpretationTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<PolicyInterpretationTable> pageTable = dao.findAll( specification, pageable);
        List<PolicyInterpretationModel> datas = new ArrayList<>();
        for( PolicyInterpretationTable table : pageTable.getContent()){
            PolicyInterpretationModel data = ModelUtils.copySignModel( table, PolicyInterpretationModel.class);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<PolicyInterpretationTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            PolicyInterpretationTable table = new PolicyInterpretationTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( PolicyInterpretationModel model){
        PolicyInterpretationTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, PolicyInterpretationTable.class);
        }
        dao.save( table);
    }

    private Specification<PolicyInterpretationTable> handleConditon( PolicyInterpretationModel model){
        Specification< PolicyInterpretationTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTitle())){
                predicate.getExpressions().add( cb.equal( root.get("title"), model.getTitle()));
            }
            if( EmptyUtil.isNotEmpty( model.getType())){
                predicate.getExpressions().add( cb.equal( root.get("type"), model.getType()));
            }
            return predicate;
        };
        return specification;
    }

}
